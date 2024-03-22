package mil.nga.tiff.util;

/**
 * TIFF Constants.
 *
 * @author osbornb
 */
public class TiffConstants {

        // This class is not meant to be instantiated.
        private TiffConstants() {}

	/**
	 * Little Endian byte order string
	 */
	public static final String BYTE_ORDER_LITTLE_ENDIAN = "II";

	/**
	 * Big Endian byte order string
	 */
	public static final String BYTE_ORDER_BIG_ENDIAN = "MM";

	/**
	 * TIFF File Identifier
	 */
	public static final int FILE_IDENTIFIER = 42;

	/**
	 * TIFF header bytes
	 */
	public static final int HEADER_BYTES = 8;

	/**
	 * Image File Directory header / number of entries bytes
	 */
	public static final int IFD_HEADER_BYTES = 2;

	/**
	 * Image File Directory offset to the next IFD bytes
	 */
	public static final int IFD_OFFSET_BYTES = 4;

	/**
	 * Image File Directory entry bytes
	 */
	public static final int IFD_ENTRY_BYTES = 12;

	/**
	 * Default max bytes per strip when writing strips
	 */
	public static final int DEFAULT_MAX_BYTES_PER_STRIP = 8000;


        /**
         * No compression.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Compression}.
         */
	public static final int COMPRESSION_NO = 1;

        /**
         * CCITT 1D compression.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Compression}.
         */
        public static final int COMPRESSION_CCITT_HUFFMAN = 2;

        /**
         * Group 3 fax compression.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Compression}.
         */
	public static final int COMPRESSION_T4 = 3;

        /**
         * Group 4 fax compression.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Compression}.
         */
	public static final int COMPRESSION_T6 = 4;

        /**
         * Lempel-Ziv and Welch (LZW) compression.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Compression}.
         */
	public static final int COMPRESSION_LZW = 5;

        /**
         * Old style JPEG compression.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Compression}.
         */
	public static final int COMPRESSION_JPEG_OLD = 6;

        /**
         * New style JPEG compression.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Compression}.
         *
         * <p>This is not in the original TIFF 6 specification. See TIFF
         * Technical Notes 22 March 2002.
         */
	public static final int COMPRESSION_JPEG_NEW = 7;

        /**
         * Deflate (zlib) compression.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Compression}.
         *
         * <p>This is not in the original TIFF 6 specification. See TIFF
         * Technical Notes 22 March 2002.
         */
	public static final int COMPRESSION_DEFLATE = 8;

        /**
         * Obsolete ZIP/flate compression.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Compression}.
         *
         * <p>This is identical implementation to COMPRESSION_DEFLATE. See
         * TIFF Technical Notes 22 March 2002.
         */
	@Deprecated
        public static final int COMPRESSION_PKZIP_DEFLATE = 32946; // PKZIP-style Deflate encoding (Obsolete).

        /**
         * PackBits compression.
         *
         * <p>Run Length Encoding, originally from Macintosh.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Compression}.
         */
	public static final int COMPRESSION_PACKBITS = 32773;

        /**
         * Unspecified extra sample type.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#ExtraSamples}.
         */
	public static final int EXTRA_SAMPLES_UNSPECIFIED = 0;

        /**
         * Associated alpha data extra sample type.
         *
         * <p>This is opacity information (pre-multiplied colour).
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#ExtraSamples}.
         */
	public static final int EXTRA_SAMPLES_ASSOCIATED_ALPHA = 1;

        /**
         * Unassociated alpha data extra sample type.
         *
         * <p>This is soft matte information (not pre-multiplied colour).
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#ExtraSamples}.
         */
        public static final int EXTRA_SAMPLES_UNASSOCIATED_ALPHA = 2;

        /**
         * Fill order high bits first.
         *
         * <p>Pixels are arranged within a byte such that pixels with lower
         * column values are stored in the higher-order bits of the byte.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#FillOrder}.
         */
	public static final int FILL_ORDER_LOWER_COLUMN_HIGHER_ORDER = 1;

        /**
         * Fill order low bits first.
         *
         * <p>Pixels are arranged within a byte such that pixels with lower
         * column values are stored in the lower-order bits of the byte.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#FillOrder}.
         */
	public static final int FILL_ORDER_LOWER_COLUMN_LOWER_ORDER = 2;

	/**
         * Gray response is in tenths of a unit.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#GrayResponseUnit}.
         */
	public static final int GRAY_RESPONSE_TENTHS = 1;

        /**
         * Gray response is in hundredths of a unit.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#GrayResponseUnit}.
         */
	public static final int GRAY_RESPONSE_HUNDREDTHS = 2;

        /**
         * Gray response is in thousandths of a unit.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#GrayResponseUnit}.
         */
	public static final int GRAY_RESPONSE_THOUSANDTHS = 3;

        /**
         * Gray response is in ten-thousandths of a unit.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#GrayResponseUnit}.
         */
	public static final int GRAY_RESPONSE_TEN_THOUSANDTHS = 4;

        /**
         * Gray response is in hundred-thousandths of a unit.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#GrayResponseUnit}.
         */
	public static final int GRAY_RESPONSE_HUNDRED_THOUSANDTHS = 5;

        /**
         * Orientation origin is top-left.
         *
         * <p>The 0<sup>th</sup> row represents the visual top of the image,
         * and the 0<sup>th</sup> column represents the visual left-hand side.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Orientation}.
         */
	public static final int ORIENTATION_TOP_ROW_LEFT_COLUMN = 1;

        /**
         * Orientation origin is top-right.
         *
         * <p>The 0<sup>th</sup> row represents the visual top of the image,
         * and the 0<sup>th</sup> column represents the visual right-hand side.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Orientation}.
         */
	public static final int ORIENTATION_TOP_ROW_RIGHT_COLUMN = 2;

        /**
         * Orientation origin is bottom-right.
         *
         * <p>The 0<sup>th</sup> row represents the visual bottom of the image,
         * and the 0<sup>th</sup> column represents the visual right-hand side.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Orientation}.
         */
	public static final int ORIENTATION_BOTTOM_ROW_RIGHT_COLUMN = 3;

        /**
         * Orientation origin is bottom-left.
         *
         * <p>The 0<sup>th</sup> row represents the visual bottom of the image,
         * and the 0<sup>th</sup> column represents the visual left-hand side.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Orientation}.
         */
	public static final int ORIENTATION_BOTTOM_ROW_LEFT_COLUMN = 4;

        /**
         * Orientation origin is left-top.
         *
         * <p>The 0<sup>th</sup> row represents the visual left-hand side of
         * the image, and the 0<sup>th</sup> column represents the visual
         * top.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Orientation}.
         */
	public static final int ORIENTATION_LEFT_ROW_TOP_COLUMN = 5;

        /**
         * Orientation origin is right-top.
         *
         * <p>The 0<sup>th</sup> row represents the visual right-hand side of
         * the image, and the 0<sup>th</sup> column represents the visual
         * top.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Orientation}.
         */
	public static final int ORIENTATION_RIGHT_ROW_TOP_COLUMN = 6;

        /**
         * Orientation origin is right-bottom.
         *
         * <p>The 0<sup>th</sup> row represents the visual right-hand side of
         * the image, and the 0<sup>th</sup> column represents the visual
         * bottom.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Orientation}.
         */
	public static final int ORIENTATION_RIGHT_ROW_BOTTOM_COLUMN = 7;

        /**
         * Orientation origin is left-bottom.
         *
         * <p>The 0<sup>th</sup> row represents the visual left-hand side of
         * the image, and the 0<sup>th</sup> column represents the visual
         * bottom.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Orientation}.
         */
	public static final int ORIENTATION_LEFT_ROW_BOTTOM_COLUMN = 8;

        /**
         * White is zero photometric interpretation.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#PhotometricInterpretation}.
         */
	public static final int PHOTOMETRIC_INTERPRETATION_WHITE_IS_ZERO = 0;

        /**
         * Black is zero photometric interpretation.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#PhotometricInterpretation}.
         */
	public static final int PHOTOMETRIC_INTERPRETATION_BLACK_IS_ZERO = 1;

        /**
         * RGB photometric interpretation.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#PhotometricInterpretation}.
         */
	public static final int PHOTOMETRIC_INTERPRETATION_RGB = 2;

        /**
         * Palette colour photometric interpretation.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#PhotometricInterpretation}.
         */
	public static final int PHOTOMETRIC_INTERPRETATION_PALETTE = 3;

        /**
         * Transparency mask photometric interpretation.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#PhotometricInterpretation}.
         */
	public static final int PHOTOMETRIC_INTERPRETATION_TRANSPARENCY = 4;

	/**
         * Chunky planar configuration.
         *
         * <p>The component values for each pixel are stored contiguously (i.e.
         * pixel interleave). The order of the components within the pixel is
         * specified by {@link  mil.nga.tiff.FieldTagType#PhotometricInterpretation}.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#PlanarConfiguration}.
         */
	public static final int PLANAR_CONFIGURATION_CHUNKY = 1;

        /**
         * Component plane planar configuration.
         *
         * <p>The components are stored in separate “component planes". (i.e.
         * component interleave). The order of the components within the pixel is
         * specified by {@link  mil.nga.tiff.FieldTagType#PhotometricInterpretation}.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#PlanarConfiguration}.
         */
	public static final int PLANAR_CONFIGURATION_PLANAR = 2;

	/**
         * Resolution unit none.
         *
         * <p>No absolute unit of measurement. Used for images that may have a
         * non-square aspect ratio, but no meaningful absolute dimensions.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#ResolutionUnit}.
         */
	public static final int RESOLUTION_UNIT_NO = 1;

        /**
         * Resolution unit inch.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#ResolutionUnit}.
         */
	public static final int RESOLUTION_UNIT_INCH = 2;

        /**
         * Resolution unit centimetre.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#ResolutionUnit}.
         */
	public static final int RESOLUTION_UNIT_CENTIMETER = 3;

	/**
         * Unsigned integer sample format.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#SampleFormat}.
         */
	public static final int SAMPLE_FORMAT_UNSIGNED_INT = 1;

        /**
         * Two's complement signed integer sample format.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#SampleFormat}.
         */
	public static final int SAMPLE_FORMAT_SIGNED_INT = 2;

        /**
         * IEEE floating point sample format.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#SampleFormat}.
         */
	public static final int SAMPLE_FORMAT_FLOAT = 3;

        /**
         * Undefined data sample format.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#SampleFormat}.
         */
	public static final int SAMPLE_FORMAT_UNDEFINED = 4;

	/**
         * Full resolution image data subfile type.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#SubfileType}.
         */
	public static final int SUBFILE_TYPE_FULL = 1;

        /**
         * Reduced resolution image data subfile type.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#SubfileType}.
         */
	public static final int SUBFILE_TYPE_REDUCED = 2;

        /**
         * Single page of multi-page image subfile type.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#SubfileType}.
         */
	public static final int SAMPLE_FORMAT_SINGLE_PAGE_MULTI_PAGE = 3;

	/**
         * No dithering or halftoning.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Threshholding}.
         */
	public static final int THRESHHOLDING_NO = 1;

        /**
         * Ordered dithering or halftoning.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Threshholding}.
         */
	public static final int THRESHHOLDING_ORDERED = 2;

        /**
         * Random dithering or halftoning.
         *
         * <p>A randomized process such as error diffusion has been applied to the image data.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Threshholding}.
         */
	public static final int THRESHHOLDING_RANDOM = 3;

	/**
         * No differencing predictor.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Predictor}.
         */
	public static final int PREDICTOR_NO = 1;

        /**
         * Horizontal differencing predictor.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Predictor}.
         */
	public static final int PREDICTOR_HORIZONTAL = 2;

        /**
         * Floating point differencing predictor.
         *
         * <p>This is one of the options for {@link mil.nga.tiff.FieldTagType#Predictor}.
         *
         * <p>This is not in the baseline TIFF 6.0 specification. See TIFF Technical
         * Note 3.
         */
	public static final int PREDICTOR_FLOATINGPOINT = 3;

}
