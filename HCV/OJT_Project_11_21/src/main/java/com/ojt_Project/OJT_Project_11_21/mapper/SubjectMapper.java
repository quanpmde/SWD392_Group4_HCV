package com.ojt_Project.OJT_Project_11_21.mapper;

import com.ojt_Project.OJT_Project_11_21.dto.request.SubjectRequest;
import com.ojt_Project.OJT_Project_11_21.dto.response.SubjectResponse;
import com.ojt_Project.OJT_Project_11_21.entity.Subject;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring",uses = ImageMapper.class,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SubjectMapper {
    @Mapping(source = "subjectImage",target = "subjectImage",qualifiedByName = "mapImage")
    Subject toSubject(SubjectRequest request);
    SubjectResponse toSubjectResponse(Subject subject);

    // Phương thức để cập nhật thông tin subject
    @Mapping(target = "subjectImage",ignore = true)
    void updateSubjectFromRequest(@MappingTarget Subject subject, SubjectRequest request);

}
