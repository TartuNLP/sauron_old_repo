package ee.ut.sauron.providers;

public interface TranslationProvider {

    String getName();

    String getLang();

    String getDomain();

    Boolean isFast();

    int load();

    String translate(String src, boolean tok, boolean tc);

}
