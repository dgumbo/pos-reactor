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
@Profile("test")
public class StoragePropertiesTest implements StorageProperties { 
    private String location = null ;
      
    @Value("${aws.region}")
    private String awsRegion;
    
    @Value("${aws.s3.bucket.name}")
    private String awsS3BucketName;
    
    @Value("${aws.access.key.id}")
    private String awsAccessKeyId;
    
    @Value("${aws.secret.access.key}")
    private String awsSecretAccessKey; 
}
