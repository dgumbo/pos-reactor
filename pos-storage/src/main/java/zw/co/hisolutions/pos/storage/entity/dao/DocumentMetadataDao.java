package zw.co.hisolutions.pos.storage.entity.dao;

import org.springframework.data.jpa.repository.JpaRepository;  
import zw.co.hisolutions.pos.storage.entity.DocumentMetadata;

/**
 *
 * @author denzil
 */
public interface DocumentMetadataDao extends JpaRepository<DocumentMetadata, Long>{

    public DocumentMetadata getByFilename(String filename);
    
}
