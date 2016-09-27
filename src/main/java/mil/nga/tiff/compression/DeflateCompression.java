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
	public byte[] decode(byte[] bytes, ByteOrder byteOrder) {
		throw new TiffException("Deflate decoder is not yet implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean rowEncoding() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] encode(byte[] bytes, ByteOrder byteOrder) {
		throw new TiffException("Deflate encoder is not yet implemented");
	}

}
