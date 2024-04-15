package com.example.demo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.jcodec.api.JCodecException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.VideoThumbnailExtractorService;
import com.example.demo.service.VideoThumbnailExtractorServiceAWT;

@RestController
public class ThumbnailController {

	@Autowired
	private ThumbnailService thumbnailService;

	@Autowired
	private ThumbnailService2 thumbnailService2;
	@Autowired
	private VideoThumbnailExtractorService extractorService;

	@Autowired
	private VideoThumbnailExtractorServiceAWT videoThumbnailExtractorServiceAWT;

	@GetMapping(value = "/thumbnail4", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getThumbnailAWT(String videoUrl) throws IOException, JCodecException {

		byte[] thumbnailData = videoThumbnailExtractorServiceAWT.extractThumbnail(videoUrl);
		// return Base64.getEncoder().encodeToString(thumbnailData);
		return thumbnailData;
	}

	@GetMapping(value = "/thumbnail3", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getThumbnailAsBase64(String videoUrl) throws IOException, JCodecException {

		byte[] thumbnailData = extractorService.extractThumbnail(videoUrl);
		// return Base64.getEncoder().encodeToString(thumbnailData);
		return thumbnailData;
	}

	@GetMapping(value = "/thumbnail2", produces = MediaType.IMAGE_JPEG_VALUE)
	public void generateThumbnail2(@RequestParam String videoUrl) throws IOException {
		// Generate thumbnail as BufferedImage
		thumbnailService2.generateThumbnail(videoUrl);

	}

	@GetMapping(value = "/thumbnail", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] generateThumbnail(@RequestParam String videoUrl) {
		try {
			// Generate thumbnail as BufferedImage
			BufferedImage thumbnailImage = thumbnailService.generateThumbnail(videoUrl);

			if (thumbnailImage != null) {
				// Convert BufferedImage to byte array
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ImageIO.write(thumbnailImage, "jpg", byteArrayOutputStream);

				byte[] response = byteArrayOutputStream.toByteArray();
				System.out.println("Response ::");
				System.out.println(response);
				return response;
			} else {
				System.out.println("Response 2 ::");
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			// Handle error response
			return null;
		}
	}
}
