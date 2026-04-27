package be.odeeh.studio.odeehservice.application.port;

import be.odeeh.studio.odeehservice.application.model.BaseUser;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEnrollmentStatus;

public interface BaseUserServicePort {

    BaseUserEnrollmentStatus validateEnrollment(String uid);

    BaseUserEntity createBaseUser(BaseUser baseUser, String uid);

    Boolean isUsernameAvailable(String username);
}
