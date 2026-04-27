package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.EventResponse;
import be.odeeh.studio.odeehservice.domain.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {

    default List<EventResponse> map(Page<EventEntity> src) {
        return src.getContent().stream()
                .map(this::map)
                .toList();
    }

    @Mapping(source = "id", target = "id")
    @Mapping(source = "venueId", target = "venueId")
    @Mapping(source = "artistId", target = "artistId")
    EventResponse map(EventEntity src);
}
