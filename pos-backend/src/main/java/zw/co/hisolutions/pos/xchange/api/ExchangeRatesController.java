//package zw.co.hisolutions.pos.xchange.api;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping; 
//import zw.co.hisolutions.pos.common.controllers.rest.BasicRestController;
//import zw.co.hisolutions.pos.common.service.GenericService;
//import zw.co.hisolutions.pos.masters.entity.Currency;
//import static zw.co.hisolutions.pos.reports.entity.ReportColumnNumberFormat.Currency;
//import zw.co.hisolutions.pos.stocks.service.ExchangeRatesService;
//import zw.co.hisolutions.pos.xchange.entity.GlobalExchangeRate;
//
///**
// *
// * @author dgumbo
// */
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
//@Controller
//@RequestMapping("/api/exchange-rates")
//public class ExchangeRatesController extends BasicRestController<GlobalExchangeRate, Long> {
//
//    private final ExchangeRatesService exchangeRatesService;
//
//    public ExchangeRatesController(ExchangeRatesService exchangeRatesService) {
//        this.exchangeRatesService = exchangeRatesService;
//    }
//
//    @GetMapping(value = "/currencies", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
//    public ResponseEntity<?> getCurrencies() {
//        return new ResponseEntity<>(Currency.values(), HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/getByCurrency/{Ccurrency}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
//    public ResponseEntity<?> getByName(@PathVariable Currency currency) {
//        GlobalExchangeRate product = exchangeRatesService.findByCurrency(currency);
//        //Resource resource = getService().buildResource(entity);
//        //System.out.println("\n T B4 Save : " + resource.getContent() + "\n");
//        return new ResponseEntity<>(product, HttpStatus.OK);
//    }
//
//    @Override
//    public GenericService getService() {
//        return this.exchangeRatesService;
//    }
//
//    @Override
//    public Class getControllerClass() {
//        return this.getClass();
//    }
//
//}
