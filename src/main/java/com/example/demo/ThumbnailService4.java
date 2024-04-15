package com.example.demo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class ThumbnailService4 {

	private static final Logger LOGGER = LoggerFactory.getLogger(ThumbnailService4.class);
	private final OkHttpClient client = new OkHttpClient();

	public void generateThumbnail(String videoUrl) throws IOException {
		LOGGER.info("Generating thumbnail for video: {}", videoUrl);
		// Download video from URL
		byte[] videoBytes = downloadVideo(videoUrl);

		if (videoBytes == null || videoBytes.length == 0) {
			LOGGER.error("Failed to download video from URL: {}", videoUrl);
		}

		System.out.println("ByteArray ::");
		System.out.println(videoBytes);
		// Generate thumbnail from video bytes
		generateThumbnailFromVideoBytes(videoBytes);
	}

	private byte[] downloadVideo(String videoUrl) throws IOException {
		Request request = new Request.Builder().url(videoUrl).build();

		try (Response response = client.newCall(request).execute()) {
			return response.body().bytes();
		}
	}

	public void generateThumbnailFromVideoBytes(byte[] videoBytes) throws IOException {
		// Convert video bytes to FFmpegFrameGrabber
		FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(new ByteArrayInputStream(videoBytes));
		frameGrabber.start();

		Frame f;
		try {
			Java2DFrameConverter c = new Java2DFrameConverter();
			f = frameGrabber.grab();
			System.out.println("Frame :: " + f);
			BufferedImage bi = c.convert(f);
			ImageIO.write(bi, "png", new File("D:/Img.png"));
			frameGrabber.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
