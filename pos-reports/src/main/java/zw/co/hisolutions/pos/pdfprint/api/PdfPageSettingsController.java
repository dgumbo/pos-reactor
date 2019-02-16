package zw.co.hisolutions.pos.pdfprint.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import zw.co.hisolutions.pos.common.controllers.rest.BasicRestController; 
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.pdfprint.entity.PdfPageSettings;
import zw.co.hisolutions.pos.pdfprint.service.PdfPageSettingsService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
@RequestMapping("/api/pdf-page-settings") 
public class PdfPageSettingsController extends BasicRestController<PdfPageSettings, Long>    {

    private final PdfPageSettingsService productService; 
 
    public PdfPageSettingsController(PdfPageSettingsService productService ) {
        this.productService = productService; 
    }  

    @GetMapping(value = "/getByName/{name}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<?> getByName (@PathVariable String name) {
        PdfPageSettings product = productService.findByName(name);
        //Resource resource = getService().buildResource(entity);
        //System.out.println("\n T B4 Save : " + resource.getContent() + "\n");
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
     
    @Override
    public GenericService getService() {
        return this.productService;
    }

    @Override
    public Class getControllerClass() {
        return this.getClass() ;
    } 
     
}
