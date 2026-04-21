package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.VenueResponse;
import be.odeeh.studio.odeehservice.domain.entity.VenueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VenueMapper {

    default List<VenueResponse> map(Page<VenueEntity> src) {
        return src.getContent().stream()
                .map(this::map)
                .toList();
    }

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "country", target = "country")
    VenueResponse map(VenueEntity src);
}
