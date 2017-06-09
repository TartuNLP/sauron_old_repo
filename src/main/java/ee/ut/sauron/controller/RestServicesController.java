package ee.ut.sauron.controller;

import ee.ut.sauron.dto.RequestDTO;
import ee.ut.sauron.dto.ResponseDTO;
import ee.ut.sauron.service.ProviderService;
import ee.ut.sauron.translation.LanguagePair;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class RestServicesController {

    private final ProviderService providerService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "/rest/translate/{langpair}")
    public ResponseDTO translationQuery(@PathVariable LanguagePair langpair,
                                        RequestDTO requestDTO) {

        log.info(requestDTO.toString());
        return providerService.handleTranslationQuery(requestDTO, langpair);
    }

}
