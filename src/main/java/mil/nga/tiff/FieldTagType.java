package mil.nga.tiff;

import java.util.HashMap;
import java.util.Map;

/**
 * Field Tag Types.
 *
 * @author osbornb
 */
public enum FieldTagType {

        /**
         * Artist.
         *
         * <p>Person who created the image.
         *
         * <p>Some older TIFF files used this tag for storing Copyright information.
         *
         * <p>Source: TIFF 6 specification.
         */
	Artist(315, false),

        /**
         * Bits per sample.
         *
         * <p>Number of bits per component. This is an array field, where
         * the number of elements is equal to the number of samples per pixel.
         * The number of bits may be different in each component.
         *
         * <p>Source: TIFF 6 specification.
         */
	BitsPerSample(258, true),

        /**
         * Cell Length.
         *
         * <p>The length of the dithering or halftoning matrix used to create a
         * dithered or halftoned bilevel file. This field should only be present
         * if Threshholding = 2.
         *
         * <p>Source: TIFF 6 specification.
         */
	CellLength(265, false),

        /**
         * Cell Width.
         *
         * <p>The width of the dithering or halftoning matrix used to create a
         * dithered or halftoned bilevel file. This field should only be present
         * if Threshholding = 2.
         *
         * <p>Source: TIFF 6 specification.
         */
	CellWidth(264, false),

        /**
         * Palette colour map (ColorMap).
         *
         * <p>A colour map for palette color images.
         *
         * <p>This field defines a Red-Green-Blue color map (often called a
         * lookup table) for palette-color images. In a palette-color image, a
         * pixel value is used to index into an RGB lookup table. For example,
         * a palette-color pixel having a value of 0 would be displayed
         * according to the 0th Red, Green, Blue triplet.
         *
         * <p>In a TIFF ColorMap, all the Red values come first, followed by the
         * Green values, then the Blue values. The number of values for each
         * color is 2**BitsPerSample. Therefore, the ColorMap field for an
         * 8-bit palette-color image would have 3 * 256 values. That is, this
         * is an array field.
         *
         * <p>The width of each value is 16 bits, as implied by the type of
         * SHORT. 0 represents the minimum intensity, and 65535 represents the
         * maximum intensity. Black is represented by 0,0,0, and white by
         * 65535, 65535, 65535.
         *
         * <p>As of Adobe PageMaker 6.0 TIFF Technical Notes (1995), ColorMap
         * can contain the color map for any color space. The number of
         * components in the ColorMap depends on the color space: for RGB and
         * CIELab images there are 3 components, for CMYK there are 4
         * components, etc.
         *
         * <p>No default. ColorMap must be included in all palette-color images.
         *
         * <p>Source: TIFF 6 specification and Adobe PageMaker 6.0 TIFF
         * Technical Notes (1995).
         *
         * @see PhotometricInterpretation
         */
	ColorMap(320, true),

        /**
         * Compression.
         *
         * <p>Compression scheme used on the image data.
         *
         * <p>Source: TIFF 6 specification.
         */
        Compression(259, false),

        /**
         * Copyright.
         *
         * <p>Copyright notice.
         *
         * <p>Copyright notice of the person or organization that claims the
         * copyright to the image. The complete copyright statement should be
         * listed in this field including any dates and statements of claims.
         * For example, “Copyright, John Smith, 19xx. All rights reserved.”
         *
         * <p>Source: TIFF 6 specification.
         */
	Copyright(33432, false),

        /**
         * Date Time.
         *
         * <p>Date and time of image creation.
         *
         * <p>The format is: {@code YYYY:MM:DD HH:MM:SS}, with hours like those
         * on a 24-hour clock, and one space character between the date and the
         * time. The length of the string, including the terminating NUL, is
         * 20 bytes.
         *
         * <p>Source: TIFF 6 specification.
         */
	DateTime(306, false),

        /**
         * Extra Samples.
         *
         * <p>Description of extra components.
         *
         * <p>Specifies that each pixel has <i>m</i> extra components whose
         * interpretation is defined by one of the values listed below. When
         * this field is used, the {@link SamplesPerPixel} field has a value
         * greater than the {@link PhotometricInterpretation} field suggests.
         *
         * <p>{@code ExtraSamples} is typically used to include non-colour
         * information, such as opacity, in an image. The possible values for
         * each item in the field's value are:
         *
         * <ul>
         * <li>0 = Unspecified data.
         * <li>1 = Associated alpha data (with pre-multiplied color)
         * <li>2 = Unassociated alpha data
         * </ul>
         *
         * <p>Source: TIFF 6 specification.
         */
	ExtraSamples(338, true),

        /**
         * Fill Order.
         *
         * <p>The logical order of bits within a byte.
         *
         * <p>1 = pixels are arranged within a byte such that pixels with lower
         * column values are stored in the higher-order bits of the byte.
         *
         * <p>2 = pixels are arranged within a byte such that pixels with lower
         * column values are stored in the lower-order bits of the byte.
         *
         * <p>Source: TIFF 6 specification.
         */
	FillOrder(266, false),

        /**
         * Free byte counts.
         *
         * <p>For each string of contiguous unused bytes in a TIFF file, the
         * number of bytes in the string.
         *
         * <p>Not recommended for general interchange.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see FreeOffsets
         */
	FreeByteCounts(289, false),

        /**
         * Free Offsets.
         *
         * <p>For each string of contiguous unused bytes in a TIFF file, the
         * byte offset of the string.
         *
         * <p>Not recommended for general interchange.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see FreeByteCounts
         */
	FreeOffsets(288, false),

        /**
         * Gray Response Curve.
         *
         * <p>For grayscale data, the optical density of each possible pixel value.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see GrayResponseUnit
         * @see PhotometricInterpretation
         */
	GrayResponseCurve(291, false),

        /**
         * Gray Response Unit.
         *
         * <p>The precision of the information contained in the
         * {@link GrayResponseCurve}.
         *
         * <p>Because optical density is specified in terms of fractional
         * numbers, this field is necessary to interpret the stored integer
         * information. For example, if {@code GrayScaleResponseUnits} is set
         * to 4 (ten-thousandths of a unit), and a {@link GrayResponseCurve}
         * number for gray level 4 is 3455, then the resulting actual value is
         * 0.3455.
         *
         * <p>Optical densitometers typically measure densities within the
         * range of 0.0 to 2.0.
         *
         * <ul>
         * <li>1 = Number represents tenths of a unit.
         * <li>2 = Number represents hundredths of a unit.
         * <li>3 = Number represents thousandths of a unit.
         * <li>4 = Number represents ten-thousandths of a unit.
         * <li>5 = Number represents hundred-thousandths of a unit.
         * </ul>
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see GrayResponseCurve
         */
	GrayResponseUnit(290, false),

        /**
         * Host Computer.
         *
         * <p>The computer and/or operating system in use at the time of image
         * creation.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see Make
         * @see Model
         * @see Software
         */
	HostComputer(316, false),

        /**
         * Image Description.
         *
         * <p>A string that describes the subject of the image.
         *
         * <p>For example, a user may wish to attach a comment such as
         * “1988 company picnic” to an image.
         *
         * <p>Source: TIFF 6 specification.
         */
	ImageDescription(270, false),

        /**
         * Image Width.
         *
         * <p>The number of columns in the image, i.e. the number of pixels per row.
         *
         * <p>Source: TIFF 6 specification.
         */
        ImageWidth(256, false),

        /**
         * Image Length.
         *
         * <p>The number of rows of pixels in the image.
         *
         * <p>Source: TIFF 6 specification.
         */
        ImageLength(257, false),

        /**
         * Make.
         *
         * <p>The scanner manufacturer.
         *
         * <p>Manufacturer of the scanner, video digitizer, or other type of
         * equipment used to generate the image. Synthetic images should not
         * include this field.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see Model
         * @see Software
         */
	Make(271, false),

        /**
         * Maximum Sample Value.
         *
         * <p>The maximum component value used.
         *
         * <p>This field is not to be used to affect the visual appearance of
         * an image when it is displayed or printed. Nor should this field
         * affect the interpretation of any other field; it is used only for
         * statistical purposes.
         *
         * <p>This is an array field, where the number of elements is equal to
         * the number of samples per pixel (i.e. number of components).
         *
         * <p>Default is 2**(BitsPerSample) - 1.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see MinSampleValue
         */
	MaxSampleValue(281, true),

        /**
         * Minimum Sample Value.
         *
         * <p>The minimum component value used.
         *
         * <p>This field is not to be used to affect the visual appearance of
         * an image when it is displayed or printed. Nor should this field
         * affect the interpretation of any other field; it is used only for
         * statistical purposes.
         *
         * <p>This is an array field, where the number of elements is equal to
         * the number of samples per pixel (i.e. number of components).
         *
         * <p>Default is 0.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see MaxSampleValue
         */
	MinSampleValue(280, false),

        /**
         * Model.
         *
         * <p>The scanner model name or number.
         *
         * <p>The model name or number of the scanner, video digitizer, or
         * other type of equipment used to generate the image.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see Make
         * @see Software
         */
	Model(272, false),

        /**
         * New Subfile Type.
         *
         * <p>A general indication of the kind of data contained in this
         * subfile.
         *
         * <p>Replaces the old {@link SubfileType} field, due to limitations in
         * the definition of that field. {@code NewSubfileType} is mainly useful
         * when there are multiple subfiles in a single TIFF file.
         *
         * <p>This field is made up of a set of 32 flag bits. See TIFF 6
         * specification for the interpretation.
         *
         * <p>Source: TIFF 6 specification.
         */
	NewSubfileType(254, false),

        /**
         * Orientation.
         *
         * <p>The orientation of the image with respect to the rows and columns.
         *
         * <ul>
         * <li>1 = The 0th row represents the visual top of the image, and the 0th
         * column represents the visual left-hand side.
         * <li>2 = The 0th row represents the visual top of the image, and the 0th
         * column represents the visual right-hand side.
         * <li>3 = The 0th row represents the visual bottom of the image, and the
         * 0th column represents the visual right-hand side.
         * <li>4 = The 0th row represents the visual bottom of the image, and the
         * 0th column represents the visual left-hand side.
         * <li>5 = The 0th row represents the visual left-hand side of the image,
         * and the 0th column represents the visual top.
         * <li>6 = The 0th row represents the visual right-hand side of the image,
         * and the 0th column represents the visual top.
         * <li>7 = The 0th row represents the visual right-hand side of the image,
         * and the 0th column represents the visual bottom.
         * <li>8 = The 0th row represents the visual left-hand side of the image,
         * and the 0th column represents the visual bottom.
         * </ul>
         *
         * <p>Default is 1.
         *
         * <p>Source: TIFF 6 specification.
         */
	Orientation(274, false),

        /**
         * Photometric Interpretation.
         *
         * <p>The color space of the image data.
         *
         * <ul>
         * <li>0 = WhiteIsZero.
         * <li>1 = BlackIsZero.
         * <li>2 = RGB.
         * <li>3 = Palette color.
         * <li>4 = Transparency Mask.
         * <li>5 = Separated (usually CMYK).
         * <li>6 = YCbCr.
         * <li>8 = 1976 CIE L*a*b*
         * <li>9 = ICCLab encoding
         * </ul>
         *
         * <p>Source: TIFF 6 specification and TIFF Technical Notes (2002).
         */
	PhotometricInterpretation(262, false),

        /**
         * Planar Configuration.
         *
         * <p>How the components of each pixel are stored.
         *
         * <p>1 = Chunky format. The component values for each pixel are stored
         * contiguously. The order of the components within the pixel is
         * specified by {@link PhotometricInterpretation}. For example, for RGB
         * data, the data is stored as {@code RGBRGBRGB...}
         *
         * <p>2 = Planar format. The components are stored in separate
         * "component planes".
         *
         * <p>If {@link SamplesPerPixel} is 1, {@code PlanarConfiguration} is
         * irrelevant, and need not be included.
         *
         * <p>Default is 1.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see BitsPerSample
         * @see SamplesPerPixel
         */
	PlanarConfiguration(284, false),

        /**
         * Resolution Unit.
         *
         * <p>The unit of measurement for {@link XResolution} and
         * {@link YResolution}.
         *
         * <p>1 = No absolute unit of measurement. Used for images that may have
         * a non-square aspect ratio, but no meaningful absolute dimensions.
         *
         * <p>2 = Inch. (Default)
         *
         * <p>3 = Centimetre.
         *
         * <p>Source: TIFF 6 specification.
         */
	ResolutionUnit(296, false),

        /**
         * Rows Per Strip.
         *
         * <p>The number of rows in each strip (except possibly the last strip).
         *
         * <p>For example, if ImageLength is 24, and RowsPerStrip is 10, then
         * there are 3 strips, with 10 rows in the first strip, 10 rows in the
         * second strip, and 4 rows in the third strip. The data in the last
         * strip is not padded with 6 extra rows of dummy data.
         *
         * <p>Source: TIFF 6 specification.
         */
	RowsPerStrip(278, false),

        /**
         * Samples Per Pixel.
         *
         * <p>The number of components per pixel.
         *
         * <p>{@code SamplesPerPixel} is <i>usually</i> 1 for bilevel,
         * grayscale, and palette-colour images.
         *
         * <p>{@code SamplesPerPixel} is <i>usually</i> 3 for RGB images.
         *
         * <p>Default = 1.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see BitsPerSample
         * @see PhotometricInterpretation
         * @see ExtraSamples
         */
	SamplesPerPixel(277, false),

        /**
         * Software.
         *
         * <p>Name and version number of the software package(s) used to create
         * the image.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see Make
         * @see Model
         */
	Software(305, false),

        /**
         * Strip Byte Counts.
         *
         * <p>For each strip, the number of bytes in the strip after compression.
         *
         * <p>This tag is required for Baseline TIFF files. There is no default.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see StripOffsets
         * @see RowsPerStrip
         * @see TileOffsets
         * @see TileByteCounts
         */
	StripByteCounts(279, true),

        /**
         * Strip Offsets.
         *
         * <p>For each strip, the byte offset of that strip.
         *
         * <p>The offset is specified with respect to the beginning of the TIFF
         * file. Note that this implies that each strip has a location
         * independent of the locations of other strips. This feature may be
         * useful for editing applications. This required field is the only way
         * for a reader to find the image data. (Unless TileOffsets is used; see
         * TileOffsets.)
         *
         * <p>No default.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see StripByteCounts
         * @see RowsPerStrip
         * @see TileOffsets
         * @see TileByteCounts
         */
	StripOffsets(273, true),

        /**
         * Subfile Type.
         *
         * <p>A general indication of the kind of data contained in this
         * subfile.
         *
         * <p>Currently defined values are:
         * <ul>
         * <li>1 = full-resolution image data.
         * <li>2 = reduced-resolution image data
         * <li>3 = a single page of a multi-page image - see the
         * {@link PageNumber} field description.
         * </ul>
         *
         * <p>Note that several image types may be found in a single TIFF file,
         * with each subfile described by its own IFD.
         *
         * <p>No default.
         *
         * <p>This field is deprecated. The {@link NewSubfileType} field should
         * be used instead.
         *
         * <p>Source: TIFF 6 specification.
         */
	SubfileType(255, false),

        /**
         * Threshholding.
         *
         * <p>For black and white TIFF files that represent shades of gray, the
         * technique used to convert from gray to black and white pixels. This
         * is an enumeration value.
         *
         * <ul>
         * <li>1 = No dithering or halftoning has been applied to the image
         * data. (Default value).
         * <li>2 = An ordered dither or halftone technique has been applied to
         * the image data.
         * <li>3 = A randomized process such as error diffusion has been applied
         * to the image data.
         * </ul>
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see CellLength
         * @see CellWidth
         */
	Threshholding(263, false),

        /**
         * X Resolution.
         *
         * <p>The number of pixels per {@link ResolutionUnit} in the
         * {@link ImageWidth} direction.
         *
         * <p>It is not mandatory that the image be actually displayed or
         * printed at the size implied by this parameter. It is up to the
         * application to use this information as it wishes.
         *
         * <p>No default.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see YResolution
         * @see ResolutionUnit
         */
	XResolution(282, false),

        /**
         * Y Resolution.
         *
         * <p>The number of pixels per {@link ResolutionUnit} in the
         * {@link ImageLength} direction.
         *
         * <p>No default.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see XResolution
         * @see ResolutionUnit
         */
	YResolution(283, false),

	// TIFF Extended

        /**
         * Bad Fax Lines.
         *
         * <p>The number of "bad" scan lines encountered by the facsimile device
         * during reception. A "bad" scanline is defined as a scanline that,
         * when decoded, comprises an incorrect number of pixels.
         *
         * <p>Source: RFC 2301 File Format for Internet Fax.
         */
	BadFaxLines(326, false),

        /**
         * Clean Fax Data(327).
         *
         * <p>Indicates if "bad" lines encountered during reception are stored
         * in the data, or if "bad" lines have been replaced by the receiver.
         *
         * <ul>
         * <li>0 = No "bad" lines
         * <li>1 = "bad" lines exist, but were regenerated by the receiver,
         * <li>2 = "bad" lines exist, but have not been regenerated.
         * </ul>
         *
         * <p>Source: RFC 2301 File Format for Internet Fax.
         */
	CleanFaxData(327, false),

        /**
         * ClipPath.
         *
         * <p>A TIFF ClipPath is intended to mirror the essentials of
         * PostScript’s path creation functionality, so that the operators
         * listed below can be easily translated into PostScript, and
         * conversely, any PostScript path can be represented as a TIFF
         * ClipPath.
         *
         * <p>Source: Adobe PageMaker 6.0 TIFF Technical Notes (1995)
         */
	ClipPath(343, true),

        /**
         * Consecutive Bad Fax Lines.
         *
         * <p>Maximum number of consecutive "bad" scanlines received. The
         * {@link BadFaxLines} field indicates only the quantity of bad lines.
         *
         * <p>Source: RFC 2301 File Format for Internet Fax.
         */
	ConsecutiveBadFaxLines(328, false),

        /**
         * Decode.
         *
         * <p>Describes how to map image sample values into the range of values
         * appropriate for the current color space. In general, the values are
         * taken in pairs and specify the minimum and maximum output value for
         * each color component.
         *
         * <p>Source: RFC 2301 File Format for Internet Fax.
         */
	Decode(433, true),

        /**
         * Default Image Color.
         *
         * <p>In areas where no image data is available, a default color is
         * needed to specify the color value. If the {@link StripByteCounts} value
         * for a strip is 0, then the color for that strip must be defined by a
         * default image color.
         *
         * <p>Source: RFC 2301 File Format for Internet Fax.
         */
	DefaultImageColor(434, true),

        /**
         * Document Name.
         *
         * <p>The name of the document from which this image was scanned.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see PageName
         */
	DocumentName(269, false),

        /**
         * Dot Range.
         *
         * <p>The component values that correspond to a 0% dot and 100% dot.
         * {@code DotRange[0]} corresponds to a 0% dot, and {@code DotRange[1]}
         * corresponds to a 100% dot.
         *
         * <p>Source: TIFF 6 specification.
         */
	DotRange(336, true),

        /**
         * Halftone Hints.
         *
         * <p>The purpose of the {@code HalftoneHints} field is to convey to
         * the halftone function the range of gray levels within a
         * colorimetrically-specified image that should retain tonal detail.
         * The field contains two values of sixteen bits each and, therefore, is
         * contained wholly within the field itself; no offset is required.
         * The first word specifies the highlight gray level which should be
         * halftoned at the lightest printable tint of the final output device.
         * The second word specifies the shadow gray level which should be
         * halftoned at the darkest printable tint of the final output device.
         * Portions of the image which are whiter than the highlight gray level
         * will quickly, if not immediately, fade to specular highlights. There
         * is no default value specified, since the highlight and shadow gray
         * levels are a function of the subject matter of a particular image.
         *
         * <p>Source: TIFF 6 specification.
         */
	HalftoneHints(321, true),

        /**
         * Indexed.
         *
         * <p>Indexed images are images where the “pixels” do not represent
         * color values, but rather an index (usually 8-bit) into a separate
         * color table, the {@link ColorMap}. ColorMap is required for an
         * Indexed image.
         *
         * <p>The {@link PhotometricInterpretation} type of PaletteColor may
         * still be used, and is equivalent to specifying an RGB image with the
         * Indexed flag set, a suitable ColorMap, and SamplesPerPixel = 1.
         *
         * <p>Source: Adobe PageMaker 6.0 TIFF Technical Notes (1995)
         */
	Indexed(346, false),

        /**
         * JPEG Tables.
         *
         * <p>{@code JPEGTables} provides default JPEG quantization and/or
         * Huffman tables which are used whenever a segment datastream does not
         * contain its own tables.
         *
         * <p>This is associated with new JPEG (Compression = 7) encoding.
         *
         * <p>Source: TIFF Technical Note 2 (1995).
         */
	JPEGTables(347, false),

        /**
         * Page Name.
         *
         * <p>The name of the page from which this image was scanned.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see DocumentName
         */
	PageName(285, false),

        /**
         * Page Number.
         *
         * <p>The page number of the page from which this image was scanned.
         *
         * <p>This field is used to specify page numbers of a multiple page
         * (e.g. facsimile) document. There are always two values.
         * {@code PageNumber[0]} is the page number; {@code PageNumber[1]} is
         * the total number of pages in the document. If
         * {@code PageNumber[1]} is 0, the total number of pages in the document
         * is not available.
         *
         * <p>Pages need not appear in numerical order.
         *
         * <p>The first page is numbered 0 (zero).
         *
         * <p>Source: TIFF 6 specification.
         */
	PageNumber(297, true),

        /**
         * Predictor.
         *
         * <p>A predictor is a mathematical operator that is applied to the
         * image data before an encoding scheme is applied.
         *
         * The possible values are:
         * <ul>
         * <li>1 = No prediction scheme used before coding.
         * <li>2 = Horizontal differencing.
         * <li>3 = Floating point predictor.
         * </ul>
         *
         * <p>Source: TIFF 6 specification and TIFF Technical Note 3.
         */
	Predictor(317, false),

        /**
         * Primary Chromaticities.
         *
         * <p>The chromaticities of the primaries of the image. This is the
         * chromaticity for each of the primaries when it has its
         * {@code ReferenceWhite} value and the other primaries have their
         * {@code ReferenceBlack} values. These values are described using the
         * 1931 CIE xy chromaticity diagram and only the chromaticities are
         * specified. These values can correspond to the chromaticities of the
         * phosphors of a monitor, the filter set and light source combination
         * of a scanner or the imaging model of a rendering package.
         * The ordering is {@code red[x], red[y], green[x], green[y], blue[x],
         * blue[y]}.
         *
         * <p>For example the CCIR Recommendation 709 primaries are:
         *
         * <p>640/1000, 330/1000,
         * <p>300/1000, 600/1000,
         * <p>150/1000, 60/1000
         *
         * <p>Source: TIFF 6 specification.
         */
	PrimaryChromaticities(319, true),

        /**
         * Reference Black White.
         *
         * <p>Specifies a pair of headroom and footroom image data values
         * (codes) for each pixel component. The first component code within a
         * pair is associated with ReferenceBlack, and the second is associated
         * with ReferenceWhite. The ordering of pairs is the same as those for
         * pixel components of the {@link PhotometricInterpretation} type.
         * ReferenceBlackWhite can be applied to images with a
         * {@link PhotometricInterpretation} value of {@code RGB} or
         * {@code YCbCr}. {@code ReferenceBlackWhite} is not used with other
         * {@link PhotometricInterpretation} values.
         *
         * <p>Source: TIFF 6 specification.
         */
	ReferenceBlackWhite(532, true),

        /**
         * Sample Format.
         *
         * <p>This field specifies how to interpret each data sample in a pixel.
         *
         * Possible values are:
         * <ul>
         * <li>1 = unsigned integer data
         * <li>2 = two’s complement signed integer data
         * <li>3 = IEEE floating point data [IEEE]
         * <li>4 = undefined data format
         * </ul>
         *
         * <p>Note that the {@code SampleFormat} field does not specify the
         * size of data samples; this is still done by the
         * {@link BitsPerSample} field.
         *
         * <p>Source: TIFF 6 specification.
         */
	SampleFormat(339, true),

        /**
         * Minimum Sample Value.
         *
         * <p>This field specifies the minimum sample value. Note that a value
         * should be given for each data sample - this is an array field. That
         * is, if the image has 3 {@link SamplesPerPixel}, 3 values must be
         * specified.
         *
         * <p>The default for {@code SMinSampleValue} and {@code SMaxSampleValue}
         * is the full range of the data type.
         *
         * <p>Source: TIFF 6 specification.
         */
	SMinSampleValue(340, true),

        /**
         * Maximum Sample Value.
         *
         * <p>This field specifies the maximum sample value. Note that a value
         * should be given for each data sample - this is an array field. That
         * is, if the image has 3 {@link SamplesPerPixel}, 3 values must be
         * specified.
         *
         * <p>The default for {@code SMinSampleValue} and {@code SMaxSampleValue}
         * is the full range of the data type.
         *
         * <p>Source: TIFF 6 specification.
         */
	SMaxSampleValue(341, true),

        /**
         * Strip Row Counts.
         *
         * <p>The number of scanlines stored in a strip. MRC allows each fax
         * strip to store a different number of scanlines. For strips with more
         * than one layer there is a maximum strip size of 256 scanlines or full
         * page size. The 256 maximum SHOULD be used unless the capability to
         * receive longer strips has been negotiated. This field replaces
         * {@link RowsPerStrip} for IFDs with variable-sized strips.
         *
         * <p>Source: RFC 2301 File Format for Internet Fax.
         */
	StripRowCounts(559, true),

        /**
         * SubIFDs.
         *
         * <p>Each value is an offset (from the beginning of the TIFF file, as
         * always) to a child IFD. Child images provide extra information for
         * the parent image - such as a subsampled version of the parent image.
         *
         * <p>TIFF data type 13, {@code IFD} is otherwise identical to LONG,
         * but is only used to point to other valid IFDs.
         *
         * <p>Source: Adobe PageMaker 6.0 TIFF Technical Notes (1995)
         */
	SubIFDs(330, true),

        /**
         * T4 Options.
         *
         * <p>This field is made up of a set of 32 flag bits and is used
         * with {@link Compression} value of {@code 3}. See TIFF 6.0
         * specification.
         *
         * <p>Source: TIFF 6 specification.
         */
	T4Options(292, false),

        /**
         * T6 Options.
         *
         * <p>This field is made up of a set of 32 flag bits and is used
         * with {@link Compression} value of {@code 4}. See TIFF 6.0
         * specification.
         *
         * <p>Source: TIFF 6 specification.
         */
	T6Options(293, false),

        /**
         * Tile Byte Counts.
         *
         * <p>For each tile, the number of (compressed) bytes in that tile.
         *
         * <p>See {@link TileOffsets} for a description of how the byte counts
         * are ordered.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see TileWidth
         * @see TileLength
         * @see TileOffsets
         */
	TileByteCounts(325, true),

        /**
         * Tile Length.
         *
         * <p>The tile length (height) in pixels. This is the number of rows in
         * each tile. {@code TileLength} must be a multiple of 16 for
         * compatibility with compression schemes such as JPEG.
         *
         * <p>Replaces {@link RowsPerStrip} in tiled TIFF files.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see TileWidth
         * @see TileOffsets
         * @see TileByteCounts
         */
	TileLength(323, false),

        /**
         * Tile Offsets.
         *
         * <p>For each tile, the byte offset of that tile, as compressed and
         * stored on disk. The offset is specified with respect to the
         * beginning of the TIFF file. Note that this implies that each tile
         * has a location independent of the locations of other tiles.
         *
         * <p>Offsets are ordered left-to-right and top-to-bottom. For
         * {@link PlanarConfiguration} of {@code 2}, the offsets for the first
         * component plane are stored first, followed by all the offsets for
         * the second component plane, and so on.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see TileWidth
         * @see TileLength
         * @see TileByteCounts
         */
	TileOffsets(324, true),

        /**
         * Tile Width.
         *
         * <p>The tile width in pixels. This is the number of columns in each
         * tile.
         *
         * <p>{@code TileWidth} and {@link ImageWidth} together determine the
         * number of tiles that span the width of the image.
         *
         * <p>{@code TileWidth} must be a multiple of 16. This restriction
         * improves performance in some graphics environments and enhances
         * compatibility with compression schemes such as JPEG.
         *
         * <p>Tiles need not be square.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see TileLength
         * @see TileOffsets
         * @see TileByteCounts
         */
	TileWidth(322, false),

        /**
         * Transfer Function.
         *
         * <p>Describes a transfer function for the image in tabular style.
         * Pixel components can be gamma-compensated, companded, non-uniformly
         * quantized, or coded in some other way. The {@code TransferFunction}
         * maps the pixel components from a non-linear {@link BitsPerSample}
         * (e.g. 8-bit) form into a 16-bit linear form without a perceptible
         * loss of accuracy.
         *
         * <p>See TIFF 6 Specification for calculations.
         *
         * <p>Source: TIFF 6 specification.
         */
	TransferFunction(301, true),

        /**
         * White Point.
         *
         * <p>The chromaticity of the white point of the image. This is the
         * chromaticity when each of the primaries has its ReferenceWhite value.
         * The value is described using the 1931 CIE xy chromaticity diagram
         * and only the chromaticity is specified. This value can correspond to
         * the chromaticity of the alignment white of a monitor, the filter set
         * and light source combination of a scanner or the imaging model of a
         * rendering package. There are always two values. The ordering is
         * {@code white[x], white[y]}.
         *
         * <p>For example, the CIE Standard Illuminant D65 used by CCIR
         * Recommendation 709 and Kodak PhotoYCC is {@code 3127/10000,3290/10000}.
         *
         * <p>Source: TIFF 6 specification.
         */
	WhitePoint(318, true),

        /**
         * X Clip Path Units.
         *
         * <p>The number of units that span the width of the image, in terms of
         * integer {@link ClipPath} coordinates.
         *
         * <p>All horizontal ClipPath coordinates will be divided by this value
         * in order to get a number that is (usually) between 0.0 and 1.0, where
         * 0.0 represents the left side of the image and 1.0 represents the
         * right side of the image.
         *
         * <p>Required for every TIFF ClipPath.
         *
         * <p>Source: Adobe PageMaker 6.0 TIFF Technical Notes (1995)
         */
	XClipPathUnits(344, false),

        /**
         * X Position.
         *
         * <p>X position of the image.
         *
         * <p>The X offset in {@link ResolutionUnit}s of the left side of the
         * image, with respect to the left side of the page.
         *
         * <p>No default.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see YPosition
         */
	XPosition(286, false),

        /**
         * YCbCr Coefficients.
         *
         * <p>The transformation from RGB to YCbCr image data. The
         * transformation is specified as three rational values that represent
         * the coefficients used to compute luminance, Y.
         *
         * <p>The three rational coefficient values are the proportions of red,
         * green, and blue respectively in luminance, Y. See TIFF 6
         * specification for calculations.
         *
         * <p>Source: TIFF 6 specification.
         */
	YCbCrCoefficients(529, true),

        /**
         * YCbCr Positioning.
         *
         * <p>Specifies the positioning of subsampled chrominance components
         * relative to luminance samples. See TIFF 6 Specification.
         *
         * <p>Source: TIFF 6 specification.
         */
	YCbCrPositioning(531, false),

        /**
         * YCbCr SubSampling.
         *
         * <p>Specifies the subsampling factors used for the chrominance
         * components of a YCbCr image. The two fields of this field,
         * {@code YCbCrSubsampleHoriz} and {@code YCbCrSubsampleVert}, specify
         * the horizontal and vertical subsampling factors respectively.
         *
         * The two fields of this field are defined as follows:
         *
         * <p>Short 0: YCbCr SubsampleHoriz:
         * <ul>
         * <li>1 = {@code ImageWidth} of this chroma image is equal to the
         * {@code ImageWidth} of the associated luma image.
         * <li>2 = {@code ImageWidth} of this chroma image is half the
         * {@code ImageWidth} of the associated luma image.
         * <li>4 = {@code ImageWidth} of this chroma image is one-quarter the
         * {@code ImageWidth} of the associated luma image.
         * </ul>
         *
         * <p>Short 1: YCbCrSubsampleVert:
         * <ul>
         * <li>1 = {@code ImageLength} (height) of this chroma image is equal
         * to the {@code ImageLength} of the associated luma image.
         * <li>2 = {@code ImageLength} (height) of this chroma image is half
         * the {@code ImageLength} of the associated luma image.
         * <li>4 = {@code ImageLength} (height) of this chroma image is
         * one-quarter the {@code ImageLength} of the associated luma image.
         * </ul>
         *
         * <p>Both Cb and Cr have the same subsampling ratio.
         *
         * <p>Source: TIFF 6 specification.
         */
	YCbCrSubSampling(530, true),

        /**
         * Y Clip Path Units.
         *
         * <p>The number of units that span the height of the image, in terms
         * of integer {@link ClipPath} coordinates.
         *
         * <p>All vertical ClipPath coordinates will be divided by this value
         * in order to get a number that is (usually) between 0.0 and 1.0,
         * where 0.0 represents the top of the image and 1.0 represents the
         * bottom of the image.
         *
         * <p>Optional. Default is {@code YClipPathUnits} =
         * {@link XClipPathUnits}.
         *
         * <p>Source: Adobe PageMaker 6.0 TIFF Technical Notes (1995)
         */
	YClipPathUnits(345, false),

        /**
         * Y Position.
         *
         * <p>Y position of the image.
         *
         * <p>The Y offset in {@link ResolutionUnit}s of the top of the image,
         * with respect to the top of the page. In the TIFF coordinate scheme,
         * the positive Y direction is down, so that {@code YPosition} is
         * always positive.
         *
         * <p>No default.
         *
         * <p>Source: TIFF 6 specification.
         *
         * @see XPosition
         */
	YPosition(287, false),

	// JPEG

        /**
         * JPEG Process.
         *
         * <p>This Field indicates the JPEG process used to produce the compressed data.
         *
         * <p>Source: TIFF 6 specification.
         *
         * <p>This is associated with "Old JPEG" process, which is discouraged as
         * of TIFF Technical Note 2 (1995).
         */
	JPEGProc(512, false),

        /**
         * JPEG Interchange Format.
         *
         * <p>This Field indicates whether a JPEG interchange format bitstream
         * is present in the TIFF file. If a JPEG interchange format bitstream
         * is present, then this Field points to the Start of Image (SOI) marker
         * code.
         *
         * <p>Source: TIFF 6 specification.
         *
         * <p>This is associated with "Old JPEG" process, which is discouraged as
         * of TIFF Technical Note 2 (1995).
         */
	JPEGInterchangeFormat(513, false),

        /**
         * JPEG Interchange Format Length.
         *
         * <p>This Field indicates the length in bytes of the JPEG interchange
         * format bitstream. This Field is useful for extracting the JPEG
         * interchange format bitstream without parsing the bitstream.
         *
         * <p>Source: TIFF 6 specification.
         *
         * <p>This is associated with "Old JPEG" process, which is discouraged as
         * of TIFF Technical Note 2 (1995).
         */
	JPEGInterchangeFormatLength(514, false),

        /**
         * JPEG Restart Interval.
         *
         * <p>This Field indicates the length of the restart interval used in
         * the compressed image data. The restart interval is defined as the
         * number of Minimum Coded Units (MCUs) between restart markers.
         *
         * <p>Source: TIFF 6 specification.
         *
         * <p>This is associated with "Old JPEG" process, which is discouraged as
         * of TIFF Technical Note 2 (1995).
         */
	JPEGRestartInterval(515, false),

        /**
         * JPEG Lossless Predictors.
         *
         * <p>This Field points to a list of lossless predictor-selection
         * values, one per component.
         *
         * <p>Source: TIFF 6 specification.
         *
         * <p>This is associated with "Old JPEG" process, which is discouraged as
         * of TIFF Technical Note 2 (1995).
         */
	JPEGLosslessPredictors(517, true),

        /**
         * JPEG Point Transforms.
         *
         * <p>This Field points to a list of point transform values, one per
         * component. This Field is relevant only for lossless processes.
         *
         * <p>Source: TIFF 6 specification.
         *
         * <p>This is associated with "Old JPEG" process, which is discouraged as
         * of TIFF Technical Note 2 (1995).
         */
	JPEGPointTransforms(518, true),

        /**
         * JPEG Quantization Tables.
         *
         * <p>This Field points to a list of offsets to the quantization tables,
         * one per component. Each table consists of 64 BYTES (one for each DCT
         * coefficient in the 8x8 block). The quantization tables are stored in
         * zigzag order.
         *
         * <p>Source: TIFF 6 specification.
         *
         * <p>This is associated with "Old JPEG"  process, which is discouraged as
         * of TIFF Technical Note 2 (1995).
         */
	JPEGQTables(519, true),

        /**
         * JPEG DC Tables.
         *
         * <p>This Field points to a list of offsets to the DC Huffman tables
         * or the lossless Huffman tables, one per component.
         *
         * <p>Source: TIFF 6 specification.
         *
         * <p>This is associated with "Old JPEG" process, which is discouraged as
         * of TIFF Technical Note 2 (1995).
         */
	JPEGDCTables(520, true),

        /**
         * JPEG AC Tables.
         *
         * <p>This Field points to a list of offsets to the Huffman AC tables,
         * one per component.
         *
         * <p>Source: TIFF 6 specification.
         *
         * <p>This is associated with "Old JPEG" process, which is discouraged as
         * of TIFF Technical Note 2 (1995).
         */
	JPEGACTables(521, true),

	// EXIF

        /**
         * Aperture Value.
         *
         * <p>The lens aperture. The unit is the APEX (Additive System of
         * Photographic Exposure) value.
         *
         * <p>Source: JEITA CP-3451F / CIPA DC-008-2023 "Exchangeable image
         * file format for digital still cameras: Exif Version 3.0".
         */
	ApertureValue(37378, false),

        /**
         *  Color Space.
         *
         * <p>The color space information tag (ColorSpace) is always recorded
         * as the color space specifier. Normally sRGB (=1) is used to define
         * the color space based on the PC monitor conditions and environment.
         * If a color space other than sRGB is used, Uncalibrated (=FFFF.H) is
         * set.
         *
         * <p>Source: JEITA CP-3451F / CIPA DC-008-2023 "Exchangeable image
         * file format for digital still cameras: Exif Version 3.0".
         */
	ColorSpace(40961, false),

        /**
         * Date Time Digitized.
         *
         * <p>The date and time when the image was stored as digital data. If,
         * for example, an image was captured by a digital still camera (DSC)
         * and at the same time the file was recorded, then the
         * {@link DateTimeOriginal} and {@code DateTimeDigitized} will have the
         * same contents.
         *
         * <p>The format is "YYYY:MM:DD HH:MM:SS" with time shown in
         * 24-hour format, and the date and time separated by one blank
         * character [20.H]. When the date and time are unknown, all the
         * character spaces except colons (":") should be filled with blank
         * characters, or else the field should be filled with blank characters.
         * The character string length is 20 Bytes including NULL for
         * termination. When the field is left blank, it is treated as unknown.
         *
         * <p>Source: JEITA CP-3451F / CIPA DC-008-2023 "Exchangeable image
         * file format for digital still cameras: Exif Version 3.0".
         */
	DateTimeDigitized(36868, false),

        /**
         *  Date Time Original.
         *
         * <p>The date and time when the original image data was generated. For
         * a digital still camera (DSC) the date and time the picture was taken
         * are recorded.
         *
         * <p>The format is "YYYY:MM:DD HH:MM:SS" with time shown in
         * 24-hour format, and the date and time separated by one blank
         * character [20.H]. When the date and time are unknown, all the
         * character spaces except colons (":") should be filled with blank
         * characters, or else the field should be filled with blank characters.
         * The character string length is 20 Bytes including NULL for
         * termination. When the field is left blank, it is treated as unknown.
         *
         * <p>Source: JEITA CP-3451F / CIPA DC-008-2023 "Exchangeable image
         * file format for digital still cameras: Exif Version 3.0".
         */
	DateTimeOriginal(36867, true),

        /**
         * Exif IFD Pointer.
         *
         * <p>A pointer to the Exif IFD. The Exif IFD has the same structure as
         * that of the IFD specified in TIFF. However, it does not contain
         * image data as in the case of TIFF.
         *
         * <p>Source: JEITA CP-3451F / CIPA DC-008-2023 "Exchangeable image
         * file format for digital still cameras: Exif Version 3.0".
         */
	ExifIFD(34665, false),

        /**
         * Exif Version.
         *
         * <p>The version of the Exif standard supported. Nonexistence of this
         * field is taken to mean nonconformance to the standard. Conformance
         * to version 2.32 would be indicated by "0232", while version 3.0
         * would be indicated by "0300” as 4-byte ASCII. Since the type is
         * UNDEFINED, it shall not be terminated with NULL.
         *
         * <p>Source: JEITA CP-3451F / CIPA DC-008-2023 "Exchangeable image
         * file format for digital still cameras: Exif Version 3.0".
         */
	ExifVersion(36864, true),

        /**
         * Exposure Time.
         *
         * <p>Exposure time, given in seconds (sec).
         *
         * <p>Source: JEITA CP-3451F / CIPA DC-008-2023 "Exchangeable image
         * file format for digital still cameras: Exif Version 3.0".
         */
	ExposureTime(33434, false),

        /**
         * File Source.
         *
         * <p>Indicates the image source. If a digital still camera recorded
         * the image, this tag value always shall be set to 3.
         *
         * <p>Source: JEITA CP-3451F / CIPA DC-008-2023 "Exchangeable image
         * file format for digital still cameras: Exif Version 3.0".
         */
	FileSource(41728, false),

        /**
         * Flash.
         *
         * <p>This tag indicates the status of flash when the image was shot.
         * Bit 0 indicates the flash firing status, bits 1 and 2 indicate the
         * flash return status, bits 3 and 4 indicate the flash mode, bit 5
         * indicates whether the flash function is present, and bit 6 indicates
         * "red eye" mode.
         *
         * <p>Source: JEITA CP-3451F / CIPA DC-008-2023 "Exchangeable image
         * file format for digital still cameras: Exif Version 3.0"
         */
	Flash(37385, false),

        /**
         * Flashpix Version.
         *
         * <p>The Flashpix format version supported by a FPXR file. If the FPXR
         * function supports Flashpix format Ver. 1.0, this is indicated
         * similarly to {@link ExifVersion} by recording "0100" as 4-byte ASCII.
         *
         * <p>Source: JEITA CP-3451F / CIPA DC-008-2023 "Exchangeable image
         * file format for digital still cameras: Exif Version 3.0"
         *
         */
	FlashpixVersion(40960, false),

        /**
         * F Number.
         *
         * <p>The F number (aka. focal ration, f-ratio or f-stop).
         *
         * <p>Source: JEITA CP-3451F / CIPA DC-008-2023 "Exchangeable image
         * file format for digital still cameras: Exif Version 3.0"
         */
	FNumber(33437, false),

        /**
         *  Image Unique ID.
         *
         * <p>This tag indicates an identifier assigned uniquely to each image.
         * It shall be recorded as an ASCII string in hexadecimal notation
         * equivalent to 128-bit fixed length UUID compliant with
         * ISO/IEC 9834-8. The UUID shall be UUID Version 1 or Version 4, and
         * UUID Version 4 is recommended. This ID shall be assigned at the time
         * of shooting the image, and the recorded ID shall not be updated or
         * erased by any subsequent editing.
         *
         * <p>Source: JEITA CP-3451F / CIPA DC-008-2023 "Exchangeable image
         * file format for digital still cameras: Exif Version 3.0"
         */
	ImageUniqueID(42016, true),

        /**
         * Light source.
         *
         * <p>The kind of light source. This is an enumerated value.
         *
         * <ul>
         * <li>0 = unknown
         * <li>1 = Daylight
         * <li>2 = Fluorescent
         * <li>3 = Tungsten (incandescent light)
         * <li>4 = Flash
         * <li>9 = Fine weather
         * <li>10 = Cloudy weather
         * <li>11 = Shade
         * <li>12 = Daylight fluorescent (D 5700 - 7100K)
         * <li>13 = Day white fluorescent (N 4600 - 5500K)
         * <li>14 = Cool white fluorescent (W 3800 - 4500K)
         * <li>15 = White fluorescent (WW 3250 - 3800K)
         * <li>16 = Warm white fluorescent (L 2600 - 3250K)
         * <li>17 = Standard light A
         * <li>18 = Standard light B
         * <li>19 = Standard light C
         * <li>20 = D55
         * <li>21 = D65
         * <li>22 = D75
         * <li>23 = D50
         * <li>24 = ISO studio tungsten
         * <li>255 = other light source
         * </ul>
         *
         * <p>Source: JEITA CP-3451F / CIPA DC-008-2023 "Exchangeable image
         * file format for digital still cameras: Exif Version 3.0"
         */
	LightSource(37384, false),

        /**
         *  Maker Note.
         *
         * <p>A tag for manufacturers of Exif/DCF writers to record any desired
         * information. The contents are up to the manufacturer, but this tag
         * shall not be used for any other than its intended purpose.
         *
         * <p>Source: JEITA CP-3451F / CIPA DC-008-2023 "Exchangeable image
         * file format for digital still cameras: Exif Version 3.0"
         */
	MakerNote(37500, true),

        /**
         * Shutter Speed Value.
         *
         * <p>Shutter speed. The unit is the APEX (Additive System of
         * Photographic Exposure) value.
         *
         * <p>Source: JEITA CP-3451F / CIPA DC-008-2023 "Exchangeable image
         * file format for digital still cameras: Exif Version 3.0"
         */
	ShutterSpeedValue(37377, false),

        /**
         * User Comment.
         *
         * <p>A tag for Exif users to freely write keywords or comments on the image
         * besides those in ImageDescription, and without the character code
         * limitations of the ImageDescription tag.
         *
         * <p>The first eight bytes indicate the character code.
         *
         * <p>Source: JEITA CP-3451F / CIPA DC-008-2023 "Exchangeable image
         * file format for digital still cameras: Exif Version 3.0"
         */
	UserComment(37510, true),

        // IPTC

        /**
         * IPTC (International Press Telecommunications Council) metadata.
         *
         * <p>Source: <a href="https://www.awaresystems.be/imaging/tiff/tifftags/iptc.html">TIFF Tag Reference site</a>
         */
	IPTC(33723, false),

	// ICC

        /**
         * International Color Consortium (ICC) profile.
         *
         * <p>An ICC device profile is embedded, in its entirety, as a single
         * TIFF field or Image File Directory (IFD) entry in the IFD containing
         * the corresponding image data. An IFD should contain no more than one
         * embedded profile. A TIFF file may contain more than one image, and
         * so, more than one IFD. Each IFD may have its own embedded profile.
         * Note, however, that Baseline TIFF readers are not required to read
         * any IFDs beyond the first one.
         *
         * <p>Source:ICC Technical Note 10-2021 "Embedding ICC profiles"
         */
	ICCProfile(34675, false),

	// XMP

        /**
         * Extensible Metadata Platform (XMP).
         *
         * <p>XMP packet.
         *
         * <p>Source: XMP Specification Part 3 - Storage in Files (January 2020).
         */
	XMP(700, false),

	// GDAL

        /**
         * GDAL additional metadata.
         *
         * <p>GDAL will use standard TIFF tags where applicable. Other non
         * standard metadata items can be stored in a TIFF file created with the
         * default profile {@code GDALGeoTIFF}. Those metadata items are grouped
         * together into a XML string stored in the non standard
         * {@code TIFFTAG_GDAL_METADATA} ASCII tag (code 42112).
         *
         * <p>Source: <a href=https://gdal.org/drivers/raster/gtiff.html#metadata>GDAL GeoTIFF web page</a>
         */
	GDAL_METADATA(42112, false),

        /**
         * GDAL no data value.
         *
         * <p>GDAL stores band {@code nodata} value in the non standard
         * TIFFTAG_GDAL_NODATA ASCII tag (code 42113) for files created with the
         * default profile {@code GDALGeoTIFF}.
         *
         * <p>Note that all bands must use the same nodata value.
         *
         * <p>Source: <a href=https://gdal.org/drivers/raster/gtiff.html#nodata-value>GDAL GeoTIFF web page</a>
         */
	GDAL_NODATA(42113, false),

	// Photoshop

        /**
         * Photoshop.
         *
         * <p>Collection of Photoshop 'Image Resource Blocks'.
         *
         * <p>Source: <a href="https://www.awaresystems.be/imaging/tiff/tifftags/photoshop.html">TIFF Tag Reference site</a>
         * and XMP Specification Part 3 - Storage in Files (January 2020).
         */
	Photoshop(34377, false),

	// GeoTiff

        /**
         * Model Pixel Scale.
         *
         * <p>Provided for defining exact affine transformations between raster
         * and model space. GeoTIFF files may use either {@code ModelPixelScale}
         * or {@link ModelTransformation}, but shall never use both within the
         * same TIFF image directory.
         *
         * <p>Specifies the size of raster pixel spacing in the model space
         * units, when the raster space can be embedded in the model space
         * coordinate reference system without rotation, and consists of the
         * following 3 values: {@code (ScaleX, ScaleY, ScaleZ)} where
         * {@code ScaleX} and {@code ScaleY} give the horizontal spacing of
         * raster pixels in the 2 directions. The {@code ScaleZ} is primarily
         * used to map the pixel value of a digital elevation model into the
         * correct Z-scale (in other words a Z-Scaling factor) and so for most
         * other purposes this value should be zero (since most model spaces
         * are 2-D, with Z=0).
         *
         * <p>Source: OGC 19-008r4 "OGC GeoTIFF Standard".
         *
         * @see ModelTiepoint
         */
	ModelPixelScale(33550, true),

        /**
         * Model Tie Points.
         *
         * <p>For most common applications, the transformation between raster
         * and model space may be defined with a set of raster-to-model
         * tiepoints and scaling parameters.
         *
         * <p>Tiepoints are specified 6 values for each tiepoint, and there
         * can be an arbitrary number of tie points, stored as
         * {@code (...,I,J,K, X,Y,Z...)}, where where {@code (I,J,K)} is the
         * point at location {@code (I,J)} in raster space with pixel-value
         * {@code K}, and {@code (X,Y,Z)} is a vector in model space. In most
         * cases the model space is only two-dimensional, in which case both
         * {@code K} and {@code Z} should be set to zero; this third dimension
         * is provided in anticipation of support for 3D digital elevation
         * models and vertical coordinate systems.
         *
         * <p>Source: OGC 19-008r4 "OGC GeoTIFF Standard".
         *
         * @see ModelPixelScale
         */
	ModelTiepoint(33922, true),

        /**
         * Model Transformation.
         *
         * <p>This tag may be used to specify the 16 element transformation
         * matrix between the raster space (and its dependent pixel-value space)
         * and the (possibly 3D) model space.
         *
         * <p>This matrix tag should not be used if the {@link ModelTiepoint}
         * and the {@link ModelPixelScale} are used.
         *
         * <p>Source: OGC 19-008r4 "OGC GeoTIFF Standard".
         */
	ModelTransformation(34264, true),

        /**
         * GeoKey Directory.
         *
         * <p>The tag is an array of unsigned SHORT values, which are primarily
         * grouped into blocks of 4. The first 4 values are special, and contain
         * GeoKey directory header information.
         *
         * <p>Source: OGC 19-008r4 "OGC GeoTIFF Standard".
         *
         * @see GeoDoubleParams
         * @see GeoAsciiParams
         */
	GeoKeyDirectory(34735, true),

        /**
         * GeoKey floating point parameters.
         *
         * <p>This tag is used to store all of the DOUBLE valued GeoKeys,
         * referenced by the {@code GeoKeyDirectoryTag}. The meaning of any
         * value of this double array is determined from the
         * {@code GeoKeyDirectoryTag} reference pointing to it. FLOAT values
         * should first be converted to DOUBLE and stored here.
         *
         * <p>Source: OGC 19-008r4 "OGC GeoTIFF Standard".
         *
         * @see GeoKeyDirectory
         * @see GeoAsciiParams
         */
	GeoDoubleParams(34736, true),

        /**
         * GeoKey ASCII parameters.
         *
         * <p>This tag is used to store all of the ASCII valued GeoKeys,
         * referenced by the {@code GeoKeyDirectoryTag}. Since keys use offsets
         * into tags, any special comments may be placed at the beginning of
         * this tag. For the most part, the only keys that are ASCII valued are
         * "Citation" keys, giving documentation and references for obscure
         * projections, datums, etc.
         *
         * <p>Source: OGC 19-008r4 "OGC GeoTIFF Standard".
         *
         * @see GeoKeyDirectory
         * @see GeoDoubleParams
         */
	GeoAsciiParams(34737, true);

	/**
	 * Tag id
	 */
	private final int id;

	/**
	 * True if an array type
	 */
	private final boolean array;

	/**
	 * Constructor
	 *
	 * @param id
	 *            tag id
	 * @param array
	 *            true if an array type
	 */
	private FieldTagType(int id, boolean array) {
		this.id = id;
		this.array = array;
	}

	/**
	 * Is this field an array type
	 *
	 * @return true if array type
	 */
	public boolean isArray() {
		return array;
	}

	/**
	 * Get the tag id
	 *
	 * @return tag id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Mapping between ids and field tag types
	 */
	private static final Map<Integer, FieldTagType> idMapping = new HashMap<>();

	/**
	 * Load the id mapping
	 */
	static {
		for (FieldTagType fieldTag : FieldTagType.values()) {
			idMapping.put(fieldTag.getId(), fieldTag);
		}
	}

	/**
	 * Get a field tag type by id
	 *
	 * @param id
	 *            tag id
	 * @return field tag type
	 */
	public static FieldTagType getById(int id) {
		FieldTagType fieldTag = idMapping.get(id);
		return fieldTag;
	}

}
