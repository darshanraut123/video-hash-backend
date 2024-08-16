package com.videohash.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.videohash.DTO.VideoMetaData;
import com.videohash.model.Videoinfo;

public interface VideoService {

	String storeVideoToGetPath(MultipartFile videoFile);
	public String getVideoFingerprint(String filePath);
	public Videoinfo saveVideoRecordsToDB(Videoinfo videoinfo);
	public List<VideoMetaData> extractMetaDataList(String videoMetaDataJSON);
	Videoinfo fetchVideoInfoUsingFingerprintHex(String fingerprintHex);

}
