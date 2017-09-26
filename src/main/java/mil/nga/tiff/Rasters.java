package mil.nga.tiff;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.beans.annotations.NonNull;
import mil.nga.tiff.util.TiffConstants;
import mil.nga.tiff.util.TiffException;

/**
 * Raster image values
 * 
 * @author osbornb
 */
public class Rasters {

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
	 * Samples per pixel
	 */
	private final int samplesPerPixel;

	/**
	 * Bits per sample
	 */
	private final List<Integer> bitsPerSample;

	/**
	 * Field type for each sample. Can be total samplesPerPixel.
	 */
	private final FieldType[] sampleFieldTypes;

	/**
	 * Constructor
	 * 
	 * @param width
	 *            width of pixels
	 * @param height
	 *            height of pixels
	 * @param samplesPerPixel
	 *            samples per pixel
	 * @param bitsPerSample
	 *            bits per sample
	 * @param sampleFieldTypes
	 *            Field type for each sample
	 * @param sampleValues
	 *            empty sample values double array
	 */
	public Rasters(int width, int height, int samplesPerPixel,
			List<Integer> bitsPerSample, FieldType[] sampleFieldTypes, ByteBuffer[] sampleValues) {
		this(width, height, samplesPerPixel, bitsPerSample, sampleFieldTypes, sampleValues, null);
	}

	/**
	 * Constructor
	 * 
	 * @param width
	 *            width of pixels
	 * @param height
	 *            height of pixels
	 * @param samplesPerPixel
	 *            samples per pixel
	 * @param bitsPerSample
	 *            bits per sample
	 * @param sampleFieldTypes
	 *            Field type for each sample
	 * @param interleaveValues
	 *            empty interleaved values array
	 */
	public Rasters(int width, int height, int samplesPerPixel,
			List<Integer> bitsPerSample, FieldType[] sampleFieldTypes, ByteBuffer interleaveValues) {
		this(width, height, samplesPerPixel, bitsPerSample, sampleFieldTypes, null,
				interleaveValues);
	}

	/**
	 * Constructor
	 * 
	 * @param width
	 *            width of pixels
	 * @param height
	 *            height of pixels
	 * @param samplesPerPixel
	 *            samples per pixel
	 * @param bitsPerSample
	 *            bits per sample
	 * @param sampleFieldTypes
	 *            Field type for each sample
	 * @param sampleValues
	 *            empty sample values double array
	 * @param interleaveValues
	 *            empty interleaved values array
	 */
	public Rasters(int width, int height, int samplesPerPixel,
			List<Integer> bitsPerSample, FieldType[] sampleFieldTypes, ByteBuffer[] sampleValues,
			ByteBuffer interleaveValues) {
		this.width = width;
		this.height = height;
		this.samplesPerPixel = samplesPerPixel;
		this.bitsPerSample = bitsPerSample;
		this.sampleFieldTypes = sampleFieldTypes;
		this.sampleValues = sampleValues;
		this.interleaveValues = interleaveValues;
		validateValues();
		for (int bits : bitsPerSample) {
			if ((bits % 8) != 0) {
				throw new TiffException("Sample bit-width of " + bits
						+ " is not supported");
			}
		}
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
	 * Constructor
	 * 
	 * @param width
	 *            width of pixels
	 * @param height
	 *            height of pixels
	 * @param samplesPerPixel
	 *            samples per pixel
	 * @param bitsPerSample
	 *            bits per sample
	 */
	public Rasters(int width, int height, int samplesPerPixel,
			List<Integer> bitsPerSample, FieldType[] sampleFieldTypes, ByteOrder order) {
		this(width, height, samplesPerPixel, bitsPerSample, sampleFieldTypes,
				new ByteBuffer[samplesPerPixel]);
		for (int i = 0; i < sampleValues.length; ++i) {
			sampleValues[i] = ByteBuffer.allocate(width * height * sizeSample(i));
			sampleValues[i].order(order);
		}
	}

	/**
	 * Constructor
	 * 
	 * @param width
	 *            width of pixels
	 * @param height
	 *            height of pixels
	 * @param samplesPerPixel
	 *            samples per pixel
	 * @param bitsPerSample
	 *            bits per sample for all samples of a pixel
	 */
	public Rasters(int width, int height, int samplesPerPixel, int bitsPerSample, FieldType[] sampleFieldTypes, ByteOrder order) {
		this(width, height, samplesPerPixel, makeBitsPerSampleList(
				samplesPerPixel, bitsPerSample), sampleFieldTypes, order);
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
		writeSample(buffer, sampleFieldTypes[sampleIndex], value);
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
		return readSample(buffer, sampleFieldTypes[sampleIndex]);
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
				coordinate * sizeSample(sampleIndex), sampleIndex, value);
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
			bufferPos += sizeSample(i);

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
		return samplesPerPixel;
	}

	/**
	 * Get the bits per sample
	 * 
	 * @return bits per sample
	 */
	public List<Integer> getBitsPerSample() {
		return bitsPerSample;
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
		Number[] pixel = new Number[samplesPerPixel];

		// Get the pixel values from each sample
		if (sampleValues != null) {
			int sampleIndex = getSampleIndexInWindow(x, y);
			for (int i = 0; i < samplesPerPixel; i++) {
				int bufferIndex = sampleIndex * sizeSample(i);
				pixel[i] = getSampleFromByteBuffer(sampleValues[i], bufferIndex, i);
			}
		} else {
			int interleaveIndex = getInterleaveIndex(x, y);
			for (int i = 0; i < samplesPerPixel; i++) {
				pixel[i] = getSampleFromByteBuffer(interleaveValues, interleaveIndex, i);
				interleaveIndex += sizeSample(i);
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
			for (int i = 0; i < samplesPerPixel; i++) {
				int bufferIndex = getSampleIndexInWindow(x, y) * sizeSample(i);
				updateSampleInByteBuffer(sampleValues[i], bufferIndex, i, values[i]);
			}
		} else {
			int interleaveIndex = getSampleIndexInWindow(x, y) * sizePixel();
			for (int i = 0; i < samplesPerPixel; i++) {
				updateSampleInByteBuffer(interleaveValues, interleaveIndex, i, values[i]);
				interleaveIndex += sizeSample(i);
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
			for (int i = 0; i < samplesPerPixel; ++i) {
				sampleValues[i].position(y * getWidth() * sizeSample(i));
			}
			for (int i = 0; i < getWidth(); ++i) {
				for (int j = 0; j < samplesPerPixel; ++j) {
					writeSample(outBuffer, sampleValues[j], sampleFieldTypes[j]);
				}
			}
		} else {
			interleaveValues.position(y * getWidth() * sizePixel());

			for (int i = 0; i < getWidth(); ++i) {
				for (int j = 0; j < samplesPerPixel; ++j) {
					writeSample(outBuffer, interleaveValues, sampleFieldTypes[j]);
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
		ByteBuffer outBuffer = ByteBuffer.allocate(getWidth() * sizeSample(sample));
		outBuffer.order(newOrder);

		if (sampleValues != null) {
			sampleValues[sample].position(y * getWidth() * sizeSample(sample));
			for (int x = 0; x < getWidth(); ++x) {
				writeSample(outBuffer, sampleValues[sample], sampleFieldTypes[sample]);
			}
		} else {
			int sampleOffset = 0;
			for (int i = 0; i < sample; ++i) {
				sampleOffset += sizeSample(i);
			}

			for (int i = 0; i < getWidth(); ++i) {
				interleaveValues.position((y * getWidth() + i) * sizePixel() + sampleOffset);
				writeSample(outBuffer, interleaveValues, sampleFieldTypes[sample]);
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
			int bufferPos = getSampleIndexInWindow(x, y) * sizeSample(sample);
			pixelSample = getSampleFromByteBuffer(sampleValues[sample], bufferPos, sample);
		} else {
			int bufferPos = getInterleaveIndex(x, y);
			for (int i = 0; i < sample; i++)
				bufferPos += sizeSample(i);

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
			int sampleIndex = getSampleIndexInWindow(x, y) * sizeSample(sample);
			updateSampleInByteBuffer(sampleValues[sample], sampleIndex, sample, value);
		}
		if (interleaveValues != null) {
			int interleaveIndex = getSampleIndexInWindow(x, y) * sizePixel();
			for (int i = 0; i < sample; ++i) {
				interleaveIndex +=  sizeSample(i);
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
		int size = 0;
		for (int i = 0; i < samplesPerPixel; i++) {
			size += sizeSample(i);
		}
		return size;
	}

	/**
	 * Size in bytes of a sample
	 * 
	 * @param sample
	 *            sample index
	 * @return bytes
	 */
	public int sizeSample(int sample) {
		return bitsPerSample.get(sample) / 8;
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
		if (sample < 0 || sample >= samplesPerPixel) {
			throw new TiffException("Pixel sample out of bounds. sample: "
					+ sample + ", samples per pixel: " + samplesPerPixel);
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
			int bitsPerPixel = 0;
			for (int sampleBits : bitsPerSample) {
				bitsPerPixel += sampleBits;
			}
			rowsPerStrip = rowsPerStrip(bitsPerPixel, maxBytesPerStrip);
		} else {

			for (int sampleBits : bitsPerSample) {
				int rowsPerStripForSample = rowsPerStrip(sampleBits,
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
	 * @param bitsPerPixel
	 *            bits per pixel
	 * @param maxBytesPerStrip
	 *            max bytes per strip
	 * @return rows per strip
	 */
	private int rowsPerStrip(int bitsPerPixel, int maxBytesPerStrip) {

		int bytesPerPixel = (int) Math.ceil(bitsPerPixel / 8.0);
		int bytesPerRow = bytesPerPixel * width;

		int rowsPerStrip = Math.max(1, maxBytesPerStrip / bytesPerRow);

		return rowsPerStrip;
	}

	/**
	 * Make a bits per sample list where each samples of a pixel has the same
	 * value
	 * 
	 * @param samplesPerPixel
	 *            samples per pixel
	 * @param bitsPerSample
	 *            bits per sample for all samples of a pixel
	 * @return bits per sample list
	 */
	public static List<Integer> makeBitsPerSampleList(int samplesPerPixel,
			int bitsPerSample) {
		List<Integer> bitsPerSampleList = new ArrayList<Integer>();
		for (int i = 0; i < samplesPerPixel; ++i) {
			bitsPerSampleList.add(bitsPerSample);
		}
		return bitsPerSampleList;
	}


	/**
	 * Reads sample from given buffer
	 *
	 * @param buffer A buffer to read from. @note Make sure position is set.
	 * @param fieldType Field type to be read
	 * @return Sample form buffer
	 */
	private Number readSample(@NonNull ByteBuffer buffer, @NonNull FieldType fieldType) {
		Number sampleValue;

		switch (fieldType) {
			case BYTE:
				sampleValue = (short)(buffer.get() & 0xff);
				break;
			case SHORT:
				sampleValue = buffer.getShort() & 0xffff;
				break;
			case LONG:
				sampleValue = buffer.getInt() & 0xffffffffL;
				break;
			case SBYTE:
				sampleValue = buffer.get();
				break;
			case SSHORT:
				sampleValue = buffer.getShort();
				break;
			case SLONG:
				sampleValue = buffer.getInt();
				break;
			case FLOAT:
				sampleValue = buffer.getFloat();
				break;
			case DOUBLE:
				sampleValue = buffer.getDouble();
				break;
			default:
				throw new TiffException("Unsupported raster field type: " + fieldType);
		}

		return sampleValue;
	}

	/**
	 * Writes sample into given buffer.
	 *
	 * @param buffer A buffer to write to. @note Make sure buffer position is set.
	 * @param fieldType Field type to be written.
	 * @param value Actual value to write.
	 */
	private void writeSample(@NonNull ByteBuffer buffer, @NonNull FieldType fieldType, @NonNull Number value) {
		switch (fieldType) {
			case BYTE:
			case SBYTE:
				buffer.put(value.byteValue());
				break;
			case SHORT:
			case SSHORT:
				buffer.putShort(value.shortValue());
				break;
			case LONG:
			case SLONG:
				buffer.putInt(value.intValue());
				break;
			case FLOAT:
				buffer.putFloat(value.floatValue());
				break;
			case DOUBLE:
				buffer.putDouble(value.doubleValue());
				break;
			default:
				throw new TiffException("Unsupported raster field type: " + fieldType);
		}
	}

	/**
	 * Writes sample from input buffer to given output buffer.
	 *
	 * @param outBuffer A buffer to write to. @note Make sure buffer position is set.
	 * @param inBuffer A buffer to read from. @note Make sure buffer position is set.
	 * @param fieldType Field type to be read.
	 */
	private void writeSample(@NonNull ByteBuffer outBuffer, @NonNull ByteBuffer inBuffer,
							 @NonNull FieldType fieldType) {
		switch (fieldType)
		{
			case BYTE:
			case SBYTE:
				outBuffer.put(inBuffer.get());
				break;
			case SHORT:
			case SSHORT:
				outBuffer.putShort(inBuffer.getShort());
				break;
			case LONG:
			case SLONG:
				outBuffer.putInt(inBuffer.getInt());
				break;
			case FLOAT:
				outBuffer.putFloat(inBuffer.getFloat());
				break;
			case DOUBLE:
				outBuffer.putDouble(inBuffer.getDouble());
				break;
			default:
				throw new TiffException("Unsupported raster field type: " + fieldType);
		}
	}
}