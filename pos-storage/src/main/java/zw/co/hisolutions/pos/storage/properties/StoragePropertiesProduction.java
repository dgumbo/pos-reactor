package zw.co.hisolutions.pos.storage.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author denzil
 */
//@ConfigurationProperties("storage")
@Configuration
@Data
@Profile("production")
public class StoragePropertiesProduction  implements StorageProperties{
    private String location = null;     
    private String awsAccessKeyId = null;     
    private String awsSecretAccessKey = null;  
    
    @Value("${aws.region}")
    private String awsRegion;
    
    @Value("${aws.s3.bucket.name}")
    private String awsS3BucketName;    
}
