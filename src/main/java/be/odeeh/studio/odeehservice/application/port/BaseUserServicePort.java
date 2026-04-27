package be.odeeh.studio.odeehservice.application.port;

import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;

public interface BaseUserServicePort {

    BaseUserEntity createBaseUser(String uid);
}
