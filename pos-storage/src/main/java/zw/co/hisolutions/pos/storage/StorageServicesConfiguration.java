package zw.co.hisolutions.pos.storage;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zw.co.hisolutions.pos.storage.properties.StorageProperties;
import zw.co.hisolutions.pos.storage.service.StorageService;

@Configuration
public class StorageServicesConfiguration {
    private final String clientRegion;
    
    @Autowired
    public StorageServicesConfiguration(StorageProperties properties) {
        clientRegion = properties.getAwsRegion();
    }

    @Bean
    CommandLineRunner initLocalStorage(StorageService storageService) {
        //, InitializeStartData initializeStartData ) {
        System.out.println("\n\nzw.co.hisolutions.storage.StorageServicesConfiguration.initLocalStorage()");
        System.out.println("Initializing Local Storage !!!!! \n\n");
        return (args) -> { 
           storageService.init();
           storageService.scanServerDirectory(); 
        };
    }  
}
