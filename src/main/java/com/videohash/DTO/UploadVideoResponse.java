package com.videohash.DTO;

import com.videohash.model.Videoinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadVideoResponse {
	
	private Videoinfo videoinfo;
	private boolean error;
	private String message;

}
