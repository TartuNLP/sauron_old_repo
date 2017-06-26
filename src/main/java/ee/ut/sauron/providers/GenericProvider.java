package ee.ut.sauron.providers;

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
    private String languagePair;
    private String translationDomain;

    private Boolean fast;

    private String ipAddress;
    private int port;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLang() {
        return languagePair;
    }

    @Override
    public String getDomain() {
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
            long t0 = System.currentTimeMillis();

            this.load.incrementAndGet();
            MessageSocket sock = new MessageSocket(new Socket(ipAddress, port));

            sock.sendMessage("ok");
            sock.receiveRawMessage();

            sock.sendMessage(src);
            String out = sock.receiveRawMessage();

            sock.sendMessage("EOT");
            sock.close();
            this.load.decrementAndGet();

            log.info("Out: " + out + String.format(" --- Translation took %s ms", System.currentTimeMillis() - t0));

            return out;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // TODO: 26/06/2017 Can this be removed?
    @XmlElement
    public void setTranslationDomain(String translationDomain) {
        this.translationDomain = translationDomain;
    }

}
