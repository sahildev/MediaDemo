package com.example.demo;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



@Service
public class ThumbnailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThumbnailService.class);
    private final OkHttpClient client = new OkHttpClient();

    public BufferedImage generateThumbnail(String videoUrl) throws IOException {
    	LOGGER.info("Generating thumbnail for video: {}", videoUrl);
        // Download video from URL
        byte[] videoBytes = downloadVideo(videoUrl);

        if (videoBytes == null || videoBytes.length == 0) {
            LOGGER.error("Failed to download video from URL: {}", videoUrl);
            return null;
        }
        
        System.out.println("ByteArray ::");
        System.out.println(videoBytes);
        // Generate thumbnail from video bytes
        return generateThumbnailFromVideoBytes(videoBytes);
    }

    private byte[] downloadVideo(String videoUrl) throws IOException {
        Request request = new Request.Builder()
                .url(videoUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().bytes();
        }
    }

    public BufferedImage generateThumbnailFromVideoBytes(byte[] videoBytes) throws IOException {
        // Convert video bytes to FFmpegFrameGrabber
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(new ByteArrayInputStream(videoBytes));
        grabber.start();

        System.out.println("FrameRate ::" +grabber.getFrameRate());
        // Get frame at specific time (e.g., 5 seconds)
        grabber.setFrameNumber(1 * (int) grabber.getFrameRate());
        Frame frame = grabber.grab();

        // Convert frame to BufferedImage
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage image = converter.getBufferedImage(frame, 1);

        // Stop the grabber
        grabber.stop();

        return image;
    }


    private BufferedImage readVideoFrame(byte[] videoBytes) throws IOException {
        // Read video frame using Java's built-in image I/O
        ByteArrayInputStream inputStream = new ByteArrayInputStream(videoBytes);
        BufferedImage image = ImageIO.read(inputStream);
        inputStream.close();

        return image;
    }
}
