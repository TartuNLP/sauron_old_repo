package ee.ut.sauron.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Slf4j
public class IllegalRequestException extends RuntimeException {

    public IllegalRequestException(String msg) {
        super(msg);
        log.warn("Illegal request, reason: " + msg);
    }
}
