package zw.co.hisolutions.pos.stocks.api;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController; 
import zw.co.hisolutions.pos.common.controllers.rest.BasicRestController;
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.common.util.Results;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.entity.StockReceive; 
import zw.co.hisolutions.pos.stocks.service.CurrentStockService;
import zw.co.hisolutions.pos.stocks.service.StockReceiveService;

/**
 *
 * @author dgumbo
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/stocks/receive")
public class StockReceiveController  extends BasicRestController<StockReceive, Long> { 
    private final StockReceiveService stockReceiveService;  
    private final CurrentStockService currentStockService;

    public StockReceiveController( StockReceiveService StockReceiveService, CurrentStockService currentStockService ) {
        this.stockReceiveService = StockReceiveService;
        this.currentStockService=currentStockService;
    }    

    @PutMapping(value = "/partial-save-stock-receive", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<StockReceive> partialSaveStockReceive(@RequestBody StockReceive stockReceive) throws Exception {
        System.out.println("\n" + stockReceive.getClass().getName() + "  B4 Save : " + stockReceive + "\n");

        ResponseEntity responseEntity;
        HttpStatus httpStatus;
        try {
            StockReceive entity = stockReceiveService.partialSaveStockReceive(stockReceive);
            httpStatus = HttpStatus.CREATED;
            responseEntity = new ResponseEntity<>(entity, httpStatus);
        } catch (Exception ex) {
            httpStatus = HttpStatus.NOT_IMPLEMENTED;
            responseEntity = new ResponseEntity<>(new Results(Results.DBActionResult.EncounteredError, "Could not create entity.", "new", this.getClass()), httpStatus);

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return responseEntity;
    }

    @PostMapping(value = "/finalize-stock-receive", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<StockReceive> finalizeStockReceive(@RequestBody StockReceive stockReceive) throws Exception {
        System.out.println("\nStockReceive B4 Save : " + stockReceive + "\n");

        ResponseEntity responseEntity;
        HttpStatus httpStatus;
        try {
            StockReceive entity = stockReceiveService.finalizeStockReceive(stockReceive);
            httpStatus = HttpStatus.CREATED;
            responseEntity = new ResponseEntity<>(entity, httpStatus);
        } catch (Exception ex) {
            httpStatus = HttpStatus.NOT_IMPLEMENTED;
            responseEntity = new ResponseEntity<>(new Results(Results.DBActionResult.EncounteredError, "Could not create entity.", "new", this.getClass()), httpStatus);

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return responseEntity;
    }

    @GetMapping(value = "/check-pending-stock-receive", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<StockReceive> checkPendingStockReceive() {
        StockReceive pendingStockReceive = stockReceiveService.getAnyPendingStock();

        //Resource resource = getService().buildResource(entity);
        //System.out.println("\n T B4 Save : " + resource.getContent() + "\n");
        pendingStockReceive = pendingStockReceive != null ? pendingStockReceive : new StockReceive();
        return new ResponseEntity<>(pendingStockReceive, HttpStatus.OK);
    }

    @GetMapping(value = "/pending-stock-receive", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<?> getPendingStockReceive() {
        StockReceive stockReceivePending = stockReceiveService.getPendingStockReceive();

        //Resource resource = getService().buildResource(entity);
        //System.out.println("\n T B4 Save : " + resource.getContent() + "\n");
        return new ResponseEntity<>(stockReceivePending, HttpStatus.OK);
    }
    
    @GetMapping(value = "/getAllByDateRange", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<List<StockReceive>> getAllByDateRange(@RequestParam(name = "fromDate", required = false) Date fromDate,
            @RequestParam(name = "toDate", required = false) Date toDate ) throws Exception { 
//        System.out.println("\n\nzw.co.hisolutions.pos.stocks.api.StockReceiveController.getAll()\n");
//        System.out.println("");
        
        ResponseEntity responseEntity;
        HttpStatus httpStatus;
        try {
            List<StockReceive> entity =stockReceiveService.findAll() ;
            System.out.println("entity");
            System.out.println(entity);
            System.out.println("\n\n");
            httpStatus = HttpStatus.OK;
            responseEntity = new ResponseEntity<>(entity, httpStatus); 
        } catch (Exception ex) {
            httpStatus = HttpStatus.NOT_IMPLEMENTED;
            responseEntity = new ResponseEntity<>(new Results(Results.DBActionResult.EncounteredError, "Could not create entity.", "new", this.getClass()), httpStatus); 
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return responseEntity;
    }   
    
//    @PutMapping(value = "/receive", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
//    @Override
//    public ResponseEntity<StockReceive> create(@RequestBody StockReceive stockReceive) throws Exception {
//        System.out.println("\n"+ stockReceive.getClass().getName() + "  B4 Save : " + stockReceive + "\n");
//     
//        ResponseEntity responseEntity;
//        HttpStatus httpStatus;
//        try {
//            StockReceive entity =stockReceiveService.receiveStock( stockReceive );
//            httpStatus = HttpStatus.CREATED;
//            responseEntity = new ResponseEntity<>(entity, httpStatus);
//        } catch (Exception ex) {
//            httpStatus = HttpStatus.NOT_IMPLEMENTED;
//            responseEntity = new ResponseEntity<>(new Results(Results.DBActionResult.EncounteredError, "Could not create entity.", "new", this.getClass()), httpStatus);
//
//            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return responseEntity;
//    }

    @GetMapping(value = "/available-stock", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<?> getAllStockProducts () {
        List<StockItem> productList = currentStockService.getAllStock();
        
        
        //Resource resource = getService().buildResource(entity);
        //System.out.println("\n T B4 Save : " + resource.getContent() + "\n");
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(500000);
    }

    @Override
    public GenericService getService() {
       return stockReceiveService ;
    }

    @Override
    public Class getControllerClass() {
        return this.getClass();
    }
}
