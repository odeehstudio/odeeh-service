package be.odeeh.studio.odeehservice.domain.exception;

import org.springframework.http.HttpStatus;

public class OdeehNotFoundException extends OdeehApplicationException {

    public OdeehNotFoundException() {
        super(
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                HttpStatus.NOT_FOUND.value()
        );
    }
}
