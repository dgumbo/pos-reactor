package zw.co.hisolutions.pos.storage.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils; 
import org.springframework.web.multipart.MultipartFile;
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
@Profile("prod")
public class ProdFileSystemStorageServiceImpl implements StorageService {

    private static final String FONT_DIRECTORY = "hms-receipts-fonts/";
    private final Path rootLocation;
    private final Tika tika = new Tika();

    @Autowired
    public ProdFileSystemStorageServiceImpl(StorageProperties properties) {
        // System.out.println("\n\n\n\n\n\n\n Development Profile : Loading !!! \n\n\n\n\n\n\n\n\n\n");
        this.rootLocation = Paths.get(properties.getLocation());
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
    public File getFile(String pathFromRoot, String filename) {
        Path path = rootLocation.resolve(pathFromRoot + filename);
        File file = path.toFile();
        return file ;
    }

    @Override
    public File getFontFile(String fontFolder, String filename) {
        Path path = rootLocation.resolve(FONT_DIRECTORY + fontFolder + filename);
        File file = path.toFile();
        return file ;
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
    public void scanServerDirectory() {
        try {
            Stream<Path> filesInDirectory = Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));

            filesInDirectory.forEach(file -> {

                Path filePath = rootLocation.resolve(file);
                
                System.out.println("\nfilePath : " + filePath);

                String mimeType;
                try {
                    mimeType = tika.detect(filePath);
                } catch (IOException ex) {
                    mimeType = "";
//                    java.util.logging.Logger.getLogger(ProdFileSystemStorageServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    } 

    @Override
    public Tika getTika() {
        return tika;
    }

    @Override
    public DocumentMetadataService getDocumentMetadataService() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DocumentMetadata store(File file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DocumentMetadata store(MultipartFile file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
