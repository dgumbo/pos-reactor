
package zw.co.psmi.hms.stocks.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.entity.Location;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.entity.StockMovementDescription;
import zw.co.psmi.hms.stocks.entity.StockRequest;
import zw.co.psmi.hms.stocks.entity.StockRequestItem;
import zw.co.psmi.hms.stocks.entity.StockStatus;
import zw.co.psmi.hms.stocks.entity.StockTransaction;
import zw.co.psmi.hms.stocks.entity.StockTransactionLine;
import zw.co.psmi.hms.stocks.entity.StockTransactionStatus;
import zw.co.psmi.hms.stocks.entity.StockTransactionType;
import zw.co.psmi.hms.stocks.entity.StockType;
import zw.co.psmi.hms.stocks.service.CurrentStockService;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.stocks.service.PreviousTransactionService;
import zw.co.psmi.hms.stocks.service.StockMovementDescriptionService;
import zw.co.psmi.hms.stocks.service.StockRequestService;
import zw.co.psmi.hms.stocks.service.StockSupplierService;
import zw.co.psmi.hms.stocks.service.StockTransactionService;


@Controller
@Slf4j
public class PurchaseReceiptController {

    private StockTransactionService stockTransactionService;
    
    private CurrentStockService currentStockService;
    private StockMovementDescriptionService stockMovementDescriptionService;
    private StockSupplierService stockSupplierService;
    private UnitService unitService;
    private GenericStockTypeService genericStockTypeService;
    private StockRequestService stockRequestService;
    @Autowired
    private PreviousTransactionService previousTransactionService;

    @Autowired
    public PurchaseReceiptController(StockTransactionService stockTransactionService, CurrentStockService currentStockService, StockMovementDescriptionService stockMovementDescriptionService, StockSupplierService stockSupplierService, UnitService unitService, GenericStockTypeService genericStockTypeService,  StockRequestService stockRequestService) {
        this.stockTransactionService = stockTransactionService;
        
        this.currentStockService = currentStockService;
        this.stockMovementDescriptionService = stockMovementDescriptionService;
        this.stockSupplierService = stockSupplierService;
        this.unitService = unitService;
        this.genericStockTypeService = genericStockTypeService;
        this.stockRequestService = stockRequestService;
    }

    @RequestMapping(value = "/stocks/purchasereceipt", method = RequestMethod.GET)
    public String purchaseReceipt( @AuthenticationPrincipal UserLogin userLogin, @ModelAttribute StockTransaction stockTransaction, Model model) {
        model.addAttribute("stockTransactionTypes", StockTransactionType.values());
        stockTransaction = stockTransaction == null ? new StockTransaction():stockTransaction;
        stockTransaction.setUnitFrom(unitService.getByUnitCode(userLogin.getUnitCode()));
        model.addAttribute("stockTransaction", stockTransaction);
        List<StockTransaction> stockTransactions = stockTransactionService.getFromStockTransactionType(StockTransactionType.PURCHASE_RECEIPT);
        model.addAttribute("stockTransactions", stockTransactions);
        return "stocks/purchasereceipt";
    }
        
    @RequestMapping(value = "/stocks/purchasereceiptaction/{id}/{stockRequestId}", method = RequestMethod.GET)
    public String purchaseReceiptAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("id") Long id, @PathVariable("stockRequestId") long stockRequestId, Model model) {
        StockTransaction stockTransaction = stockTransactionService.getByID(id);
        stockTransaction = (stockTransaction == null ? new StockTransaction() : stockTransaction);
        List<StockMovementDescription> stockMovementDescriptions = stockMovementDescriptionService.findAllActive();
        List<Location> locations = unitService.getLocationActive(userLogin.getUnitCode());
        if (id > 0) {
            List<StockTransactionLine> stockTransactionLines = stockTransactionService.findStockTransactionLinesByStockTransaction(stockTransaction);
            stockTransaction.setStockTransactionLines(stockTransactionLines);
        }
        if(stockRequestId > 0)
           {
            StockRequest stockRequest = stockRequestService.getByID(stockRequestId);
           stockTransaction.setLocationTo(stockRequest.getIssueLocation());
           stockTransaction.setUnitFrom(stockRequest.getRequestUnit());
           stockTransaction.setLocationFrom(stockRequest.getLocation()); 
           stockTransaction.setStockRequest(stockRequest);
           stockTransaction.setUnitTo(stockRequest.getIssueUnit());
           stockTransaction.setStockTransactionType(StockTransactionType.PURCHASE_RECEIPT);    
           List<StockRequestItem> stockRequestItems = stockRequestService.getStockRequestItemsByStockRequestId(stockRequestId);
            stockRequestItems.stream().forEach(line->{ 
                //line.setCurrentStocks(currentStockService.getByGenericIdAndUomId(line.getGenericStockType().getId(),  line.getUnitOfMeasure().getId())); 
                line.setStockTypes(genericStockTypeService.getStockTypeByGenericIdAndUomId(line.getGenericStockType().getId(), line.getUnitOfMeasure().getId()));
            });
            stockTransaction.setStockRequestItems(stockRequestItems); 
           }
        List<StockType> stockTypes = new ArrayList<>();//genericStockTypeService.getStockTypeActive();
        model.addAttribute("stockTransactionTypes", StockTransactionType.values());
        model.addAttribute("stockTransactionStatuses", StockTransactionStatus.values());
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockMovementDescriptions", stockMovementDescriptions);
        model.addAttribute("stockStatuses", Arrays.asList(StockStatus.values()).stream().filter(s->s!=StockStatus.MERGED&&s!=StockStatus.SPLIT).collect(Collectors.toList()));
        model.addAttribute("stockTypes", stockTypes);
        model.addAttribute("locations", locations);
        model.addAttribute("stockRequestId", stockRequestId);
        
        return "stocks/purchasereceiptaction";
    }
    
    @RequestMapping(value = "/stocks/purchasereceiptform", method = RequestMethod.POST)
    public String purchaseReceiptForm(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute StockTransaction stockTransaction, Model model, RedirectAttributes redirectAttributes) {
        stockTransaction.setUser(userLogin.getUsername());  
        
        
        if(previousTransactionService.pastTransactionComplete(userLogin.getUsername(), stockTransaction.getStockTransactionType())){
            log.info("Too Close, transactions");
        redirectAttributes.addFlashAttribute("msg", "setMsg('Previous Transaction Not yet Complete')");
        redirectAttributes.addFlashAttribute("msg", "setError('Previous Transaction Not yet Complete')");
        redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());
        return "redirect:/stocks/purchasereceipt/";
        }
        
        stockTransaction.setStockRequest(stockRequestService.getByID(stockTransaction.getStockRequest().getId()));
        String msg = stockTransactionService.save(stockTransaction);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());
        return "redirect:/stocks/purchasereceipt/";
    }
    
    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setAutoGrowCollectionLimit(500000);
}
}
