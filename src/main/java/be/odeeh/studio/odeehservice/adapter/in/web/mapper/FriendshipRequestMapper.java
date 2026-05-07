package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.FriendshipRequest;
import be.odeeh.studio.odeehservice.application.model.Friendship;
import be.odeeh.studio.odeehservice.domain.exception.OdeehBadRequestException;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FriendshipRequestMapper extends ModelMapper {

    @BeforeMapping
    default void validate(FriendshipRequest src) {
        if (src.uid() == null || src.uid().isBlank()) throw new OdeehBadRequestException();
    }

    @Mapping(source = "uid", target = "uid", qualifiedByName = "trim")
    Friendship toDomain(FriendshipRequest src);
}
