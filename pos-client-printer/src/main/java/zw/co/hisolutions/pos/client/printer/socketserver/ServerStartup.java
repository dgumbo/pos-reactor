package zw.co.hisolutions.pos.client.printer.socketserver;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 *
 * @author rise3
 */
@Service
@Slf4j
public class ServerStartup {
    
    private ServerThread serverThread;

    @Autowired
    public void setServerThread(ServerThread serverThread) {
        this.serverThread = serverThread;
    }
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
    
    @PostConstruct
    public void startServer(){
        System.err.println("starting server");
        log.info("Starting server:"+serverThread);
        taskExecutor.execute(serverThread);
    }
}
