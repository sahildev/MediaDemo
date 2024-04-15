package com.example.demo.controller;

import java.io.IOException;

import org.jcodec.api.JCodecException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.VideoThumbnailService;
import com.example.demo.service.VideoThumbnailServiceUsingFFMPEG;
import com.example.demo.service.VideoThumbnailServiceUsingImageIO;

@RestController
@RequestMapping("/thumbnail")
public class FileUploadController {

	private final VideoThumbnailService thumbnailService;

	private final VideoThumbnailServiceUsingImageIO videoThumbnailServiceUsingFFMPEG;

	@Autowired
	public FileUploadController(VideoThumbnailService thumbnailService,
			VideoThumbnailServiceUsingImageIO videoThumbnailServiceUsingFFMPEG) {
		this.thumbnailService = thumbnailService;
		this.videoThumbnailServiceUsingFFMPEG = videoThumbnailServiceUsingFFMPEG;
	}

	@PostMapping(value = "/extract", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] extractThumbnail(@RequestParam("file") MultipartFile file) {
		try {
			return thumbnailService.extractThumbnail(file.getBytes());
		} catch (IOException | JCodecException e) {
			e.printStackTrace();
			return null;
		}
	}

	@PostMapping(value = "/extract2", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] extractThumbnailFFMPED(@RequestParam("file") MultipartFile file) throws InterruptedException {
		try {
			return videoThumbnailServiceUsingFFMPEG.extractThumbnail(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
