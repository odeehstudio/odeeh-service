package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.FriendshipResponse;
import be.odeeh.studio.odeehservice.domain.model.FriendshipEntityQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FriendshipResponseMapper {

    List<FriendshipResponse> toResponse(List<FriendshipEntityQuery> src);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    FriendshipResponse toResponse(FriendshipEntityQuery src);
}
