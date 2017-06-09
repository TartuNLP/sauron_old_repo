package ee.ut.sauron.providers;

import ee.ut.sauron.translation.LanguagePair;
import ee.ut.sauron.translation.TranslationDomain;

public interface TranslationProvider {

    String getName();

    LanguagePair getLang();

    TranslationDomain getDomain();

    Boolean isFast();

    int load();

    String translate(String src, boolean tok, boolean tc);

}
