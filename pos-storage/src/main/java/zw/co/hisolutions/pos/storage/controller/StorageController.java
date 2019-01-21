package zw.co.hisolutions.pos.storage.controller;
 
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import zw.co.hisolutions.pos.storage.entity.DocumentMetadata;
import zw.co.hisolutions.pos.storage.service.StorageService; 

/**
 *
 * @author dgumbo
 */
@Controller
@RequestMapping("/storage")
public class StorageController {
//    @Value("${spring.profiles.active}")
//    private String springProfilesActive ;

    StorageService storageService; 

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }       

    // List<String> files = new ArrayList<>();
    @GetMapping("")
    @ResponseBody
    public String StorageIndexHandler() {
        System.out.println("zw.co.hisolutions.storage.controllers.StorageController.StorageIndexHandler()");
        return "<p>Storage Service Storage Location : " + storageService.getStorageLocation() + "</p>";
    } 

    @PostMapping(value="/upload")//, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, "application/hal+json"})
    public ResponseEntity<DocumentMetadata> handleFileUpload(@RequestParam("file") MultipartFile file) {
        System.out.println("zw.co.hisolutions.storage.controllers.StorageController.handleFileUpload()");
        
        // String message = "";
        DocumentMetadata documentMetadata = new DocumentMetadata();
        try {
            //message = "You successfully uploaded " + file.getOriginalFilename() + "!";
            documentMetadata = storageService.store(file);
            return new ResponseEntity<>(documentMetadata, HttpStatus.CREATED);
        } catch (Exception e) {
            //message = "FAIL to upload " + file.getOriginalFilename() + "!"; 
            return new ResponseEntity<>(documentMetadata, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/listallfiles")
    public ResponseEntity<?> listAllFiles() {
        System.out.println("zw.co.hisolutions.storage.controllers.StorageController.listAllFiles()");
        List<String> files = storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(StorageController.class,
                        "viewFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList());

        return new ResponseEntity<>(files, HttpStatus.OK); 
    }

    @GetMapping("/view/{filename:.+}") //using Path Variable
    @ResponseBody
    public ResponseEntity<Resource> viewFile(@PathVariable String filename) {
        System.out.println("zw.co.hisolutions.storage.controllers.StorageController.viewFile()");
        return disposeFileContent(filename, ContentDisposalType.inline);
    }

    @GetMapping("/view") //using Request Paramemeter
    @ResponseBody
    public ResponseEntity<Resource> viewFileR(@RequestParam("filename") String filename) {
        System.out.println("zw.co.hisolutions.storage.controllers.StorageController.viewFileR()");
        return disposeFileContent(filename, ContentDisposalType.inline);
    }

    @GetMapping("/download/{filename:.+}") // Download File Using Path Variable
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        System.out.println("zw.co.hisolutions.storage.controllers.StorageController.downloadFile()");
        return disposeFileContent(filename, ContentDisposalType.attachment);
    }

    @GetMapping("/download") // Download File Using Path Variable
    @ResponseBody
    public ResponseEntity<Resource> downloadFileR(@RequestParam("filename") String filename) {
        System.out.println("zw.co.hisolutions.storage.controllers.StorageController.downloadFileR()");
        return disposeFileContent(filename, ContentDisposalType.attachment);
    }

    private ResponseEntity<Resource> disposeFileContent(String filename, ContentDisposalType contentDisposalType) {
        Resource file = storageService.loadAsResource(filename);
        MediaType mediaType = MediaType.ALL;
        long contentLength = 0;

        try {
            String mimeType = storageService.getMimeType(file);
            mediaType = MediaType.valueOf(mimeType);
        } catch (IOException ex) {
            Logger.getLogger(StorageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            contentLength = file.contentLength();
        } catch (IOException ex) {
            Logger.getLogger(StorageController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .contentLength(contentLength)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposalType + "; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping(value = "/getmetadata/{filename}")//, produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<?> getMetadata(@RequestParam(value = "filename") String filename) {
        System.out.println("zw.co.hisolutions.core.controllers.rest.ProductCategoryController.getMetadata()");

        Path data = storageService.load(filename);

        return new ResponseEntity<>(data, HttpStatus.OK);

        // return new ResponseEntity<>("entity", HttpStatus.OK);
    }

    @GetMapping(value = "/getallmetadata")//, produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<?> getAllMetadata() {
        System.out.println("zw.co.hisolutions.core.controllers.rest.ProductCategoryController.getAllMetadata()");
        //Stream<Path> data = fileSystemDocumentStorageService.loadAll();

        List<DocumentMetadata> data = storageService.getAllMetadata();
        return new ResponseEntity<>(data, HttpStatus.OK);

        // return new ResponseEntity<>("entity", HttpStatus.OK);
    }
}
