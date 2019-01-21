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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.entity.Location;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.dao.StockRequestItemViewDao;
import zw.co.psmi.hms.stocks.dao.StockTransactionLineDao;
import zw.co.psmi.hms.stocks.entity.ApprovalStatus;
import zw.co.psmi.hms.stocks.entity.CurrentStock;
import zw.co.psmi.hms.stocks.entity.StockMovementDescription;
import zw.co.psmi.hms.stocks.entity.StockRequest;
import zw.co.psmi.hms.stocks.entity.StockRequestItem;
import zw.co.psmi.hms.stocks.entity.StockRequestItemView;
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
import zw.co.psmi.hms.util.Utils;

@Controller
@Slf4j
public class StockIssueController {

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
    public StockIssueController(StockTransactionService stockTransactionService, CurrentStockService currentStockService, StockMovementDescriptionService stockMovementDescriptionService, StockSupplierService stockSupplierService, UnitService unitService, GenericStockTypeService genericStockTypeService, StockRequestService stockRequestService) {
        this.stockTransactionService = stockTransactionService;

        this.currentStockService = currentStockService;
        this.stockMovementDescriptionService = stockMovementDescriptionService;
        this.stockSupplierService = stockSupplierService;
        this.unitService = unitService;
        this.genericStockTypeService = genericStockTypeService;
        this.stockRequestService = stockRequestService;
    }

    @RequestMapping(value = "/stocks/stockissue", method = RequestMethod.GET)
    public String stockIssue(Model model, @AuthenticationPrincipal UserLogin userLogin, @ModelAttribute StockTransaction stockTransaction) {
        stockTransaction = stockTransaction == null ? new StockTransaction() : stockTransaction;
        stockTransaction.setUnitTo(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockTransaction.setLocations(unitService.getLocationActive(userLogin.getUnitCode()));
        stockTransaction.setStockTransactionStatus(StockTransactionStatus.COMPLETED);
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockTransactions", stockTransactionService.findByAllBYSearchCriteria(StockTransactionType.STOCK_ISSUE, stockTransaction));
        return "stocks/stockissue";
    }

//    @RequestMapping(value = "/stocks/stockissueaction/{id}/{stockRequestId}", method = RequestMethod.GET)
//    public String stockIssueAction(@AuthenticationPrincipal UserLogin userLogin,
//            @PathVariable("id") Long id, @PathVariable("stockRequestId") long stockRequestId,
//            Model model, RedirectAttributes redirectAttributes) {
//        return stockIssueActionLocalMain(userLogin, id, stockRequestId, model, redirectAttributes);
//    }
    @RequestMapping(value = "/stocks/stockissueaction/{id}/{stockRequestId}", method = RequestMethod.GET)
    public String stockIssueActionLocation(@AuthenticationPrincipal UserLogin userLogin,
            @PathVariable("id") Long id, 
            @PathVariable("stockRequestId") long stockRequestId,
            @RequestParam(name = "locationId",required = false) Long locationId,
            Model model, RedirectAttributes redirectAttributes) {
        Location location=(locationId==null)?null:unitService.getLocationById(locationId);
        StockTransaction stockTransaction = stockTransactionService.getByID(id);
        stockTransaction = (stockTransaction == null ? new StockTransaction() : stockTransaction);
        StockRequest stockRequest;
        if (id > 0) {
            List<StockTransactionLine> stockTransactionLines = stockTransactionService.findStockTransactionLinesByStockTransaction(stockTransaction);
            stockTransaction.setStockTransactionLines(stockTransactionLines);
            stockRequestId = stockTransaction.getStockRequest().getId();
            stockRequest = stockTransaction.getStockRequest();            
        }else{
            stockRequest = stockRequestService.getByID(stockRequestId);            
        }
        if (stockRequestId > 0) {
            if (stockRequest.getApprovalStatus() == ApprovalStatus.CREATED || stockRequest.getApprovalStatus() == ApprovalStatus.PENDING) {
                redirectAttributes.addFlashAttribute("msg", "setMsg('StockRequest not yet fully approved')");
                return "redirect:/stocks/stockissue/";
            }
            if (stockRequest.getApprovalStatus() == ApprovalStatus.COMPLETED && id == 0) {
                redirectAttributes.addFlashAttribute("msg", "setMsg('StockRequest already issued out')");
                return "redirect:/stocks/stockissue/";
            }
            if(location!=null){
                stockRequest.setIssueLocation(location);
            }
            stockTransaction.setLocationFrom(stockRequest.getIssueLocation());
            stockTransaction.setUnitFrom(stockRequest.getIssueUnit());
            stockTransaction.setUnitTo(stockRequest.getRequestUnit());
            stockTransaction.setLocationTo(stockRequest.getLocation());
            stockTransaction.setStockRequest(stockRequest);
            stockTransaction.setStockTransactionType(StockTransactionType.STOCK_ISSUE);
            List<StockRequestItem> stockRequestItems = new ArrayList<>();            
            stockRequestService.processStockRequestItems(stockRequest, stockRequestItems);
            stockTransaction.setStockRequestItems(stockRequestItems);
        }
        return stockIssueActionLocal(userLogin, stockTransaction, model);
    }
    private String stockIssueActionLocal(UserLogin userLogin, StockTransaction stockTransaction, Model model) {
        List<StockMovementDescription> stockMovementDescriptions = stockMovementDescriptionService.findAllActive();
        List<Location> locations = unitService.getLocationActive(userLogin.getUnitCode());

        List<StockType> stockTypes = new ArrayList<>();//genericStockTypeService.getStockTypeActive();
        model.addAttribute("stockTransactionTypes", StockTransactionType.values());
        model.addAttribute("stockTransactionStatuses", StockTransactionStatus.values());
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockMovementDescriptions", stockMovementDescriptions);
        model.addAttribute("stockStatuses", Arrays.asList(StockStatus.values()).stream().filter(s -> s != StockStatus.MERGED && s != StockStatus.SPLIT).collect(Collectors.toList()));
        model.addAttribute("stockTypes", stockTypes);
        model.addAttribute("locations", locations);
        model.addAttribute("stockRequestId", stockTransaction.getStockRequest().getId());

        return "stocks/stockissueaction";
    }

    @RequestMapping(value = "/stocks/stockissueform", method = RequestMethod.POST)
    public String stockIssueForm(@AuthenticationPrincipal UserLogin userLogin,
            @ModelAttribute StockTransaction stockTransaction,
            Model model, RedirectAttributes redirectAttributes) {
        stockTransaction.setUser(userLogin.getUsername());
        if(previousTransactionService.pastTransactionComplete(userLogin.getUsername(), stockTransaction.getStockTransactionType())){
            log.info("Too Close, transactions");
        redirectAttributes.addFlashAttribute("msg", "setMsg('Previous Transaction Not yet Complete')");
        redirectAttributes.addFlashAttribute("msg", "setError('Previous Transaction Not yet Complete')");
        redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());
        return "redirect:/stocks/stockissue/";
        }
        stockTransaction.setStockTransactionType(StockTransactionType.STOCK_ISSUE);
        String msg = stockTransactionService.save(stockTransaction);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());
        return "redirect:/stocks/stockissue/";
    }

    @RequestMapping(value = "/stocks/stockreceivelist", method = RequestMethod.GET)
    public String stockreceievelist(Model model, @AuthenticationPrincipal UserLogin userLogin, @ModelAttribute StockTransaction stockTransaction) {
        stockTransaction = stockTransaction == null ? new StockTransaction() : stockTransaction;
        stockTransaction.setUnitFrom(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockTransaction.setLocations(unitService.getLocationActive(userLogin.getUnitCode()));
        stockTransaction.setStockTransactionStatus(StockTransactionStatus.PENDING);
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockTransactions", stockTransactionService.findByAllBYSearchCriteria(StockTransactionType.STOCK_ISSUE, stockTransaction));
        return "stocks/stockreceivelist";
    }
@RequestMapping(value = "/stocks/getsubstitute", method = RequestMethod.GET)
    public String getsubstitute(Model model, @AuthenticationPrincipal UserLogin userLogin, @RequestParam("index") Long index, @RequestParam("id") Long id, @RequestParam("location") Long location) {
        List<CurrentStock> currentStocks = currentStockService.findAllAvailableByLocation(unitService.getLocationById(location));
        model.addAttribute("currentStocks", currentStocks);
        model.addAttribute("index", index);
        return "stocks/getsubstitute";
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(500000);
    }
}
