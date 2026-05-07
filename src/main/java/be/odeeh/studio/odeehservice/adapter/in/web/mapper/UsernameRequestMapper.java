package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.UsernameRequest;
import be.odeeh.studio.odeehservice.application.model.Username;
import be.odeeh.studio.odeehservice.domain.exception.OdeehBadRequestException;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsernameRequestMapper extends ModelMapper {

    @BeforeMapping
    default void validate(UsernameRequest src) {
        if (src.value() == null || src.value().isBlank()) throw new OdeehBadRequestException();
    }

    @Mapping(source = "value", target = "value", qualifiedByName = "trim")
    Username toDomain(UsernameRequest src);
}
