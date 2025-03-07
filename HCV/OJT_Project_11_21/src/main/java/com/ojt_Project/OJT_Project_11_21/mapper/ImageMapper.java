package com.ojt_Project.OJT_Project_11_21.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    @Named("mapImage")
    default String mapImage(MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            return imageFile.getOriginalFilename();
        }
        return null;
    }
}