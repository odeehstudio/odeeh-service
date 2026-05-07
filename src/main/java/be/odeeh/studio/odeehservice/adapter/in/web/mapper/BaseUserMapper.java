package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.BaseUserResponse;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BaseUserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    BaseUserResponse toResponse(BaseUserEntity src);
}
