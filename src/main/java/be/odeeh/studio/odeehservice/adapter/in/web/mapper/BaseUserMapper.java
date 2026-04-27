package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.BaseUserRequest;
import be.odeeh.studio.odeehservice.adapter.in.web.dto.BaseUserResponse;
import be.odeeh.studio.odeehservice.application.model.BaseUser;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import be.odeeh.studio.odeehservice.domain.exception.OdeehBadRequestException;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BaseUserMapper extends ModelMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "friendshipCode", target = "friendshipCode")
    BaseUserResponse map(BaseUserEntity src);

    @BeforeMapping
    default void validate(BaseUserRequest src) {
        if (src.username() == null) throw new OdeehBadRequestException();
    }

    @Mapping(source = "username", target = "username", qualifiedByName = "trim")
    @Mapping(source = ".", target = "friendshipCode", qualifiedByName = "generateUUID")
    BaseUser mapRequestToDomain(BaseUserRequest src);

    @Named("generateUUID")
    default UUID generateUUID(BaseUserRequest src) {
        return UUID.randomUUID();
    }
}
