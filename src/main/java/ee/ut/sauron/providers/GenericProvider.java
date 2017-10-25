package ee.ut.sauron.providers;

import com.google.gson.Gson;
import ee.ut.sauron.dto.NazgulRequestDTO;
import jsock.net.MessageSocket;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

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
    public String translate(String src, boolean tok, boolean tc, boolean alignWeights) {

        try {
            long t0 = System.currentTimeMillis();

            this.load.incrementAndGet();
            MessageSocket sock = new MessageSocket(new Socket(ipAddress, port));

            sock.sendMessage("HI");
            sock.receiveRawMessage();

            sock.sendMessage(new Gson().toJson(new NazgulRequestDTO(src, tok, tc, alignWeights)));
            String sizeStr = sock.receiveRawMessage();
            log.info("size: " + sizeStr);
            int size = Integer.parseInt(sizeStr);
            String out = sock.receiveRawMessage(size);

            sock.sendMessage("EOT");
            sock.close();
            this.load.decrementAndGet();

            log.info("Translation took {} ms", System.currentTimeMillis() - t0);

            return out;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}

