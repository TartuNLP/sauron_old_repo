package ee.ut.sauron.controller;

import ee.ut.sauron.dto.*;
import ee.ut.sauron.exception.IllegalRequestException;
import ee.ut.sauron.service.TranslationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class RestServicesController {

    private final TranslationService translationService;
    //private final boolean SAURON_OPENNMT_INTERFACE_ENABLED = System.getenv("SAURON_OPENNMT_INTERFACE_ENABLED").equalsIgnoreCase("TRUE");
    private final boolean SAURON_OPENNMT_INTERFACE_ENABLED = true;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "/v1.0/translate")
    @CrossOrigin
    public ResponseDTO translationQuery(RequestDTO requestDTO) {
        return translationService.handleTranslationQuery(requestDTO);
    }

    @RequestMapping(method = {RequestMethod.POST}, path = "/v1.0/{langpair}/translator/translate")
    @CrossOrigin
    public List<List<OpenNMTReturnDTO>> openNMTQuery(@PathVariable String langpair, @RequestBody List<OpenNMTRequestDTO> requestDTO) {
        if (!SAURON_OPENNMT_INTERFACE_ENABLED){
            throw new IllegalRequestException("OpenNMT interface not enabled");
        }

        RequestDTO request = new RequestDTO();
        request.setLangpair(langpair.substring(0, 4));
        request.setSrc(requestDTO.get(0).getSrc());
        request.setAuth(requestDTO.get(0).getFeats().get(0));
	request.setDomain("opennmt");

        ResponseDTO response = translationQuery(request);
        return new ArrayList<List<OpenNMTReturnDTO>>(Arrays.asList(new ArrayList<>(Arrays.asList(new OpenNMTReturnDTO(response.getTgt())))));
    }

}
