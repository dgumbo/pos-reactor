package zw.co.hisolutions.pos.stocks.api;

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
import org.springframework.web.bind.annotation.RestController;
import zw.co.hisolutions.pos.common.controllers.rest.BasicRestController;
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.common.util.Results;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.entity.StockTake;
import zw.co.hisolutions.pos.stocks.service.StockTakeService;

/**
 *
 * @author dgumbo
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/stocks/take")
public class StockTakeController extends BasicRestController<StockTake, Long> {

    private final StockTakeService stockTakeService;

    public StockTakeController(StockTakeService stockTakeService) {
        this.stockTakeService = stockTakeService;
    }

    @PutMapping(value = "/partial-save-stock-take", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<StockTake> partialSaveStockTake(@RequestBody StockTake stockTake) throws Exception {
        System.out.println("\n" + stockTake.getClass().getName() + "  B4 Save : " + stockTake + "\n");

        ResponseEntity responseEntity;
        HttpStatus httpStatus;
        try {
            StockTake entity = stockTakeService.partialSaveStockTake(stockTake);
            httpStatus = HttpStatus.CREATED;
            responseEntity = new ResponseEntity<>(entity, httpStatus);
        } catch (Exception ex) {
            httpStatus = HttpStatus.NOT_IMPLEMENTED;
            responseEntity = new ResponseEntity<>(new Results(Results.DBActionResult.EncounteredError, "Could not create entity.", "new", this.getClass()), httpStatus);

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return responseEntity;
    }

    @PostMapping(value = "/finalize-stock-take", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<StockTake> finalizeStockTake(@RequestBody StockTake stockTake) throws Exception {
        System.out.println("\nStockTake B4 Save : " + stockTake + "\n");

        ResponseEntity responseEntity;
        HttpStatus httpStatus;
        try {
            StockTake entity = stockTakeService.finalizeStockTake(stockTake);
            httpStatus = HttpStatus.CREATED;
            responseEntity = new ResponseEntity<>(entity, httpStatus);
        } catch (Exception ex) {
            httpStatus = HttpStatus.NOT_IMPLEMENTED;
            responseEntity = new ResponseEntity<>(new Results(Results.DBActionResult.EncounteredError, "Could not create entity.", "new", this.getClass()), httpStatus);

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return responseEntity;
    }

    @GetMapping(value = "/check-pending-stock-take", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<StockTake> checkPendingStockTake() {
        StockTake pendingStockTake = stockTakeService.getAnyPendingStock();

        //Resource resource = getService().buildResource(entity);
        //System.out.println("\n T B4 Save : " + resource.getContent() + "\n");
        pendingStockTake = pendingStockTake != null ? pendingStockTake : new StockTake();
        return new ResponseEntity<>(pendingStockTake, HttpStatus.OK);
    }

    @GetMapping(value = "/pending-stock-take", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<?> getPendingStockTake() {
        StockTake stockTakePending = stockTakeService.getPendingStockTake();

        //Resource resource = getService().buildResource(entity);
        //System.out.println("\n T B4 Save : " + resource.getContent() + "\n");
        return new ResponseEntity<>(stockTakePending, HttpStatus.OK);
    }

    @GetMapping(value = "/all-stock-items", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<?> getAllStockProducts() {
        List<StockItem> productList = stockTakeService.getAllStock();

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
        return stockTakeService;
    }

    @Override
    public Class getControllerClass() {
        return this.getClass();
    }
}
