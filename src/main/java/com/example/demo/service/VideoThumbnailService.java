package com.example.demo.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.stereotype.Service;

@Service
public class VideoThumbnailService {

	public byte[] extractThumbnail(byte[] videoData) throws IOException, JCodecException {
		File tempFile = File.createTempFile("video", ".mp4");
		try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
			outputStream.write(videoData);
		}

		try {
			Picture picture = FrameGrab.getFrameFromFile(tempFile, 1);
			// Convert the Picture to a BufferedImage
			BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);

			// Write the BufferedImage to a ByteArrayOutputStream
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "jpeg", byteArrayOutputStream);

			return byteArrayOutputStream.toByteArray();
		} finally {
			tempFile.delete(); // Clean up the temporary file
		}
	}
}
