package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import org.mapstruct.Named;

public interface ModelMapper {

    @Named("trim")
    default String trim(String src) {
        return src != null ? src.trim() : null;
    }
}
