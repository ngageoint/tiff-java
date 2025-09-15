package mil.nga.tiff.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import mil.nga.tiff.util.TiffException;

/**
 * Read through a byte array or file with streaming support
 *
 * @author osbornb
 */
public class ByteReader implements AutoCloseable {

	/**
	 * Next byte index to read
	 */
	private long nextByte = 0;

	/**
	 * Bytes to read (for memory mode)
	 */
	private final byte[] bytes;

	/**
	 * Byte order
	 */
	private ByteOrder byteOrder = null;

	/**
	 * Streaming mode fields
	 */
	private final boolean isStreaming;
	private final RandomAccessFile randomAccessFile;
	private final FileChannel channel;
	private final long fileSize;

	/**
	 * Constructor for memory mode
	 *
	 * @param bytes
	 *            bytes
	 */
	public ByteReader(byte[] bytes) {
		this(bytes, ByteOrder.nativeOrder());
	}

	/**
	 * Constructor for memory mode
	 *
	 * @param bytes
	 *            bytes
	 * @param byteOrder
	 *            byte order
	 */
	public ByteReader(byte[] bytes, ByteOrder byteOrder) {
		this.bytes = bytes;
		this.byteOrder = byteOrder;
		this.isStreaming = false;
		this.randomAccessFile = null;
		this.channel = null;
		this.fileSize = bytes != null ? bytes.length : 0;
	}

	/**
	 * Constructor for streaming mode
	 *
	 * @param file
	 *            file to read
	 * @throws IOException
	 *             upon file access error
	 */
	public ByteReader(File file) throws IOException {
		this(file, ByteOrder.nativeOrder());
	}

	/**
	 * Constructor for streaming mode
	 *
	 * @param file
	 *            file to read
	 * @param byteOrder
	 *            byte order
	 * @throws IOException
	 *             upon file access error
	 */
	public ByteReader(File file, ByteOrder byteOrder) throws IOException {
		this.bytes = null;
		this.byteOrder = byteOrder;
		this.isStreaming = true;
		this.randomAccessFile = new RandomAccessFile(file, "r");
		this.channel = randomAccessFile.getChannel();
		this.fileSize = channel.size();
	}

	/**
	 * Get the next byte to be read
	 *
	 * @return next byte to be read
	 */
	public long getNextByte() {
		return nextByte;
	}

	/**
	 * Set the next byte to be read
	 *
	 * @param nextByte
	 *            next byte
	 */
	public void setNextByte(long nextByte) {
		if (nextByte >= fileSize) {
			throw new TiffException("Byte offset out of range. Total Bytes: "
					+ fileSize + ", Byte offset: " + nextByte);
		}
		this.nextByte = nextByte;
		if (isStreaming) {
			try {
				channel.position(nextByte);
			} catch (IOException e) {
				throw new TiffException("Failed to set position: " + nextByte, e);
			}
		}
	}

	/**
	 * Get the byte order
	 * 
	 * @return byte order
	 */
	public ByteOrder getByteOrder() {
		return byteOrder;
	}

	/**
	 * Set the byte order
	 * 
	 * @param byteOrder
	 *            byte order
	 */
	public void setByteOrder(ByteOrder byteOrder) {
		this.byteOrder = byteOrder;
	}

	/**
	 * Check if there is at least one more byte left to read
	 * 
	 * @return true more bytes left to read
	 */
	public boolean hasByte() {
		return hasBytes(1);
	}

	/**
	 * Check if there is at least one more byte left to read
	 * 
	 * @param offset
	 *            byte offset
	 * @return true more bytes left to read
	 */
	public boolean hasByte(long offset) {
		return hasBytes(offset, 1);
	}

	/**
	 * Check if there are the provided number of bytes left to read
	 * 
	 * @param count
	 *            number of bytes
	 * @return true if has at least the number of bytes left
	 */
	public boolean hasBytes(int count) {
		return hasBytes(nextByte, count);
	}

	/**
	 * Check if there are the provided number of bytes left to read
	 * 
	 * @param offset
	 *            byte offset
	 * @param count
	 *            number of bytes
	 * @return true if has at least the number of bytes left
	 */
	public boolean hasBytes(long offset, int count) {
		return offset + count <= fileSize;
	}

	/**
	 * Read a String from the provided number of bytes
	 * 
	 * @param num
	 *            number of bytes
	 * @return String
	 * @throws UnsupportedEncodingException
	 *             upon string encoding error
	 */
	public String readString(int num) throws UnsupportedEncodingException {
		String value = readString(nextByte, num);
		nextByte += num;
		return value;
	}

	/**
	 * Read a String from the provided number of bytes
	 * 
	 * @param offset
	 *            byte offset
	 * @param num
	 *            number of bytes
	 * @return String
	 * @throws UnsupportedEncodingException
	 *             upon string encoding error
	 */
	public String readString(long offset, int num)
			throws UnsupportedEncodingException {
		verifyRemainingBytes(offset, num);
		String value = null;
		if (isStreaming) {
			byte[] stringBytes = readBytesInternal(offset, num);
			if (num != 1 || stringBytes[0] != 0) {
				value = new String(stringBytes, 0, num, StandardCharsets.US_ASCII);
			}
		} else {
			if (num != 1 || bytes[(int)offset] != 0) {
				value = new String(bytes, (int)offset, num, StandardCharsets.US_ASCII);
			}
		}
		return value;
	}

	/**
	 * Read a byte
	 * 
	 * @return byte
	 */
	public byte readByte() {
		byte value = readByte(nextByte);
		nextByte++;
		return value;
	}

	/**
	 * Read a byte
	 * 
	 * @param offset
	 *            byte offset
	 * @return byte
	 */
	public byte readByte(long offset) {
		verifyRemainingBytes(offset, 1);
		if (isStreaming) {
			return readBytesInternal(offset, 1)[0];
		} else {
			return bytes[(int)offset];
		}
	}

	/**
	 * Read an unsigned byte
	 * 
	 * @return unsigned byte as short
	 */
	public short readUnsignedByte() {
		short value = readUnsignedByte(nextByte);
		nextByte++;
		return value;
	}

	/**
	 * Read an unsigned byte
	 * 
	 * @param offset
	 *            byte offset
	 * @return unsigned byte as short
	 */
	public short readUnsignedByte(long offset) {
		return ((short) (readByte(offset) & 0xff));
	}

	/**
	 * Read a number of bytes
	 * 
	 * @param num
	 *            number of bytes
	 * @return bytes
	 */
	public byte[] readBytes(int num) {
		byte[] readBytes = readBytes(nextByte, num);
		nextByte += num;
		return readBytes;
	}

	/**
	 * Read a number of bytes
	 * 
	 * @param offset
	 *            byte offset
	 * @param num
	 *            number of bytes
	 * @return bytes
	 */
	public byte[] readBytes(long offset, int num) {
		verifyRemainingBytes(offset, num);
		return readBytesInternal(offset, num);
	}

	/**
	 * Read a short
	 * 
	 * @return short
	 */
	public short readShort() {
		short value = readShort(nextByte);
		nextByte += 2;
		return value;
	}

	/**
	 * Read a short
	 * 
	 * @param offset
	 *            byte offset
	 * @return short
	 */
	public short readShort(long offset) {
		verifyRemainingBytes(offset, 2);
		byte[] shortBytes = readBytesInternal(offset, 2);
		short value = ByteBuffer.wrap(shortBytes).order(byteOrder).getShort();
		return value;
	}

	/**
	 * Read an unsigned short
	 * 
	 * @return unsigned short as int
	 */
	public int readUnsignedShort() {
		int value = readUnsignedShort(nextByte);
		nextByte += 2;
		return value;
	}

	/**
	 * Read an unsigned short
	 * 
	 * @param offset
	 *            byte offset
	 * @return unsigned short as int
	 */
	public int readUnsignedShort(long offset) {
		return (readShort(offset) & 0xffff);
	}

	/**
	 * Read an integer
	 * 
	 * @return integer
	 */
	public int readInt() {
		int value = readInt(nextByte);
		nextByte += 4;
		return value;
	}

	/**
	 * Read an integer
	 * 
	 * @param offset
	 *            byte offset
	 * @return integer
	 */
	public int readInt(long offset) {
		verifyRemainingBytes(offset, 4);
		byte[] intBytes = readBytesInternal(offset, 4);
		int value = ByteBuffer.wrap(intBytes).order(byteOrder).getInt();
		return value;
	}

	/**
	 * Read an unsigned int
	 * 
	 * @return unsigned int as long
	 */
	public long readUnsignedInt() {
		long value = readUnsignedInt(nextByte);
		nextByte += 4;
		return value;
	}

	/**
	 * Read an unsigned int
	 * 
	 * @param offset
	 *            byte offset
	 * @return unsigned int as long
	 */
	public long readUnsignedInt(long offset) {
		return ((long) readInt(offset) & 0xffffffffL);
	}

	/**
	 * Read a float
	 * 
	 * @return float
	 */
	public float readFloat() {
		float value = readFloat(nextByte);
		nextByte += 4;
		return value;
	}

	/**
	 * Read a float
	 * 
	 * @param offset
	 *            byte offset
	 * @return float
	 */
	public float readFloat(long offset) {
		verifyRemainingBytes(offset, 4);
		byte[] floatBytes = readBytesInternal(offset, 4);
		float value = ByteBuffer.wrap(floatBytes).order(byteOrder).getFloat();
		return value;
	}

	/**
	 * Read a double
	 * 
	 * @return double
	 */
	public double readDouble() {
		double value = readDouble(nextByte);
		nextByte += 8;
		return value;
	}

	/**
	 * Read a double
	 * 
	 * @param offset
	 *            byte offset
	 * @return double
	 */
	public double readDouble(long offset) {
		verifyRemainingBytes(offset, 8);
		byte[] doubleBytes = readBytesInternal(offset, 8);
		double value = ByteBuffer.wrap(doubleBytes).order(byteOrder).getDouble();
		return value;
	}

	/**
	 * Get the byte length
	 * 
	 * @return byte length
	 */
	public long byteLength() {
		return fileSize;
	}

	/**
	 * Close the reader and release resources
	 */
	@Override
	public void close() throws IOException {
		if (isStreaming && randomAccessFile != null) {
			randomAccessFile.close();
		}
	}

	/**
	 * Internal method to read bytes from file or memory
	 *
	 * @param offset
	 *            byte offset
	 * @param num
	 *            number of bytes
	 * @return bytes
	 */
	private byte[] readBytesInternal(long offset, int num) {
		if (isStreaming) {
			try {
				ByteBuffer buffer = ByteBuffer.allocate(num);
				channel.read(buffer, offset);
				return buffer.array();
			} catch (IOException e) {
				throw new TiffException("Failed to read bytes at offset " + offset, e);
			}
		} else {
			return Arrays.copyOfRange(bytes, (int)offset, (int)offset + num);
		}
	}

	/**
	 * Verify with the remaining bytes that there are enough remaining to read
	 * the provided amount
	 * 
	 * @param offset
	 *            byte offset
	 * @param bytesToRead
	 *            number of bytes to read
	 */
	private void verifyRemainingBytes(long offset, int bytesToRead) {
		if (offset + bytesToRead > fileSize) {
			throw new TiffException(
					"No more remaining bytes to read. Total Bytes: "
							+ fileSize + ", Byte offset: " + offset
							+ ", Attempted to read: " + bytesToRead);
		}
	}

}
