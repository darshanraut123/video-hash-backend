package com.videohash.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.videohash.DTO.VideoMetaData;

import lombok.Data;

@Data
@Document("videoinfo")
public class Videoinfo {

        @Id
        private String id;
        private String name;
        private String videoHash;
        private List<VideoMetaData> videoMetaDataList;
        
}