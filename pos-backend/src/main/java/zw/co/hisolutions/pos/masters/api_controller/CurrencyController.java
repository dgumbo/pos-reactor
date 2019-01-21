package zw.co.hisolutions.pos.masters.api_controller;
 
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
import zw.co.hisolutions.pos.masters.entity.Currency;  
import zw.co.hisolutions.pos.masters.service.CurrencyService;

/**
 *
 * @author dgumbo
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
@RequestMapping("/api/masters/currency")
public class CurrencyController extends BasicRestController<Currency, Long> {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    } 

    @GetMapping(value = "/getByCurrencyName/{currencyName}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<?> getByCurrencyName(@PathVariable String currencyName) {
        Currency currency = currencyService.getByName(currencyName);
        //Resource resource = getService().buildResource(entity);
        //System.out.println("\n T B4 Save : " + resource.getContent() + "\n");
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }
    
    @Override
    public GenericService getService() {
        return this.currencyService;
    }

    @Override
    public Class getControllerClass() {
        return this.getClass();
    }

}
