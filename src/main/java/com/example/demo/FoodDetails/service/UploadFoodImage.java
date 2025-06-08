package com.example.demo.FoodDetails.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class UploadFoodImage {

    @Autowired
    private Cloudinary cloudinary;

    public Map UploadFoodImage(byte[] fileBytes, String publicId) throws Exception {
        return cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap(
            "public_id", publicId,
            "resource_type", "image"
        ));
    }
}
