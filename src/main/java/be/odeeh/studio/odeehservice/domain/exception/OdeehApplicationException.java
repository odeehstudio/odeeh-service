package be.odeeh.studio.odeehservice.domain.exception;

import lombok.Getter;

@Getter
public abstract class OdeehApplicationException extends RuntimeException {

    private final String code;
    private final int status;

    protected OdeehApplicationException(String code, int status) {
        super(code);
        this.code = code;
        this.status = status;
    }
}
