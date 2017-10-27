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
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
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
    public NazgulResponseDTO translate(String src, boolean tok, boolean tc, boolean alignWeights) {

        try {
            long t0 = System.currentTimeMillis();

            this.load.incrementAndGet();
            MessageSocket sock = new MessageSocket(new Socket(ipAddress, port));

            sock.sendMessage("HI");
            sock.receiveRawMessage();

            String nazgulIn = new Gson().toJson(new NazgulRequestDTO(src, tok, tc, alignWeights));
            int msgSize = nazgulIn.getBytes("UTF-8").length;

            if (msgSize > 1024) {
                String sizeMsg = "msize:" + msgSize;
                log.info("SENDING SIZE MSG: {}", sizeMsg);
                sock.sendMessage(sizeMsg);
                String ok = sock.receiveRawMessage();
                log.info("NAZGUL RESPONDED: {}", ok);
            }

            log.info("SENDING REQUEST: {}", nazgulIn);
            sock.sendMessage(nazgulIn);
            String res = sock.receiveRawMessage();
            log.info("NAZGUL RESPONSE: {}", res);

            if (res.startsWith("msize:")) {
                String responseSizeStr = res.split(":")[1].trim();
                log.info("MSG SIZE: {}", responseSizeStr);
                int responseSize = Integer.parseInt(responseSizeStr);
                sock.sendMessage("OK");
                res = sock.receiveRawMessage(responseSize);
                log.info("REAL MESSAGE: {}", res);
            }

            sock.sendMessage("EOT");
            sock.close();

            this.load.decrementAndGet();

            log.info("TRANSLATION TIME: {} ms", System.currentTimeMillis() - t0);

            return new Gson().fromJson(res, NazgulResponseDTO.class);

        } catch (ConnectException e) {
            log.error("Nazgul {} ({}:{}) failed to connect.", name, ipAddress, port);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

