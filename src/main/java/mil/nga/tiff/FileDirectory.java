package mil.nga.tiff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import mil.nga.tiff.compression.CompressionDecoder;
import mil.nga.tiff.compression.DeflateCompression;
import mil.nga.tiff.compression.LZWCompression;
import mil.nga.tiff.compression.PackbitsCompression;
import mil.nga.tiff.compression.RawCompression;
import mil.nga.tiff.io.ByteReader;
import mil.nga.tiff.util.TiffConstants;
import mil.nga.tiff.util.TiffException;

/**
 * File Directory, represents all directory entries and can be used to read the
 * image raster
 * 
 * @author osbornb
 */
public class FileDirectory {

	/**
	 * File directory entries in sorted tag id order
	 */
	private final SortedSet<FileDirectoryEntry> entries;

	/**
	 * Mapping between tags and entries
	 */
	private final Map<TagName, FileDirectoryEntry> fieldTagTypeMapping = new HashMap<>();

	/**
	 * Byte reader
	 */
	private ByteReader reader;

	/**
	 * Tiled flag
	 */
	private boolean tiled;

	/**
	 * Planar configuration
	 */
	private int planarConfiguration;

	/**
	 * Compression decoder
	 */
	private CompressionDecoder decoder;

	/**
	 * Cache
	 */
	private Map<Integer, byte[]> cache = null;

	/**
	 * Rasters to write to the TIFF file
	 */
	private Rasters writeRasters = null;

	/**
	 * Last block index, index of single block cache
	 */
	private int lastBlockIndex = -1;

	/**
	 * Last block, single block cache when caching is not enabled
	 */
	private byte[] lastBlock;

	/**
	 * Constructor, for reading TIFF files
	 * 
	 * @param entries
	 *            file directory entries
	 * @param reader
	 *            TIFF file byte reader
	 */
	public FileDirectory(SortedSet<FileDirectoryEntry> entries,
			ByteReader reader) {
		this(entries, reader, false);
	}

	/**
	 * Constructor, for reading TIFF files
	 * 
	 * @param entries
	 *            file directory entries
	 * @param reader
	 *            TIFF file byte reader
	 * @param cacheData
	 *            true to cache tiles and strips
	 */
	public FileDirectory(SortedSet<FileDirectoryEntry> entries,
			ByteReader reader, boolean cacheData) {
		// Set the entries and the field tag type mapping
		this.entries = entries;
		for (FileDirectoryEntry entry : entries) {
			fieldTagTypeMapping.put(entry.getFieldTag(), entry);
		}

		this.reader = reader;

		// Set the cache
		setCache(cacheData);

		// Determine if tiled
		tiled = getRowsPerStrip() == null;

		// Determine and validate the planar configuration
		Integer pc = getPlanarConfiguration();
		planarConfiguration = pc != null ? pc
				: TiffConstants.PLANAR_CONFIGURATION_CHUNKY;
		if (planarConfiguration != TiffConstants.PLANAR_CONFIGURATION_CHUNKY
				&& planarConfiguration != TiffConstants.PLANAR_CONFIGURATION_PLANAR) {
			throw new TiffException("Invalid planar configuration: "
					+ planarConfiguration);
		}

		// Determine the decoder based upon the compression
		Integer compression = getCompression();
		if (compression == null) {
			compression = TiffConstants.COMPRESSION_NO;
		}
		switch (compression) {
		case TiffConstants.COMPRESSION_NO:
			decoder = new RawCompression();
			break;
		case TiffConstants.COMPRESSION_CCITT_HUFFMAN:
			throw new TiffException("CCITT Huffman compression not supported: "
					+ compression);
		case TiffConstants.COMPRESSION_T4:
			throw new TiffException("T4-encoding compression not supported: "
					+ compression);
		case TiffConstants.COMPRESSION_T6:
			throw new TiffException("T6-encoding compression not supported: "
					+ compression);
		case TiffConstants.COMPRESSION_LZW:
			decoder = new LZWCompression();
			break;
		case TiffConstants.COMPRESSION_JPEG_OLD:
		case TiffConstants.COMPRESSION_JPEG_NEW:
			throw new TiffException("JPEG compression not supported: "
					+ compression);
		case TiffConstants.COMPRESSION_DEFLATE:
			decoder = new DeflateCompression();
			break;
		case TiffConstants.COMPRESSION_PACKBITS:
			decoder = new PackbitsCompression();
			break;
		default:
			throw new TiffException("Unknown compression method identifier: "
					+ compression);
		}
	}

	/**
	 * Constructor, for writing TIFF files
	 */
	public FileDirectory() {
		this(null);
	}

	/**
	 * Constructor, for writing TIFF files
	 * 
	 * @param rasters
	 *            image rasters to write
	 */
	public FileDirectory(Rasters rasters) {
		this(new TreeSet<FileDirectoryEntry>(), rasters);
	}

	/**
	 * Constructor, for writing TIFF files
	 * 
	 * @param entries
	 *            file directory entries
	 * @param rasters
	 *            image rasters to write
	 */
	public FileDirectory(SortedSet<FileDirectoryEntry> entries, Rasters rasters) {
		this.entries = entries;
		for (FileDirectoryEntry entry : entries) {
			fieldTagTypeMapping.put(entry.getFieldTag(), entry);
		}
		this.writeRasters = rasters;
	}

	/**
	 * Add an entry
	 * 
	 * @param entry
	 *            file directory entry
	 */
	public void addEntry(FileDirectoryEntry entry) {
		entries.remove(entry);
		entries.add(entry);
		fieldTagTypeMapping.put(entry.getFieldTag(), entry);
	}

	/**
	 * Set whether to cache tiles. Does nothing is already caching tiles, clears
	 * the existing cache if set to false.
	 * 
	 * @param cacheData
	 *            true to cache tiles and strips
	 */
	public void setCache(boolean cacheData) {
		if (cacheData) {
			if (cache == null) {
				cache = new HashMap<>();
			}
		} else {
			cache = null;
		}
	}

	/**
	 * Get the byte reader
	 * 
	 * @return byte reader
	 */
	public ByteReader getReader() {
		return reader;
	}

	/**
	 * Is this a tiled image
	 * 
	 * @return true if tiled
	 */
	public boolean isTiled() {
		return tiled;
	}

	/**
	 * Get the compression decoder
	 * 
	 * @return compression decoder
	 */
	public CompressionDecoder getDecoder() {
		return decoder;
	}

	/**
	 * Get the number of entries
	 * 
	 * @return entry count
	 */
	public int numEntries() {
		return entries.size();
	}

	/**
	 * Get a file directory entry from the field tag type
	 * 
	 * @param tagName
	 *            field tag type
	 * @return file directory entry
	 */
	public FileDirectoryEntry get(TagName tagName) {
		return fieldTagTypeMapping.get(tagName);
	}

	/**
	 * Get the file directory entries
	 * 
	 * @return file directory entries
	 */
	public Set<FileDirectoryEntry> getEntries() {
		return Collections.unmodifiableSet(entries);
	}

	/**
	 * Get the field tag type to file directory entry mapping
	 * 
	 * @return field tag type mapping
	 */
	public Map<TagName, FileDirectoryEntry> getFieldTagTypeMapping() {
		return Collections.unmodifiableMap(fieldTagTypeMapping);
	}

	/**
	 * Get the image width
	 * 
	 * @return image width
	 */
	public Number getImageWidth() {
		return getNumberTag(TagName.ImageWidth);
	}

	/**
	 * Set the image width
	 * 
	 * @param width
	 *            image width
	 */
	public void setImageWidth(int width) {
		setUnsignedIntegerTag(TagName.ImageWidth, width);
	}

	/**
	 * Set the image width
	 * 
	 * @param width
	 *            image width
	 */
	public void setImageWidthAsLong(long width) {
		setUnsignedLongTag(TagName.ImageWidth, width);
	}

	/**
	 * Get the image height
	 * 
	 * @return image height
	 */
	public Number getImageHeight() {
		return getNumberTag(TagName.ImageLength);
	}

	/**
	 * Set the image height
	 * 
	 * @param height
	 *            image height
	 */
	public void setImageHeight(int height) {
		setUnsignedIntegerTag(TagName.ImageLength, height);
	}

	/**
	 * Set the image height
	 * 
	 * @param height
	 *            image height
	 */
	public void setImageHeightAsLong(long height) {
		setUnsignedLongTag(TagName.ImageLength, height);
	}

	/**
	 * Get the bits per sample
	 * 
	 * @return bits per sample
	 */
	public List<Integer> getBitsPerSample() {
		return getIntegerListTag(TagName.BitsPerSample);
	}

	/**
	 * Set the bits per sample
	 * 
	 * @param bitsPerSample
	 *            bits per sample
	 */
	public void setBitsPerSample(List<Integer> bitsPerSample) {
		setUnsignedIntegerListTag(TagName.BitsPerSample,
				bitsPerSample);
	}

	/**
	 * Set a single value bits per sample
	 * 
	 * @param bitsPerSample
	 *            bits per sample
	 */
	public void setBitsPerSample(int bitsPerSample) {
		setBitsPerSample(createSingleIntegerList(bitsPerSample));
	}

	/**
	 * Get the max bits per sample
	 * 
	 * @return max bits per sample
	 */
	public Integer getMaxBitsPerSample() {
		return getMaxIntegerTag(TagName.BitsPerSample);
	}

	/**
	 * Get the compression
	 * 
	 * @return compression
	 */
	public Integer getCompression() {
		return getIntegerTag(TagName.Compression);
	}

	/**
	 * Set the compression
	 * 
	 * @param compression
	 *            compression
	 */
	public void setCompression(int compression) {
		setUnsignedIntegerTag(TagName.Compression, compression);
	}

	/**
	 * Get the photometric interpretation
	 * 
	 * @return photometric interpretation
	 */
	public Integer getPhotometricInterpretation() {
		return getIntegerTag(TagName.PhotometricInterpretation);
	}

	/**
	 * Set the photometric interpretation
	 * 
	 * @param photometricInterpretation
	 *            photometric interpretation
	 */
	public void setPhotometricInterpretation(int photometricInterpretation) {
		setUnsignedIntegerTag(TagName.PhotometricInterpretation,
				photometricInterpretation);
	}

	/**
	 * Get the strip offsets
	 * 
	 * @return strip offsets
	 */
	public List<Number> getStripOffsets() {
		return getNumberListTag(TagName.StripOffsets);
	}

	/**
	 * Set the strip offsets
	 * 
	 * @param stripOffsets
	 *            strip offsets
	 */
	public void setStripOffsets(List<Integer> stripOffsets) {
		setUnsignedIntegerListTag(TagName.StripOffsets,
				stripOffsets);
	}

	/**
	 * Set the strip offsets
	 * 
	 * @param stripOffsets
	 *            strip offsets
	 */
	public void setStripOffsetsAsLongs(List<Long> stripOffsets) {
		setUnsignedLongListTag(TagName.StripOffsets, stripOffsets);
	}

	/**
	 * Set a single value strip offset
	 * 
	 * @param stripOffset
	 *            strip offset
	 */
	public void setStripOffsets(int stripOffset) {
		setStripOffsets(createSingleIntegerList(stripOffset));
	}

	/**
	 * Set a single value strip offset
	 * 
	 * @param stripOffset
	 *            strip offset
	 */
	public void setStripOffsets(long stripOffset) {
		setStripOffsetsAsLongs(createSingleLongList(stripOffset));
	}

	/**
	 * Get the samples per pixel
	 * 
	 * @return samples per pixel
	 */
	public Integer getSamplesPerPixel() {
		Integer samplesPerPixel = getIntegerTag(TagName.SamplesPerPixel);
		if (samplesPerPixel == null) {
			// if SamplesPerPixel tag is missing, try using length of
			// BitsPerSample list
			List<Integer> bitsPerSampleList = getBitsPerSample();
			if (bitsPerSampleList != null) {
				samplesPerPixel = bitsPerSampleList.size();
			}
		}

		return samplesPerPixel;
	}

	/**
	 * Set the samples per pixel
	 * 
	 * @param samplesPerPixel
	 *            samples per pixel
	 */
	public void setSamplesPerPixel(int samplesPerPixel) {
		setUnsignedIntegerTag(TagName.SamplesPerPixel,
				samplesPerPixel);
	}

	/**
	 * Get the rows per strip
	 * 
	 * @return rows per strip
	 */
	public Number getRowsPerStrip() {
		return getNumberTag(TagName.RowsPerStrip);
	}

	/**
	 * Set the rows per strip
	 * 
	 * @param rowsPerStrip
	 *            rows per strip
	 */
	public void setRowsPerStrip(int rowsPerStrip) {
		setUnsignedIntegerTag(TagName.RowsPerStrip, rowsPerStrip);
	}

	/**
	 * Set the rows per strip
	 * 
	 * @param rowsPerStrip
	 *            rows per strip
	 */
	public void setRowsPerStripAsLong(long rowsPerStrip) {
		setUnsignedLongTag(TagName.RowsPerStrip, rowsPerStrip);
	}

	/**
	 * Get the strip byte counts
	 * 
	 * @return strip byte counts
	 */
	public List<Number> getStripByteCounts() {
		return getNumberListTag(TagName.StripByteCounts);
	}

	/**
	 * Set the strip byte counts
	 * 
	 * @param stripByteCounts
	 *            strip byte counts
	 */
	public void setStripByteCounts(List<Integer> stripByteCounts) {
		setUnsignedIntegerListTag(TagName.StripByteCounts,
				stripByteCounts);
	}

	/**
	 * Set the strip byte counts
	 * 
	 * @param stripByteCounts
	 *            strip byte counts
	 */
	public void setStripByteCountsAsLongs(List<Long> stripByteCounts) {
		setUnsignedLongListTag(TagName.StripByteCounts,
				stripByteCounts);
	}

	/**
	 * Set a single value strip byte count
	 * 
	 * @param stripByteCount
	 *            strip byte count
	 */
	public void setStripByteCounts(int stripByteCount) {
		setStripByteCounts(createSingleIntegerList(stripByteCount));
	}

	/**
	 * Set a single value strip byte count
	 * 
	 * @param stripByteCount
	 *            strip byte count
	 */
	public void setStripByteCounts(long stripByteCount) {
		setStripByteCountsAsLongs(createSingleLongList(stripByteCount));
	}

	/**
	 * Get the x resolution
	 * 
	 * @return x resolution
	 */
	public List<Long> getXResolution() {
		return getLongListTag(TagName.XResolution);
	}

	/**
	 * Set the x resolution
	 * 
	 * @param xResolution
	 *            x resolution
	 */
	public void setXResolution(List<Long> xResolution) {
		setUnsignedLongListTag(TagName.XResolution, xResolution);
	}

	/**
	 * Set a single value x resolution
	 * 
	 * @param xResolution
	 *            x resolution
	 */
	public void setXResolution(long xResolution) {
		setXResolution(createSingleLongList(xResolution));
	}

	/**
	 * Get the y resolution
	 * 
	 * @return y resolution
	 */
	public List<Long> getYResolution() {
		return getLongListTag(TagName.YResolution);
	}

	/**
	 * Set the y resolution
	 * 
	 * @param yResolution
	 *            y resolution
	 */
	public void setYResolution(List<Long> yResolution) {
		setUnsignedLongListTag(TagName.YResolution, yResolution);
	}

	/**
	 * Set a single value y resolution
	 * 
	 * @param yResolution
	 *            y resolution
	 */
	public void setYResolution(long yResolution) {
		setYResolution(createSingleLongList(yResolution));
	}

	/**
	 * Get the planar configuration
	 * 
	 * @return planar configuration
	 */
	public Integer getPlanarConfiguration() {
		return getIntegerTag(TagName.PlanarConfiguration);
	}

	/**
	 * Set the planar configuration
	 * 
	 * @param planarConfiguration
	 *            planar configuration
	 */
	public void setPlanarConfiguration(int planarConfiguration) {
		setUnsignedIntegerTag(TagName.PlanarConfiguration,
				planarConfiguration);
	}

	/**
	 * Get the resolution unit
	 * 
	 * @return resolution unit
	 */
	public Integer getResolutionUnit() {
		return getIntegerTag(TagName.ResolutionUnit);
	}

	/**
	 * Set the resolution unit
	 * 
	 * @param resolutionUnit
	 *            resolution unit
	 */
	public void setResolutionUnit(int resolutionUnit) {
		setUnsignedIntegerTag(TagName.ResolutionUnit,
				resolutionUnit);
	}

	/**
	 * Get the color map
	 * 
	 * @return color map
	 */
	public List<Integer> getColorMap() {
		return getIntegerListTag(TagName.ColorMap);
	}

	/**
	 * Set the color map
	 * 
	 * @param colorMap
	 *            color map
	 */
	public void setColorMap(List<Integer> colorMap) {
		setUnsignedIntegerListTag(TagName.ColorMap, colorMap);
	}

	/**
	 * Set a single value color map
	 * 
	 * @param colorMap
	 *            color map
	 */
	public void setColorMap(int colorMap) {
		setColorMap(createSingleIntegerList(colorMap));
	}

	/**
	 * Get the tile width
	 * 
	 * @return tile width
	 */
	public Number getTileWidth() {
		return tiled ? getNumberTag(TagName.TileWidth)
				: getImageWidth();
	}

	/**
	 * Set the tile width
	 * 
	 * @param tileWidth
	 *            tile width
	 */
	public void setTileWidth(int tileWidth) {
		setUnsignedIntegerTag(TagName.TileWidth, tileWidth);
	}

	/**
	 * Set the tile width
	 * 
	 * @param tileWidth
	 *            tile width
	 */
	public void setTileWidthAsLong(long tileWidth) {
		setUnsignedLongTag(TagName.TileWidth, tileWidth);
	}

	/**
	 * Get the tile height
	 * 
	 * @return tile height
	 */
	public Number getTileHeight() {
		return tiled ? getNumberTag(TagName.TileLength)
				: getRowsPerStrip();
	}

	/**
	 * Set the tile height
	 * 
	 * @param tileHeight
	 *            tile height
	 */
	public void setTileHeight(int tileHeight) {
		setUnsignedIntegerTag(TagName.TileLength, tileHeight);
	}

	/**
	 * Set the tile height
	 * 
	 * @param tileHeight
	 *            tile height
	 */
	public void setTileHeightAsLong(long tileHeight) {
		setUnsignedLongTag(TagName.TileLength, tileHeight);
	}

	/**
	 * Get the tile offsets
	 * 
	 * @return tile offsets
	 */
	public List<Long> getTileOffsets() {
		return getLongListTag(TagName.TileOffsets);
	}

	/**
	 * Set the tile offsets
	 * 
	 * @param tileOffsets
	 *            tile offsets
	 */
	public void setTileOffsets(List<Long> tileOffsets) {
		setUnsignedLongListTag(TagName.TileOffsets, tileOffsets);
	}

	/**
	 * Set a single value tile offset
	 * 
	 * @param tileOffset
	 *            tile offset
	 */
	public void setTileOffsets(long tileOffset) {
		setTileOffsets(createSingleLongList(tileOffset));
	}

	/**
	 * Get the tile byte counts
	 * 
	 * @return tile byte counts
	 */
	public List<Number> getTileByteCounts() {
		return getNumberListTag(TagName.TileByteCounts);
	}

	/**
	 * Set the tile byte counts
	 * 
	 * @param tileByteCounts
	 *            tile byte counts
	 */
	public void setTileByteCounts(List<Integer> tileByteCounts) {
		setUnsignedIntegerListTag(TagName.TileByteCounts,
				tileByteCounts);
	}

	/**
	 * Set the tile byte counts
	 * 
	 * @param tileByteCounts
	 *            tile byte counts
	 */
	public void setTileByteCountsAsLongs(List<Long> tileByteCounts) {
		setUnsignedLongListTag(TagName.TileByteCounts,
				tileByteCounts);
	}

	/**
	 * Set a single value tile byte count
	 * 
	 * @param tileByteCount
	 *            tile byte count
	 */
	public void setTileByteCounts(int tileByteCount) {
		setTileByteCounts(createSingleIntegerList(tileByteCount));
	}

	/**
	 * Set a single value tile byte count
	 * 
	 * @param tileByteCount
	 *            tile byte count
	 */
	public void setTileByteCounts(long tileByteCount) {
		setTileByteCountsAsLongs(createSingleLongList(tileByteCount));
	}

	/**
	 * Get the sample format
	 * 
	 * @return sample format
	 */
	public List<Integer> getSampleFormat() {
		return getIntegerListTag(TagName.SampleFormat);
	}

	/**
	 * Set the sample format
	 * 
	 * @param sampleFormat
	 *            sample format
	 */
	public void setSampleFormat(List<Integer> sampleFormat) {
		setUnsignedIntegerListTag(TagName.SampleFormat,
				sampleFormat);
	}

	/**
	 * Set a single value sample format
	 * 
	 * @param sampleFormat
	 *            sample format
	 */
	public void setSampleFormat(int sampleFormat) {
		setSampleFormat(createSingleIntegerList(sampleFormat));
	}

	/**
	 * Get the max sample format
	 * 
	 * @return max sample format
	 */
	public Integer getMaxSampleFormat() {
		return getMaxIntegerTag(TagName.SampleFormat);
	}

	/**
	 * Get the rasters for writing a TIFF file
	 * 
	 * @return rasters image rasters
	 */
	public Rasters getWriteRasters() {
		return writeRasters;
	}

	/**
	 * Set the rasters for writing a TIFF file
	 * 
	 * @param rasters
	 *            image rasters
	 */
	public void setWriteRasters(Rasters rasters) {
		writeRasters = rasters;
	}

	/**
	 * Read the rasters
	 * 
	 * @return rasters
	 */
	public Rasters readRasters() {
		ImageWindow window = new ImageWindow(this);
		return readRasters(window);
	}

	/**
	 * Read the rasters as interleaved
	 * 
	 * @return rasters
	 */
	public Rasters readInterleavedRasters() {
		ImageWindow window = new ImageWindow(this);
		return readInterleavedRasters(window);
	}

	/**
	 * Read the rasters
	 * 
	 * @param window
	 *            image window
	 * @return rasters
	 */
	public Rasters readRasters(ImageWindow window) {
		return readRasters(window, null);
	}

	/**
	 * Read the rasters as interleaved
	 * 
	 * @param window
	 *            image window
	 * @return rasters
	 */
	public Rasters readInterleavedRasters(ImageWindow window) {
		return readInterleavedRasters(window, null);
	}

	/**
	 * Read the rasters
	 * 
	 * @param samples
	 *            pixel samples to read
	 * @return rasters
	 */
	public Rasters readRasters(int[] samples) {
		ImageWindow window = new ImageWindow(this);
		return readRasters(window, samples);
	}

	/**
	 * Read the rasters as interleaved
	 * 
	 * @param samples
	 *            pixel samples to read
	 * @return rasters
	 */
	public Rasters readInterleavedRasters(int[] samples) {
		ImageWindow window = new ImageWindow(this);
		return readInterleavedRasters(window, samples);
	}

	/**
	 * Read the rasters
	 * 
	 * @param window
	 *            image window
	 * @param samples
	 *            pixel samples to read
	 * @return rasters
	 */
	public Rasters readRasters(ImageWindow window, int[] samples) {
		return readRasters(window, samples, true, false);
	}

	/**
	 * Read the rasters as interleaved
	 * 
	 * @param window
	 *            image window
	 * @param samples
	 *            pixel samples to read
	 * @return rasters
	 */
	public Rasters readInterleavedRasters(ImageWindow window, int[] samples) {
		return readRasters(window, samples, false, true);
	}

	/**
	 * Read the rasters
	 * 
	 * @param sampleValues
	 *            true to read results per sample
	 * @param interleaveValues
	 *            true to read results as interleaved
	 * @return rasters
	 */
	public Rasters readRasters(boolean sampleValues, boolean interleaveValues) {
		ImageWindow window = new ImageWindow(this);
		return readRasters(window, sampleValues, interleaveValues);
	}

	/**
	 * Read the rasters
	 * 
	 * @param window
	 *            image window
	 * @param sampleValues
	 *            true to read results per sample
	 * @param interleaveValues
	 *            true to read results as interleaved
	 * @return rasters
	 */
	public Rasters readRasters(ImageWindow window, boolean sampleValues,
			boolean interleaveValues) {
		return readRasters(window, null, sampleValues, interleaveValues);
	}

	/**
	 * Read the rasters
	 * 
	 * @param samples
	 *            pixel samples to read
	 * @param sampleValues
	 *            true to read results per sample
	 * @param interleaveValues
	 *            true to read results as interleaved
	 * @return rasters
	 */
	public Rasters readRasters(int[] samples, boolean sampleValues,
			boolean interleaveValues) {
		ImageWindow window = new ImageWindow(this);
		return readRasters(window, samples, sampleValues, interleaveValues);
	}

	/**
	 * Read the rasters
	 * 
	 * @param window
	 *            image window
	 * @param samples
	 *            pixel samples to read
	 * @param sampleValues
	 *            true to read results per sample
	 * @param interleaveValues
	 *            true to read results as interleaved
	 * @return rasters
	 */
	public Rasters readRasters(ImageWindow window, int[] samples,
			boolean sampleValues, boolean interleaveValues) {

		int width = getImageWidth().intValue();
		int height = getImageHeight().intValue();

		// Validate the image window
		if (window.getMinX() < 0 || window.getMinY() < 0
				|| window.getMaxX() > width || window.getMaxY() > height) {
			throw new TiffException(
					"Window is out of the image bounds. Width: " + width
							+ ", Height: " + height + ", Window: " + window);
		} else if (window.getMinX() > window.getMaxX()
				|| window.getMinY() > window.getMaxY()) {
			throw new TiffException("Invalid window range: " + window);
		}

		int windowWidth = window.getMaxX() - window.getMinX();
		int windowHeight = window.getMaxY() - window.getMinY();
		int numPixels = windowWidth * windowHeight;

		// Set or validate the samples
		int samplesPerPixel = getSamplesPerPixel();
		if (samples == null) {
			samples = new int[samplesPerPixel];
			for (int i = 0; i < samples.length; i++) {
				samples[i] = i;
			}
		} else {
			for (int i = 0; i < samples.length; i++) {
				if (samples[i] >= samplesPerPixel) {
					throw new TiffException("Invalid sample index: "
							+ samples[i]);
				}
			}
		}

		// Create the interleaved result array
		Number[] interleave = null;
		if (interleaveValues) {
			interleave = new Number[numPixels * samples.length];
		}

		// Create the sample indexed result double array
		Number[][] sample = null;
		if (sampleValues) {
			sample = new Number[samples.length][numPixels];
		}

		// Create the rasters results
		Rasters rasters = new Rasters(windowWidth, windowHeight,
				samplesPerPixel, getBitsPerSample(), sample, interleave);

		// Read the rasters
		readRaster(window, samples, rasters);

		return rasters;
	}

	/**
	 * Read and populate the rasters
	 * 
	 * @param window
	 *            image window
	 * @param samples
	 *            pixel samples to read
	 * @param rasters
	 *            rasters to populate
	 */
	private void readRaster(ImageWindow window, int[] samples, Rasters rasters) {

		int tileWidth = getTileWidth().intValue();
		int tileHeight = getTileHeight().intValue();

		int minXTile = window.getMinX() / tileWidth;
		int maxXTile = (window.getMaxX() + tileWidth - 1) / tileWidth;
		int minYTile = window.getMinY() / tileHeight;
		int maxYTile = (window.getMaxY() + tileHeight - 1) / tileHeight;

		int windowWidth = window.getMaxX() - window.getMinX();

		int bytesPerPixel = getBytesPerPixel();

		int[] srcSampleOffsets = new int[samples.length];
		TagType[] sampleTagTypes = new TagType[samples.length];
		for (int i = 0; i < samples.length; i++) {
			int sampleOffset = 0;
			if (planarConfiguration == TiffConstants.PLANAR_CONFIGURATION_CHUNKY) {
				sampleOffset = sum(getBitsPerSample(), 0, samples[i]) / 8;
			}
			srcSampleOffsets[i] = sampleOffset;
			sampleTagTypes[i] = getFieldTypeForSample(samples[i]);
		}

		for (int yTile = minYTile; yTile < maxYTile; yTile++) {
			for (int xTile = minXTile; xTile < maxXTile; xTile++) {

				int firstLine = yTile * tileHeight;
				int firstCol = xTile * tileWidth;
				int lastLine = (yTile + 1) * tileHeight;
				int lastCol = (xTile + 1) * tileWidth;

				for (int sampleIndex = 0; sampleIndex < samples.length; sampleIndex++) {
					int sample = samples[sampleIndex];
					if (planarConfiguration == TiffConstants.PLANAR_CONFIGURATION_PLANAR) {
						bytesPerPixel = getSampleByteSize(sample);
					}

					byte[] block = getTileOrStrip(xTile, yTile, sample);
					ByteReader blockReader = new ByteReader(block,
							reader.getByteOrder());

					for (int y = Math.max(0, window.getMinY() - firstLine); y < Math
							.min(tileHeight,
									tileHeight - (lastLine - window.getMaxY())); y++) {

						for (int x = Math.max(0, window.getMinX() - firstCol); x < Math
								.min(tileWidth,
										tileWidth
												- (lastCol - window.getMaxX())); x++) {

							int pixelOffset = (y * tileWidth + x)
									* bytesPerPixel;
							int valueOffset = pixelOffset
									+ srcSampleOffsets[sampleIndex];
							blockReader.setNextByte(valueOffset);

							// Read the value
							Number value = readValue(blockReader,
									sampleTagTypes[sampleIndex]);

							if (rasters.hasInterleaveValues()) {
								int windowCoordinate = (y + firstLine - window
										.getMinY())
										* windowWidth
										* samples.length
										+ (x + firstCol - window.getMinX())
										* samples.length + sampleIndex;
								rasters.addToInterleave(windowCoordinate, value);
							}

							if (rasters.hasSampleValues()) {
								int windowCoordinate = (y + firstLine - window
										.getMinY())
										* windowWidth
										+ x
										+ firstCol - window.getMinX();
								rasters.addToSample(sampleIndex,
										windowCoordinate, value);
							}
						}

					}
				}
			}
		}
	}

	/**
	 * Read the value from the reader according to the field type
	 * 
	 * @param reader
	 *            byte reader
	 * @param tagType
	 *            field type
	 * @return value
	 */
	private Number readValue(ByteReader reader, TagType tagType) {

		Number value = null;

		switch (tagType) {
		case BYTE:
			value = reader.readUnsignedByte();
			break;
		case SHORT:
			value = reader.readUnsignedShort();
			break;
		case LONG:
			value = reader.readUnsignedInt();
			break;
		case SBYTE:
			value = reader.readByte();
			break;
		case SSHORT:
			value = reader.readShort();
			break;
		case SLONG:
			value = reader.readInt();
			break;
		case FLOAT:
			value = reader.readFloat();
			break;
		case DOUBLE:
			value = reader.readDouble();
			break;
		default:
			throw new TiffException("Unsupported raster field type: "
					+ tagType);
		}

		return value;
	}

	/**
	 * Get the field type for the sample
	 * 
	 * @param sampleIndex
	 *            sample index
	 * @return field type
	 */
	public TagType getFieldTypeForSample(int sampleIndex) {

		TagType tagType = null;

		List<Integer> sampleFormat = getSampleFormat();
		int format = sampleFormat != null && sampleIndex < sampleFormat.size() ? sampleFormat
				.get(sampleIndex) : TiffConstants.SAMPLE_FORMAT_UNSIGNED_INT;
		int bitsPerSample = getBitsPerSample().get(sampleIndex);
		switch (format) {
		case TiffConstants.SAMPLE_FORMAT_UNSIGNED_INT:
			switch (bitsPerSample) {
			case 8:
				tagType = TagType.BYTE;
				break;
			case 16:
				tagType = TagType.SHORT;
				break;
			case 32:
				tagType = TagType.LONG;
				break;
			}
			break;
		case TiffConstants.SAMPLE_FORMAT_SIGNED_INT:
			switch (bitsPerSample) {
			case 8:
				tagType = TagType.SBYTE;
				break;
			case 16:
				tagType = TagType.SSHORT;
				break;
			case 32:
				tagType = TagType.SLONG;
				break;
			}
			break;
		case TiffConstants.SAMPLE_FORMAT_FLOAT:
			switch (bitsPerSample) {
			case 32:
				tagType = TagType.FLOAT;
				break;
			case 64:
				tagType = TagType.DOUBLE;
				break;
			}
			break;
		}

		return tagType;
	}

	/**
	 * Get the tile or strip for the sample coordinate
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param sample
	 *            sample index
	 * @return bytes
	 */
	private byte[] getTileOrStrip(int x, int y, int sample) {

		byte[] tileOrStrip = null;

		int imageWidth = getImageWidth().intValue();
		int imageHeight = getImageHeight().intValue();
		int tileWidth = getTileWidth().intValue();
		int tileHeight = getTileHeight().intValue();
		int numTilesPerRow = (imageWidth + tileWidth - 1) / tileWidth;
		int numTilesPerCol = (imageHeight + tileHeight - 1) / tileHeight;

		int index = 0;
		if (planarConfiguration == TiffConstants.PLANAR_CONFIGURATION_CHUNKY) {
			index = y * numTilesPerRow + x;
		} else if (planarConfiguration == TiffConstants.PLANAR_CONFIGURATION_PLANAR) {
			index = sample * numTilesPerRow * numTilesPerCol + y
					* numTilesPerRow + x;
		}

		// Attempt to pull from the cache
		if (cache != null && cache.containsKey(index)) {
			tileOrStrip = cache.get(index);
		} else if (lastBlockIndex == index && lastBlock != null) {
			tileOrStrip = lastBlock;
		} else {

			// Read and decode the block

			int offset = 0;
			int byteCount = 0;
			if (tiled) {
				offset = getTileOffsets().get(index).intValue();
				byteCount = getTileByteCounts().get(index).intValue();
			} else {
				offset = getStripOffsets().get(index).intValue();
				byteCount = getStripByteCounts().get(index).intValue();
			}

			reader.setNextByte(offset);
			byte[] bytes = reader.readBytes(byteCount);
			tileOrStrip = decoder.decode(bytes, reader.getByteOrder());

			// Cache the data
			if (cache != null) {
				cache.put(index, tileOrStrip);
			} else {
				lastBlockIndex = index;
				lastBlock = tileOrStrip;
			}
		}

		return tileOrStrip;
	}

	/**
	 * Get the sample byte size
	 * 
	 * @param sampleIndex
	 *            sample index
	 * @return byte size
	 */
	private int getSampleByteSize(int sampleIndex) {
		List<Integer> bitsPerSample = getBitsPerSample();
		if (sampleIndex >= bitsPerSample.size()) {
			throw new TiffException("Sample index " + sampleIndex
					+ " is out of range");
		}
		int bits = bitsPerSample.get(sampleIndex);
		if ((bits % 8) != 0) {
			throw new TiffException("Sample bit-width of " + bits
					+ " is not supported");
		}
		return (bits / 8);
	}

	/**
	 * Calculates the number of bytes for each pixel across all samples. Only
	 * full bytes are supported, an exception is thrown when this is not the
	 * case.
	 * 
	 * @return the bytes per pixel
	 */
	private int getBytesPerPixel() {
		int bitsPerSample = 0;
		List<Integer> bitsPerSamples = getBitsPerSample();
		for (int i = 0; i < bitsPerSamples.size(); i++) {
			int bits = bitsPerSamples.get(i);
			if ((bits % 8) != 0) {
				throw new TiffException("Sample bit-width of " + bits
						+ " is not supported");
			} else if (bits != bitsPerSamples.get(0)) {
				throw new TiffException(
						"Differing size of samples in a pixel are not supported. sample 0 = "
								+ bitsPerSamples.get(0) + ", sample " + i
								+ " = " + bits);
			}
			bitsPerSample += bits;
		}
		return bitsPerSample / 8;
	}

	/**
	 * Get an integer entry value
	 * 
	 * @param tagName
	 *            field tag type
	 * @return integer value
	 */
	public Integer getIntegerTag(TagName tagName) {
		return getTag(tagName);
	}

	/**
	 * Set an unsigned integer entry value for the field tag type
	 * 
	 * @param tagName
	 *            field tag type
	 * @param value
	 *            unsigned integer value (16 bit)
	 */
	public void setUnsignedIntegerTag(TagName tagName,
			int value) {
		setTag(tagName, TagType.SHORT, 1, value);
	}

	/**
	 * Get an number entry value
	 * 
	 * @param tagName
	 *            field tag type
	 * @return number value
	 */
	public Number getNumberTag(TagName tagName) {
		return getTag(tagName);
	}

	/**
	 * Set an unsigned long entry value for the field tag type
	 * 
	 * @param tagName
	 *            field tag type
	 * @param value
	 *            unsigned long value (32 bit)
	 */
	public void setUnsignedLongTag(TagName tagName, long value) {
		setTag(tagName, TagType.LONG, 1, value);
	}

	/**
	 * Get a string entry value for the field tag type
	 *
	 * @param tagName
	 *            field tag type
	 * @return number value
	 */
	public String getStringTag(TagName tagName) {
		String value = null;
		List<String> values = getTag(tagName);
		if (values != null && !values.isEmpty()) {
			value = values.get(0);
		}
		return value;
	}

	/**
	 * Set string value for the field tag type
	 *
	 * @param tagName
	 * @param value
	 */
	public void setStringTag(TagName tagName, String value) {
		List<String> values = new ArrayList<>();
		values.add(value);
		setTag(tagName, TagType.ASCII, value.length() + 1, values);
	}

	/**
	 * Get an integer list entry value
	 * 
	 * @param tagName
	 *            field tag type
	 * @return integer list value
	 */
	public List<Integer> getIntegerListTag(TagName tagName) {
		return getTag(tagName);
	}

	/**
	 * Set an unsigned integer list of values for the field tag type
	 * 
	 * @param tagName
	 * @param value
	 */
	public void setUnsignedIntegerListTag(TagName tagName,
			List<Integer> value) {
		setTag(tagName, TagType.SHORT, value.size(), value);
	}

	/**
	 * Get the max integer from integer list entry values
	 * 
	 * @param tagName
	 *            field tag type
	 * @return max integer value
	 */
	public Integer getMaxIntegerTag(TagName tagName) {
		Integer maxValue = null;
		List<Integer> values = getIntegerListTag(tagName);
		if (values != null) {
			maxValue = Collections.max(values);
		}
		return maxValue;
	}

	/**
	 * Get a number list entry value
	 * 
	 * @param tagName
	 *            field tag type
	 * @return long list value
	 */
	public List<Number> getNumberListTag(TagName tagName) {
		return getTag(tagName);
	}

	/**
	 * Get a long list entry value
	 * 
	 * @param tagName
	 *            field tag type
	 * @return long list value
	 */
	public List<Long> getLongListTag(TagName tagName) {
		return getTag(tagName);
	}

	/**
	 * Set an unsigned long list of values for the field tag type
	 * 
	 * @param tagName
	 * @param value
	 */
	public void setUnsignedLongListTag(TagName tagName,
			List<Long> value) {
		setTag(tagName, TagType.LONG, value.size(), value);
	}

	/**
	 * Get an entry value
	 * 
	 * @param tagName
	 *            field tag type
	 * @return value
	 */
	@SuppressWarnings("unchecked")
	private <T> T getTag(TagName tagName) {
		T value = null;
		FileDirectoryEntry entry = fieldTagTypeMapping.get(tagName);
		if (entry != null) {
			value = (T) entry.getValues();
		}
		return value;
	}

	/**
	 * Create and set the entry value
	 * 
	 * @param tagName
	 *            field tag type
	 * @param tagType
	 *            field type
	 * @param typeCount
	 *            type count
	 * @param values
	 *            entry values
	 */
	private void setTag(TagName tagName, TagType tagType,
			long typeCount, Object values) {
		FileDirectoryEntry entry = new FileDirectoryEntry(tagName,
				tagType, typeCount, values);
		addEntry(entry);
	}

	/**
	 * Sum the list integer values in the provided range
	 * 
	 * @param values
	 *            integer values
	 * @param start
	 *            inclusive start index
	 * @param end
	 *            exclusive end index
	 * @return sum
	 */
	private int sum(List<Integer> values, int start, int end) {
		int sum = 0;
		for (int i = start; i < end; i++) {
			sum += values.get(i);
		}
		return sum;
	}

	/**
	 * Create a single integer list with the value
	 * 
	 * @param value
	 *            int value
	 * @return single value list
	 */
	static private List<Integer> createSingleIntegerList(int value) {
		List<Integer> valueList = new ArrayList<>();
		valueList.add(value);
		return valueList;
	}

	/**
	 * Create a single long list with the value
	 * 
	 * @param value
	 *            long value
	 * @return single value list
	 */
	static private List<Long> createSingleLongList(long value) {
		List<Long> valueList = new ArrayList<>();
		valueList.add(value);
		return valueList;
	}

	/**
	 * Size in bytes of the Image File Directory (all contiguous)
	 * 
	 * @return size in bytes
	 */
	public long size() {
		return TiffConstants.IFD_HEADER_BYTES
				+ (entries.size() * TiffConstants.IFD_ENTRY_BYTES)
				+ TiffConstants.IFD_OFFSET_BYTES;
	}

	/**
	 * Size in bytes of the image file directory including entry values (not
	 * contiguous bytes)
	 * 
	 * @return size in bytes
	 */
	public long sizeWithValues() {
		long size = TiffConstants.IFD_HEADER_BYTES
				+ TiffConstants.IFD_OFFSET_BYTES;
		for (FileDirectoryEntry entry : entries) {
			size += entry.sizeWithValues();
		}
		return size;
	}

}
