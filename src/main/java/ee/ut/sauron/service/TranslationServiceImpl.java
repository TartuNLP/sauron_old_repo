package ee.ut.sauron.service;

import ee.ut.sauron.dto.NazgulResponseDTO;
import ee.ut.sauron.dto.RequestDTO;
import ee.ut.sauron.dto.ResponseDTO;
import ee.ut.sauron.exception.IllegalRequestException;
import ee.ut.sauron.exception.NoProviderException;
import ee.ut.sauron.exception.TeapotException;
import ee.ut.sauron.exception.UnauthorizedException;
import ee.ut.sauron.providers.GenericProviders;
import ee.ut.sauron.providers.TranslationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TranslationServiceImpl implements TranslationService {
    private final AuthService authService;
    private final List<? extends TranslationProvider> providers;

    @Autowired
    public TranslationServiceImpl(AuthService authService) {
        this.authService = authService;

        try (InputStream is = TranslationService.class.getResourceAsStream("/providers.xml")) {

            Unmarshaller um = JAXBContext.newInstance(GenericProviders.class).createUnmarshaller();
            this.providers = ((GenericProviders) um.unmarshal(is)).getProvider();

            log.info("Translation providers:");
            providers.forEach(p -> log.info(p.toString()));

        } catch (JAXBException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public ResponseDTO handleTranslationQuery(RequestDTO requestDTO) {

        long requestStartTime = System.currentTimeMillis();
        log.info("REQUEST: {}", requestDTO);

        if (!isConsistent(requestDTO)) {
            throw new IllegalRequestException("Inconsistent request");
        }

        if (!authService.isAuthenticated(requestDTO.getAuth())) {
            throw new UnauthorizedException("Authentication failed");
        }

        if (requestDTO.getDomain().equals("coffee")) {
            throw new TeapotException("No coffee for you");
        }

        TranslationProvider provider =
                chooseBestProvider(requestDTO.getFast(), requestDTO.getEngine(), requestDTO.getDomain());
        log.info("CHOSE PROVIDER: {} ({}:{})", provider.getName(), provider.getIpAddress(), provider.getPort());

        long nazgulStartTime = System.currentTimeMillis();
        NazgulResponseDTO nazgulResponse = provider.translate(requestDTO.getSrc(), requestDTO.getTok(),
                requestDTO.getTc(), requestDTO.getAlignweights(), requestDTO.getQualityestimation(), requestDTO.getConf());
        long nazgulEndTime = System.currentTimeMillis();

        ResponseDTO response = new ResponseDTO(nazgulResponse.getRaw_input(), nazgulResponse.getFinal_trans(),
                nazgulResponse.getRaw_trans(), nazgulResponse.getWeights(), nazgulResponse.getEstimation());
        log.info("RESPONSE: {}", response);

        long requestTime = System.currentTimeMillis() - requestStartTime;
        long nazgulTime = nazgulEndTime - nazgulStartTime;
        long sauronTime = requestTime - nazgulTime;
        log.info("REQUEST COMPLETED IN {} ms (SAURON: {} ms, NAZGUL: {} ms)", requestTime, sauronTime, nazgulTime);

        return response;
    }


    private TranslationProvider chooseBestProvider(Boolean isFast, String engine, String domain) {

        // Search for a perfect match
        Optional<? extends TranslationProvider> provider = providers.stream()
                .filter(p -> p.isFast() == isFast)
                .filter(p -> p.getLang().equals(engine))
                .filter(p -> p.getDomain().equals(domain))
                .min(Comparator.comparingInt(TranslationProvider::load));

        if (provider.isPresent()) {
            return provider.get();
        }

        // Search for a partial match ignoring GPU/CPU preference
        provider = providers.stream()
                .filter(p -> p.getLang().equals(engine))
                .filter(p -> p.getDomain().equals(domain))
                .min(Comparator.comparingInt(TranslationProvider::load));

        return provider.orElseThrow(() -> new NoProviderException("No suitable translation provider found"));
    }


    private boolean isConsistent(RequestDTO requestDTO) {
        if (requestDTO.getAuth() == null) {
            log.warn("Auth token is null");
            return false;
        }

        if (requestDTO.getSrc() == null) {
            log.warn("Source text is null");
            return false;
        }

        if (requestDTO.getEngine() == null) {
            log.warn("Language pair is null");
            return false;
        }

        return true;
    }

}
