package ee.ut.sauron.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
@Slf4j
public class TeapotException extends RuntimeException {

    public TeapotException(String msg) {
        super(msg);
        log.warn("Coffee requested, tea served");
    }
}
