package mil.nga.tiff.compression;

import java.nio.ByteOrder;

/**
 * Compression encoder interface
 * 
 * @author osbornb
 */
public interface CompressionEncoder {

	/**
	 * Encode the byte block
	 * 
	 * @param block
	 *            block of bytes
	 * @param byteOrder
	 *            byte order
	 * @return encoded block of bytes
	 */
	public byte[] encodeBlock(byte[] block, ByteOrder byteOrder);

}
