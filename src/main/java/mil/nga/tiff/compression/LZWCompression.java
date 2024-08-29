package mil.nga.tiff.compression;

import java.io.ByteArrayOutputStream;
import java.nio.ByteOrder;

import mil.nga.tiff.io.ByteReader;
import mil.nga.tiff.util.TiffException;

/**
 * LZW Compression
 * 
 * @author osbornb
 */
public class LZWCompression implements CompressionDecoder, CompressionEncoder {

	/**
	 * Clear code
	 */
	private static final int CLEAR_CODE = 256;

	/**
	 * End of information code
	 */
	private static final int EOI_CODE = 257;

	/**
	 * Min bits
	 */
	private static final int MIN_BITS = 9;

	/**
	 * Max bits
	 */
	private static final int MAX_BITS = 12;

	/**
	 * Table entries
	 */
	private byte[][] table;

	/**
	 * Current max table code
	 */
	private int maxCode;

	/**
	 * Current byte length
	 */
	private int byteLength;
	
	/**
	 * The next code to cause a bit size increase
	 */
	private int nextBitSizeIncrease;
	
	/**
	 * Any remaining code info from previous code reads.
	 */
	private int codeRemainder;

	/**
	 * The number of bits stored in codeRemainder
	 */
	private int numBitsInCodeRemainer;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] decode(byte[] bytes, ByteOrder byteOrder) {

		// Create the byte reader and decoded stream to write to
		ByteReader reader = new ByteReader(bytes, byteOrder);
		ByteArrayOutputStream decodedStream = new ByteArrayOutputStream();

		// Initialize the table, starting position, and old code
		initializeTable();
		codeRemainder = 0;
		numBitsInCodeRemainer = 0;
		byte[] oldValue = null;

		// Read codes until end of input
		int code = getNextCode(reader);
		while (code != EOI_CODE) {

			// If a clear code
			if (code == CLEAR_CODE) {

				// Reset the table
				initializeTable();

				// Read past clear codes
				code = getNextCode(reader);
				while (code == CLEAR_CODE) {
					code = getNextCode(reader);
				}
				if (code == EOI_CODE) {
					break;
				}
				if (code > CLEAR_CODE) {
					throw new TiffException("Corrupted code at scan line: "
							+ code);
				}

				// Write the code value
				byte[] value = table[code];
				decodedStream.writeBytes(value);
				oldValue = value;

			} else {

				// If already in the table
			    byte[] value = table[code];
				if (value != null) {

					// Write the code value
				    decodedStream.writeBytes(value);

					// Create new value and add to table
				    byte[] newValue = combine(oldValue, value);
					addToTable(newValue);
					oldValue = value;

				} else {

					// Create and write new value from old value
					byte[] newValue = combine(oldValue, oldValue);
					decodedStream.writeBytes(newValue);

					// Write value to the table
					addToTable(newValue);
					oldValue = newValue;
				}
			}

			// Get the next code
			code = getNextCode(reader);
		}

		byte[] decoded = decodedStream.toByteArray();

		return decoded;
	}

	/**
	 * Initialize the table and byte length
	 */
	private void initializeTable() {
        table = new byte[ 2<<(MAX_BITS-1) ][];//size is 4096
		for (int i = 0; i < 256; i++) {
			table[i] = new byte[]{ (byte)i };
		}
		maxCode = 257;
		nextBitSizeIncrease = (2<<(MIN_BITS-1)) - 2;//510
		byteLength = MIN_BITS;
	}

	/**
	 * Check the byte length and increase if needed
	 */
	private void checkByteLength() {
        if( maxCode == nextBitSizeIncrease )
        {
            nextBitSizeIncrease = nextBitSizeIncrease*2 + 2;
            if( byteLength < MAX_BITS )
            {
                byteLength++;
            }
        }
	}

	/**
	 * Add the value to the table
	 * 
	 * @param value
	 *            value
	 */
	private void addToTable(byte[] value) {
	    table[++maxCode] = value;
		checkByteLength();
	}

	/**
	 * Combines the two values such that the result is the concatenation of the first value and the first element in the second value.
	 * 
	 * @param first
	 *            first value
	 * @param second
	 *            second value
	 * @return concatenated value
	 */
	private byte[] combine(byte[] first, byte[] second) {
	    byte[] combined = new byte[first.length + 1];
		System.arraycopy(first, 0, combined, 0, first.length);
		combined[first.length] = second[0];
		return combined;
	}

	/**
	 * Get the next code
	 * 
	 * @param reader
	 *            byte reader
	 * @return code
	 */
	private int getNextCode(ByteReader reader) {
        while( numBitsInCodeRemainer < byteLength ) {
            if( !reader.hasByte() ) {
                return EOI_CODE;
            }
            codeRemainder = (codeRemainder<<8) | reader.readUnsignedByte();
            numBitsInCodeRemainer += 8;
        }

        numBitsInCodeRemainer -= byteLength;
        int code = codeRemainder >>> numBitsInCodeRemainer;
        codeRemainder ^= code << numBitsInCodeRemainer;

        return code;
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
		throw new TiffException("LZW encoder is not yet implemented");
	}

}
