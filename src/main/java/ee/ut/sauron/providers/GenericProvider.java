package ee.ut.sauron.providers;

import ee.ut.sauron.translation.LanguagePair;
import ee.ut.sauron.translation.TranslationDomain;
import jsock.net.MessageSocket;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;


@Data
@ToString(exclude = "load")
@Slf4j
@XmlType(propOrder = {"name", "languagePair", "translationDomain", "fast", "ipAddress", "port"})
public class GenericProvider implements TranslationProvider {

    @XmlTransient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private AtomicInteger load = new AtomicInteger(0);

    private String name;
    private LanguagePair languagePair;
    private TranslationDomain translationDomain;

    private Boolean fast;

    private String ipAddress;
    private int port;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public LanguagePair getLang() {
        return languagePair;
    }

    @Override
    public TranslationDomain getDomain() {
        return translationDomain;
    }

    @Override
    public Boolean isFast() {
        return fast;
    }

    @Override
    public int load() {
        return load.get();
    }

    @Override
    public String translate(String src, boolean tok, boolean tc) {

        try {
            this.load.incrementAndGet();
            long t0 = System.currentTimeMillis();

            Socket socket = new Socket(ipAddress, port);
            MessageSocket messageSocket = new MessageSocket(socket);

            messageSocket.send_msg("ok");
            messageSocket.recv_msg();
            messageSocket.send(src.getBytes("UTF-8"));
            String out = messageSocket.recv_msg();
            messageSocket.send_msg("EOT");
            messageSocket.close();

            log.info("Out: " + out + String.format(" --- Translation took %s ms", System.currentTimeMillis() - t0));
            this.load.decrementAndGet();

            return out;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @XmlElement
    public void setTranslationDomain(TranslationDomain translationDomain) {
        this.translationDomain = translationDomain;
    }

}
