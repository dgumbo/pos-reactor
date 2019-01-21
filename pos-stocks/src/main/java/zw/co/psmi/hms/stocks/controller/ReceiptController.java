package zw.co.psmi.hms.stocks.controller;

import ch.qos.logback.classic.pattern.Util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.entity.Location;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.entity.StockMovementDescription;
import zw.co.psmi.hms.stocks.entity.StockStatus;
import zw.co.psmi.hms.stocks.entity.StockTransaction;
import zw.co.psmi.hms.stocks.entity.StockTransactionLine;
import zw.co.psmi.hms.stocks.entity.StockTransactionStatus;
import zw.co.psmi.hms.stocks.entity.StockTransactionType;
import zw.co.psmi.hms.stocks.entity.StockType;
import zw.co.psmi.hms.stocks.txn.service.CostAdjustmentService;
import zw.co.psmi.hms.stocks.service.CurrentStockService;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.stocks.service.PreviousTransactionService;
import zw.co.psmi.hms.stocks.service.StockMovementDescriptionService;
import zw.co.psmi.hms.stocks.service.StockSupplierService;

import zw.co.psmi.hms.stocks.service.StockTransactionService;
import zw.co.psmi.hms.util.Utils;

@Controller
@Slf4j
public class ReceiptController {

    private StockTransactionService stockTransactionService;

    private CurrentStockService currentStockService;
    private StockMovementDescriptionService stockMovementDescriptionService;
    private StockSupplierService stockSupplierService;
    private UnitService unitService;
    private GenericStockTypeService genericStockTypeService;
    @Autowired
    private CostAdjustmentService costAdjustmentService;
    @Autowired
    private PreviousTransactionService previousTransactionService;

    @Autowired
    public ReceiptController(StockTransactionService stockTransactionService, CurrentStockService currentStockService, StockMovementDescriptionService stockMovementDescriptionService, StockSupplierService stockSupplierService, UnitService unitService, GenericStockTypeService genericStockTypeService) {
        this.stockTransactionService = stockTransactionService;

        this.currentStockService = currentStockService;
        this.stockMovementDescriptionService = stockMovementDescriptionService;
        this.stockSupplierService = stockSupplierService;
        this.unitService = unitService;
        this.genericStockTypeService = genericStockTypeService;
    }

    @RequestMapping(value = "/stocks/receiptall/{stockTransactionType}", method = RequestMethod.GET)
    public String adjustmentReceiptAll(@PathVariable("stockTransactionType") StockTransactionType stockTransactionType, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute StockTransaction stockTransaction) {
        stockTransaction = stockTransaction == null ? new StockTransaction() : stockTransaction;
        String title = stockTransactionType == StockTransactionType.ADJUSTMENT_RECEIPT ? "Adjustment receipt"
                : stockTransactionType == StockTransactionType.CASH_PURCHASE ? "Cash Purchase"
                        : "";
        if (Utils.empty(title)) {
            String msg = "Menu Error, Please contact IT Support";
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
            return "redirect:/home";
        }
//        stockTransaction.setUnitTo(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockTransaction.setStockTransactionStatus(StockTransactionStatus.COMPLETED);
        stockTransaction.setLocations(unitService.getLocationActive(userLogin.getUnitCode()));
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockTransactionType", stockTransactionType);
        model.addAttribute("units", unitService.getActive());
        model.addAttribute("title", title);
        model.addAttribute("stockTransactionTypes", StockTransactionType.values());
        model.addAttribute("stockTransactions", stockTransactionService.findByAllBYSearchCriteria("",stockTransactionType, stockTransaction));

        return "stocks/receiptall";
    }
    @RequestMapping(value = "/stocks/receipt/{stockTransactionType}", method = RequestMethod.GET)
    public String adjustmentReceipt(@PathVariable("stockTransactionType") StockTransactionType stockTransactionType, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute StockTransaction stockTransaction) {
        stockTransaction = stockTransaction == null ? new StockTransaction() : stockTransaction;
        String title = stockTransactionType == StockTransactionType.ADJUSTMENT_RECEIPT ? "Adjustment receipt"
                : stockTransactionType == StockTransactionType.CASH_PURCHASE ? "Cash Purchase"
                        : "";
        if (Utils.empty(title)) {
            String msg = "Menu Error, Please contact IT Support";
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
            return "redirect:/home";
        }
        stockTransaction.setUnitTo(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockTransaction.setStockTransactionStatus(StockTransactionStatus.COMPLETED);
        stockTransaction.setLocations(unitService.getLocationActive(userLogin.getUnitCode()));
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockTransactionType", stockTransactionType);
        model.addAttribute("title", title);
        model.addAttribute("stockTransactionTypes", StockTransactionType.values());
        model.addAttribute("stockTransactions", stockTransactionService.findByAllBYSearchCriteria(stockTransactionType, stockTransaction));

        return "stocks/receipt";
    }

    @RequestMapping(value = "/stocks/costadjustment", method = RequestMethod.GET)
    public String costAdjustment(RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute StockTransaction stockTransaction) {
        stockTransaction = stockTransaction == null ? new StockTransaction() : stockTransaction;
        StockTransactionType stockTransactionType = StockTransactionType.COST_ADJUSTMENT;
        String title = "Cost Adjustment";
        stockTransaction.setUnitTo(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockTransaction.setStockTransactionStatus(StockTransactionStatus.COMPLETED);
        stockTransaction.setLocations(unitService.getLocationActive(userLogin.getUnitCode()));
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockTransactionType", stockTransactionType);
        model.addAttribute("title", title);
        model.addAttribute("stockTransactionTypes", StockTransactionType.values());
        model.addAttribute("stockTransactions", stockTransactionService.findByAllBYSearchCriteria(stockTransactionType, stockTransaction));
        return "stocks/costadjustment";
    }

    @RequestMapping(value = "/stocks/costadjustmentaction/{id}", method = RequestMethod.GET)
    public String costAdjustmentAction(@AuthenticationPrincipal UserLogin userLogin, RedirectAttributes redirectAttributes, @PathVariable("id") Long id, Model model) {
        String title = "Cost Adjustment";
        model.addAttribute("stockTransactionType", StockTransactionType.COST_ADJUSTMENT);
        model.addAttribute("title", title);
        StockTransaction stockTransaction = stockTransactionService.getByID(id);
        stockTransaction = (stockTransaction == null ? new StockTransaction() : stockTransaction);
        stockTransaction.setStockTransactionType(StockTransactionType.COST_ADJUSTMENT);
        List<StockMovementDescription> stockMovementDescriptions = stockMovementDescriptionService.findAllActive();
        List<Location> locations = unitService.getLocationActive(userLogin.getUnitCode());
        if (stockTransaction.getId() > 0) {
            List<StockTransactionLine> stockTransactionLines = stockTransactionService.findStockTransactionLinesByStockTransaction(stockTransaction);
            stockTransaction.setStockTransactionLines(stockTransactionLines);
        }
        stockTransaction.setLocationFrom(locations.get(0));
        stockTransaction.setUnitFrom(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockTransaction.setUnitTo(unitService.getByUnitCode(userLogin.getUnitCode()));
        List<StockType> stockTypes = new ArrayList<>();//genericStockTypeService.getStockTypeActive();
        model.addAttribute("stockTransactionTypes", StockTransactionType.values());
        model.addAttribute("stockTransactionStatuses", StockTransactionStatus.values());
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockMovementDescriptions", stockMovementDescriptions);
        model.addAttribute("stockStatuses", Arrays.asList(StockStatus.values()).stream().filter(s -> s != StockStatus.MERGED && s != StockStatus.SPLIT).collect(Collectors.toList()));
        model.addAttribute("stockTypes", stockTypes);
        model.addAttribute("locations", locations);
        return "stocks/costadjustmentaction";
    }

    @RequestMapping(value = "/stocks/receiptaction/{id}/{stockTransactionType}", method = RequestMethod.GET)
    public String adjustmentReceiptAction(@AuthenticationPrincipal UserLogin userLogin, RedirectAttributes redirectAttributes, @PathVariable("id") Long id, @PathVariable("stockTransactionType") StockTransactionType stockTransactionType, Model model) {
        String title = stockTransactionType == StockTransactionType.ADJUSTMENT_RECEIPT ? "Adjustment receipt"
                : stockTransactionType == StockTransactionType.CASH_PURCHASE ? "Cash Purchase"
                        : "";
        if (Utils.empty(title)) {
            String msg = "Menu Error, Please contact IT Support";
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
            return "redirect://stocks/receipt/" + stockTransactionType;
        }
        model.addAttribute("stockTransactionType", stockTransactionType);
        model.addAttribute("title", title);
        StockTransaction stockTransaction = stockTransactionService.getByID(id);
        stockTransaction = (stockTransaction == null ? new StockTransaction() : stockTransaction);
        stockTransaction.setStockTransactionType(stockTransactionType);
        return adjustmentReceiptActionLocal(userLogin, stockTransaction, model);
    }

    @RequestMapping(value = "/stocks/receiptallaction/{id}/{stockTransactionType}", method = RequestMethod.GET)
    public String adjustmentAllReceiptAction(@AuthenticationPrincipal UserLogin userLogin, RedirectAttributes redirectAttributes, @PathVariable("id") Long id, @PathVariable("stockTransactionType") StockTransactionType stockTransactionType, Model model) {
        String title = stockTransactionType == StockTransactionType.ADJUSTMENT_RECEIPT ? "Adjustment receipt"
                : stockTransactionType == StockTransactionType.CASH_PURCHASE ? "Cash Purchase"
                        : "";
        if (Utils.empty(title)) {
            String msg = "Menu Error, Please contact IT Support";
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
            return "redirect://stocks/receiptall/" + stockTransactionType;
        }
        model.addAttribute("stockTransactionType", stockTransactionType);
        model.addAttribute("title", title);
        StockTransaction stockTransaction = stockTransactionService.getByID(id);
        stockTransaction = (stockTransaction == null ? new StockTransaction() : stockTransaction);
        stockTransaction.setStockTransactionType(stockTransactionType);
        if (stockTransaction.getStockTransactionType() == StockTransactionType.CASH_PURCHASE) {
            stockTransaction.setStockSuppliers(stockSupplierService.findAllActive());
        }
        List<StockMovementDescription> stockMovementDescriptions = stockMovementDescriptionService.findAllActive();
        List<Location> locations = unitService.getLocationActive(userLogin.getUnitCode());
        if (stockTransaction.getId() > 0) {
            List<StockTransactionLine> stockTransactionLines = stockTransactionService.findStockTransactionLinesByStockTransaction(stockTransaction);
            stockTransaction.setStockTransactionLines(stockTransactionLines);
        }
        stockTransaction.setLocationFrom(locations.get(0));
        stockTransaction.setUnitFrom(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockTransaction.setUnitTo(unitService.getByUnitCode(userLogin.getUnitCode()));
        List<StockType> stockTypes = new ArrayList<>();//genericStockTypeService.getStockTypeActive();
        model.addAttribute("stockTransactionTypes", StockTransactionType.values());
        model.addAttribute("stockTransactionStatuses", StockTransactionStatus.values());
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockMovementDescriptions", stockMovementDescriptions);
        model.addAttribute("stockStatuses", Arrays.asList(StockStatus.values()).stream().filter(s -> s != StockStatus.MERGED && s != StockStatus.SPLIT).collect(Collectors.toList()));
        model.addAttribute("stockTypes", stockTypes);
        model.addAttribute("locations", locations);
        return "stocks/receiptallaction";
    }

    private String adjustmentReceiptActionLocal(UserLogin userLogin, StockTransaction stockTransaction, Model model) {
        if (stockTransaction.getStockTransactionType() == StockTransactionType.CASH_PURCHASE) {
            stockTransaction.setStockSuppliers(stockSupplierService.findAllActive());
        }
        List<StockMovementDescription> stockMovementDescriptions = stockMovementDescriptionService.findAllActive();
        List<Location> locations = unitService.getLocationActive(userLogin.getUnitCode());
        if (stockTransaction.getId() > 0) {
            List<StockTransactionLine> stockTransactionLines = stockTransactionService.findStockTransactionLinesByStockTransaction(stockTransaction);
            stockTransaction.setStockTransactionLines(stockTransactionLines);
        }
        stockTransaction.setLocationFrom(locations.get(0));
        stockTransaction.setUnitFrom(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockTransaction.setUnitTo(unitService.getByUnitCode(userLogin.getUnitCode()));
        List<StockType> stockTypes = new ArrayList<>();//genericStockTypeService.getStockTypeActive();
        model.addAttribute("stockTransactionTypes", StockTransactionType.values());
        model.addAttribute("stockTransactionStatuses", StockTransactionStatus.values());
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockMovementDescriptions", stockMovementDescriptions);
        model.addAttribute("stockStatuses", Arrays.asList(StockStatus.values()).stream().filter(s -> s != StockStatus.MERGED && s != StockStatus.SPLIT).collect(Collectors.toList()));
        model.addAttribute("stockTypes", stockTypes);
        model.addAttribute("locations", locations);
        return "stocks/receiptaction";
    }

    @RequestMapping(value = "/stocks/receiptform", method = RequestMethod.POST)
    public String adjustmentReceiptForm(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute StockTransaction stockTransaction, Model model, RedirectAttributes redirectAttributes) {
        stockTransaction.setUser(userLogin.getUsername());
        if(previousTransactionService.pastTransactionComplete(userLogin.getUsername(), stockTransaction.getStockTransactionType())){
            log.info("Too Close, transactions");
        redirectAttributes.addFlashAttribute("msg", "setMsg('Previous Transaction Not yet Complete')");
        redirectAttributes.addFlashAttribute("msg", "setError('Previous Transaction Not yet Complete')");
        redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());
        return "redirect:/stocks/receipt/" + stockTransaction.getStockTransactionType().name();
        }
        if (stockTransaction.getStockTransactionType() == StockTransactionType.COST_ADJUSTMENT) {
            String msg = costAdjustmentService.save(stockTransaction);
            log.info("tran tyep ***************************" + stockTransaction.getStockTransactionType());
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
            redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());
            return "redirect:/stocks/costadjustment";
        }
        String msg = stockTransactionService.save(stockTransaction);
        log.info("tran tyep ***************************" + stockTransaction.getStockTransactionType());
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());
        return "redirect:/stocks/receipt/" + stockTransaction.getStockTransactionType().name();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(500000);
    }
}
