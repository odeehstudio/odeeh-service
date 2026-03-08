package be.odeeh.studio.odeehservice.domain.exception;

import org.springframework.http.HttpStatus;

public class OdeehDuplicateException extends OdeehApplicationException {

    public OdeehDuplicateException() {
        super(
                HttpStatus.CONFLICT.getReasonPhrase(),
                HttpStatus.CONFLICT.value()
        );
    }
}
