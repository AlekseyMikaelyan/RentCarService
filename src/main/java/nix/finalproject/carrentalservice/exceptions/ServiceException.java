package nix.finalproject.carrentalservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class ServiceException {

    private ServiceException() {

    }

    public static ResponseStatusException entityNotFound(String exceptionMessage) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, exceptionMessage);
    }

}
