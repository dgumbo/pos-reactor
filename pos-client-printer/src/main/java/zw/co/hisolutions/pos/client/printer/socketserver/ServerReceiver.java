package zw.co.hisolutions.pos.client.printer.socketserver;

/**
 *
 * @author rise3
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import zw.co.hisolutions.pos.client.printer.model.PrintMsg;
import zw.co.hisolutions.pos.client.printer.service.PrintService;

@Component
@Scope("prototype")
@Slf4j
public class ServerReceiver implements Runnable {

    private final static List<ServerReceiver> us = new ArrayList<>();
    private Socket socket;
    private byte[] buff;
    private InputStream inputStream;
    private OutputStream outputStream;
    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private PrintService printService;

    public ServerReceiver() {
    }

    public void setSocket(Socket socket) throws IOException {
        this.buff = new byte[1024 * 8];
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        us.add(this);
    }

    public void write(byte[] data) throws IOException {
        outputStream.write(data);
        outputStream.flush();
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] data = read(inputStream);
                if (data == null) {
                    continue;
                }
                process(data, outputStream);
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ServerReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private byte[] read(InputStream inputStream) throws IOException, InterruptedException {
        int read1 = inputStream.read(buff);
        if (read1 < 1) {
            return null;
        }
        byte[] ret = Arrays.copyOf(buff, read1);
        return ret;
    }

    private void process(byte[] data, OutputStream outputStream) throws IOException {
        if (data == null) {
            return;
        }
        PrintMsg request = mapper.readValue(data, PrintMsg.class);
        PrintMsg response = printService.processRequest(request);
        write(mapper.writeValueAsBytes(response));
    }
}
