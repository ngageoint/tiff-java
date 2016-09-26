package mil.nga.tiff.compression;

import java.nio.ByteOrder;

import mil.nga.tiff.util.TiffException;

/**
 * Deflate Compression
 * 
 * @author osbornb
 */
public class DeflateCompression implements CompressionDecoder,
		CompressionEncoder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] decodeBlock(byte[] block, ByteOrder byteOrder) {
		throw new TiffException("Deflate decoder is not yet implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] encodeBlock(byte[] block, ByteOrder byteOrder) {
		throw new TiffException("Deflate encoder is not yet implemented");
	}

}
