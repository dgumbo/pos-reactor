package zw.co.hisolutions.pos.storage.service;

import zw.co.hisolutions.pos.common.service.GenericService; 
import zw.co.hisolutions.pos.storage.entity.DocumentMetadata;

/**
 *
 * @author denzil
 */
public interface DocumentMetadataService extends GenericService<DocumentMetadata, Long>{

    public DocumentMetadata getByDocumentName(String filename);
    
}
