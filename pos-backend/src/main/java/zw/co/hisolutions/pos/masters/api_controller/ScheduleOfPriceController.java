package zw.co.hisolutions.pos.masters.api_controller;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zw.co.hisolutions.pos.common.controllers.rest.BasicRestController;
import zw.co.hisolutions.pos.masters.entity.ScheduleOfPrice;
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.masters.service.ScheduleOfPriceService;
import zw.co.hisolutions.pos.common.util.Results;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.entity.StockTransaction;

@RestController
@RequestMapping("/api/schedule-of-prices")
public class ScheduleOfPriceController extends BasicRestController<ScheduleOfPrice, Long> {

    private final ScheduleOfPriceService scheduleOfPriceService;

    @Autowired
    public ScheduleOfPriceController(ScheduleOfPriceService scheduleOfPriceService) {
        this.scheduleOfPriceService = scheduleOfPriceService;
    }

    @Override
    public GenericService getService() {
        return this.scheduleOfPriceService;
    }

    @Override
    public Class getControllerClass() {
        return this.getClass();
    }

    @PostMapping(value = "/update-product-price", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<StockItem> updateProductPrice(@RequestBody(required = true) StockItem product, @RequestParam(name = "newPrice", required = true) BigDecimal newPrice, @RequestParam(name = "currencyId", required = true) long currencyId) throws Exception {
//        System.out.println("\n\nzw.co.hisolutions.pos.stocks.api.StockReceiveController.updateProductPrice()");

        ResponseEntity responseEntity;
        HttpStatus httpStatus;
        try {
            product = scheduleOfPriceService.updateBondPrice(product, newPrice, currencyId);
            httpStatus = HttpStatus.OK;
            responseEntity = new ResponseEntity<>(product, httpStatus);
        } catch (Exception ex) {
            httpStatus = HttpStatus.NOT_IMPLEMENTED;
            responseEntity = new ResponseEntity<>(new Results(Results.DBActionResult.EncounteredError, "Could not create entity.", "new", this.getClass()), httpStatus);

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return responseEntity;
    }

    @PostMapping(value = "/unit-cost-adjustment")
    public String unitCostAdjustment(@RequestBody StockTransaction stockTransaction) {
//        if (stockTransaction.getStockTransactionType() == StockTransactionType.COST_ADJUSTMENT) {
//            Product product = stockReceiveService.adjustUnitCost(stockTransaction); 
//        }

        return "redirect:/stocks/receipt/";
    }

    @PostMapping(value = "/update-all-bond-from-usd-price", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<Results> updateAllBondFromUsdPrice() {
//        if (stockTransaction.getStockTransactionType() == StockTransactionType.COST_ADJUSTMENT) {
//            Product product = stockReceiveService.adjustUnitCost(stockTransaction); 
//        }

        ResponseEntity responseEntity;
        HttpStatus httpStatus;
        try {
            scheduleOfPriceService.updateAllBondFromUsdPrice();
            httpStatus = HttpStatus.OK;
            Results r = new Results();
            r.setMessage("Success");
            r.setResult(Results.DBActionResult.Success);  
            responseEntity = new ResponseEntity<>(r, httpStatus);
        } catch (Exception ex) {
            httpStatus = HttpStatus.NOT_IMPLEMENTED;
            responseEntity = new ResponseEntity<>(new Results(Results.DBActionResult.EncounteredError, "Could not create entity.", "new", this.getClass()), httpStatus);

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return responseEntity;
    }

    @PostMapping(value = "/update-all-usd-from-bond-price", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<Results> updateAllUsdFromBondPrice() {
        ResponseEntity responseEntity;
        HttpStatus httpStatus;
        try {
            scheduleOfPriceService.updateAllUsdFromBondPrice();
            httpStatus = HttpStatus.OK;
            Results r = new Results();
            r.setResult(Results.DBActionResult.Success);
            r.setMessage("Success"); 
            responseEntity = new ResponseEntity<>(r, httpStatus);
        } catch (Exception ex) {
            httpStatus = HttpStatus.NOT_IMPLEMENTED;
            responseEntity = new ResponseEntity<>(new Results(Results.DBActionResult.EncounteredError, "Could not create entity.", "new", this.getClass()), httpStatus);

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } 

        return responseEntity;
    }

}
