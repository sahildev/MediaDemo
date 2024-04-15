package com.example.demo;
import net.coobird.thumbnailator.Thumbnails;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class ThumbnailService3 {

    public void generateThumbnailFromVideoUrl(String videoUrl) throws IOException {
    	
    	String outputDirectory = "D:\\10. Workspaces\\3. SpringLearning\\Mediademo\\thumbnail\\";
    	
        // Download the video file from the URL
        File videoFile = downloadVideoFromUrl(videoUrl);

        if (videoFile == null) {
            throw new IOException("Failed to download video from URL: " + videoUrl);
        }

        // Extract the first frame from the video file
        BufferedImage image = extractFirstFrame(videoFile);

        // Generate thumbnail using Thumbnailator
        String thumbnailFileName = "video-thumbnail-" + UUID.randomUUID().toString() + ".png";
        File thumbnailFile = new File(outputDirectory, thumbnailFileName);

        Thumbnails.of(image)
                .size(100, 100) // Thumbnail size
                .outputFormat("png") // Output format
                .toFile(thumbnailFile);

        System.out.println("Thumbnail generated: " + thumbnailFile.getAbsolutePath());
    }

    private File downloadVideoFromUrl(String videoUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(videoUrl).openConnection();
        connection.setRequestMethod("GET");

        try (InputStream in = connection.getInputStream()) {
            // Generate unique filename for downloaded video
            String fileName = "downloaded-video-" + UUID.randomUUID().toString() + ".mp4";
            File videoFile = new File(fileName);

            // Write the downloaded video data to the file
            try (OutputStream out = new FileOutputStream(videoFile)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return videoFile;
        }
    }

    private BufferedImage extractFirstFrame(File videoFile) throws IOException {
        // Extract the first frame from the video file using a suitable library (e.g., JavaCV)
        // For example:
        // - Use JavaCV to extract the first frame
        // - Convert the frame to a BufferedImage

        // Placeholder implementation for extracting the first frame
        // Replace this with actual code to extract frames from video files
        return ImageIO.read(videoFile);
    }
}
