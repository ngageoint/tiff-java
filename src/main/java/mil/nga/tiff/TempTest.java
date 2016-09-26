package mil.nga.tiff;

import java.io.File;

import mil.nga.tiff.util.TiffConstants;

public class TempTest {

	public static void main(String[] args) throws Exception {

		File denver = new File("/Users/osbornb/Downloads/denver.tiff");
		File elevation = new File("/Users/osbornb/Desktop/elevation.tiff");
		File tiled = new File("/Users/osbornb/Downloads/tiled.tiff");
		File stripped = new File(
				"/Users/osbornb/Documents/geotiff.js-master/test/data/stripped.tiff");
		File packbits = new File(
				"/Users/osbornb/Documents/geotiff.js-master/test/data/packbits.tiff");
		File lzw = new File(
				"/Users/osbornb/Documents/geotiff.js-master/test/data/lzw.tiff");

		File file = stripped;

		TIFFImage tiffImage = TiffReader.readTiff(file, false);
		FileDirectory fileDirectory = tiffImage.getFileDirectory();
		Rasters rasters = fileDirectory.readRasters();
		Rasters rasters2 = fileDirectory.readInterleavedRasters();

		File file2 = packbits;

		TIFFImage tiffImage2 = TiffReader.readTiff(file2, true);
		FileDirectory fileDirectory2 = tiffImage2.getFileDirectory();
		Rasters rasters2_1 = fileDirectory2.readRasters();
		Rasters rasters2_2 = fileDirectory2.readInterleavedRasters();

		File file3 = lzw;

		TIFFImage tiffImage3 = TiffReader.readTiff(file3, true);
		FileDirectory fileDirectory3 = tiffImage3.getFileDirectory();
		Rasters rasters3_1 = fileDirectory3.readRasters();
		Rasters rasters3_2 = fileDirectory3.readInterleavedRasters();

		for (int i = 0; i < rasters.getSampleValues().length; i++) {
			for (int j = 0; j < rasters.getSampleValues()[i].length; j++) {
				if (!rasters.getSampleValues()[i][j].equals(rasters2_1
						.getSampleValues()[i][j])
						|| !rasters.getSampleValues()[i][j].equals(rasters3_1
								.getSampleValues()[i][j])) {
					System.out.println("Compression values do not match");
				}
			}
		}

		for (int i = 0; i < rasters2.getInterleaveValues().length; i++) {
			if (!rasters2.getInterleaveValues()[i].equals(rasters2_2
					.getInterleaveValues()[i])
					|| !rasters2.getInterleaveValues()[i].equals(rasters3_2
							.getInterleaveValues()[i])) {
				System.out.println("Compression values do not match");
			}
		}

		if (rasters.getWidth() != fileDirectory.getImageWidth().intValue()) {
			System.out.println("WIDTH");
		}
		if (rasters.getHeight() != fileDirectory.getImageHeight().intValue()) {
			System.out.println("HEIGHT");
		}

		for (int y = 0; y < rasters.getHeight(); y++) {
			for (int x = 0; x < rasters.getWidth(); x++) {
				Number[] pixel = rasters.getPixel(x, y);
				Number[] pixel2 = rasters2.getPixel(x, y);

				ImageWindow window = new ImageWindow(x, y);
				Rasters rasters3 = fileDirectory.readRasters(window);
				Rasters rasters4 = fileDirectory.readInterleavedRasters(window);
				if (rasters3.getNumPixels() != 1
						|| rasters4.getNumPixels() != 1) {
					System.out.println("Num pixels");
				}

				for (int i = 0; i < rasters.getSamplesPerPixel(); i++) {
					Number sample = rasters.getPixelSample(i, x, y);
					Number sample2 = rasters2.getPixelSample(i, x, y);
					if (sample.intValue() != sample2.intValue()) {
						System.out.println("SAMPLE VS INTERLEAVE");
					}
					if (pixel[i].intValue() != sample.intValue()) {
						System.out.println("PIXEL SAMPLE");
					}
					if (pixel[i].intValue() != pixel2[i].intValue()) {
						System.out.println("PIXEL SAMPLE VS INTERLEAVE");
					}

					Number sample3 = rasters3.getPixelSample(i, 0, 0);
					Number sample4 = rasters4.getPixelSample(i, 0, 0);
					if (pixel[i].intValue() != sample3.intValue()
							|| pixel[i].intValue() != sample4.intValue()) {
						System.out.println("SINGLE PIXEL VALUE");
					}

				}
			}
		}

		fileDirectory.setWriteRasters(rasters);
		fileDirectory.setCompression(TiffConstants.COMPRESSION_NO);
		int rowsPerStrip = rasters.calculateRowsPerStrip(fileDirectory
				.getPlanarConfiguration());
		fileDirectory.setRowsPerStrip(rowsPerStrip);
		byte[] tiffBytes = TiffWriter.writeTiffToBytes(tiffImage);

		TIFFImage tiffImage4 = TiffReader.readTiff(tiffBytes, false);
		FileDirectory fileDirectory4 = tiffImage4.getFileDirectory();
		Rasters rasters4_1 = fileDirectory4.readRasters();
		Rasters rasters4_2 = fileDirectory4.readInterleavedRasters();

		fileDirectory.setWriteRasters(rasters);
		fileDirectory
				.setPlanarConfiguration(TiffConstants.PLANAR_CONFIGURATION_PLANAR);
		int rowsPerStrip2 = rasters.calculateRowsPerStrip(fileDirectory
				.getPlanarConfiguration());
		fileDirectory.setRowsPerStrip(rowsPerStrip2);
		byte[] tiffBytes2 = TiffWriter.writeTiffToBytes(tiffImage);

		TIFFImage tiffImage5 = TiffReader.readTiff(tiffBytes2, false);
		FileDirectory fileDirectory5 = tiffImage5.getFileDirectory();
		Rasters rasters5_1 = fileDirectory5.readRasters();
		Rasters rasters5_2 = fileDirectory5.readInterleavedRasters();

		for (int i = 0; i < rasters.getSampleValues().length; i++) {
			for (int j = 0; j < rasters.getSampleValues()[i].length; j++) {
				if (!rasters.getSampleValues()[i][j].equals(rasters4_1
						.getSampleValues()[i][j])
						|| !rasters.getSampleValues()[i][j].equals(rasters5_1
								.getSampleValues()[i][j])) {
					System.out.println("Compression values do not match");
				}
			}
		}

		for (int i = 0; i < rasters2.getInterleaveValues().length; i++) {
			if (!rasters2.getInterleaveValues()[i].equals(rasters4_2
					.getInterleaveValues()[i])
					|| !rasters2.getInterleaveValues()[i].equals(rasters5_2
							.getInterleaveValues()[i])) {
				System.out.println("Compression values do not match");
			}
		}

		System.out.println("DONE");
	}
}
