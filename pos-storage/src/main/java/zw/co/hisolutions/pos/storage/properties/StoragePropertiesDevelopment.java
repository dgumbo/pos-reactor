package zw.co.hisolutions.pos.storage.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author denzil
 */
@Configuration
//@ConfigurationProperties("storage")
@Data
@Profile("dev")
public class StoragePropertiesDevelopment implements StorageProperties { 
    
    @Value("${server.upload.dir}")
    private String location ;
     
    private String awsRegion = null;
     
    private String awsS3BucketName = null;
     
    private String awsAccessKeyId = null;
     
    private String awsSecretAccessKey = null; 
     
}
