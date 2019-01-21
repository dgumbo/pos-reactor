package zw.co.hisolutions.pos.storage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;  
import zw.co.hisolutions.pos.storage.controller.StorageController;
import zw.co.hisolutions.pos.storage.entity.DocumentMetadata;
import zw.co.hisolutions.pos.storage.entity.dao.DocumentMetadataDao;
import zw.co.hisolutions.pos.storage.service.DocumentMetadataService;

/**
 *
 * @author denzil
 */
@Service
public class DocumentMetadataServiceImpl implements DocumentMetadataService{
    DocumentMetadataDao documentMetadataDao;

    @Autowired
    public DocumentMetadataServiceImpl(DocumentMetadataDao documentMetadata) {
        this.documentMetadataDao = documentMetadata;
    }

    @Override
    public JpaRepository<DocumentMetadata, Long> getDao() {
        return documentMetadataDao;
    }

    @Override
    public Class getController() {
        return StorageController.class;
    } 

    @Override
    public DocumentMetadata getByDocumentName(String filename) {
        return documentMetadataDao.getByFilename(filename);
    }
}
