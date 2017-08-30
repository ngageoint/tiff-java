package mil.nga.tiff;

import mil.nga.tiff.util.TiffConstants;

/**
 * TIFF File Directory Entry
 * 
 * @author osbornb
 */
public class FileDirectoryEntry implements Comparable<FileDirectoryEntry> {

	/**
	 * Field Tag Type
	 */
	private final TagName fieldTag;

	/**
	 * Field Type
	 */
	private final TagType tagType;

	/**
	 * Type Count
	 */
	private final long typeCount;

	/**
	 * Values
	 */
	private final Object values;

	/**
	 * Constructor
	 * 
	 * @param fieldTag
	 *            field tag type
	 * @param tagType
	 *            field type
	 * @param typeCount
	 *            type count
	 * @param values
	 *            values
	 */
	public FileDirectoryEntry(TagName fieldTag, TagType tagType,
			long typeCount, Object values) {
		this.fieldTag = fieldTag;
		this.tagType = tagType;
		this.typeCount = typeCount;
		this.values = values;
	}

	/**
	 * Get the field tag type
	 * 
	 * @return field tag type
	 */
	public TagName getFieldTag() {
		return fieldTag;
	}

	/**
	 * Get the field type
	 * 
	 * @return field type
	 */
	public TagType getTagType() {
		return tagType;
	}

	/**
	 * Get the type count
	 * 
	 * @return type count
	 */
	public long getTypeCount() {
		return typeCount;
	}

	/**
	 * Get the values
	 * 
	 * @return values
	 */
	public Object getValues() {
		return values;
	}

	/**
	 * Size in bytes of the image file directory entry and its values (not
	 * contiguous bytes)
	 * 
	 * @return size in bytes
	 */
	public long sizeWithValues() {
		long size = TiffConstants.IFD_ENTRY_BYTES + sizeOfValues();
		return size;
	}

	/**
	 * Size of the values not included in the directory entry bytes
	 * 
	 * @return size in bytes
	 */
	public long sizeOfValues() {
		long size = 0;
		long valueBytes = tagType.getBytes() * typeCount;
		if (valueBytes > 4) {
			size = valueBytes;
		}
		return size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(FileDirectoryEntry other) {
		return fieldTag.getId() - other.getFieldTag().getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return fieldTag.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileDirectoryEntry other = (FileDirectoryEntry) obj;
		if (fieldTag != other.fieldTag)
			return false;
		return true;
	}

}
