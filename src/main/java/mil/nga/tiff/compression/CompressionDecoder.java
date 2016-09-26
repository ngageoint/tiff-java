package mil.nga.tiff.compression;

import java.nio.ByteOrder;

/**
 * Compression decoder interface
 * 
 * @author osbornb
 */
public interface CompressionDecoder {

	/**
	 * Decode the byte block
	 * 
	 * @param block
	 *            block of bytes
	 * @param byteOrder
	 *            byte order
	 * @return decoded block of bytes
	 */
	public byte[] decodeBlock(byte[] block, ByteOrder byteOrder);

}
