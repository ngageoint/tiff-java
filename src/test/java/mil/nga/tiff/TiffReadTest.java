package mil.nga.tiff;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import junit.framework.TestCase;
import mil.nga.tiff.util.TiffException;

/**
 * TIFF Read tests
 * 
 * @author osbornb
 */
public class TiffReadTest {

	/**
	 * Test the stripped TIFF file vs the same data tiled
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testStrippedVsTiled() throws IOException {

		File strippedFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_STRIPPED);
		TIFFImage strippedTiff = TiffReader.readTiff(strippedFile);

		File tiledFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_TILED);
		TIFFImage tiledTiff = TiffReader.readTiff(tiledFile);

		TiffTestUtils.compareTIFFImages(strippedTiff, tiledTiff);

	}

	/**
	 * Test the stripped TIFF file vs the same data as int 32
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testStrippedVsInt32() throws IOException {

		File strippedFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_STRIPPED);
		TIFFImage strippedTiff = TiffReader.readTiff(strippedFile);

		File int32File = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_INT32);
		TIFFImage int32Tiff = TiffReader.readTiff(int32File);

		TiffTestUtils.compareTIFFImages(strippedTiff, int32Tiff, true, false);

	}

	/**
	 * Test the stripped TIFF file vs the same data as unsigned int 32
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testStrippedVsUInt32() throws IOException {

		File strippedFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_STRIPPED);
		TIFFImage strippedTiff = TiffReader.readTiff(strippedFile);

		File uint32File = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_UINT32);
		TIFFImage uint32Tiff = TiffReader.readTiff(uint32File);

		TiffTestUtils.compareTIFFImages(strippedTiff, uint32Tiff, false, false);

	}

	/**
	 * Test the stripped TIFF file vs the same data as float 32
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testStrippedVsFloat32() throws IOException {

		File strippedFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_STRIPPED);
		TIFFImage strippedTiff = TiffReader.readTiff(strippedFile);

		File float32File = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_FLOAT32);
		TIFFImage float32Tiff = TiffReader.readTiff(float32File);

		TiffTestUtils.compareTIFFImages(strippedTiff, float32Tiff, false,
				false);

	}

	/**
	 * Test the stripped TIFF file vs the same data as float 64
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testStrippedVsFloat64() throws IOException {

		File strippedFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_STRIPPED);
		TIFFImage strippedTiff = TiffReader.readTiff(strippedFile);

		File float64File = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_FLOAT64);
		TIFFImage float64Tiff = TiffReader.readTiff(float64File);

		TiffTestUtils.compareTIFFImages(strippedTiff, float64Tiff, false,
				false);

	}

	/**
	 * Test the stripped TIFF file vs the same data compressed as LZW
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testStrippedVsLzw() throws IOException {

		File strippedFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_STRIPPED);
		TIFFImage strippedTiff = TiffReader.readTiff(strippedFile);

		File lzwFile = TiffTestUtils.getTestFile(TiffTestConstants.FILE_LZW);
		TIFFImage lzwTiff = TiffReader.readTiff(lzwFile);

		TiffTestUtils.compareTIFFImages(strippedTiff, lzwTiff);

	}

	/**
	 * Test the stripped TIFF file vs the same data compressed as Packbits
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testStrippedVsPackbits() throws IOException {

		File strippedFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_STRIPPED);
		TIFFImage strippedTiff = TiffReader.readTiff(strippedFile);

		File packbitsFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_PACKBITS);
		TIFFImage packbitsTiff = TiffReader.readTiff(packbitsFile);

		TiffTestUtils.compareTIFFImages(strippedTiff, packbitsTiff);

	}

	/**
	 * Test the stripped TIFF file vs the same data as interleaved
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testStrippedVsInterleave() throws IOException {

		File strippedFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_STRIPPED);
		TIFFImage strippedTiff = TiffReader.readTiff(strippedFile);

		File interleaveFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_INTERLEAVE);
		TIFFImage interleaveTiff = TiffReader.readTiff(interleaveFile);

		TiffTestUtils.compareTIFFImages(strippedTiff, interleaveTiff);

	}

	/**
	 * Test the stripped TIFF file vs the same data as tiled planar
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testStrippedVsTiledPlanar() throws IOException {

		File strippedFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_STRIPPED);
		TIFFImage strippedTiff = TiffReader.readTiff(strippedFile);

		File tiledPlanarFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_TILED_PLANAR);
		TIFFImage tiledPlanarTiff = TiffReader.readTiff(tiledPlanarFile);

		TiffTestUtils.compareTIFFImages(strippedTiff, tiledPlanarTiff);

	}

	/**
	 * Test the JPEG file header
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testJPEGHeader() throws IOException {

		File jpegFile = TiffTestUtils.getTestFile(TiffTestConstants.FILE_JPEG);
		TIFFImage jpegTiff = TiffReader.readTiff(jpegFile);

		TestCase.assertNotNull(jpegTiff);
		TestCase.assertTrue(jpegTiff.getFileDirectories().size() > 0);
		for (int i = 0; i < jpegTiff.getFileDirectories().size(); i++) {
			FileDirectory fileDirectory = jpegTiff.getFileDirectory(i);
			TestCase.assertNotNull(fileDirectory);
			try {
				fileDirectory.readRasters();
				TestCase.fail(
						"JPEG compression was not expected to be implemented");
			} catch (Exception e) {
				// expected
			}

		}

	}

	/**
	 * Test an invalid offset value
	 */
	@Test
	public void testInvalidOffset() {

		String base64Bytes = "TU0AKoAAAAAAAAAAAAAAAQAAKgAAGABNAA==";
		byte[] bytes = java.util.Base64.getDecoder().decode(base64Bytes);
		try {
			TiffReader.readTiff(bytes);
			fail("Unexpected success");
		} catch (TiffException e) {
			// expected
		}

	}

}
