package com.example.demo.service;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.springframework.stereotype.Service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class VideoThumbnailExtractorService {
	private final OkHttpClient client;

	public VideoThumbnailExtractorService() {
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
			System.out.println("Extracting thumbnail from the video...");
			Picture picture = FrameGrab.getFrameFromFile(tempFile, 1);
			// Here, 1 indicates the frame number. You can adjust this number to get a
			// different frame.

			// Convert the Picture to a BufferedImage
			BufferedImage bufferedImage = PictureToBufferedImage(picture);

			// Convert BufferedImage to byte array
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "jpeg", byteArrayOutputStream);
			System.out.println("Thumbnail extracted successfully.");
			return byteArrayOutputStream.toByteArray();
		} finally {
			tempFile.delete(); // Clean up the temporary file
		}
	}

	private BufferedImage PictureToBufferedImage(Picture picture) {
		int width = picture.getWidth();
		int height = picture.getHeight();

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int yValue = picture.getPlaneData(0)[x + y * picture.getPlaneWidth(0)];
				int uValue = picture.getPlaneData(1)[x / 2 + (y / 2) * picture.getPlaneWidth(1)];
				int vValue = picture.getPlaneData(2)[x / 2 + (y / 2) * picture.getPlaneWidth(2)];

				// Convert YUV to RGB
				int r = (int) (yValue + 1.402 * (vValue - 128));
				int g = (int) (yValue - 0.344136 * (uValue - 128) - 0.714136 * (vValue - 128));
				int b = (int) (yValue + 1.772 * (uValue - 128));

				// Clamp the RGB values
				r = Math.max(0, Math.min(255, r));
				g = Math.max(0, Math.min(255, g));
				b = Math.max(0, Math.min(255, b));

				int rgb = (r << 16) | (g << 8) | b;
				bufferedImage.setRGB(x, y, rgb);
			}
		}

		return bufferedImage;
	}

}