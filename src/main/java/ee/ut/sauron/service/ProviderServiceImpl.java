package ee.ut.sauron.service;

import ee.ut.sauron.dto.RequestDTO;
import ee.ut.sauron.dto.ResponseDTO;
import ee.ut.sauron.exception.IllegalRequestException;
import ee.ut.sauron.providers.GenericProviders;
import ee.ut.sauron.providers.TranslationProvider;
import ee.ut.sauron.translation.LanguagePair;
import ee.ut.sauron.translation.TranslationDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProviderServiceImpl implements ProviderService {
    private final AuthService authService;
    private final List<? extends TranslationProvider> providers;

    @Autowired
    public ProviderServiceImpl(AuthService authService) {
        this.authService = authService;

        try {
            Unmarshaller um = JAXBContext.newInstance(GenericProviders.class).createUnmarshaller();
            InputStream is = ProviderService.class.getResourceAsStream("/providers.xml");
            this.providers = ((GenericProviders) um.unmarshal(is)).getProvider();

            log.info("Translation providers:");
            providers.forEach(p -> log.info(p.toString()));

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseDTO handleTranslationQuery(RequestDTO requestDTO, LanguagePair langpair) {

        if (!isConsistent(requestDTO)) {
            throw new IllegalRequestException("Inconsistent request");
        }

        if (!authService.isAuthenticated(requestDTO.getAuth())) {
            throw new IllegalRequestException("Authentication failed");
        }

        TranslationProvider provider = chooseBestProvider(requestDTO.getFast(), langpair, requestDTO.getDomain());

        log.info("Chose provider: " + provider.getName());
        String out = provider.translate(requestDTO.getSrc(), requestDTO.getTok(), requestDTO.getTc());

        return new ResponseDTO(out);
    }


    private TranslationProvider chooseBestProvider(Boolean isFast, LanguagePair langpair, TranslationDomain domain) {

        // Search for a perfect match
        Optional<? extends TranslationProvider> provider = providers.stream()
                .filter(p -> p.isFast() == isFast)
                .filter(p -> p.getLang().equals(langpair))
                .filter(p -> p.getDomain().equals(domain))
                .min(Comparator.comparingInt(TranslationProvider::load));

        if (provider.isPresent()) {
            return provider.get();
        }


        // Search for a partial match ignoring GPU/CPU preference
        provider = providers.stream()
                .filter(p -> p.getLang().equals(langpair))
                .filter(p -> p.getDomain().equals(domain))
                .min(Comparator.comparingInt(TranslationProvider::load));

        return provider.orElseThrow(() -> new IllegalRequestException("No suitable provider"));
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

        return true;
    }

}
