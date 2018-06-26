package ee.ut.sauron.controller;

import ee.ut.sauron.PersistenceUtil;
import ee.ut.sauron.dto.*;
import ee.ut.sauron.entities.TranslationRequestEntity;
import ee.ut.sauron.exception.IllegalRequestException;
import ee.ut.sauron.service.TranslationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    public ResponseDTO translationQuery(RequestDTO requestDTO, HttpServletRequest httpRequest) {
        return handleRequest(requestDTO, httpRequest);
    }

    @RequestMapping(method = {RequestMethod.POST}, path = "/v1.0/{langpair}/translator/translate")
    @CrossOrigin
    public List<List<OpenNMTReturnDTO>> openNMTQuery(
            @PathVariable String langpair,
            @RequestBody List<OpenNMTRequestDTO> requestDTO,
            HttpServletRequest httpRequest) {
        if (!SAURON_OPENNMT_INTERFACE_ENABLED){
            throw new IllegalRequestException("OpenNMT interface not enabled");
        }

        // Convert the OpenNMT DTO into a regular DTO
        RequestDTO request = new RequestDTO();
        request.setLangpair(langpair.substring(0, 4));
        request.setSrc(requestDTO.get(0).getSrc());
        request.setAuth(requestDTO.get(0).getFeats().get(0));
	    request.setDomain("opennmt");

        ResponseDTO response = handleRequest(request, httpRequest);
        // This looks ugly because it has to deal with weird nested JSON arrays that OpenNMT uses
        return new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(new OpenNMTReturnDTO(response.getTgt())))));
    }

    private ResponseDTO handleRequest(RequestDTO requestDTO, HttpServletRequest httpRequest){
        updateDatabase(requestDTO, httpRequest);
        return translationService.handleTranslationQuery(requestDTO);
    }

    private void updateDatabase(RequestDTO requestDTO, HttpServletRequest httpRequest){
        EntityManager entityManager = PersistenceUtil.getEntityManager();
        entityManager.getTransaction().begin();

        // Create entity to be stored in the database
        TranslationRequestEntity entity = new TranslationRequestEntity();
        entity.setToken(requestDTO.getAuth());
        // Get sender ip address
        try {
            InetAddress ip = InetAddress.getByName(httpRequest.getHeader("X-Forwarded-For"));
            byte[] bytes = ip.getAddress();
            entity.setIpv4(bytes);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // Insert entity into database
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

}
