package zw.co.hisolutions.pos.storage.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import zw.co.hisolutions.pos.storage.entity.DocumentMetadata;
import zw.co.hisolutions.pos.storage.exceptions.StorageException;

/**
 *
 * @author dgumbo
 */
public interface StorageService {

    DocumentMetadataService getDocumentMetadataService();

    Tika getTika();

    void init();

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    String getStorageLocation();

    DocumentMetadata store(File file);

    DocumentMetadata store(MultipartFile file);

    void scanServerDirectory();

    default DocumentMetadata getDocumentMetadata(String filename) {
        return getDocumentMetadataService().getByDocumentName(filename);
    }

    default List<DocumentMetadata> getAllMetadata() {
        return getDocumentMetadataService().findAll();
    }

    default byte[] documentToByteArray(File document) { 
        byte[] bytes = null;
        try {
            FileInputStream fis = new FileInputStream(document);
            BufferedInputStream inputStream = new BufferedInputStream(fis);
            bytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new StorageException("Could not convert document", e);
        }
        return bytes;
    }

    default String getMimeType(byte[] bytes) {
        return getTika().detect(bytes);
    }

    default String getMimeType(File file) throws IOException {
        return getTika().detect(file);
    }

    default String getMimeType(Resource file) throws IOException {
        return getTika().detect(file.getInputStream());
    }
}
