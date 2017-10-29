package ee.ut.sauron.providers;


import ee.ut.sauron.dto.NazgulResponseDTO;


public interface TranslationProvider {

    String getName();

    String getLang();

    String getDomain();

    Boolean isFast();

    String getIpAddress();

    int getPort();

    int load();

    NazgulResponseDTO translate(String src, boolean tok, boolean tc, boolean alignWeights, boolean qualityEstimation);

}
