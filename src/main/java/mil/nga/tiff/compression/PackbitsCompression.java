package mil.nga.tiff.compression;

import java.io.ByteArrayOutputStream;
import java.nio.ByteOrder;

import mil.nga.tiff.io.ByteReader;
import mil.nga.tiff.util.TiffException;

/**
 * Packbits Compression
 * 
 * @author osbornb
 */
public class PackbitsCompression implements CompressionDecoder,
		CompressionEncoder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] decode(byte[] bytes, ByteOrder byteOrder) {

		ByteReader reader = new ByteReader(bytes, byteOrder);

		ByteArrayOutputStream decodedStream = new ByteArrayOutputStream();

		try {
			while (reader.hasByte()) {
				int header = reader.readByte();
				if (header != -128) {
					if (header < 0) {
						int next = reader.readUnsignedByte();
						header = -header;
						for (int i = 0; i <= header; i++) {
							decodedStream.write(next);
						}
					} else {
						for (int i = 0; i <= header; i++) {
							decodedStream.write(reader.readUnsignedByte());
						}
					}
				}
			}

			byte[] decoded = decodedStream.toByteArray();

			return decoded;
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				// Ignore close exception
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean rowEncoding() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] encode(byte[] bytes, ByteOrder byteOrder) {
		throw new TiffException("Packbits encoder is not yet implemented");
	}

}
