package com.example.demo.service;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jcodec.scale.*;

import javax.imageio.ImageIO;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.springframework.stereotype.Service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class VideoThumbnailExtractorServiceAWT {
	private final OkHttpClient client;

	public VideoThumbnailExtractorServiceAWT() {
		this.client = new OkHttpClient();
	}

	public byte[] extractThumbnail(String videoUrl) throws IOException, JCodecException {
		System.out.println("Downloading video from: " + videoUrl);
		byte[] videoData = downloadVideo(videoUrl);
		System.out.println("Video downloaded successfully.");
		return extractThumbnailFromVideo(videoData);
	}

	private byte[] downloadVideo(String videoUrl) throws IOException {
		Request request = new Request.Builder().url(videoUrl).build();

		try (Response response = client.newCall(request).execute()) {
			return response.body().bytes();
		}
	}

	private byte[] extractThumbnailFromVideo(byte[] videoData) throws IOException, JCodecException {
		File tempFile = File.createTempFile("video", ".mp4");
		try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
			outputStream.write(videoData);
		}

		try {
			Picture picture = FrameGrab.getFrameFromFile(tempFile, 1);
			// Here, 1 indicates the frame number. You can adjust this number to get a
			// different frame.

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