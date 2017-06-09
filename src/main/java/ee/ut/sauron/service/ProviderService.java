package ee.ut.sauron.service;

import ee.ut.sauron.dto.RequestDTO;
import ee.ut.sauron.dto.ResponseDTO;
import ee.ut.sauron.translation.LanguagePair;

public interface ProviderService {

    ResponseDTO handleTranslationQuery(RequestDTO requestDTO, LanguagePair langpair);

}
