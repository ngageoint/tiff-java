/**
 * Primary TIFF API.
 * 
 * <h2>Overview</h2>
 * 
 * <p><a href="http://ngageoint.github.io/tiff-java/">TIFF</a> is a Java library
 * for reading and writing Tagged Image File Format files. It was primarily
 * created to provide license friendly TIFF functionality to Android
 * applications. Implementation is based on the 
 * <a href="https://partners.adobe.com/public/developer/en/tiff/TIFF6.pdf">TIFF specification</a>
 * and <a href="https://github.com/constantinius/geotiff.js">this JavaScript implementation</a>.
 * 
 * <h2>Usage</h2>
 * 
 * <h3>Reading</h3>
 *
 * <pre>{@code
 * //File input = ...
 * //InputStream input = ...
 * //byte[] input = ...
 * //ByteReader input = ...
 *
 * TIFFImage tiffImage = TiffReader.readTiff(input);
 * List<FileDirectory> directories = tiffImage.getFileDirectories();
 * FileDirectory directory = directories.get(0);
 * Rasters rasters = directory.readRasters();
 * }</pre> 
 * 
 * <h3>Writing</h3>
 * 
 * <pre>{@code
 * int width = 256;
 * int height = 256;
 * int samplesPerPixel = 1;
 * FieldType fieldType = FieldType.FLOAT;
 * int bitsPerSample = fieldType.getBits();
 * 
 * Rasters rasters = new Rasters(width, height, samplesPerPixel, fieldType);
 * 
 * int rowsPerStrip = rasters.calculateRowsPerStrip(TiffConstants.PLANAR_CONFIGURATION_CHUNKY);
 *
 * FileDirectory directory = new FileDirectory();
 * directory.setImageWidth(width);
 * directory.setImageHeight(height);
 * directory.setBitsPerSample(bitsPerSample);
 * directory.setCompression(TiffConstants.COMPRESSION_NO);
 * directory.setPhotometricInterpretation(TiffConstants.PHOTOMETRIC_INTERPRETATION_BLACK_IS_ZERO);
 * directory.setSamplesPerPixel(samplesPerPixel);
 * directory.setRowsPerStrip(rowsPerStrip);
 * directory.setPlanarConfiguration(TiffConstants.PLANAR_CONFIGURATION_CHUNKY);
 * directory.setSampleFormat(TiffConstants.SAMPLE_FORMAT_FLOAT);
 * directory.setWriteRasters(rasters);
 *
 * for (int y = 0; y < height; y++) {
 *     for (int x = 0; x < width; x++) {
 *         float pixelValue = 1.0f; // any pixel value
 *         rasters.setFirstPixelSample(x, y, pixelValue);
 *     }
 * }
 *
 * TIFFImage tiffImage = new TIFFImage();
 * tiffImage.add(directory);
 * byte[] bytes = TiffWriter.writeTiffToBytes(tiffImage);
 * // or
 * // File file = ...
 * // TiffWriter.writeTiff(file, tiffImage);
 * }</pre> 
 */
package mil.nga.tiff;
