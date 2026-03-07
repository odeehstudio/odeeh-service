package be.odeeh.studio.odeehservice.domain.exception;

import lombok.Getter;

@Getter
public abstract class OdeehApplicationException extends RuntimeException {

    private final String code;
    private final int status;

    protected OdeehApplicationException(String message, String code, int status) {
        super(message);
        this.code = code;
        this.status = status;
    }
}
