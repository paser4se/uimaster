package org.shaolin.bmdp.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.SanselanConstants;
import org.apache.sanselan.common.IBufferedImageFactory;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.RationalNumber;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.formats.tiff.write.TiffOutputDirectory;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;
import org.apache.sanselan.util.IOUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(ImageUtil.class);

	public static byte[] imageWriteExample(final File file)
			throws ImageReadException, ImageWriteException, IOException {
		// read image
		final BufferedImage image = Sanselan.getBufferedImage(file);
		final ImageFormat format = ImageFormat.IMAGE_FORMAT_TIFF;
		final Map<String, Object> params = new HashMap<String, Object>();

		// set optional parameters if you like
		params.put(SanselanConstants.PARAM_KEY_COMPRESSION, new Integer(
				TiffConstants.TIFF_COMPRESSION_UNCOMPRESSED));

		final byte[] bytes = Sanselan.writeImageToBytes(image, format, params);
		return bytes;
	}

	
	public static BufferedImage imageReadExample(final File file)
            throws ImageReadException, IOException {
        final Map<String, Object> params = new HashMap<String, Object>();

        // set optional parameters if you like
        params.put(SanselanConstants.BUFFERED_IMAGE_FACTORY,
                new ManagedImageBufferedImageFactory());

        // params.put(ImagingConstants.PARAM_KEY_VERBOSE, Boolean.TRUE);

        // read image
        final BufferedImage image = Sanselan.getBufferedImage(file, params);
        return image;
    }
	
	/**
	 * translate a binary array to a buffered image
	 * @param binary
	 * @return
	 * @throws IOException
	 */
	public final BufferedImage toBufferedImage(byte[] bytes,String format) throws IOException {
		try {
			return Sanselan.getBufferedImage(new ByteArrayInputStream(bytes));
		} 
		catch (ImageReadException e) {
			throw new IOException(e);
		}
	}

    public static class ManagedImageBufferedImageFactory implements
            IBufferedImageFactory {

        public BufferedImage getColorBufferedImage(final int width, final int height,
                final boolean hasAlpha) {
            final GraphicsEnvironment ge = GraphicsEnvironment
                    .getLocalGraphicsEnvironment();
            final GraphicsDevice gd = ge.getDefaultScreenDevice();
            final GraphicsConfiguration gc = gd.getDefaultConfiguration();
            return gc.createCompatibleImage(width, height,
                    Transparency.TRANSLUCENT);
        }

        public BufferedImage getGrayscaleBufferedImage(final int width, final int height,
                final boolean hasAlpha) {
            return getColorBufferedImage(width, height, hasAlpha);
        }
    }
	
	public static void removeExifMetadata(final File jpegImageFile,
			final File dst) throws IOException, ImageReadException,
			ImageWriteException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(dst);
			os = new BufferedOutputStream(os);
			new ExifRewriter().removeExifMetadata(jpegImageFile, os);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (final IOException e) {
				}
			}
		}
	}

	/**
	 * 
	 * This example illustrates how to add/update EXIF metadata in a JPEG file.
	 * 
	 *
	 * 
	 * @param jpegImageFile
	 * 
	 *            A source image file.
	 * 
	 * @param dst
	 * 
	 *            The output file.
	 * 
	 * @throws IOException
	 * 
	 * @throws ImageReadException
	 * 
	 * @throws ImageWriteException
	 */

	public static void changeExifMetadata(final File jpegImageFile,
			final File dst) throws IOException, ImageReadException,
			ImageWriteException {
		OutputStream os = null;

		try {
			TiffOutputSet outputSet = null;
			// note that metadata might be null if no metadata is found.
			final IImageMetadata metadata = Sanselan.getMetadata(jpegImageFile);
			final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			if (null != jpegMetadata) {
				// note that exif might be null if no Exif metadata is found.
				final TiffImageMetadata exif = jpegMetadata.getExif();
				if (null != exif) {
					// TiffImageMetadata class is immutable (read-only).
					// TiffOutputSet class represents the Exif data to write.
					//
					// Usually, we want to update existing Exif metadata by
					// changing
					// the values of a few fields, or adding a field.
					// In these cases, it is easiest to use getOutputSet() to
					// start with a "copy" of the fields read from the image.
					outputSet = exif.getOutputSet();
				}
			}

			// if file does not contain any exif metadata, we create an empty

			// set of exif metadata. Otherwise, we keep all of the other

			// existing tags.

			if (null == outputSet) {
				outputSet = new TiffOutputSet();
			}

			{

				// Example of how to add a field/tag to the output set.

				//

				// Note that you should first remove the field/tag if it already

				// exists in this directory, or you may end up with duplicate

				// tags. See above.

				//

				// Certain fields/tags are expected in certain Exif directories;

				// Others can occur in more than one directory (and often have a

				// different meaning in different directories).

				//

				// TagInfo constants often contain a description of what

				// directories are associated with a given tag.

				//

				// see

				// org.apache.commons.sanselan.formats.tiff.constants.AllTagConstants

				//

				final TiffOutputDirectory exifDirectory = outputSet
						.getOrCreateExifDirectory();
				// make sure to remove old value if present (this method will

				// not fail if the tag does not exist).
				exifDirectory
						.removeField(ExifTagConstants.EXIF_TAG_APERTURE_VALUE);
				// exifDirectory.add(ExifTagConstants.EXIF_TAG_APERTURE_VALUE,
				// RationalNumber.factoryMethod(3, 10));

			}

			os = new FileOutputStream(dst);
			os = new BufferedOutputStream(os);
			new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os,
					outputSet);

			os.close();
			os = null;

		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (final IOException e) {

				}

			}

		}

	}

	/**
	 * 
	 * This example illustrates how to remove a tag (if present) from EXIF
	 * 
	 * metadata in a JPEG file.
	 * 
	 *
	 * 
	 * In this case, we remove the "aperture" tag from the EXIF metadata if
	 * 
	 * present.
	 * 
	 *
	 * 
	 * @param jpegImageFile
	 * 
	 *            A source image file.
	 * 
	 * @param dst
	 * 
	 *            The output file.
	 * 
	 * @throws IOException
	 * 
	 * @throws ImageReadException
	 * 
	 * @throws ImageWriteException
	 */

	public static void removeExifTag(final File jpegImageFile, final File dst)
			throws IOException,

			ImageReadException, ImageWriteException {

		OutputStream os = null;

		try {

			TiffOutputSet outputSet = null;

			// note that metadata might be null if no metadata is found.

			final IImageMetadata metadata = Sanselan.getMetadata(jpegImageFile);

			final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

			if (null != jpegMetadata) {

				// note that exif might be null if no Exif metadata is found.

				final TiffImageMetadata exif = jpegMetadata.getExif();

				if (null != exif) {

					// TiffImageMetadata class is immutable (read-only).

					// TiffOutputSet class represents the Exif data to write.

					//

					// Usually, we want to update existing Exif metadata by

					// changing

					// the values of a few fields, or adding a field.

					// In these cases, it is easiest to use getOutputSet() to

					// start with a "copy" of the fields read from the image.

					outputSet = exif.getOutputSet();

				}

			}

			if (null == outputSet) {
				// file does not contain any exif metadata. We don't need to
				// update the file; just copy it.
				IOUtils.copyFileNio(jpegImageFile, dst);
				return;

			}

			{

				// Example of how to remove a single tag/field.

				// There are two ways to do this.

				// Option 1: brute force

				// Note that this approach is crude: Exif data is organized in

				// directories. The same tag/field may appear in more than one

				// directory, and have different meanings in each.

				outputSet.removeField(ExifTagConstants.EXIF_TAG_APERTURE_VALUE);

				// Option 2: precision

				// We know the exact directory the tag should appear in, in this

				// case the "exif" directory.

				// One complicating factor is that in some cases, manufacturers

				// will place the same tag in different directories.

				// To learn which directory a tag appears in, either refer to

				// the constants in ExifTagConstants.java or go to Phil Harvey's

				// EXIF website.

				final TiffOutputDirectory exifDirectory = outputSet

				.getExifDirectory();

				if (null != exifDirectory) {

					exifDirectory

					.removeField(ExifTagConstants.EXIF_TAG_APERTURE_VALUE);

				}

			}

			os = new FileOutputStream(dst);

			os = new BufferedOutputStream(os);

			new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os,

			outputSet);

			os.close();

			os = null;

		} finally {

			if (os != null) {

				try {

					os.close();

				} catch (final IOException e) {

				}

			}

		}

	}

	/**
	 * 
	 * This example illustrates how to set the GPS values in JPEG EXIF metadata.
	 * 
	 *
	 * 
	 * @param jpegImageFile
	 * 
	 *            A source image file.
	 * 
	 * @param dst
	 * 
	 *            The output file.
	 * 
	 * @throws IOException
	 * 
	 * @throws ImageReadException
	 * 
	 * @throws ImageWriteException
	 */

	public static void setExifGPSTag(final File jpegImageFile, final File dst)
			throws IOException, ImageReadException, ImageWriteException {
		OutputStream os = null;
		try {
			TiffOutputSet outputSet = null;
			// note that metadata might be null if no metadata is found.
			final IImageMetadata metadata = Sanselan.getMetadata(jpegImageFile);
			final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			if (null != jpegMetadata) {
				// note that exif might be null if no Exif metadata is found.
				final TiffImageMetadata exif = jpegMetadata.getExif();
				if (null != exif) {
					// TiffImageMetadata class is immutable (read-only).
					// TiffOutputSet class represents the Exif data to write.
					//
					// Usually, we want to update existing Exif metadata by
					// changing
					// the values of a few fields, or adding a field.
					// In these cases, it is easiest to use getOutputSet() to
					// start with a "copy" of the fields read from the image.
					outputSet = exif.getOutputSet();
				}
			}

			// if file does not contain any exif metadata, we create an empty

			// set of exif metadata. Otherwise, we keep all of the other

			// existing tags.

			if (null == outputSet) {
				outputSet = new TiffOutputSet();
			}

			{
				// Example of how to add/update GPS info to output set.
				// New York City
				final double longitude = -74.0; // 74 degrees W (in Degrees
												// East)
				final double latitude = 40 + 43 / 60.0; // 40 degrees N (in
														// Degrees
				// North)
				outputSet.setGPSInDegrees(longitude, latitude);
			}
			os = new FileOutputStream(dst);
			os = new BufferedOutputStream(os);
			new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os,
					outputSet);
			os.close();
			os = null;
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (final IOException e) {
				}
			}
		}

	}

	public static Optional<LocalDateTime> getShootingTime(File image) {
		try {
			IImageMetadata metadata = Sanselan.getMetadata(image);
			if (metadata instanceof JpegImageMetadata) {
				TiffField dateTimeValue = ((JpegImageMetadata) metadata)
						.findEXIFValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
				if (dateTimeValue != null) {
					String stringValue = dateTimeValue.getStringValue().trim();

					String dateString = stringValue.split(" ")[0];
					String timeString = stringValue.split(" ")[1];

					String[] dateParts = dateString.split(":");
					String[] timeParts = timeString.split(":");

					LocalDate localDate = LocalDate.of(
							Integer.valueOf(dateParts[0]),
							Integer.valueOf(dateParts[1]),
							Integer.valueOf(dateParts[2]));
					LocalTime localTime = LocalTime.of(
							Integer.valueOf(timeParts[0]),
							Integer.valueOf(timeParts[1]),
							Integer.valueOf(timeParts[2]));

					return Optional.of(LocalDateTime.of(localDate, localTime));
				}
			}
		} catch (Exception e) {
			logger.error("Could not get metdata from {}", image, e);
		}
		return Optional.empty();
	}
	
	public static TiffOutputSet getSanselanOutputSet(File jpegImageFile, int defaultByteOrder)
	        throws IOException, ImageReadException, ImageWriteException {
	    TiffImageMetadata exif = null;
	    TiffOutputSet outputSet = null;

	    IImageMetadata metadata = Sanselan.getMetadata(jpegImageFile);
	    JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
	    if (jpegMetadata != null) {
	        exif = jpegMetadata.getExif();

	        if (exif != null) {
	            outputSet = exif.getOutputSet();
	        }
	    }

	    // If JPEG file contains no EXIF metadata, create an empty set
	    // of EXIF metadata. Otherwise, use existing EXIF metadata to
	    // keep all other existing tags
	    if (outputSet == null)
	        outputSet = new TiffOutputSet(exif==null?defaultByteOrder:exif.contents.header.byteOrder);

	    return outputSet;
	}

	/**
	 * Analyze Image Files
	 *
	 * @param path         Path
	 * @param imageFactory Image Factory
	 * @return ImageFile
	 * @throws IOException        IO Exception when reading Files
	 * @throws ImageReadException Exception from Sanselan when parsing Image metadata
	 */
//	private ImageFile analyzeImageFiles(Path path, ImageFactory imageFactory) throws IOException, ImageReadException {
//	    // TODO: Hidden or SymbolicLink
//	    if (path != null && Files.isRegularFile(path) && path.isAbsolute()) {
//	        // Create a new image path
//	        ImageFile imageFile = imageFactory.createImageFile();
//	        // Analyze
//	        imageFile.setFileName(path.getFileName().toString());
//	        imageFile.setFilePath(path.getParent().toString());
//	        imageFile.setAbsolutePath(path.toString());
//	        imageFile.setFileExtension(FilenameUtils.getExtension(path.getFileName().toString()));
//	        imageFile.setSize(Files.size(path));
//	        // Sanselan
//	        ImageInfo info = Sanselan.getImageInfo(path.toFile());
//	        imageFile.setType(info.getFormat().name);
//	        imageFile.setWidth(info.getWidth());
//	        imageFile.setHeight(info.getHeight());
//	        imageFile.setWidthDPI(info.getPhysicalWidthDpi());
//	        imageFile.setHeightDPI(info.getPhysicalHeightDpi());
//	        imageFile.setColorDepth(info.getBitsPerPixel());
//	        imageFile.setColorType(info.getColorTypeDescription());
//	        imageFile.setMimeType(info.getMimeType());
//	        imageFile.setUserComment(this.parseUserComment(info.getComments()));
//
//	        IImageMetadata metadata = Sanselan.getMetadata(path.toFile());
//	        if (metadata instanceof JpegImageMetadata) {
//	            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
//	            imageFile.setCameraMaker(getJPEGMetadataStringValue(jpegMetadata, TiffConstants.EXIF_TAG_MAKE));
//	            imageFile.setCameraModel(getJPEGMetadataStringValue(jpegMetadata, TiffConstants.EXIF_TAG_MODEL));
//	            imageFile.setISO(getJPEGMetadataIntValue(jpegMetadata, TiffConstants.EXIF_TAG_ISO));
//	            imageFile.setExposureTime(parseRationalNumber(getJPEGMetadataValue(jpegMetadata, TiffConstants.EXIF_TAG_EXPOSURE_TIME)));
//	            imageFile.setFlash(getJPEGMetadataIntValue(jpegMetadata, TiffConstants.EXIF_TAG_FLASH));
//	            imageFile.setFNumber(parseRationalNumber(getJPEGMetadataValue(jpegMetadata, TiffConstants.EXIF_TAG_FNUMBER)));
//	            imageFile.setFocalLength(parseRationalNumber(getJPEGMetadataValue(jpegMetadata, TiffConstants.EXIF_TAG_FOCAL_LENGTH)));
//	            imageFile.setShutterSpeed(parseRationalNumber(getJPEGMetadataValue(jpegMetadata, TiffConstants.EXIF_TAG_SHUTTER_SPEED_VALUE)));
//	            imageFile.setDateTaken(parseDateTimeString(getJPEGMetadataStringValue(jpegMetadata, TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL)));
//	        } else if (metadata instanceof TiffImageMetadata) {
//	            TiffImageMetadata tiffMetadata = (TiffImageMetadata) metadata;
//	            imageFile.setCameraMaker(getTiffMetadataStringValue(tiffMetadata, TiffConstants.EXIF_TAG_MAKE));
//	            imageFile.setCameraModel(getTiffMetadataStringValue(tiffMetadata, TiffConstants.EXIF_TAG_MODEL));
//	            imageFile.setISO(getTiffMetadataIntValue(tiffMetadata, TiffConstants.EXIF_TAG_ISO));
//	            imageFile.setExposureTime(parseRationalNumber(getTiffMetadataValue(tiffMetadata, TiffConstants.EXIF_TAG_EXPOSURE_TIME)));
//	            imageFile.setFlash(getTiffMetadataIntValue(tiffMetadata, TiffConstants.EXIF_TAG_FLASH));
//	            imageFile.setFNumber(parseRationalNumber(getTiffMetadataValue(tiffMetadata, TiffConstants.EXIF_TAG_FNUMBER)));
//	            imageFile.setFocalLength(parseRationalNumber(getTiffMetadataValue(tiffMetadata, TiffConstants.EXIF_TAG_FOCAL_LENGTH)));
//	            imageFile.setShutterSpeed(parseRationalNumber(getTiffMetadataValue(tiffMetadata, TiffConstants.EXIF_TAG_SHUTTER_SPEED_VALUE)));
//	            imageFile.setDateTaken(parseDateTimeString(getTiffMetadataStringValue(tiffMetadata, TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL)));
//	        } else {
//	            imageFile.setDateTaken(new Date(Files.getLastModifiedTime(path).toMillis()));
//	        }
//	        // Result
//	        return imageFile;
//	    } else {
//	        return null;
//	    }
//	}
	
	public static Object getJPEGMetadataValue(JpegImageMetadata jpegMetadata, TagInfo tagInfo) throws ImageReadException { 
        if (jpegMetadata != null) { 
            TiffField field = jpegMetadata.findEXIFValue(tagInfo); 
            if (field != null) { 
                return field.getValue(); 
            } 
        } 
        return null; 
    } 
 
	public static String getJPEGMetadataStringValue(JpegImageMetadata jpegMetadata, TagInfo tagInfo) throws ImageReadException { 
        if (jpegMetadata != null) { 
            TiffField field = jpegMetadata.findEXIFValue(tagInfo); 
            if (field != null) { 
                String value = field.getStringValue(); 
                if (value != null) { 
                    while (value.endsWith("\u0000")) { 
                        value = value.substring(0, value.length() - 1); 
                    } 
                } 
                return value; 
            } 
        } 
        return null; 
    } 
 
	public static int getJPEGMetadataIntValue(JpegImageMetadata jpegMetadata, TagInfo tagInfo) throws ImageReadException { 
        if (jpegMetadata != null) { 
            TiffField field = jpegMetadata.findEXIFValue(tagInfo); 
            if (field != null) { 
                return field.getIntValue(); 
            } 
        } 
        return 0; 
    } 
 
	public static Object getTiffMetadataValue(TiffImageMetadata tiffMetadata, TagInfo tagInfo) throws ImageReadException { 
        if (tiffMetadata != null) { 
            TiffField field = tiffMetadata.findField(tagInfo); 
            if (field != null) { 
                return field.getValue(); 
            } 
        } 
        return null; 
    } 
 
	public static String getTiffMetadataStringValue(TiffImageMetadata tiffMetadata, TagInfo tagInfo) throws ImageReadException { 
        if (tiffMetadata != null) { 
            TiffField field = tiffMetadata.findField(tagInfo); 
            if (field != null) { 
                String value = field.getStringValue(); 
                if (value != null) { 
                    while (value.endsWith("\u0000")) { 
                        value = value.substring(0, value.length() - 1); 
                    } 
                } 
                return value; 
            } 
        } 
        return null; 
    } 
 
	public static int getTiffMetadataIntValue(TiffImageMetadata tiffMetadata, TagInfo tagInfo) throws ImageReadException { 
        if (tiffMetadata != null) { 
            TiffField field = tiffMetadata.findField(tagInfo); 
            if (field != null) { 
                return field.getIntValue(); 
            } 
        } 
        return 0; 
    } 
    
    /**
     * Parse List of User Comment into One Comment String 
     * 
     * @param comments User Comments 
     * @return Comment 
     */ 
    @SuppressWarnings("unchecked") 
    public static String parseUserComment(List<String> comments) { 
        StringBuilder sb = new StringBuilder(); 
        if (comments != null) { 
            for (String comment : comments) { 
                if (sb.length() > 0) { 
                    sb.append(" "); 
                } 
                sb.append(comment); 
            } 
        } 
        return sb.toString(); 
    } 
 
    /**
     * Parse Rational Number to String like "numerator/divisor" 
     * 
     * @param value Rational Number 
     * @return String 
     */ 
    public static String parseRationalNumber(Object value) { 
        if (value instanceof RationalNumber) { 
            if (((RationalNumber) value).isValid()) { 
                return ((RationalNumber) value).numerator + "/" + ((RationalNumber) value).divisor; 
            } 
        } 
        return null; 
    } 
 
    public static BufferedImage getStyledImage(File img, float brightness, float transparency, int size) {
        BufferedImage outputImage = null;
        try {
            BufferedImage readBufferedImage = ImageIO.read(img);
            BufferedImage inputBufferedImage = convertToArgb(readBufferedImage);
            outputImage = inputBufferedImage;
            if (size > 0) {
                outputImage = Scalr.resize(inputBufferedImage
                        , Method.BALANCED
                        , size
                        , size);
            }

            float brightnessFactor = 1.0f + brightness/100.0f;
            float transparencyFactor = Math.abs(transparency/100.0f - 1.0f);
            RescaleOp rescale = new RescaleOp(
                    new float[]{brightnessFactor, brightnessFactor, brightnessFactor, transparencyFactor},
                    new float[]{0f, 0f, 0f, 0f}, null);
            rescale.filter(outputImage, outputImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputImage;
    }
    
    private static BufferedImage convertToArgb(java.awt.Image image) { 
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null) 
                , BufferedImage.TYPE_INT_ARGB); 
        Graphics2D graphics2D = bufferedImage.createGraphics(); 
        graphics2D.drawImage(image, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight() 
                , new Color(0, 0, 0, 0), null); 
        return bufferedImage; 
    } 
    
    /**
     * Resizes an image to a specific size and adds black lines in respect to
     * the ratio. The resizing will take care of the original ratio.
     *
     * @param inputImage - Original input image. Will not be changed.
     * @param newWidth - The out coming width
     * @param newHeight - The out coming height
     * @return - The out coming image
     * @throws IOException 
     */
	public static void resizeImage(File image,
			int newWidth, int newHeight, File dest) throws IOException {
		BufferedImage inputImage = ImageIO.read(image);
		// first get the width and the height of the image
		int originWidth = inputImage.getWidth();
		int originHeight = inputImage.getHeight();
		String suffix = image.getName().substring(image.getName().lastIndexOf(".") + 1);
		// let us check if we have to scale the image
		if (originWidth <= newWidth && originHeight <= newHeight) {
			// we don't have to scale the image, just return the origin
			ImageIO.write(inputImage, suffix, dest);
			return;
		}

		// Scale in respect to width or height?
		Scalr.Mode scaleMode = Scalr.Mode.AUTOMATIC;

		// find out which side is the shortest
		int maxSize = 0;
		if (originHeight > originWidth) {
			// scale to width
			scaleMode = Scalr.Mode.FIT_TO_WIDTH;
			maxSize = newWidth;
		} else if (originWidth >= originHeight) {
			scaleMode = Scalr.Mode.FIT_TO_HEIGHT;
			maxSize = newHeight;
		}

		// Scale the image to given size
		BufferedImage outputImage = Scalr.resize(inputImage,
				Scalr.Method.QUALITY, scaleMode, maxSize);

		// okay, now let us check that both sides are fitting to our result
		// scaling
		if (scaleMode.equals(Scalr.Mode.FIT_TO_WIDTH)
				&& outputImage.getHeight() > newHeight) {
			// the height is too large, resize again
			outputImage = Scalr.resize(outputImage, Scalr.Method.QUALITY,
					Scalr.Mode.FIT_TO_HEIGHT, newHeight);
		} else if (scaleMode.equals(Scalr.Mode.FIT_TO_HEIGHT)
				&& outputImage.getWidth() > newWidth) {
			// the width is too large, resize again
			outputImage = Scalr.resize(outputImage, Scalr.Method.QUALITY,
					Scalr.Mode.FIT_TO_WIDTH, newWidth);
		}

		// now we have an image that is definitely equal or smaller to the given
		// size
		// Now let us check, which side needs black lines
		int paddingSize = 0;
		if (outputImage.getWidth() != newWidth) {
			// we need padding on the width axis
			paddingSize = (newWidth - outputImage.getWidth()) / 2;
		} else if (outputImage.getHeight() != newHeight) {
			// we need padding on the height axis
			paddingSize = (newHeight - outputImage.getHeight()) / 2;
		}

		// we need padding?
		if (paddingSize > 0) {
			// add the padding to the image
			outputImage = Scalr.pad(outputImage, paddingSize);

			// now we have to crop the image because the padding was added to
			// all sides
			int x = 0, y = 0, width = 0, height = 0;
			if (outputImage.getWidth() > newWidth) {
				// set the correct range
				x = paddingSize;
				y = 0;
				width = outputImage.getWidth() - (2 * paddingSize);
				height = outputImage.getHeight();
			} else if (outputImage.getHeight() > newHeight) {
				// set the correct range
				x = 0;
				y = paddingSize;
				width = outputImage.getWidth();
				height = outputImage.getHeight() - (2 * paddingSize);
			}

			// Crop the image
			if (width > 0 && height > 0) {
				outputImage = Scalr.crop(outputImage, x, y, width, height);
			}
		}

		ImageIO.write(outputImage, suffix, dest);
	}
    
    /**
     * Return the image which resized(support JPG, PNG, GIF)
     * @param image The BufferedImage which based on InputStream
     * BufferedImage image = ImageIO.read(InputStream)
     * @param newWidth Max width of thumbnail will be resized
     * @param newHeight Max height of thumbnail will be resized
     * @return InputStream
     * @throws Exception
     */  
	public static void cropImage(File i, int newWidth, int newHeight) throws Exception {
		BufferedImage image = ImageIO.read(i);
		// first get the width and the height of the image
		int originWidth = image.getWidth();
		int originHeight = image.getHeight();
		if (originWidth <= newWidth && originHeight <= newHeight) {
			return;
		}
		// Make sure the aspect ratio is maintained, so the image is not skewed
		BufferedImage thumbImage = Scalr.crop(image, newWidth, newHeight);
		String suffix = i.getName().substring(i.getName().lastIndexOf(".") + 1);
		ImageIO.write(thumbImage, suffix, i);
	}
 
	/**
	 * creates the Thumbnail for the given picture and stores it on the disk
	 * 
	 * @param image
	 */
	public static void createThumbnail(File image, int size, File dest) throws IOException {
	    BufferedImage thumbnail = ImageIO.read(image);
	    int width = thumbnail.getWidth();
	    int height = thumbnail.getHeight();
	    if (width > size || height > size) {
	        if (width > height) {
	            thumbnail = Scalr.resize(thumbnail, Scalr.Mode.FIT_TO_HEIGHT, size);
	            thumbnail = Scalr.crop(thumbnail, thumbnail.getWidth() / 2 - size / 2, 0, size, size);
	        } else {
	            thumbnail = Scalr.resize(thumbnail, Scalr.Mode.FIT_TO_WIDTH, size);
	            thumbnail = Scalr.crop(thumbnail, 0, thumbnail.getWidth() / 2 - size / 2, size, size);
	        }
	    }
	    String suffix;
	    if (image.getName().lastIndexOf(".") != -1) {
	    	suffix = image.getName().substring(image.getName().lastIndexOf(".") + 1);
	    } else {
	    	suffix = "png";//by default.
	    }
        ImageIO.write(thumbnail, suffix, dest);
	}
	
//		String buiduImageURL = "http://image.baidu.com/search/index?tn=baiduimage"
//				+ "&ipn=r&ct=201326592&cl=2&lm=-1&st=-1&fm=index&fr=&sf=1&fmq=&pv="
//				+ "&ic=0&nc=1&z=&se=1&showtab=0&fb=0&width=&height=&face=0&istype=2"
//				+ "&ie=utf-8&word=" + StringUtil.string2Unicode(word) + "&f=3&oq="
//				+ StringUtil.string2Unicode(preword)+"&rsp=2";
	public static List<String> searchImageOnInternet(String imageURL) {
		if (logger.isDebugEnabled()) {
			logger.debug("parsing url: " + imageURL);
		}
		try {
			List<String> links = new ArrayList<String>();
			Document doc = Jsoup.connect(imageURL).get();
			Elements result = doc.select("script");
			Iterator<Element> i = result.iterator();
			while (i.hasNext()) {
				Element e = i.next();
				String text = e.toString();
				if (text.indexOf("app.setPageInfo") != -1) {
					// grab the link in between.
					String regexString = Pattern.quote("middleURL") + "(.*?)" + Pattern.quote("largeTnImageUrl");
					Pattern p1 = Pattern.compile(regexString);
			        Matcher m = p1.matcher(text);
					while (m.find()) {
						MatchResult matched = m.toMatchResult();
						String item = matched.group(1);
						item = item.replace("\":\"", "").replace("\",            \"", "");
						links.add(item);
					}
					break;
				};
			}
			if (logger.isDebugEnabled()) {
				logger.debug("get image links: " + links);
			}
			return links;
		} catch (Exception e) {
			logger.error("Could not get metdata from {}", imageURL, e);
			return Collections.emptyList();
		}
	}
	
	public static void searchImageOnHTML(String html) {
		try {
			Document doc = Jsoup.parse(html);
			Elements result = doc.select("script");
			Iterator<Element> i = result.iterator();
			while (i.hasNext()) {
				Element e = i.next();
				String text = e.toString();
				if (text.indexOf("app.setPageInfo") != -1) {
					// grab the link in between.
					String regexString = Pattern.quote("middleURL") + "(.*?)" + Pattern.quote("largeTnImageUrl");
					Pattern p1 = Pattern.compile(regexString);
			        Matcher m = p1.matcher(text);
					while (m.find()) {
						MatchResult matched = m.toMatchResult();
						System.out.println(matched.group(1));
					}
					break;
				};
			}
		} catch (Exception e) {
			logger.error("Could not parse html!!!", e);
		}
	}
	
	public static void downloadImage(String urlString, File sf, String fileName) throws Exception {
		URL url = new URL(urlString);
		URLConnection con = url.openConnection();
		con.setConnectTimeout(5 * 1000);
		InputStream is = con.getInputStream();

		byte[] bs = new byte[1024];
		int len;
		if (!sf.exists()) {
			sf.mkdirs();
		}
		OutputStream os = new FileOutputStream(sf.getPath()+"/"+fileName);
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		os.close();
		is.close();
	}  
	
}
