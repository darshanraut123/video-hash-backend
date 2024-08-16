package com.videohash.serviceImple;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.videohash.DTO.VideoMetaData;
import com.videohash.model.Videoinfo;
import com.videohash.repository.VideoinfoRepository;
import com.videohash.service.VideoService;

import io.metaloom.video4j.Video;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.fingerprint.v2.MultiSectorFingerprint;
import io.metaloom.video4j.fingerprint.v2.MultiSectorVideoFingerprinter;
import io.metaloom.video4j.fingerprint.v2.impl.MultiSectorVideoFingerprinterImpl;

@Service
public class VideoServiceImpl implements VideoService {
	
	@Autowired
	VideoinfoRepository videoinfoRepository;
	
	public List<VideoMetaData> extractMetaDataList(String videoMetaDataJSON) {
		
		if(videoMetaDataJSON==null && videoMetaDataJSON.length()==0)
			return null;
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<VideoMetaData> videoMetaDataList = null;
		try {
			videoMetaDataList = objectMapper.readValue(videoMetaDataJSON, new TypeReference<List<VideoMetaData>>() {
			});
			for (VideoMetaData vid : videoMetaDataList) {
				System.out.println(vid.getKey() + "   " + vid.getValue());
			}
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (videoMetaDataList != null && videoMetaDataList.size() > 0)
			return videoMetaDataList;
		else
			return null;
	}
	

	public String getVideoFingerprint(String videoFilePath) {
		
		String extractedFingerprintHex = "dkhjgkaeh289y7egou9q7tf"; //initially null
//
////		System.setProperty("java.library.path", "/usr/local/Cellar/opencv/4.10.0_2/share/opencv4");
////	    System.loadLibrary("opencv_java4100");
//		Video4j.init();
//
//		// Create a finger printer for the video
//		MultiSectorVideoFingerprinter gen = new MultiSectorVideoFingerprinterImpl();
//
//		Video video = null;
//		try  {
//			video = Videos.open(videoFilePath);
//			// Run the actual hashing process
//			MultiSectorFingerprint fingerprint = gen.hash(video);
//			extractedFingerprintHex = fingerprint.hex();
//			
//		}
//		catch(Exception e) {
//			extractedFingerprintHex = null;
//		}
//		finally {
//            video.close();
//		}
		return extractedFingerprintHex;

	}

	public Videoinfo saveVideoRecordsToDB(Videoinfo videoinfo){	
		Videoinfo savedVideoinfo = videoinfoRepository.save(videoinfo);
		return savedVideoinfo;
	}


	public String storeVideoToGetPath(MultipartFile videoFile) {
		String videoFilePath = null;
		try {
			String currentDir = System.getProperty("user.dir");
			String uploadDirectory = currentDir + File.separator + "uploads";

			// Create the directory if it does not exist
			File directory = new File(uploadDirectory);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			
			File destinationFile = new File(uploadDirectory + File.separator + videoFile.getOriginalFilename());
			videoFile.transferTo(destinationFile);

			videoFilePath =  uploadDirectory + File.separator + videoFile.getOriginalFilename();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return videoFilePath;
	}


	@Override
	public Videoinfo fetchVideoInfoUsingFingerprintHex(String fingerprintHex) {
		return videoinfoRepository.findByVideoHash(fingerprintHex);
	}


}
