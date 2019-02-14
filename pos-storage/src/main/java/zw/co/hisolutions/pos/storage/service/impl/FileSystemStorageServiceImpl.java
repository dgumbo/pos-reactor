package zw.co.hisolutions.pos.storage.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level; 
import java.util.stream.Stream;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import zw.co.hisolutions.pos.storage.entity.Status;
import zw.co.hisolutions.pos.storage.entity.DocumentMetadata;
import zw.co.hisolutions.pos.storage.exceptions.StorageException;
import zw.co.hisolutions.pos.storage.exceptions.StorageFileNotFoundException;
import zw.co.hisolutions.pos.storage.properties.StorageProperties;
import zw.co.hisolutions.pos.storage.service.DocumentMetadataService;
import zw.co.hisolutions.pos.storage.service.StorageService;

/**
 *
 * @author dgumbo
 */
@Service
@Profile("devs")
public class FileSystemStorageServiceImpl implements StorageService { 
    
    private final Path rootLocation;
    private final DocumentMetadataService documentMetadataService;
    private final Tika tika = new Tika();

    @Autowired
    public FileSystemStorageServiceImpl(StorageProperties properties, DocumentMetadataService documentMetadataService) {
        // System.out.println("\n\n\n\n\n\n\n Development Profile : Loading !!! \n\n\n\n\n\n\n\n\n\n");
       this.rootLocation = Paths.get(properties.getLocation());
        this.documentMetadataService = documentMetadataService;        
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public String getStorageLocation() {
        return rootLocation.toString();
    }

    @Override
    @Transactional
    public DocumentMetadata store(MultipartFile file) {
        System.out.println("Starting Save");
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file " + filename);
        }

        DocumentMetadata documentMetadata = null;
        try {
            documentMetadata = getDocumentMetadata(filename);
            if (documentMetadata != null) {
                documentMetadata.setStatus(Status.DocumentAlreadyExists);
            } else {
                documentMetadata = new DocumentMetadata();

                if (filename.contains("..")) {
                    // This is a security check
                    throw new StorageException("Cannot store file with relative path outside current directory " + filename);
                }

                System.out.println("this.rootLocation.resolve(filename) : " + this.rootLocation.resolve(filename));
                Files.copy(file.getInputStream(), this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);

                String mimeType = tika.detect(file.getBytes());

                documentMetadata.setActiveStatus(true);
                documentMetadata.setFilename("/storage/view/" + filename);
                documentMetadata.setFilePath(this.rootLocation.resolve(filename).toString());
                documentMetadata.setStatus(Status.Success);
                documentMetadata.setMimeType(mimeType);

                documentMetadata = documentMetadataService.create(documentMetadata);
            }

        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FileSystemStorageServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new StorageException("Failed to store file " + filename, ex);
        }

        System.out.println("Finishing Save");
        return documentMetadata;
    } 
 
    @Override
    public DocumentMetadata store(File file) {
       
        System.out.println("Starting Save");
        String filename = file.getName();
        if (!file.exists()) {
            throw new StorageException("Failed to store empty file " + filename);
        }

        DocumentMetadata documentMetadata = null;
        try {
            documentMetadata = getDocumentMetadata(filename);
            if (documentMetadata != null) {
                documentMetadata.setStatus(Status.DocumentAlreadyExists);
            } else {
                documentMetadata = new DocumentMetadata();

                if (filename.contains("..")) {
                    // This is a security check
                    throw new StorageException("Cannot store file with relative path outside current directory " + filename);
                }

                System.out.println("this.rootLocation.resolve(filename) : " + this.rootLocation.resolve(filename));
                FileInputStream fis = new FileInputStream(file);
                Files.copy( fis, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);

                String mimeType = tika.detect(fis);

                documentMetadata.setActiveStatus(true);
                documentMetadata.setFilename(filename);
                documentMetadata.setFilePath(this.rootLocation.resolve(filename).toString());
                documentMetadata.setStatus(Status.Success);
                documentMetadata.setMimeType(mimeType);

                documentMetadata = documentMetadataService.create(documentMetadata);
            }

        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FileSystemStorageServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new StorageException("Failed to store file " + filename, ex);
        }

        System.out.println("Finishing Save");
        return documentMetadata;
    }
    
    @Override
    public void scanServerDirectory() {
        try {
            Stream<Path> filesInDirectory = Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));

            filesInDirectory.forEach(file -> {
                DocumentMetadata documentMetadata = getDocumentMetadata(file.toFile().getName());
                if (documentMetadata == null) {
                    Path filePath = rootLocation.resolve(file); 

                    String mimeType;
                    try {
                        mimeType = tika.detect(filePath);
                    } catch (IOException ex) {
                        mimeType = "";
                        java.util.logging.Logger.getLogger(FileSystemStorageServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    documentMetadata = new DocumentMetadata();
                    documentMetadata.setActiveStatus(true);
                    documentMetadata.setFilename(file.toFile().getName());
                    documentMetadata.setFilePath(this.rootLocation.resolve(file.getFileName()).toString());
                    documentMetadata.setStatus(Status.Success);
                    documentMetadata.setMimeType(mimeType);
                    try {
                        documentMetadataService.create(documentMetadata);
                    } catch (IllegalArgumentException ex) {
                        java.util.logging.Logger.getLogger(FileSystemStorageServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public DocumentMetadataService getDocumentMetadataService() {
       return documentMetadataService;
    }

    @Override
    public Tika getTika() {
        return tika;
    }

    @Override
    public File getFile(String pathFromRoot, String filename) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File getFontFile(String fontFolder, String filename) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
