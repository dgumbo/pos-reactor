package zw.co.hisolutions.pos.client.printer.socketserver;

/**
 *
 * @author rise3
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ServerThread implements Runnable {

    private ObjectFactory<ServerReceiver> receiverFactory;
    private ThreadPoolTaskExecutor taskExecutor;
    private ServerSocket welcomeSocket = null;

    @Autowired
    public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Autowired
    public void setReceiverFactory(ObjectFactory<ServerReceiver> receiverFactory) {
        this.receiverFactory = receiverFactory;
    }

    @Override
    public void run() {
        try {
            int portNumber = 12755;
            welcomeSocket = new ServerSocket(portNumber);
            System.out.println("open socket");
            log.info("open socket");
            while (true) {
                System.out.println("opened socket on port:" + portNumber);
                log.info("opened socket on port:" + portNumber);
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("connected socket:" + connectionSocket.getRemoteSocketAddress());
                log.info("connected socket:" + connectionSocket.getRemoteSocketAddress());
                ServerReceiver rec1 = receiverFactory.getObject();
                rec1.setSocket(connectionSocket);
                taskExecutor.execute(rec1);
                //_e_(rec1);
            }
        } catch (IOException ex) {
            log.error("Error:{}", ex);
        }
    }

    @PreDestroy
    public void _preDestroy() {
        if (welcomeSocket != null) {
            try {
                welcomeSocket.close();
            } catch (IOException ex) {
                log.error("Error:{}", ex);
            }
        }
        welcomeSocket = null;
    }
}
