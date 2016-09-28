package mil.nga.tiff;

import junit.framework.TestCase;

/**
 * TIFF Test Utility methods
 * 
 * @author osbornb
 */
public class TiffTestUtils {

	/**
	 * Compare two TIFF Images
	 * 
	 * @param tiffImage1
	 *            tiff image 1
	 * @param tiffImage2
	 *            tiff image 2
	 */
	public static void compareTIFFImages(TIFFImage tiffImage1,
			TIFFImage tiffImage2) {

		TestCase.assertNotNull(tiffImage1);
		TestCase.assertNotNull(tiffImage2);
		TestCase.assertEquals(tiffImage1.getFileDirectories().size(),
				tiffImage2.getFileDirectories().size());
		for (int i = 0; i < tiffImage1.getFileDirectories().size(); i++) {
			FileDirectory fileDirectory1 = tiffImage1.getFileDirectory(i);
			FileDirectory fileDirectory2 = tiffImage2.getFileDirectory(i);

			Rasters sampleRasters1 = fileDirectory1.readRasters();
			compareFileDirectoryAndRastersMetadata(fileDirectory1,
					sampleRasters1);
			Rasters sampleRasters2 = fileDirectory2.readRasters();
			compareFileDirectoryAndRastersMetadata(fileDirectory2,
					sampleRasters2);
			compareRastersSampleValues(sampleRasters1, sampleRasters2);

			Rasters interleaveRasters1 = fileDirectory1
					.readInterleavedRasters();
			compareFileDirectoryAndRastersMetadata(fileDirectory1,
					interleaveRasters1);
			Rasters interleaveRasters2 = fileDirectory2
					.readInterleavedRasters();
			compareFileDirectoryAndRastersMetadata(fileDirectory2,
					interleaveRasters2);
			compareRastersInterleaveValues(interleaveRasters1,
					interleaveRasters2);

			compareRasters(sampleRasters1, interleaveRasters2);
			compareRasters(interleaveRasters1, sampleRasters2);
		}

	}

	/**
	 * Compare the metadata between a file directory and rasters
	 * 
	 * @param fileDirectory
	 *            file directory
	 * @param rasters
	 *            rasters
	 */
	private static void compareFileDirectoryAndRastersMetadata(
			FileDirectory fileDirectory, Rasters rasters) {
		TestCase.assertEquals(fileDirectory.getImageWidth(), rasters.getWidth());
		TestCase.assertEquals(fileDirectory.getImageHeight(),
				rasters.getHeight());
		TestCase.assertEquals(fileDirectory.getSamplesPerPixel().intValue(),
				rasters.getSamplesPerPixel());
		TestCase.assertEquals(fileDirectory.getBitsPerSample().size(), rasters
				.getBitsPerSample().size());
		for (int i = 0; i < fileDirectory.getBitsPerSample().size(); i++) {
			TestCase.assertEquals(fileDirectory.getBitsPerSample().get(i),
					rasters.getBitsPerSample().get(i));
		}
	}

	/**
	 * Compare rasters sample values
	 * 
	 * @param rasters1
	 *            rasters 1
	 * @param rasters2
	 *            rasters 2
	 */
	public static void compareRastersSampleValues(Rasters rasters1,
			Rasters rasters2) {

		compareRastersMetadata(rasters1, rasters2);

		TestCase.assertNotNull(rasters1.getSampleValues());
		TestCase.assertNotNull(rasters2.getSampleValues());
		TestCase.assertEquals(rasters1.getSampleValues().length,
				rasters2.getSampleValues().length);

		for (int i = 0; i < rasters1.getSampleValues().length; i++) {
			TestCase.assertEquals(rasters1.getSampleValues()[i].length,
					rasters2.getSampleValues()[i].length);
			for (int j = 0; j < rasters2.getSampleValues()[i].length; j++) {
				TestCase.assertEquals(rasters1.getSampleValues()[i][j],
						rasters2.getSampleValues()[i][j]);
			}
		}
	}

	/**
	 * Compare rasters interleave values
	 * 
	 * @param rasters1
	 *            rasters 1
	 * @param rasters2
	 *            rasters 2
	 */
	public static void compareRastersInterleaveValues(Rasters rasters1,
			Rasters rasters2) {

		compareRastersMetadata(rasters1, rasters2);

		TestCase.assertNotNull(rasters1.getInterleaveValues());
		TestCase.assertNotNull(rasters2.getInterleaveValues());
		TestCase.assertEquals(rasters1.getInterleaveValues().length,
				rasters2.getInterleaveValues().length);

		for (int i = 0; i < rasters1.getInterleaveValues().length; i++) {
			TestCase.assertEquals(rasters1.getInterleaveValues()[i],
					rasters2.getInterleaveValues()[i]);
		}
	}

	/**
	 * Compare rasters pixel values
	 * 
	 * @param rasters1
	 *            rasters 1
	 * @param rasters2
	 *            rasters 2
	 */
	public static void compareRasters(Rasters rasters1, Rasters rasters2) {

		compareRastersMetadata(rasters1, rasters2);

		for (int x = 0; x < rasters1.getWidth(); x++) {
			for (int y = 0; y < rasters1.getHeight(); y++) {
				for (int sample = 0; sample < rasters1.getSamplesPerPixel(); sample++) {
					TestCase.assertEquals(
							rasters1.getPixelSample(sample, x, y),
							rasters2.getPixelSample(sample, x, y));
				}
			}
		}
	}

	/**
	 * Compare the rasters metadata
	 * 
	 * @param rasters1
	 *            rasters 1
	 * @param rasters2
	 *            rasters 2
	 */
	private static void compareRastersMetadata(Rasters rasters1,
			Rasters rasters2) {
		TestCase.assertNotNull(rasters1);
		TestCase.assertNotNull(rasters2);
		TestCase.assertEquals(rasters1.getWidth(), rasters2.getWidth());
		TestCase.assertEquals(rasters1.getHeight(), rasters2.getHeight());
		TestCase.assertEquals(rasters1.getNumPixels(), rasters2.getNumPixels());
		TestCase.assertEquals(rasters1.getBitsPerSample().size(), rasters2
				.getBitsPerSample().size());
		for (int i = 0; i < rasters1.getBitsPerSample().size(); i++) {
			TestCase.assertEquals(rasters1.getBitsPerSample().get(i), rasters2
					.getBitsPerSample().get(i));
		}
	}

}
