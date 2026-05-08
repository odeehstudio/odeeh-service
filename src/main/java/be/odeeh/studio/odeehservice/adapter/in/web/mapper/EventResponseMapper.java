package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.EventResponse;
import be.odeeh.studio.odeehservice.domain.model.VenueEventEntityQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventResponseMapper {

    default List<EventResponse> toResponse(Page<VenueEventEntityQuery> src) {
        return src.getContent().stream()
                .map(this::toResponse)
                .toList();
    }

    @Mapping(source = "id", target = "id")
    @Mapping(source = "artistName", target = "artistName")
    EventResponse toResponse(VenueEventEntityQuery src);
}
