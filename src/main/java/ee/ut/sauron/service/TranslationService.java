package ee.ut.sauron.service;

import ee.ut.sauron.dto.RequestDTO;
import ee.ut.sauron.dto.ResponseDTO;


public interface TranslationService {

    ResponseDTO handleTranslationQuery(RequestDTO requestDTO);

}
