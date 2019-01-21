
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.dao.LocationDao;
import zw.co.psmi.hms.authentication.entity.Department;
import zw.co.psmi.hms.authentication.entity.Location;
import zw.co.psmi.hms.authentication.entity.Unit;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.basic.HmsException;

import zw.co.psmi.hms.stocks.entity.CurrentStock;
import zw.co.psmi.hms.stocks.entity.StockMovementDescription;
import zw.co.psmi.hms.stocks.entity.StockTransaction;
import zw.co.psmi.hms.stocks.entity.StockTransactionLine;
import zw.co.psmi.hms.stocks.entity.StockTransactionStatus;
import zw.co.psmi.hms.stocks.entity.StockTransactionType;
import zw.co.psmi.hms.stocks.entity.StockType;
import zw.co.psmi.hms.stocks.service.CurrentStockService;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.stocks.service.PreviousTransactionService;
import zw.co.psmi.hms.stocks.service.StockMovementDescriptionService;

import zw.co.psmi.hms.stocks.service.StockTransactionService;


@Controller
@Slf4j

public class StockConversionController {

    private StockTransactionService stockTransactionService;
    
    private CurrentStockService currentStockService;
    private StockMovementDescriptionService stockMovementDescriptionService;
    private UnitService unitService;
    private LocationDao locationDao;
    
    @Autowired
    private GenericStockTypeService genericStockTypeService;
    @Autowired
    private PreviousTransactionService previousTransactionService;

    @Autowired
    public StockConversionController(StockTransactionService stockTransactionService, CurrentStockService currentStockService, StockMovementDescriptionService stockMovementDescriptionService, UnitService unitService, LocationDao locationDao) {
        this.stockTransactionService = stockTransactionService;
        
        this.currentStockService = currentStockService;
        this.stockMovementDescriptionService = stockMovementDescriptionService;
        this.unitService = unitService;
        this.locationDao = locationDao;
        
    }
   
    @RequestMapping(value = "/stocks/conversion", method = RequestMethod.GET)
    public String conversion(@AuthenticationPrincipal UserLogin userLogin, Model model,@ModelAttribute StockTransaction stockTransaction) {
        stockTransaction = stockTransaction == null? new StockTransaction(): stockTransaction;  
        stockTransaction.setUnitFrom(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockTransaction.setStockTransactionStatus(StockTransactionStatus.COMPLETED);
        model.addAttribute("title", "Stock Conversion");
        model.addAttribute("stockTransactionTypes", StockTransactionType.values());
        model.addAttribute("stockTransactions", stockTransactionService.getFromStockTransactionType(StockTransactionType.STOCK_CONVERSION));
        model.addAttribute("stockTransactions", stockTransactionService.findByAllBYSearchCriteria(StockTransactionType.STOCK_CONVERSION,stockTransaction));
        model.addAttribute("stockTransaction", stockTransaction);
        return "stocks/conversion"; 
    }

    private String conversionActionLocal(UserLogin userLogin, StockTransaction stockTransaction, Model model) {
        
        model.addAttribute("stockTransactionType", StockTransactionType.STOCK_CONVERSION);     
        model.addAttribute("title", "Stock Conversion");

        List<StockMovementDescription> stockMovementDescriptions = stockMovementDescriptionService.findAllActive();
        String unitCode = userLogin.getUnitCode();
        List<Location> locations = unitService.getLocationActive(unitCode);
        List<Department> departments = unitService.getDepartmentActive(unitCode);
        if (stockTransaction.getId() > 0) {
            List<StockTransactionLine> stockTransactionLines = stockTransactionService.findStockTransactionLinesByStockTransaction(stockTransaction);
            stockTransaction.setStockTransactionLines(stockTransactionLines);
        }
        stockTransaction.setDepartments(departments);
        stockTransaction.setLocations(locations);
        model.addAttribute("stockTypes", StockTransactionType.values());
        model.addAttribute("stockTransactionStatuses", StockTransactionStatus.values());
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockTransactionLines", stockTransaction.getStockTransactionLines());
        model.addAttribute("stockMovementDescriptions", stockMovementDescriptions);
        return "stocks/conversionaction";
    }
   
    
    @RequestMapping(value = "/stocks/conversionaction/{id}", method = RequestMethod.GET)
    public String conversionAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("id") Long id, Model model) {
        StockTransaction stockTransaction = stockTransactionService.getByID(id);
        stockTransaction = (stockTransaction == null ? new StockTransaction() : stockTransaction);
        return conversionActionLocal(userLogin, stockTransaction, model);
    }

    @RequestMapping(value = "/stocks/conversionform", method = RequestMethod.POST)
    public String conversionform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute StockTransaction stockTransaction, Model model, RedirectAttributes redirectAttributes) {
        if(previousTransactionService.pastTransactionComplete(userLogin.getUsername(), stockTransaction.getStockTransactionType())){
            log.info("Too Close, transactions");
        redirectAttributes.addFlashAttribute("msg", "setMsg('Previous Transaction Not yet Complete')");
        redirectAttributes.addFlashAttribute("msg", "setError('Previous Transaction Not yet Complete')");
        redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());
        return "redirect:/stocks/conversion";
        }
        log.info("postlist:::" + stockTransaction.getStockTransactionLines());
        stockTransaction.setStockTransactionType(StockTransactionType.STOCK_CONVERSION);
        stockTransaction.setUser(userLogin.getUsername());
        stockTransaction.setStockTransactionStatus(StockTransactionStatus.COMPLETED);
        String unitCode = userLogin.getUnitCode();
        Unit unit  = unitService.getByUnitCode(unitCode);
        stockTransaction.setUnitFrom(unit);
        stockTransaction.setUnitTo(unit);
        Location location  = unitService.getLocationById(stockTransaction.getLocationFrom().getId());
        stockTransaction.setLocationTo(location);
        if(stockTransaction.getDepartment()!=null && stockTransaction.getDepartment().getId()!=0){
            Department department  = unitService.getDepartmentById(stockTransaction.getDepartment().getId());
            stockTransaction.setDepartment(department);
        }
        String msg="";
        try{
         msg = stockTransactionService.save(stockTransaction);
        }catch(HmsException e){
        msg=e.getMessage();
        }
        log.info("persistlist:::" + stockTransaction.getStockTransactionLines());
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        redirectAttributes.addFlashAttribute("error", "setMsg('" + msg + "')");
        return "redirect:/stocks/conversion";
    }   
    
    @RequestMapping(value = "/stocks/stocktypeslist", method = RequestMethod.GET)
    @ResponseBody
    public List<StockType> findStockTypeForCurrentStockConversion(@RequestParam(value = "id", required = true) Long id) {
        CurrentStock currentStock=currentStockService.getByID(id);
        List<StockType> stockTypes = genericStockTypeService.findStockTypeForConversion(currentStock.getStockType());
        return stockTypes;
    }
       
    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setAutoGrowCollectionLimit(500000);
}
}
