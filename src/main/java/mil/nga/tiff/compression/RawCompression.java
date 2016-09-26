package mil.nga.tiff.compression;

import java.nio.ByteOrder;

/**
 * Raw / no compression
 * 
 * @author osbornb
 */
public class RawCompression implements CompressionDecoder, CompressionEncoder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] decodeBlock(byte[] block, ByteOrder byteOrder) {
		return block;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] encodeBlock(byte[] block, ByteOrder byteOrder) {
		return block;
	}

}
