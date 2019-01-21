package zw.co.psmi.hms.stocks.controller;

import java.util.ArrayList;
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
import zw.co.psmi.hms.stocks.entity.BookStock;
import zw.co.psmi.hms.stocks.entity.CurrentStock;
import zw.co.psmi.hms.stocks.entity.StockCount;
import zw.co.psmi.hms.stocks.entity.StockTransaction;
import zw.co.psmi.hms.stocks.entity.StockTransactionLine;
import zw.co.psmi.hms.stocks.entity.StockTransactionStatus;
import zw.co.psmi.hms.stocks.entity.StockTransactionType;
import zw.co.psmi.hms.stocks.service.BookStockService;
import zw.co.psmi.hms.stocks.service.CurrentStockService;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.stocks.service.PreviousTransactionService;
import zw.co.psmi.hms.stocks.service.StockCountService;
import zw.co.psmi.hms.stocks.service.StockMovementDescriptionService;
import zw.co.psmi.hms.stocks.service.StockRequestService;
import zw.co.psmi.hms.stocks.service.StockSupplierService;

import zw.co.psmi.hms.stocks.service.StockTransactionService;

@Controller
@Slf4j
public class StockUpdateController {

    private StockTransactionService stockTransactionService;

    private CurrentStockService currentStockService;
    private StockMovementDescriptionService stockMovementDescriptionService;
    private StockSupplierService stockSupplierService;
    private UnitService unitService;
    private GenericStockTypeService genericStockTypeService;
    private StockRequestService stockRequestService;
    private BookStockService bookStockService;
    @Autowired
    private StockCountService stockCountService;
    @Autowired
    private PreviousTransactionService previousTransactionService;

    @Autowired

    public StockUpdateController(StockTransactionService stockTransactionService, CurrentStockService currentStockService, StockMovementDescriptionService stockMovementDescriptionService, StockSupplierService stockSupplierService, UnitService unitService, GenericStockTypeService genericStockTypeService, StockRequestService stockRequestService, BookStockService bookStockService) {
        this.stockTransactionService = stockTransactionService;

        this.currentStockService = currentStockService;
        this.stockMovementDescriptionService = stockMovementDescriptionService;
        this.stockSupplierService = stockSupplierService;
        this.unitService = unitService;
        this.genericStockTypeService = genericStockTypeService;
        this.stockRequestService = stockRequestService;
        this.bookStockService = bookStockService;
    }

    @RequestMapping(value = "/stocks/stockupdate", method = RequestMethod.GET)
    public String stockUpdate(Model model, @AuthenticationPrincipal UserLogin userLogin, @ModelAttribute StockTransaction stockTransaction) {
        stockTransaction = stockTransaction == null ? new StockTransaction() : stockTransaction;
        stockTransaction.setUnitTo(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockTransaction.setLocations(unitService.getLocationActive(userLogin.getUnitCode()));
        stockTransaction.setStockTransactionStatus(StockTransactionStatus.COMPLETED);
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockTransactions", stockTransactionService.findByAllBYSearchCriteria(StockTransactionType.STOCK_UPDATE, stockTransaction));
        return "stocks/stockupdate";
    }

    @RequestMapping(value = "/stocks/stockupdateaction/{id}/{bookStockId}", method = RequestMethod.GET)
    public String stockUpdateAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("id") Long id, @PathVariable("bookStockId") long bookStockId, Model model) {
        List<CurrentStock> stockCounts = new ArrayList<>();
        StockTransaction stockTransaction = new StockTransaction();
        stockTransaction = (stockTransaction == null ? new StockTransaction() : stockTransaction);
        if (bookStockId > 0) {
            stockTransaction.setBooksStockId(bookStockId);
            stockCounts = currentStockService.findExceptionByBookStockId(bookStockId);
        }
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockCounts", stockCounts);
        return "stocks/stockupdateaction";
    }

    @RequestMapping(value = "/stocks/stockupdateform", method = RequestMethod.POST)
    public String stockUpdateForm(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute StockTransaction stockTransaction, Model model, RedirectAttributes redirectAttributes) {
        Long id = stockTransaction.getBooksStockId();
        if (!currentStockService.findExceptionByBookStockId(id).isEmpty()) {
            return stockUpdateAction(userLogin, 0l, id, model);
        }
        
        if (previousTransactionService.pastTransactionComplete(userLogin.getUsername(), stockTransaction.getStockTransactionType())) {
            log.info("Too Close, transactions");
            redirectAttributes.addFlashAttribute("msg", "setMsg('Previous Transaction Not yet Complete')");
            redirectAttributes.addFlashAttribute("msg", "setError('Previous Transaction Not yet Complete')");
            redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());
            return "redirect:/stocks/stockupdate/";
        }
        
        stockTransaction = stockTransactionService.createUpdateStockTransactionFromBookStock(id);
        stockTransaction.setUser(userLogin.getUsername());
        stockTransaction.setBookStock(bookStockService.getByID(id));
        String msg = stockTransactionService.save(stockTransaction);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());
        return "redirect:/stocks/stockupdate/";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(500000);
    }
}
