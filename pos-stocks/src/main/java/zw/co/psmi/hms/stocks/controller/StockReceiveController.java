package zw.co.psmi.hms.stocks.controller;

import java.util.List;
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
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.entity.StockTransaction;
import zw.co.psmi.hms.stocks.entity.StockTransactionLine;
import zw.co.psmi.hms.stocks.entity.StockTransactionStatus;
import zw.co.psmi.hms.stocks.entity.StockTransactionType;
import zw.co.psmi.hms.stocks.service.CurrentStockService;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.stocks.service.PreviousTransactionService;
import zw.co.psmi.hms.stocks.service.StockMovementDescriptionService;
import zw.co.psmi.hms.stocks.service.StockRequestService;
import zw.co.psmi.hms.stocks.service.StockSupplierService;

import zw.co.psmi.hms.stocks.service.StockTransactionService;
import zw.co.psmi.hms.util.Utils;

@Controller
@Slf4j
public class StockReceiveController {

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
    public StockReceiveController(StockTransactionService stockTransactionService, CurrentStockService currentStockService, StockMovementDescriptionService stockMovementDescriptionService, StockSupplierService stockSupplierService, UnitService unitService, GenericStockTypeService genericStockTypeService, StockRequestService stockRequestService) {
        this.stockTransactionService = stockTransactionService;

        this.currentStockService = currentStockService;
        this.stockMovementDescriptionService = stockMovementDescriptionService;
        this.stockSupplierService = stockSupplierService;
        this.unitService = unitService;
        this.genericStockTypeService = genericStockTypeService;
        this.stockRequestService = stockRequestService;
    }

    @RequestMapping(value = "/stocks/stockreceive", method = RequestMethod.GET)
    public String stockReceive(Model model, @AuthenticationPrincipal UserLogin userLogin, @ModelAttribute StockTransaction stockTransaction) {
        stockTransaction = stockTransaction == null ? new StockTransaction() : stockTransaction;
        stockTransaction.setUnitFrom(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockTransaction.setLocations(unitService.getLocationActive(userLogin.getUnitCode()));
        stockTransaction.setStockTransactionStatus(StockTransactionStatus.PENDING);
        model.addAttribute("stockTransactionType", StockTransactionType.STOCK_RECEIVE);
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockTransactions", stockTransactionService.findByAllBYSearchCriteria(StockTransactionType.STOCK_ISSUE, stockTransaction));
        return "stocks/stockreceive";
    }

    @RequestMapping(value = "/stocks/stockreceiveaction/{id}/{stockIssueTransactionId}", method = RequestMethod.GET)
    public String stockReceiveAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("id") Long id, @PathVariable("stockIssueTransactionId") long stockIssueTransactionId, Model model) {
        StockTransaction stockTransaction = stockTransactionService.getByID(id);
        stockTransaction = (stockTransaction == null ? new StockTransaction() : stockTransaction);
//        return stockReceiveActionLocal(userLogin, stockTransaction, stockIssueTransactionId, model);
//    }
//    private String stockReceiveActionLocal(UserLogin userLogin, StockTransaction stockTransaction,long stockIssueTransactionId, Model model) {
        if (stockTransaction.getId() > 0) {
            List<StockTransactionLine> stockTransactionLines = stockTransactionService.findStockTransactionLinesByStockTransaction(stockTransaction);
            stockTransaction.setStockTransactionLines(stockTransactionLines);
        } else {
            stockTransaction = stockTransactionService.createStockTransactionFromStockTransaction(stockIssueTransactionId);
            stockTransaction.setOriginalStockTransaction(stockIssueTransactionId);
        }
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockTransactionId", stockIssueTransactionId);
        return "stocks/stockreceiveaction";
    }

    @RequestMapping(value = "/stocks/stockreceiveform", method = RequestMethod.POST)
    public String stockReceiveForm(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute StockTransaction stockTransaction, Model model, RedirectAttributes redirectAttributes) {
        long id = stockTransaction.getOriginalStockTransaction();
        log.info("............" + id);
        stockTransaction.setUser(userLogin.getUsername());

        if (previousTransactionService.pastTransactionComplete(userLogin.getUsername(), stockTransaction.getStockTransactionType())) {
            log.info("Too Close, transactions");
            redirectAttributes.addFlashAttribute("msg", "setMsg('Previous Transaction Not yet Complete')");
            redirectAttributes.addFlashAttribute("msg", "setError('Previous Transaction Not yet Complete')");
            redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());
            return "redirect:/stocks/stockreceive/";
        }

        StockTransaction stockTransactionOld = stockTransactionService.getByID(id);
        stockTransaction.setLocationTo(stockTransactionOld.getLocationTo());
        stockTransaction.setLocationFrom(stockTransactionOld.getLocationFrom());
        stockTransaction.setStockRequest(stockTransactionOld.getStockRequest());
        stockTransaction.setUnitTo(stockTransactionOld.getUnitTo());
        stockTransaction.setUnitFrom(stockTransactionOld.getUnitFrom());
        
        stockTransaction.setStockTransactionType(StockTransactionType.STOCK_RECEIVE);
        stockTransaction.setStockTransactionStatus(StockTransactionStatus.COMPLETED);
        // System.out.println("\n\n\n\n\nstockTransaction.getRemarks() : " + stockTransaction.getRemarks() + "\n\n\n\n\n");  
        //stockTransactionService.preProcess(stockTransaction,false);
        String msg = stockTransactionService.save(stockTransaction);
        // log.info("persistlist:::" + stockTransaction.getStockTransactionLines());
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());
        return "redirect:/stocks/stockreceive/";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(500000);
    }
}
