/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.psmi.hms.stocks.controller;

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
import zw.co.psmi.hms.stocks.dao.StockTransactionLineDao;
import zw.co.psmi.hms.stocks.entity.ApprovalStatus;
import zw.co.psmi.hms.stocks.entity.StockMovementDescription;
import zw.co.psmi.hms.stocks.entity.StockRequest;
import zw.co.psmi.hms.stocks.entity.StockRequestItem;
import zw.co.psmi.hms.stocks.entity.StockStatus;
import zw.co.psmi.hms.stocks.entity.StockTransaction;
import zw.co.psmi.hms.stocks.entity.StockTransactionAdditionalCostType;
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

/**
 *
 * @author kinah
 */

@Controller
@Slf4j
public class GoodsReceivedController {
  

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
    public GoodsReceivedController(StockTransactionService stockTransactionService, CurrentStockService currentStockService, StockMovementDescriptionService stockMovementDescriptionService, StockSupplierService stockSupplierService, UnitService unitService, GenericStockTypeService genericStockTypeService,  StockRequestService stockRequestService) {
        this.stockTransactionService = stockTransactionService;
        this.currentStockService = currentStockService;
        this.stockMovementDescriptionService = stockMovementDescriptionService;
        this.stockSupplierService = stockSupplierService;
        this.unitService = unitService;
        this.genericStockTypeService = genericStockTypeService;
        this.stockRequestService = stockRequestService;
    }
  
    @RequestMapping(value = "/stocks/goodsreceived", method = RequestMethod.GET)
    public String goodsReceived( @AuthenticationPrincipal UserLogin userLogin, @ModelAttribute StockTransaction stockTransaction, Model model) {
        model.addAttribute("stockTransactionTypes", StockTransactionType.values());
        List<Location> locations = unitService.getLocationActive(userLogin.getUnitCode());
        stockTransaction = stockTransaction == null ? new StockTransaction():stockTransaction;
        stockTransaction.setStockTransactionType(StockTransactionType.GOODS_RECEIVED_VOUCHER);
        stockTransaction.setUnitTo(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockTransaction.setLocations(locations);
        model.addAttribute("stockTransaction", stockTransaction);
        List<StockTransaction> stockTransactions = stockTransactionService.findByAllBYSearchCriteria(StockTransactionType.GOODS_RECEIVED_VOUCHER, stockTransaction);
        model.addAttribute("stockTransactions", stockTransactions);
        return "stocks/goodsreceived";
    }
        
    @RequestMapping(value = "/stocks/goodsreceivedaction/{id}/{stockRequestId}", method = RequestMethod.GET)
    public String goodsReceivedAction(@AuthenticationPrincipal UserLogin userLogin, 
            @PathVariable("id") Long id, @PathVariable("stockRequestId") long stockRequestId, 
            Model model, RedirectAttributes redirectAttributes) {
        StockTransaction stockTransaction = stockTransactionService.getByID(id);
        stockTransaction = (stockTransaction == null ? new StockTransaction() : stockTransaction);
        List<StockMovementDescription> stockMovementDescriptions = stockMovementDescriptionService.findAllActive();
        List<Location> locations = unitService.getLocationActive(userLogin.getUnitCode());
        if (id > 0) {
            List<StockTransactionLine> stockTransactionLines = stockTransactionService.findStockTransactionLinesByStockTransaction(stockTransaction);
            stockTransaction.setStockTransactionLines(stockTransactionLines);
            stockRequestId=stockTransaction.getStockRequest().getId();
        }
        if(stockRequestId > 0)
           {
               System.out.println(".............................Part 1 "+stockRequestId);
            StockRequest stockRequest = stockRequestService.getByID(stockRequestId);
           List<StockRequestItem> stockRequestItems = stockTransactionService.getRequesitionRequestItems(stockRequest,id==0);
            stockTransaction.setStockRequestItems(stockRequestItems); 
            if(id==0&&stockRequest.getApprovalStatus()!=ApprovalStatus.ACCEPTED){
                redirectAttributes.addFlashAttribute("msg", "setError('Your Requisition has not been Approved')");
        return "redirect:/stocks/goodsreceived/";
            }
            System.out.println(".............................Part 2 "+stockRequest.getId());
           stockTransaction.setLocationTo(stockRequest.getIssueLocation());
           stockTransaction.setUnitFrom(stockRequest.getRequestUnit());
           stockTransaction.setLocationFrom(stockRequest.getLocation()); 
           stockTransaction.setStockRequest(stockRequest);
           System.out.println(".............................Part 3 "+stockTransaction.getStockRequest().getId());
           stockTransaction.setUnitTo(stockRequest.getIssueUnit());
           stockTransaction.setStockTransactionType(StockTransactionType.GOODS_RECEIVED_VOUCHER);   
           stockTransaction.setStockSuppliers(stockSupplierService.findAllActive());
            stockTransaction.setAdditionalCostTypes(Arrays.asList(StockTransactionAdditionalCostType.values()));
           }
           System.out.println(".............................Part 4 "+stockTransaction.getStockRequest().getId());
        //List<StockType> stockTypes = genericStockTypeService.getStockTypeActive();
        model.addAttribute("stockTransactionTypes", StockTransactionType.values());
        model.addAttribute("stockTransactionStatuses", StockTransactionStatus.values());
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockMovementDescriptions", stockMovementDescriptions);
        model.addAttribute("stockStatuses", Arrays.asList(StockStatus.values()).stream().filter(s->s!=StockStatus.MERGED&&s!=StockStatus.SPLIT).collect(Collectors.toList()));
        //model.addAttribute("stockTypes", stockTypes);
        model.addAttribute("locations", locations);
        model.addAttribute("stockRequestId", stockRequestId);
        
        return "stocks/goodsreceivedaction";
    }
    
    @RequestMapping(value = "/stocks/goodsreceivedform", method = RequestMethod.POST)
    public String goodsReceivedForm(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute StockTransaction stockTransaction, Model model, RedirectAttributes redirectAttributes) {
        stockTransaction.setUser(userLogin.getUsername());  
        
        if(previousTransactionService.pastTransactionComplete(userLogin.getUsername(), stockTransaction.getStockTransactionType())){
            log.info("Too Close, transactions");
        redirectAttributes.addFlashAttribute("msg", "setMsg('Previous Transaction Not yet Complete')");
        redirectAttributes.addFlashAttribute("msg", "setError('Previous Transaction Not yet Complete')");
        redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());
        return "redirect:/stocks/goodsreceived/";
        }
        stockTransaction.setStockRequest(stockRequestService.getByID(stockTransaction.getStockRequest().getId()));
        String msg=stockTransactionService.verifyGrv(stockTransaction);
        if(!Utils.empty(msg)){
        redirectAttributes.addFlashAttribute("msg", "setError('" + msg + "')");
        redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());
        return "redirect:/stocks/goodsreceivedaction/0/"+stockTransaction.getStockRequest().getId();
        }
        msg =Utils.empty(msg)? stockTransactionService.save(stockTransaction):msg;
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());
        return "redirect:/stocks/goodsreceived/";
    }  
    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setAutoGrowCollectionLimit(500000);
}
}
