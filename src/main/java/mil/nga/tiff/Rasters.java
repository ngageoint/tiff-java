package mil.nga.tiff;

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
	 * Values separated by sample
	 */
	private Number[][] sampleValues;

	/**
	 * Interleaved pixel sample values
	 */
	private Number[] interleaveValues;

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
	 * @param sampleValues
	 *            empty sample values double array
	 */
	public Rasters(int width, int height, int samplesPerPixel,
			List<Integer> bitsPerSample, Number[][] sampleValues) {
		this(width, height, samplesPerPixel, bitsPerSample, sampleValues, null);
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
	 * @param interleaveValues
	 *            empty interleaved values array
	 */
	public Rasters(int width, int height, int samplesPerPixel,
			List<Integer> bitsPerSample, Number[] interleaveValues) {
		this(width, height, samplesPerPixel, bitsPerSample, null,
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
	 * @param sampleValues
	 *            empty sample values double array
	 * @param interleaveValues
	 *            empty interleaved values array
	 */
	public Rasters(int width, int height, int samplesPerPixel,
			List<Integer> bitsPerSample, Number[][] sampleValues,
			Number[] interleaveValues) {
		this.width = width;
		this.height = height;
		this.samplesPerPixel = samplesPerPixel;
		this.bitsPerSample = bitsPerSample;
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
			List<Integer> bitsPerSample) {
		this(width, height, samplesPerPixel, bitsPerSample,
				new Number[samplesPerPixel][width * height]);
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
	 *            single sample bits per sample
	 */
	public Rasters(int width, int height, int samplesPerPixel, int bitsPerSample) {
		this(width, height, samplesPerPixel, new ArrayList<Integer>(
				Arrays.asList(bitsPerSample)));
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
		sampleValues[sampleIndex][coordinate] = value;
	}

	/**
	 * Add a value to the interleaved results
	 * 
	 * @param coordinate
	 *            coordinate location
	 * @param value
	 *            value
	 */
	public void addToInterleave(int coordinate, Number value) {
		interleaveValues[coordinate] = value;
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
	public Number[][] getSampleValues() {
		return sampleValues;
	}

	/**
	 * Set the results stored by samples
	 * 
	 * @param sampleValues
	 *            sample values
	 */
	public void setSampleValues(Number[][] sampleValues) {
		this.sampleValues = sampleValues;
		validateValues();
	}

	/**
	 * Get the results stored as interleaved pixel samples
	 * 
	 * @return interleaved values
	 */
	public Number[] getInterleaveValues() {
		return interleaveValues;
	}

	/**
	 * Set the results stored as interleaved pixel samples
	 * 
	 * @param interleaveValues
	 *            interleaved values
	 */
	public void setInterleaveValues(Number[] interleaveValues) {
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
			int sampleIndex = getSampleIndex(x, y);
			for (int i = 0; i < samplesPerPixel; i++) {
				pixel[i] = sampleValues[i][sampleIndex];
			}
		} else {
			int interleaveIndex = getInterleaveIndex(x, y);
			for (int i = 0; i < samplesPerPixel; i++) {
				pixel[i] = interleaveValues[interleaveIndex++];
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
			int sampleIndex = getSampleIndex(x, y);
			for (int i = 0; i < samplesPerPixel; i++) {
				sampleValues[i][sampleIndex] = values[i];
			}
		} else {
			int interleaveIndex = getInterleaveIndex(x, y);
			for (int i = 0; i < samplesPerPixel; i++) {
				interleaveValues[interleaveIndex++] = values[i];
			}
		}
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
			int sampleIndex = getSampleIndex(x, y);
			pixelSample = sampleValues[sample][sampleIndex];
		} else {
			int interleaveIndex = getInterleaveIndex(x, y);
			pixelSample = interleaveValues[interleaveIndex + sample];
		}

		return pixelSample;
	}

	/**
	 * Set a pixel vample value
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
			int sampleIndex = getSampleIndex(x, y);
			sampleValues[sample][sampleIndex] = value;
		}
		if (interleaveValues != null) {
			int interleaveIndex = getInterleaveIndex(x, y);
			interleaveValues[interleaveIndex + sample] = value;
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
	 * Get the sample index location
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return sample index
	 */
	public int getSampleIndex(int x, int y) {
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
		return (y * width * samplesPerPixel) + (x * samplesPerPixel);
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

}