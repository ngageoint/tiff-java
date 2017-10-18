package mil.nga.tiff;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mil.nga.tiff.util.TiffConstants;
import mil.nga.tiff.util.TiffException;

/**
 * Raster image values
 * 
 * @author osbornb
 */
public class Rasters {
	/**
	 * Sample type for pixel
	 */
	public enum SampleType {
		BYTE(1, TiffConstants.SAMPLE_FORMAT_UNSIGNED_INT),		// 8 bits
		SIGNED_BYTE(1, TiffConstants.SAMPLE_FORMAT_SIGNED_INT),	// 8 bits
		SHORT(2, TiffConstants.SAMPLE_FORMAT_UNSIGNED_INT),		// 16 bits
		SIGNED_SHORT(2, TiffConstants.SAMPLE_FORMAT_SIGNED_INT),// 16 bits
		LONG(4, TiffConstants.SAMPLE_FORMAT_UNSIGNED_INT),		// 32 bits
		SIGNED_LONG(4, TiffConstants.SAMPLE_FORMAT_SIGNED_INT),	// 32 bits
		FLOAT(4, TiffConstants.SAMPLE_FORMAT_FLOAT)	,			// 32 bits
		DOUBLE(8, TiffConstants.SAMPLE_FORMAT_FLOAT);			// 64 bits

		SampleType(int byteSize, int sampleFormat) {
			this.byteSize = byteSize;
			this.sampleFormat = sampleFormat;
		}

		int bitSize() { return  byteSize * 8; }

		final int byteSize;
		final int sampleFormat;
	}

	/**
	 * Values separated by sample
	 */
	private ByteBuffer[] sampleValues;

	/**
	 * Interleaved pixel sample values
	 */
	private ByteBuffer interleaveValues;

	/**
	 * Width of pixels
	 */
	private final int width;

	/**
	 * Height of pixels
	 */
	private final int height;

	/**
	 * Sample type for each sample.
	 */
	private final SampleType[] sampleTypes;

	// Calculated values

	/**
	 * Calculated pixel size in bytes
	 */
	private Integer pixelSize;

	/**
	 * @see getBitsPerSample()
	 */
	private List<Integer> bitsPerSample;

	/**
	 * @see getSampleFormat()
	 */
	private List<Integer> sampleFormat;

	/**
	 * Constructor
	 * 
	 * @param width
	 *            width of pixels
	 * @param height
	 *            height of pixels
	 * @param sampleTypes
	 *            Sample type for each sample
	 * @param sampleValues
	 *            empty sample values double array
	 */
	public Rasters(int width, int height, SampleType[] sampleTypes, ByteBuffer[] sampleValues) {
		this(width, height, sampleTypes, sampleValues, null);
	}

	/**
	 * Constructor
	 * 
	 * @param width
	 *            width of pixels
	 * @param height
	 *            height of pixels
	 * @param sampleTypes
	 *            Sample type for each sample
	 * @param interleaveValues
	 *            empty interleaved values array
	 */
	public Rasters(int width, int height, SampleType[] sampleTypes, ByteBuffer interleaveValues) {
		this(width, height, sampleTypes, null, interleaveValues);
	}

	/**
	 * Constructor
	 * 
	 * @param width
	 *            width of pixels
	 * @param height
	 *            height of pixels
	 * @param sampleTypes
	 *            Sample type for each sample
	 * @param sampleValues
	 *            empty sample values double array
	 * @param interleaveValues
	 *            empty interleaved values array
	 */
	public Rasters(int width, int height, SampleType[] sampleTypes, ByteBuffer[] sampleValues,
					ByteBuffer interleaveValues) {
		this.width = width;
		this.height = height;
		this.sampleTypes = sampleTypes;
		this.sampleValues = sampleValues;
		this.interleaveValues = interleaveValues;
		validateValues();
	}

	/**
	 *  Constructor
	 *
	 *  Creates Rasters object where given sample type used for each sample.
	 *
	 * @param width width of pixels
	 * @param height height of pixels
	 * @param samplesPerPixel number of samples per pixel
	 * @param sampleType type of sample for each sample
	 */
	public Rasters(int width, int height, int samplesPerPixel, SampleType sampleType) {
		this(width, height, createSampleTypeArray(samplesPerPixel, sampleType),
				ByteOrder.nativeOrder());
	}

	/**
	 * Validate that either sample or interleave values exist
	 */
	private void validateValues() {
		if (sampleValues == null && interleaveValues == null) {
			throw new TiffException(
					"Results must be sample and/or interleave based");
		}
	}

	/**
	 * Helper method used to create {@link SampleType} array of given size and
	 * filled with given values.
	 */
	private static SampleType[] createSampleTypeArray(int size, SampleType sampleType) {
		SampleType[] result = new SampleType[size];
		Arrays.fill(result, sampleType);
		return result;
	}

	/**
	 * Constructor
	 * 
	 * @param width
	 *            width of pixels
	 * @param height
	 *            height of pixels
	 * @param sampleTypes
	 *            Sample types per sample
	 */
	public Rasters(int width, int height, SampleType[] sampleTypes, ByteOrder order) {
		this(width, height, sampleTypes, new ByteBuffer[sampleTypes.length]);
		for (int i = 0; i < sampleValues.length; ++i) {
			sampleValues[i] = ByteBuffer
								.allocateDirect(width * height * sampleTypes[i].byteSize)
								.order(order);
		}
	}

	/**
	 * True if the results are stored by samples
	 * 
	 * @return true if results exist
	 */
	public boolean hasSampleValues() {
		return sampleValues != null;
	}

	/**
	 * True if the results are stored interleaved
	 * 
	 * @return true if results exist
	 */
	public boolean hasInterleaveValues() {
		return interleaveValues != null;
	}

	/**
	 * Updates sample to given value in buffer.
	 *
	 * @param buffer A buffer to be updated.
	 * @param bufferIndex Position in buffer where to update.
	 * @param sampleIndex Sample index in sampleFieldTypes. Needed for determining sample size.
	 * @param value A Number value to be put in buffer. Has to be same size as sampleFieldTypes[sampleIndex].
	 */
	private void updateSampleInByteBuffer(ByteBuffer buffer, int bufferIndex, int sampleIndex, Number value) {
		if (bufferIndex < 0 || bufferIndex >= buffer.capacity()) {
			throw new IndexOutOfBoundsException("index: " + bufferIndex + ". Buffer capacity: " + buffer.capacity());
		}

		buffer.position(bufferIndex);
		writeSample(buffer, sampleTypes[sampleIndex], value);
	}

	/**
	 * Reads sample from given buffer.
	 *
	 * @param buffer A buffer to read from
	 * @param index Position in buffer where to read from
	 * @param sampleIndex Index of sample type to read
	 * @return Number read from buffer
	 */
	private Number getSampleFromByteBuffer(ByteBuffer buffer, int index, int sampleIndex) {

		if (index < 0 || index >= buffer.capacity()) {
			throw new IndexOutOfBoundsException("Requested index: " + index +
												", but size of buffer is: " + buffer.capacity());
		}

		buffer.position(index);
		return readSample(buffer, sampleTypes[sampleIndex]);
	}

	/**
	 * Add a value to the sample results
	 * 
	 * @param sampleIndex
	 *            sample index
	 * @param coordinate
	 *            coordinate location
	 * @param value
	 *            value
	 */
	public void addToSample(int sampleIndex, int coordinate, Number value) {
		updateSampleInByteBuffer(sampleValues[sampleIndex],
				coordinate * sampleTypes[sampleIndex].byteSize, sampleIndex, value);
	}

	/**
	 * Add a value to the interleaved results
	 * 
	 * @param coordinate
	 *            coordinate location
	 * @param value
	 *            value
	 */
	public void addToInterleave(int coordinate, Number value, int sampleIndex) {
		int bufferPos = coordinate * sizePixel();
		for (int i = 0; i < sampleIndex; ++i)
			bufferPos += sampleTypes[i].byteSize;

		updateSampleInByteBuffer(interleaveValues, bufferPos, sampleIndex, value);
	}

	/**
	 * Get the width of pixels
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the height of pixels
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Return the number of pixels
	 * 
	 * @return number of pixels
	 */
	public int getNumPixels() {
		return width * height;
	}

	/**
	 * Get the number of samples per pixel
	 * 
	 * @return samples per pixel
	 */
	public int getSamplesPerPixel() {
		return sampleTypes.length;
	}

	/**
	 * Get the bits per sample
	 * 
	 * @return bits per sample
	 */
	public List<Integer> getBitsPerSample() {
		if (bitsPerSample != null) {
			return bitsPerSample;
		}

		List<Integer> result = new ArrayList<>(sampleTypes.length);
		for (SampleType sampleType : sampleTypes) {
			result.add(sampleType.bitSize());
		}
		bitsPerSample = result;
		return result;
	}

	/**
	 * Returns list of sample types constants
	 *
	 * Returns list of sample types constants (SAMPLE_FORMAT_UNSIGNED_INT,
	 * SAMPLE_FORMAT_SIGNED_INT or SAMPLE_FORMAT_FLOAT) for each sample in
	 * sample list @see getSampleTypes(). @see {@link TiffConstants}
	 * @return list of sample type constants
	 */
	public List<Integer> getSampleFormat() {
		if (sampleFormat != null) {
			return sampleFormat;
		}

		List<Integer> result = new ArrayList<>(sampleTypes.length);
		for (SampleType sampleType : sampleTypes) {
			result.add(sampleType.sampleFormat);
		}
		sampleFormat = result;
		return result;
	}

	/**
	 * Get the results stored by samples
	 * 
	 * @return sample values
	 */
	public ByteBuffer[] getSampleValues() {
		for (int i = 0; i < sampleValues.length; ++i) {
			sampleValues[i].rewind();
		}
		return sampleValues;
	}

	/**
	 * Set the results stored by samples
	 * 
	 * @param sampleValues
	 *            sample values
	 */
	public void setSampleValues(ByteBuffer[] sampleValues) {
		this.sampleValues = sampleValues;
		this.sampleFormat = null;
		this.bitsPerSample = null;
		this.pixelSize = null;
		validateValues();
	}

	/**
	 * Get the results stored as interleaved pixel samples
	 * 
	 * @return interleaved values
	 */
	public ByteBuffer getInterleaveValues() {
		interleaveValues.rewind();
		return interleaveValues;
	}

	/**
	 * Set the results stored as interleaved pixel samples
	 * 
	 * @param interleaveValues
	 *            interleaved values
	 */
	public void setInterleaveValues(ByteBuffer interleaveValues) {
		this.interleaveValues = interleaveValues;
		validateValues();
	}

	/**
	 * Get the pixel sample values
	 * 
	 * @param x
	 *            x coordinate (>= 0 && < {@link #getWidth()})
	 * @param y
	 *            y coordinate (>= 0 && < {@link #getHeight()})
	 * @return pixel sample values
	 */
	public Number[] getPixel(int x, int y) {

		validateCoordinates(x, y);

		// Pixel with each sample value
		Number[] pixel = new Number[getSamplesPerPixel()];

		// Get the pixel values from each sample
		if (sampleValues != null) {
			int sampleIndex = getSampleIndexInWindow(x, y);
			for (int i = 0; i < getSamplesPerPixel(); i++) {
				int bufferIndex = sampleIndex * sampleTypes[i].byteSize;
				pixel[i] = getSampleFromByteBuffer(sampleValues[i], bufferIndex, i);
			}
		} else {
			int interleaveIndex = getInterleaveIndex(x, y);
			for (int i = 0; i < getSamplesPerPixel(); i++) {
				pixel[i] = getSampleFromByteBuffer(interleaveValues, interleaveIndex, i);
				interleaveIndex += sampleTypes[i].byteSize;
			}
		}

		return pixel;
	}

	/**
	 * Set the pixel sample values
	 * 
	 * @param x
	 *            x coordinate (>= 0 && < {@link #getWidth()})
	 * @param y
	 *            y coordinate (>= 0 && < {@link #getHeight()})
	 * @param values
	 *            pixel values
	 */
	public void setPixel(int x, int y, Number[] values) {

		validateCoordinates(x, y);
		validateSample(values.length + 1);

		// Set the pixel values from each sample
		if (sampleValues != null) {
			for (int i = 0; i < getSamplesPerPixel(); i++) {
				int bufferIndex = getSampleIndexInWindow(x, y) * sampleTypes[i].byteSize;
				updateSampleInByteBuffer(sampleValues[i], bufferIndex, i, values[i]);
			}
		} else {
			int interleaveIndex = getSampleIndexInWindow(x, y) * sizePixel();
			for (int i = 0; i < getSamplesPerPixel(); i++) {
				updateSampleInByteBuffer(interleaveValues, interleaveIndex, i, values[i]);
				interleaveIndex += sampleTypes[i].byteSize;
			}
		}
	}

	/**
	 * Returns byte array of pixel row.
	 *
	 * @param y Row index
	 * @param newOrder Desired byte order of result byte array
	 * @return Byte array of pixel row
	 */
	public byte[] getPixelRow(int y, ByteOrder newOrder) {
		ByteBuffer outBuffer = ByteBuffer.allocate(getWidth() * sizePixel());
		outBuffer.order(newOrder);

		if (sampleValues != null) {
			for (int i = 0; i < getSamplesPerPixel(); ++i) {
				sampleValues[i].position(y * getWidth() * sampleTypes[i].byteSize);
			}
			for (int i = 0; i < getWidth(); ++i) {
				for (int j = 0; j < getSamplesPerPixel(); ++j) {
					writeSample(outBuffer, sampleValues[j], sampleTypes[j]);
				}
			}
		} else {
			interleaveValues.position(y * getWidth() * sizePixel());

			for (int i = 0; i < getWidth(); ++i) {
				for (int j = 0; j < getSamplesPerPixel(); ++j) {
					writeSample(outBuffer, interleaveValues, sampleTypes[j]);
				}
			}
		}

		return outBuffer.array();
	}

	/**
	 * Returns byte array of sample row.
	 *
	 * @param y Row index
	 * @param sample Sample index
	 * @param newOrder Desired byte order of resulting byte array
	 * @return Byte array of sample row
	 */
	public byte[] getSampleRow(int y, int sample, ByteOrder newOrder) {
		ByteBuffer outBuffer = ByteBuffer.allocate(getWidth() * sampleTypes[sample].byteSize);
		outBuffer.order(newOrder);

		if (sampleValues != null) {
			sampleValues[sample].position(y * getWidth() * sampleTypes[sample].byteSize);
			for (int x = 0; x < getWidth(); ++x) {
				writeSample(outBuffer, sampleValues[sample], sampleTypes[sample]);
			}
		} else {
			int sampleOffset = 0;
			for (int i = 0; i < sample; ++i) {
				sampleOffset += sampleTypes[sample].byteSize;
			}

			for (int i = 0; i < getWidth(); ++i) {
				interleaveValues.position((y * getWidth() + i) * sizePixel() + sampleOffset);
				writeSample(outBuffer, interleaveValues, sampleTypes[sample]);
			}
		}

		return outBuffer.array();
	}

	/**
	 * Get a pixel sample value
	 * 
	 * @param sample
	 *            sample index (>= 0 && < {@link #getSamplesPerPixel()})
	 * @param x
	 *            x coordinate (>= 0 && < {@link #getWidth()})
	 * @param y
	 *            y coordinate (>= 0 && < {@link #getHeight()})
	 * @return pixel sample
	 */
	public Number getPixelSample(int sample, int x, int y) {

		validateCoordinates(x, y);
		validateSample(sample);

		// Pixel sample value
		Number pixelSample = null;

		// Get the pixel sample
		if (sampleValues != null) {
			int bufferPos = getSampleIndexInWindow(x, y) * sampleTypes[sample].byteSize;
			pixelSample = getSampleFromByteBuffer(sampleValues[sample], bufferPos, sample);
		} else {
			int bufferPos = getInterleaveIndex(x, y);
			for (int i = 0; i < sample; i++)
				bufferPos += sampleTypes[sample].byteSize;

			pixelSample = getSampleFromByteBuffer(interleaveValues, bufferPos, sample);
		}

		return pixelSample;
	}

	/**
	 * Set a pixel sample value
	 * 
	 * @param sample
	 *            sample index (>= 0 && < {@link #getSamplesPerPixel()})
	 * @param x
	 *            x coordinate (>= 0 && < {@link #getWidth()})
	 * @param y
	 *            y coordinate (>= 0 && < {@link #getHeight()})
	 * @param value
	 *            pixel value
	 */
	public void setPixelSample(int sample, int x, int y, Number value) {

		validateCoordinates(x, y);
		validateSample(sample);

		// Set the pixel sample
		if (sampleValues != null) {
			int sampleIndex = getSampleIndexInWindow(x, y) * sampleTypes[sample].byteSize;
			updateSampleInByteBuffer(sampleValues[sample], sampleIndex, sample, value);
		}
		if (interleaveValues != null) {
			int interleaveIndex = getSampleIndexInWindow(x, y) * sizePixel();
			for (int i = 0; i < sample; ++i) {
				interleaveIndex += sampleTypes[sample].byteSize;
			}
			updateSampleInByteBuffer(interleaveValues, interleaveIndex, sample, value);
		}
	}

	/**
	 * Get the first pixel sample value, useful for single sample pixels
	 * (grayscale)
	 * 
	 * @param x
	 *            x coordinate (>= 0 && < {@link #getWidth()})
	 * @param y
	 *            y coordinate (>= 0 && < {@link #getHeight()})
	 * @return first pixel sample
	 */
	public Number getFirstPixelSample(int x, int y) {
		return getPixelSample(0, x, y);
	}

	/**
	 * Set the first pixel sample value, useful for single sample pixels
	 * (grayscale)
	 * 
	 * @param x
	 *            x coordinate (>= 0 && < {@link #getWidth()})
	 * @param y
	 *            y coordinate (>= 0 && < {@link #getHeight()})
	 * @param value
	 *            pixel value
	 */
	public void setFirstPixelSample(int x, int y, Number value) {
		setPixelSample(0, x, y, value);
	}

	/**
	 * Get the sample index location in window
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return sample index
	 */
	public int getSampleIndexInWindow(int x, int y) {
		return y * width + x;
	}

	/**
	 * Get the interleave index location
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return interleave index
	 */
	public int getInterleaveIndex(int x, int y) {
		return (y * width * sizePixel()) + (x * sizePixel());
	}

	/**
	 * Size in bytes of the image
	 * 
	 * @return bytes
	 */
	public int size() {
		return getNumPixels() * sizePixel();
	}

	/**
	 * Size in bytes of a pixel
	 * 
	 * @return bytes
	 */
	public int sizePixel() {
		if (pixelSize != null) {
			return pixelSize;
		}

		int size = 0;
		for (int i = 0; i < getSamplesPerPixel(); i++) {
			size += sampleTypes[i].byteSize;
		}
		pixelSize = size;
		return size;
	}

	/**
	 * Validate the coordinates range
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	private void validateCoordinates(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y > height) {
			throw new TiffException("Pixel oustide of raster range. Width: "
					+ width + ", Height: " + height + ", x: " + x + ", y: " + y);
		}
	}

	/**
	 * Validate the sample index
	 * 
	 * @param sample
	 *            sample index
	 */
	private void validateSample(int sample) {
		if (sample < 0 || sample >= getSamplesPerPixel()) {
			throw new TiffException("Pixel sample out of bounds. sample: "
					+ sample + ", samples per pixel: " + getSamplesPerPixel());
		}
	}

	/**
	 * Calculate the rows per strip to write
	 * 
	 * @param planarConfiguration
	 *            chunky or planar
	 * @return rows per strip
	 */
	public int calculateRowsPerStrip(int planarConfiguration) {
		return calculateRowsPerStrip(planarConfiguration,
				TiffConstants.DEFAULT_MAX_BYTES_PER_STRIP);
	}

	/**
	 * Calculate the rows per strip to write
	 * 
	 * @param planarConfiguration
	 *            chunky or planar
	 * @param maxBytesPerStrip
	 *            attempted max bytes per strip
	 * @return rows per strip
	 */
	public int calculateRowsPerStrip(int planarConfiguration,
			int maxBytesPerStrip) {

		Integer rowsPerStrip = null;

		if (planarConfiguration == TiffConstants.PLANAR_CONFIGURATION_CHUNKY) {
			rowsPerStrip = rowsPerStrip(sizePixel(), maxBytesPerStrip);
		} else {

			for (int sample = 0; sample < getSamplesPerPixel(); ++sample) {
				int rowsPerStripForSample = rowsPerStrip(sampleTypes[sample].byteSize,
						maxBytesPerStrip);
				if (rowsPerStrip == null
						|| rowsPerStripForSample < rowsPerStrip) {
					rowsPerStrip = rowsPerStripForSample;
				}
			}
		}

		return rowsPerStrip;
	}

	/**
	 * Get the rows per strip based upon the bits per pixel and max bytes per
	 * strip
	 * 
	 * @param bytesPerPixel
	 *            bytes per pixel
	 * @param maxBytesPerStrip
	 *            max bytes per strip
	 * @return rows per strip
	 */
	private int rowsPerStrip(int bytesPerPixel, int maxBytesPerStrip) {

		int bytesPerRow = bytesPerPixel * width;

		int rowsPerStrip = Math.max(1, maxBytesPerStrip / bytesPerRow);

		return rowsPerStrip;
	}

	/**
	 * Reads sample from given buffer
	 *
	 * @param buffer A buffer to read from. @note Make sure position is set.
	 * @param sampleType Sample type to be read
	 * @return Sample form buffer
	 */
	private Number readSample(ByteBuffer buffer, SampleType sampleType) {
		Number sampleValue;

		switch (sampleType) {
			case BYTE:
				sampleValue = (short)(buffer.get() & 0xff);
				break;
			case SHORT:
				sampleValue = buffer.getShort() & 0xffff;
				break;
			case LONG:
				sampleValue = buffer.getInt() & 0xffffffffL;
				break;
			case SIGNED_BYTE:
				sampleValue = buffer.get();
				break;
			case SIGNED_SHORT:
				sampleValue = buffer.getShort();
				break;
			case SIGNED_LONG:
				sampleValue = buffer.getInt();
				break;
			case FLOAT:
				sampleValue = buffer.getFloat();
				break;
			case DOUBLE:
				sampleValue = buffer.getDouble();
				break;
			default:
				throw new TiffException("Unsupported raster sample type: " + sampleType);
		}

		return sampleValue;
	}

	/**
	 * Writes sample into given buffer.
	 *
	 * @param buffer A buffer to write to. @note Make sure buffer position is set.
	 * @param sampleType Sample type to be written.
	 * @param value Actual value to write.
	 */
	private void writeSample(ByteBuffer buffer, SampleType sampleType, Number value) {
		switch (sampleType) {
			case BYTE:
			case SIGNED_BYTE:
				buffer.put(value.byteValue());
				break;
			case SHORT:
			case SIGNED_SHORT:
				buffer.putShort(value.shortValue());
				break;
			case LONG:
			case SIGNED_LONG:
				buffer.putInt(value.intValue());
				break;
			case FLOAT:
				buffer.putFloat(value.floatValue());
				break;
			case DOUBLE:
				buffer.putDouble(value.doubleValue());
				break;
			default:
				throw new TiffException("Unsupported raster sample type: " + sampleType);
		}
	}

	/**
	 * Writes sample from input buffer to given output buffer.
	 *
	 * @param outBuffer A buffer to write to. @note Make sure buffer position is set.
	 * @param inBuffer A buffer to read from. @note Make sure buffer position is set.
	 * @param sampleType Field type to be read.
	 */
	private void writeSample(ByteBuffer outBuffer, ByteBuffer inBuffer,
							 SampleType sampleType) {
		switch (sampleType)
		{
			case BYTE:
			case SIGNED_BYTE:
				outBuffer.put(inBuffer.get());
				break;
			case SHORT:
			case SIGNED_SHORT:
				outBuffer.putShort(inBuffer.getShort());
				break;
			case LONG:
			case SIGNED_LONG:
				outBuffer.putInt(inBuffer.getInt());
				break;
			case FLOAT:
				outBuffer.putFloat(inBuffer.getFloat());
				break;
			case DOUBLE:
				outBuffer.putDouble(inBuffer.getDouble());
				break;
			default:
				throw new TiffException("Unsupported raster sample type: " + sampleType);
		}
	}

	/**
	 * Returns sample types
	 */
	public SampleType[] getSampleTypes() {
		return sampleTypes;
	}
}