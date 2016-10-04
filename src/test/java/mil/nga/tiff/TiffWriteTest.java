package mil.nga.tiff;

import java.io.File;
import java.io.IOException;

import mil.nga.tiff.util.TiffConstants;

import org.junit.Test;

/**
 * TIFF Write tests
 * 
 * @author osbornb
 */
public class TiffWriteTest {

	/**
	 * Test writing and reading a stripped TIFF file
	 * 
	 * @throws IOException
	 */
	@Test
	public void testWriteStrippedChunky() throws IOException {

		File strippedFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_STRIPPED);
		TIFFImage strippedTiff = TiffReader.readTiff(strippedFile);

		FileDirectory fileDirectory = strippedTiff.getFileDirectory();
		Rasters rasters = fileDirectory.readRasters();
		Rasters rastersInterleaved = fileDirectory.readInterleavedRasters();

		fileDirectory.setWriteRasters(rasters);
		fileDirectory.setCompression(TiffConstants.COMPRESSION_NO);
		fileDirectory
				.setPlanarConfiguration(TiffConstants.PLANAR_CONFIGURATION_CHUNKY);
		int rowsPerStrip = rasters.calculateRowsPerStrip(fileDirectory
				.getPlanarConfiguration());
		fileDirectory.setRowsPerStrip(rowsPerStrip);
		byte[] tiffBytes = TiffWriter.writeTiffToBytes(strippedTiff);

		TIFFImage readTiffImage = TiffReader.readTiff(tiffBytes);
		FileDirectory fileDirectory2 = readTiffImage.getFileDirectory();
		Rasters rasters2 = fileDirectory2.readRasters();
		Rasters rasters2Interleaved = fileDirectory2.readInterleavedRasters();

		TiffTestUtils.compareRastersSampleValues(rasters, rasters2);
		TiffTestUtils.compareRastersInterleaveValues(rastersInterleaved,
				rasters2Interleaved);

	}

	/**
	 * Test writing and reading a stripped TIFF file
	 * 
	 * @throws IOException
	 */
	@Test
	public void testWriteStrippedPlanar() throws IOException {

		File strippedFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_STRIPPED);
		TIFFImage strippedTiff = TiffReader.readTiff(strippedFile);

		FileDirectory fileDirectory = strippedTiff.getFileDirectory();
		Rasters rasters = fileDirectory.readRasters();
		Rasters rastersInterleaved = fileDirectory.readInterleavedRasters();

		fileDirectory.setWriteRasters(rasters);
		fileDirectory.setCompression(TiffConstants.COMPRESSION_NO);
		fileDirectory
				.setPlanarConfiguration(TiffConstants.PLANAR_CONFIGURATION_PLANAR);
		int rowsPerStrip = rasters.calculateRowsPerStrip(fileDirectory
				.getPlanarConfiguration());
		fileDirectory.setRowsPerStrip(rowsPerStrip);
		byte[] tiffBytes = TiffWriter.writeTiffToBytes(strippedTiff);

		TIFFImage readTiffImage = TiffReader.readTiff(tiffBytes);
		FileDirectory fileDirectory2 = readTiffImage.getFileDirectory();
		Rasters rasters2 = fileDirectory2.readRasters();
		Rasters rasters2Interleaved = fileDirectory2.readInterleavedRasters();

		TiffTestUtils.compareRastersSampleValues(rasters, rasters2);
		TiffTestUtils.compareRastersInterleaveValues(rastersInterleaved,
				rasters2Interleaved);

	}

}
