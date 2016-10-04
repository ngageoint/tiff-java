package mil.nga.tiff;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

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
	 */
	@Test
	public void testStrippedVsFloat32() throws IOException {

		File strippedFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_STRIPPED);
		TIFFImage strippedTiff = TiffReader.readTiff(strippedFile);

		File float32File = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_FLOAT32);
		TIFFImage float32Tiff = TiffReader.readTiff(float32File);

		TiffTestUtils
				.compareTIFFImages(strippedTiff, float32Tiff, false, false);

	}

	/**
	 * Test the stripped TIFF file vs the same data as float 64
	 * 
	 * @throws IOException
	 */
	@Test
	public void testStrippedVsFloat64() throws IOException {

		File strippedFile = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_STRIPPED);
		TIFFImage strippedTiff = TiffReader.readTiff(strippedFile);

		File float64File = TiffTestUtils
				.getTestFile(TiffTestConstants.FILE_FLOAT64);
		TIFFImage float64Tiff = TiffReader.readTiff(float64File);

		TiffTestUtils
				.compareTIFFImages(strippedTiff, float64Tiff, false, false);

	}

	/**
	 * Test the stripped TIFF file vs the same data compressed as LZW
	 * 
	 * @throws IOException
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

}
