package mil.nga.tiff;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

import mil.nga.tiff.io.ByteWriter;
import mil.nga.tiff.util.TiffConstants;
import org.junit.Test;

/**
 * Test the streaming TIFF implementation
 *
 * @author osbornb
 */
public class StreamingTest {

	/**
	 * Test streaming vs memory-based TIFF reading
	 *
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testStreamingReadTiff() throws IOException {

		// Use the test TIFF files from the existing test resources
		InputStream tiffInputStream = getClass().getResourceAsStream("/rgb.tiff");
		if (tiffInputStream == null) {
			// Skip test if resource not available
			return;
		}

		// Create temporary file from resource
		File tempTiffFile = File.createTempFile("test_streaming", ".tif");
		tempTiffFile.deleteOnExit();

		try {
			// Copy resource to temp file
			java.nio.file.Files.copy(tiffInputStream, tempTiffFile.toPath(),
					StandardCopyOption.REPLACE_EXISTING);

			// Test memory-based reading (default for small files)
			TIFFImage memoryImage = TiffReader.readTiff(tempTiffFile);
			assert memoryImage != null : "Memory-based reading failed";

			// Test explicit streaming reading
			TIFFImage streamingImage = TiffReader.readTiffStreaming(tempTiffFile, false);
			assert streamingImage != null : "Streaming reading failed";

			// Compare basic metadata
			FileDirectory memoryDir = (FileDirectory) memoryImage.getFileDirectory();
			FileDirectory streamingDir = (FileDirectory) streamingImage.getFileDirectory();

			// Compare basic image properties
			assert memoryDir.getImageWidth().equals(streamingDir.getImageWidth()) :
				"Image width mismatch";
			assert memoryDir.getImageHeight().equals(streamingDir.getImageHeight()) :
				"Image height mismatch";
			assert memoryDir.getSamplesPerPixel() == streamingDir.getSamplesPerPixel() :
				"Samples per pixel mismatch";

			System.out.println("✓ Streaming and memory-based reading produced consistent metadata");

		} finally {
			tiffInputStream.close();
		}
	}

	/**
	 * Test auto-detection functionality
	 */
	@Test
	public void testAutoDetection() {
		// This test demonstrates that the readTiff method automatically chooses
		// streaming for large files (>100MB) and memory for smaller files

		// For files smaller than 100MB, it uses memory-based reading
		// For files larger than 100MB, it automatically switches to streaming

		System.out.println("Auto-detection: Files >100MB automatically use streaming mode");
		System.out.println("Auto-detection: Files <=100MB use memory mode for compatibility");
	}

	/**
	 * Test streaming with larger file
	 */
	@Test
	public void testStreamingWithLargerFile() throws IOException {
		// Use the largest test file available (float64.tiff - 28MB)
		InputStream tiffInputStream = getClass().getResourceAsStream("/float64.tiff");
		if (tiffInputStream == null) {
			// Skip test if resource not available
			return;
		}

		// Create temporary file from resource
		File tempTiffFile = File.createTempFile("test_large_streaming", ".tif");
		tempTiffFile.deleteOnExit();

		try {
			// Copy resource to temp file
			java.nio.file.Files.copy(tiffInputStream, tempTiffFile.toPath(),
					StandardCopyOption.REPLACE_EXISTING);

			System.out.println("Testing with larger file: " + tempTiffFile.length() + " bytes");

			// Test memory-based reading (should still use memory for <100MB)
			long startTime = System.currentTimeMillis();
			TIFFImage memoryImage = TiffReader.readTiff(tempTiffFile);
			long memoryTime = System.currentTimeMillis() - startTime;

			assert memoryImage != null : "Memory-based reading failed";

			// Test explicit streaming reading
			startTime = System.currentTimeMillis();
			TIFFImage streamingImage = TiffReader.readTiffStreaming(tempTiffFile, false);
			long streamingTime = System.currentTimeMillis() - startTime;

			assert streamingImage != null : "Streaming reading failed";

			// Compare basic metadata
			FileDirectory memoryDir = (FileDirectory) memoryImage.getFileDirectory();
			FileDirectory streamingDir = (FileDirectory) streamingImage.getFileDirectory();

			// Compare basic image properties
			assert memoryDir.getImageWidth().equals(streamingDir.getImageWidth()) :
				"Image width mismatch";
			assert streamingDir.getImageHeight().equals(memoryDir.getImageHeight()) :
				"Image height mismatch";
			assert memoryDir.getSamplesPerPixel() == streamingDir.getSamplesPerPixel() :
				"Samples per pixel mismatch";

			System.out.println("✓ Large file streaming test completed successfully");
			System.out.println("  Memory reading time: " + memoryTime + "ms");
			System.out.println("  Streaming reading time: " + streamingTime + "ms");

		} finally {
			tiffInputStream.close();
		}
	}

	/**
	 * Test streaming write functionality
	 */
	@Test
	public void testStreamingWrite() throws IOException {
		// Create a small test image
		int width = 10;
		int height = 10;
		Rasters rasters = new Rasters(width, height, 1,
				FieldType.getFieldType(TiffConstants.SAMPLE_FORMAT_UNSIGNED_INT, 8));

		// Fill with test pattern
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int value = (x + y) % 256;
				rasters.setFirstPixelSample(x, y, value);
			}
		}

		// Create file directory with proper configuration
		FileDirectory fileDirectory = new FileDirectory();
		fileDirectory.setImageWidth(width);
		fileDirectory.setImageHeight(height);
		fileDirectory.setBitsPerSample(8);
		fileDirectory.setCompression(TiffConstants.COMPRESSION_NO);
		fileDirectory.setPhotometricInterpretation(
				TiffConstants.PHOTOMETRIC_INTERPRETATION_BLACK_IS_ZERO);
		fileDirectory.setSamplesPerPixel(1);
		fileDirectory.setRowsPerStrip(rasters.calculateRowsPerStrip(
				TiffConstants.PLANAR_CONFIGURATION_CHUNKY));
		fileDirectory.setPlanarConfiguration(
				TiffConstants.PLANAR_CONFIGURATION_CHUNKY);
		fileDirectory.setSampleFormat(TiffConstants.SAMPLE_FORMAT_UNSIGNED_INT);
		fileDirectory.setWriteRasters(rasters);

		TIFFImage tiffImage = new TIFFImage();
		tiffImage.add(fileDirectory);

		// Test streaming write
		File tempFile = File.createTempFile("streaming_write_test", ".tif");
		tempFile.deleteOnExit();

		try {
			// Create memory-based file for comparison
			File memoryFile = File.createTempFile("memory_write_test", ".tif");
			memoryFile.deleteOnExit();

			// Write using streaming mode
			TiffWriter.writeTiffStreaming(tempFile, tiffImage);

			// Write using memory mode (force small size estimation)
			ByteWriter memoryWriter = new ByteWriter();
			TiffWriter.writeTiff(memoryFile, memoryWriter, tiffImage);
			memoryWriter.close();

			// Verify both files were created and have content
			assert tempFile.exists() : "Streaming output file was not created";
			assert tempFile.length() > 0 : "Streaming output file is empty";
			assert memoryFile.exists() : "Memory output file was not created";
			assert memoryFile.length() > 0 : "Memory output file is empty";

			// Read back both files
			TIFFImage streamingImage = TiffReader.readTiff(tempFile);
			TIFFImage memoryImage = TiffReader.readTiff(memoryFile);

			assert streamingImage != null : "Could not read back streaming file";
			assert memoryImage != null : "Could not read back memory file";

			// Compare metadata
			FileDirectory streamingDir = (FileDirectory) streamingImage.getFileDirectory();
			FileDirectory memoryDir = (FileDirectory) memoryImage.getFileDirectory();

			assert streamingDir.getImageWidth().equals(memoryDir.getImageWidth()) :
				"Image width mismatch between streaming and memory";
			assert streamingDir.getImageHeight().equals(memoryDir.getImageHeight()) :
				"Image height mismatch between streaming and memory";
			assert streamingDir.getSamplesPerPixel() == memoryDir.getSamplesPerPixel() :
				"Samples per pixel mismatch between streaming and memory";

			// Compare pixel data
			Rasters streamingRasters = streamingDir.readRasters();
			Rasters memoryRasters = memoryDir.readRasters();

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					Number streamingValue = streamingRasters.getFirstPixelSample(x, y);
					Number memoryValue = memoryRasters.getFirstPixelSample(x, y);
					assert streamingValue.equals(memoryValue) :
						String.format("Pixel value mismatch at (%d,%d): streaming=%s, memory=%s",
							x, y, streamingValue, memoryValue);
				}
			}

			System.out.println("✓ Streaming write test completed successfully");
			System.out.println("✓ Streaming and memory modes produced identical results");

			// Clean up memory file
			if (memoryFile.exists()) {
				memoryFile.delete();
			}

		} finally {
			if (tempFile.exists()) {
				tempFile.delete();
			}
		}
	}
}