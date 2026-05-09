package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.AttendanceResponse;
import be.odeeh.studio.odeehservice.domain.model.AttendanceEntityQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttendanceResponseMapper {

    List<AttendanceResponse> toResponse(List<AttendanceEntityQuery> src);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "baseUserId", source = "baseUserId")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "score", source = "score")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "venueName", source = "venueName")
    @Mapping(target = "artistName", source = "artistName")
    AttendanceResponse toResponse(AttendanceEntityQuery src);
}
