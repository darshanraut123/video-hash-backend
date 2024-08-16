package com.videohash.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.videohash.model.Videoinfo;

public interface VideoinfoRepository extends MongoRepository<Videoinfo, String> {

	Videoinfo findByVideoHash(String fingerprintHex);


}
