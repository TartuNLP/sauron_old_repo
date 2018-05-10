package ee.ut.sauron.providers;

import com.google.gson.Gson;
import ee.ut.sauron.dto.NazgulRequestDTO;
import ee.ut.sauron.dto.NazgulResponseDTO;
import jsock.net.MessageSocket;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;
import java.net.ConnectException;
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
    public String getLang() {
        return getLanguagePair();
    }

    @Override
    public String getDomain() {
        return getTranslationDomain();
    }

    @Override
    public Boolean isFast() {
        return getFast();
    }

    @Override
    public int load() {
        return load.get();
    }

    @Override
    public NazgulResponseDTO translate(String src, boolean tok, boolean tc, boolean alignWeights,
                                       boolean qualityEstimation) {

        try {
            this.load.incrementAndGet();
            MessageSocket sock = new MessageSocket(new Socket(ipAddress, port));

            sock.sendMessage("HI");
            sock.receiveRawMessage();

            String nazgulIn = new Gson().toJson(new NazgulRequestDTO(src, tok, tc, alignWeights, qualityEstimation));
            int msgSize = nazgulIn.getBytes("UTF-8").length;

            //if (msgSize > 1024) {
                String sizeMsg = "msize:" + msgSize;
                log.info("SENDING SIZE MSG: {}", sizeMsg);
                sock.sendMessage(sizeMsg);
                String ok = sock.receiveRawMessage();
                log.info("NAZGUL RESPONDED: {}", ok);
            //}

            log.info("NAZGUL REQUEST: {}", nazgulIn);
            sock.sendMessage(nazgulIn);
            String nazgulOut = sock.receiveRawMessage();
            log.info("NAZGUL RESPONSE: {}", nazgulOut);

            if (nazgulOut.startsWith("msize:")) {
                String responseSizeStr = nazgulOut.split(":")[1].trim();
                log.info("MSG SIZE: {}", responseSizeStr);
                int responseSize = Integer.parseInt(responseSizeStr);
                sock.sendMessage("OK");
                nazgulOut = sock.receiveRawMessage(responseSize);
                log.info("REAL MESSAGE: {}", nazgulOut);
            }

            sock.sendMessage("EOT");
            sock.close();

            this.load.decrementAndGet();

            return new Gson().fromJson(nazgulOut, NazgulResponseDTO.class);

        } catch (ConnectException e) {
            log.error("Nazgul {} ({}:{}) failed to connect.", name, ipAddress, port);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

