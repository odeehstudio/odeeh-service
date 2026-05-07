package be.odeeh.studio.odeehservice.application.port;

import be.odeeh.studio.odeehservice.application.model.Username;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;

public interface BaseUserServicePort {

    Boolean isUserEnrolled(String uid);

    Boolean isUsernameAvailable(Username request);

    BaseUserEntity enroll(String uid, Username request);
}
