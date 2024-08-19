package com.videohash.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.videohash.DTO.UploadVideoResponse;
import com.videohash.DTO.VideoMetaData;
import com.videohash.model.Videoinfo;
import com.videohash.service.VideoService;

@CrossOrigin
@RestController
public class VideoHashController {
	
	@Autowired
	VideoService videoService;

	@GetMapping("/serverstatus")
	public ResponseEntity<String> isServiceUpAndRunning() {
		return new ResponseEntity<String>("Service is up and running!", HttpStatus.OK);
	}
	
	@PostMapping(value = "/upload-video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<UploadVideoResponse> uploadVideo(@RequestPart MultipartFile videoFile,
			@RequestParam String videoMetaDataJSON) {
		try {
			System.out.println("Inside uploadVideo method");
			List<VideoMetaData> videoMetaDataList = videoService.extractMetaDataList(videoMetaDataJSON);
			if (videoMetaDataList == null)
				throw new Exception("Meta Data cannot be empty");

			String videoPath = videoService.storeVideoToGetPath(videoFile);
			if (videoPath == null)
				throw new Exception("Unable to uplaod video.");

			String fingerprintHex = videoService.getVideoFingerprint(videoPath);
			if (fingerprintHex == null)
				throw new Exception("Error occured creating hash");

			Videoinfo videoinfo = new Videoinfo();
			videoinfo.setVideoHash("Temp Name");
			videoinfo.setVideoHash(fingerprintHex);
			videoinfo.setVideoMetaDataList(videoMetaDataList);
			videoinfo = videoService.saveVideoRecordsToDB(videoinfo);

			System.out.println("Exit uploadVideo method");
			return new ResponseEntity<UploadVideoResponse>(
					new UploadVideoResponse(videoinfo, false, "Saved successfully!"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<UploadVideoResponse>(new UploadVideoResponse(null, true, e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value="/verify-similarity")
	public ResponseEntity<UploadVideoResponse> verifyForSimilarity(@RequestPart MultipartFile videoFile){
		try {	
			String videoPath = videoService.storeVideoToGetPath(videoFile);
			if (videoPath == null)
				throw new Exception("Unable to uplaod video.");
			String fingerprintHex = videoService.getVideoFingerprint(videoPath);
			if (fingerprintHex == null)
				throw new Exception("Error occured creating hash");
			
			Videoinfo fetchedVideoinfo = videoService.fetchVideoInfoUsingFingerprintHex(fingerprintHex);
			if (fetchedVideoinfo == null)
				throw new Exception("We did not find any matching data.");
			return new ResponseEntity<UploadVideoResponse>(new UploadVideoResponse(fetchedVideoinfo, false,"Verification success!") ,
					HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<UploadVideoResponse>(new UploadVideoResponse(null, true, e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}
	
}
