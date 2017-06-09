package ee.ut.sauron.providers;

import ee.ut.sauron.translation.LanguagePair;
import ee.ut.sauron.translation.TranslationDomain;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ShatProvider implements TranslationProvider {

    private AtomicInteger load = new AtomicInteger(0);

    @Override
    public String getName() {
        return "ShatProvider";
    }

    @Override
    public LanguagePair getLang() {
        return LanguagePair.eten;
    }

    @Override
    public TranslationDomain getDomain() {
        return TranslationDomain.general;
    }

    @Override
    public Boolean isFast() {
        return true;
    }

    @Override
    public int load() {
        log.info(getName() + " load: " + load.get());
        return load.get();
    }

    @Override
    public String translate(String src, boolean tok, boolean tc) {
        this.load.incrementAndGet();
        log.info(getName() + " started translating src: " + src);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String out = "shat";


        log.info(getName() + " finished translating src: " + src);
        this.load.decrementAndGet();
        return out;
    }

}
