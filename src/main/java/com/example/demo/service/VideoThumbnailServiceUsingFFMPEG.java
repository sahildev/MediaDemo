package com.example.demo.service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import org.springframework.stereotype.Service;
import java.io.*;

@Service
public class VideoThumbnailServiceUsingFFMPEG {

	public byte[] extractThumbnail(byte[] videoData) throws IOException, InterruptedException {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			// Create a temporary file to store the thumbnail
			File tempFile = File.createTempFile("thumbnail", ".jpg");

			// Write the video data to a temporary file
			try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
				fileOutputStream.write(videoData);
			}

			// Execute FFmpeg command to extract thumbnail
			FFmpeg ffmpeg = new FFmpeg("ffmpeg");
			net.bramp.ffmpeg.builder.FFmpegBuilder builder = new net.bramp.ffmpeg.builder.FFmpegBuilder()
					.addInput(tempFile.getAbsolutePath()) // Input from temporary file
					.addOutput(tempFile.getAbsolutePath() + ".jpg") // Output to temporary file
					.setFrames(1).done();

			FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
			executor.createJob(builder).run();

			// Read the thumbnail file into a byte array
			byte[] thumbnailData;
			try (FileInputStream fileInputStream = new FileInputStream(tempFile.getAbsolutePath() + ".jpg")) {
				thumbnailData = fileInputStream.readAllBytes();
			}

			// Delete temporary files
			tempFile.delete();
			new File(tempFile.getAbsolutePath() + ".jpg").delete();

			return thumbnailData;
		}
	}
}
