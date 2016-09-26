package mil.nga.tiff;

/**
 * Field Types
 * 
 * @author osbornb
 */
public enum FieldType {

	/**
	 * 8-bit unsigned integer
	 */
	BYTE(1),

	/**
	 * 8-bit byte that contains a 7-bit ASCII code; the last byte must be NUL
	 * (binary zero)
	 */
	ASCII(1),

	/**
	 * 16-bit (2-byte) unsigned integer
	 */
	SHORT(2),

	/**
	 * 32-bit (4-byte) unsigned integer
	 */
	LONG(4),

	/**
	 * Two LONGs: the first represents the numerator of a fraction; the second,
	 * the denominator
	 */
	RATIONAL(8),

	/**
	 * An 8-bit signed (twos-complement) integer
	 */
	SBYTE(1),

	/**
	 * An 8-bit byte that may contain anything, depending on the definition of
	 * the field
	 */
	UNDEFINED(1),

	/**
	 * A 16-bit (2-byte) signed (twos-complement) integer
	 */
	SSHORT(2),

	/**
	 * A 32-bit (4-byte) signed (twos-complement) integer
	 */
	SLONG(4),

	/**
	 * Two SLONG’s: the first represents the numerator of a fraction, the second
	 * the denominator
	 */
	SRATIONAL(8),

	/**
	 * Single precision (4-byte) IEEE format
	 */
	FLOAT(4),

	/**
	 * Double precision (8-byte) IEEE format
	 */
	DOUBLE(8);

	/**
	 * Number of bytes per field value
	 */
	private final int bytes;

	/**
	 * Constructor
	 * 
	 * @param bytes
	 *            bytes per value
	 */
	private FieldType(int bytes) {
		this.bytes = bytes;
	}

	/**
	 * Get the field type value
	 * 
	 * @return field type value
	 */
	public int getValue() {
		return ordinal() + 1;
	}

	/**
	 * Get the number of bytes per value
	 * 
	 * @return number of bytes
	 */
	public int getBytes() {
		return bytes;
	}

	/**
	 * Get the field type
	 * 
	 * @param fieldType
	 *            field type number
	 * @return field type
	 */
	public static FieldType getFieldType(int fieldType) {
		return FieldType.values()[fieldType - 1];
	}

}
