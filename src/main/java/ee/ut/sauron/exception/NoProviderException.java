package ee.ut.sauron.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
@Slf4j
public class NoProviderException extends RuntimeException {

    public NoProviderException(String msg) {
        super(msg);
        log.warn("No provider found for request");
    }
}
