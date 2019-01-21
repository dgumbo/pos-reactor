package zw.co.hisolutions.pos.storage.properties;

/**
 *
 * @author denzil
 */ 
public interface StorageProperties {  
    
      String getLocation ();
     
      String getAwsRegion ();
     
      String getAwsS3BucketName ();
     
      String getAwsAccessKeyId ();
     
      String getAwsSecretAccessKey ();  
}
