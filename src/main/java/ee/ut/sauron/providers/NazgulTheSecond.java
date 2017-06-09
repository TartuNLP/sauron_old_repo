package ee.ut.sauron.providers;

import ee.ut.sauron.translation.LanguagePair;
import ee.ut.sauron.translation.TranslationDomain;
import jsock.net.MessageSocket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class NazgulTheSecond implements TranslationProvider {

    private static final String IP = "54.229.234.102";
    private static final int PORT = 12345;

    private AtomicInteger load = new AtomicInteger(0);

    @Override
    public String getName() {
        return "Nazgul the Second";
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
        return load.get();
    }

    @Override
    public String translate(String src, boolean tok, boolean tc) {

        try {
            Socket socket = new Socket(IP, PORT);
            MessageSocket jsock2 = new MessageSocket(socket);

            jsock2.send_msg("ok");
            log.info("Socket handshake: " + jsock2.recv_msg());
            jsock2.send(src.getBytes("UTF-8"));
            String out = jsock2.recv_msg();
            jsock2.send_msg("EOT");

            log.info("Out: " + out);
            return out;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
