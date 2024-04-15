package com.example.demo.service;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class VideoThumbnailServiceUsingImageIO {

	public byte[] extractThumbnail(byte[] videoData) throws IOException {
	    // Read the video data as BufferedImage
	    BufferedImage videoImage = readVideoAsImage(videoData);

	    // Check if videoImage is null
	    if (videoImage == null) {
	        throw new IOException("Failed to read video data as image");
	    }

	    // Perform any necessary processing on the image (e.g., resize, crop)

	    // Convert the BufferedImage to byte array (JPEG format)
	    return convertImageToByteArray(videoImage);
	}

	private BufferedImage readVideoAsImage(byte[] videoData) throws IOException {
	    // Read the video data as BufferedImage
	    ByteArrayInputStream inputStream = new ByteArrayInputStream(videoData);
	    BufferedImage image = null;
	    try {
	        image = ImageIO.read(inputStream);
	    } catch (IOException e) {
	        // Handle IOException
	        e.printStackTrace();
	        throw e; // Rethrow the exception
	    }
	    return image;
	}

	private byte[] convertImageToByteArray(BufferedImage image) throws IOException {
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    ImageIO.write(image, "jpeg", outputStream);
	    return outputStream.toByteArray();
	}


}
