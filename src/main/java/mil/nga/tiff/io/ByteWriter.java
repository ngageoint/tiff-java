package mil.nga.tiff.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import mil.nga.tiff.util.TiffException;

/**
 * Write a byte array or file with streaming support
 *
 * @author osbornb
 */
public class ByteWriter implements AutoCloseable {

	/**
	 * Output stream to write bytes to (for memory mode)
	 */
	private final ByteArrayOutputStream os;

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

	/**
	 * Constructor for memory mode
	 */
	public ByteWriter() {
		this(ByteOrder.nativeOrder());
	}

	/**
	 * Constructor for memory mode
	 *
	 * @param byteOrder
	 *            byte order
	 */
	public ByteWriter(ByteOrder byteOrder) {
		this.byteOrder = byteOrder;
		this.isStreaming = false;
		this.os = new ByteArrayOutputStream();
		this.randomAccessFile = null;
		this.channel = null;
	}

	/**
	 * Constructor for streaming mode
	 *
	 * @param file
	 *             file to write
	 * @throws IOException
	 *                     upon file access error
	 */
	public ByteWriter(File file) throws IOException {
		this(file, ByteOrder.nativeOrder());
	}

	/**
	 * Constructor for streaming mode
	 *
	 * @param file
	 *                  file to write
	 * @param byteOrder
	 *                  byte order
	 * @throws IOException
	 *                     upon file access error
	 */
	public ByteWriter(File file, ByteOrder byteOrder) throws IOException {
		this.byteOrder = byteOrder;
		this.isStreaming = true;
		this.os = null;
		this.randomAccessFile = new RandomAccessFile(file, "rw");
		this.channel = randomAccessFile.getChannel();
	}

	/**
	 * Close the byte writer
	 */
	@Override
	public void close() {
		try {
			if (isStreaming) {
				if (randomAccessFile != null) {
					randomAccessFile.close();
				}
			} else {
				if (os != null) {
					os.close();
				}
			}
		} catch (IOException e) {
		}
	}

	/**
	 * Get the byte array output stream
	 * 
	 * @return byte array output stream
	 */
	public ByteArrayOutputStream getOutputStream() {
		if (isStreaming) {
			throw new UnsupportedOperationException("OutputStream not available in streaming mode");
		}
		return os;
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
	 * Get the written bytes
	 * 
	 * @return written bytes
	 */
	public byte[] getBytes() throws IOException {
		if (isStreaming) {
			throw new UnsupportedOperationException("getBytes() not supported in streaming mode");
		}
		return os.toByteArray();
	}

	/**
	 * Get the current size in bytes written
	 * 
	 * @return bytes written
	 */
	public long size() throws IOException {
		if (isStreaming) {
			return channel.position();
		} else {
			return os.size();
		}
	}

	/**
	 * Internal method to write bytes to file or memory
	 *
	 * @param value
	 *              bytes to write
	 * @throws IOException
	 *                     upon failure to write
	 */
	private void writeBytesInternal(byte[] value) {
		try {
			if (isStreaming) {
				ByteBuffer buffer = ByteBuffer.wrap(value);
				while (buffer.hasRemaining()) {
					channel.write(buffer);
				}
			} else {
				os.write(value);
			}
		} catch (IOException e) {
			throw new TiffException("Failed to write bytes", e);
		}
	}

	/**
	 * Write a String
	 * 
	 * @param value
	 *            string value
	 * @return bytes written
	 * @throws IOException
	 *             upon failure to write
	 */
	public int writeString(String value) {
		byte[] valueBytes = value.getBytes();
		writeBytesInternal(valueBytes);
		return valueBytes.length;
	}

	/**
	 * Write a byte
	 * 
	 * @param value
	 *            byte
	 */
	public void writeByte(byte value) {
		writeBytesInternal(new byte[] { value });
	}

	/**
	 * Write an unsigned byte
	 * 
	 * @param value
	 *            unsigned byte as a short
	 */
	public void writeUnsignedByte(short value) {
		writeBytesInternal(new byte[] { (byte) (value & 0xff) });
	}

	/**
	 * Write the bytes
	 * 
	 * @param value
	 *            bytes
	 * @throws IOException
	 *             upon failure to write
	 */
	public void writeBytes(byte[] value) throws IOException {
		writeBytesInternal(value);
	}

	/**
	 * Write a short
	 * 
	 * @param value
	 *            short
	 * @throws IOException
	 *             upon failure to write
	 */
	public void writeShort(short value) throws IOException {
		byte[] valueBytes = new byte[2];
		ByteBuffer byteBuffer = ByteBuffer.allocate(2).order(byteOrder)
				.putShort(value);
		byteBuffer.flip();
		byteBuffer.get(valueBytes);
		writeBytesInternal(valueBytes);
	}

	/**
	 * Write an unsigned short
	 * 
	 * @param value
	 *            unsigned short as an int
	 * @throws IOException
	 *             upon failure to write
	 */
	public void writeUnsignedShort(int value) throws IOException {
		byte[] valueBytes = new byte[2];
		ByteBuffer byteBuffer = ByteBuffer.allocate(2).order(byteOrder)
				.putShort((short) (value & 0xffff));
		byteBuffer.flip();
		byteBuffer.get(valueBytes);
		writeBytesInternal(valueBytes);
	}

	/**
	 * Write an integer
	 * 
	 * @param value
	 *            int
	 * @throws IOException
	 *             upon failure to write
	 */
	public void writeInt(int value) throws IOException {
		byte[] valueBytes = new byte[4];
		ByteBuffer byteBuffer = ByteBuffer.allocate(4).order(byteOrder)
				.putInt(value);
		byteBuffer.flip();
		byteBuffer.get(valueBytes);
		writeBytesInternal(valueBytes);
	}

	/**
	 * Write an unsigned int
	 * 
	 * @param value
	 *            unsigned int as long
	 * @throws IOException
	 *             upon failure to write
	 */
	public void writeUnsignedInt(long value) throws IOException {
		byte[] valueBytes = new byte[4];
		ByteBuffer byteBuffer = ByteBuffer.allocate(4).order(byteOrder)
				.putInt((int) (value & 0xffffffffL));
		byteBuffer.flip();
		byteBuffer.get(valueBytes);
		writeBytesInternal(valueBytes);
	}

	/**
	 * Write a float
	 * 
	 * @param value
	 *            float
	 * @throws IOException
	 *             upon failure to write
	 */
	public void writeFloat(float value) throws IOException {
		byte[] valueBytes = new byte[4];
		ByteBuffer byteBuffer = ByteBuffer.allocate(4).order(byteOrder)
				.putFloat(value);
		byteBuffer.flip();
		byteBuffer.get(valueBytes);
		writeBytesInternal(valueBytes);
	}

	/**
	 * Write a double
	 * 
	 * @param value
	 *            double
	 * @throws IOException
	 *             upon failure to write
	 */
	public void writeDouble(double value) throws IOException {
		byte[] valueBytes = new byte[8];
		ByteBuffer byteBuffer = ByteBuffer.allocate(8).order(byteOrder)
				.putDouble(value);
		byteBuffer.flip();
		byteBuffer.get(valueBytes);
		writeBytesInternal(valueBytes);
	}

}
