package ee.ut.sauron.controller;

import ee.ut.sauron.dto.RequestDTO;
import ee.ut.sauron.dto.ResponseDTO;
import ee.ut.sauron.service.TranslationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class RestServicesController {

    private final TranslationService translationService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "/v1.0/translate")
    public ResponseDTO translationQuery(RequestDTO requestDTO) {

        log.info(requestDTO.toString());
        return translationService.handleTranslationQuery(requestDTO);
    }

}
