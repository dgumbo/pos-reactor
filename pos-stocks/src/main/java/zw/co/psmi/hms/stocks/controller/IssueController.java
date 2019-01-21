
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
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.entity.CurrentStock;
import zw.co.psmi.hms.stocks.entity.StockMovementDescription;
import zw.co.psmi.hms.stocks.entity.StockTransaction;
import zw.co.psmi.hms.stocks.entity.StockTransactionLine;
import zw.co.psmi.hms.stocks.entity.StockTransactionStatus;
import zw.co.psmi.hms.stocks.entity.StockTransactionType;
import zw.co.psmi.hms.stocks.service.CurrentStockService;
import zw.co.psmi.hms.stocks.service.PreviousTransactionService;
import zw.co.psmi.hms.stocks.service.StockMovementDescriptionService;
import zw.co.psmi.hms.stocks.service.StockTransactionService;
import zw.co.psmi.hms.util.Utils;



@Controller
@Slf4j
public class IssueController {

    private StockTransactionService stockTransactionService;
    
    private CurrentStockService currentStockService;
    private StockMovementDescriptionService stockMovementDescriptionService;
    private UnitService unitService;
    private LocationDao locationDao;
    @Autowired
    private PreviousTransactionService previousTransactionService;

    @Autowired
    public IssueController(StockTransactionService stockTransactionService, CurrentStockService currentStockService, StockMovementDescriptionService stockMovementDescriptionService, UnitService unitService, LocationDao locationDao) {
        this.stockTransactionService = stockTransactionService;
        
        this.currentStockService = currentStockService;
        this.stockMovementDescriptionService = stockMovementDescriptionService;
        this.unitService = unitService;
        this.locationDao = locationDao;
    }
   
    @RequestMapping(value = "/stocks/issue/{stockTransactionType}", method = RequestMethod.GET)
    public String Issue(@AuthenticationPrincipal UserLogin userLogin, RedirectAttributes redirectAttributes,@PathVariable("stockTransactionType") StockTransactionType stockTransactionType, Model model,@ModelAttribute StockTransaction stockTransaction) {
        stockTransaction = stockTransaction == null? new StockTransaction(): stockTransaction;
        String title = stockTransactionType == StockTransactionType.ADJUSTMENT_ISSUE ? "Adjustment Issue" : 
                stockTransactionType == StockTransactionType.DEPARTMENT_ISSUE? "Department Issue"
                :"";
        if(Utils.empty(title)){
            String msg="Menu Error, Please contact IT Support";
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
            return "redirect:/home";
        }
        model.addAttribute("title", title);
        stockTransaction.setUnitFrom(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockTransaction.setStockTransactionStatus(StockTransactionStatus.COMPLETED);      
        stockTransaction.setLocations(unitService.getLocationActive(userLogin.getUnitCode()));
        model.addAttribute("stockTransactionTypes", StockTransactionType.values());
        model.addAttribute("stockTransactions", stockTransactionService.getFromStockTransactionType(stockTransactionType));
        model.addAttribute("stockTransactions", stockTransactionService.findByAllBYSearchCriteria(stockTransactionType,stockTransaction));
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockTransactionType", stockTransactionType);
        return "stocks/issue";
    }

    @RequestMapping(value = "/stocks/issueaction/{id}/{stockTransactionType}", method = RequestMethod.GET)
    public String IssueAction(@AuthenticationPrincipal UserLogin userLogin, RedirectAttributes redirectAttributes, @PathVariable("id") Long id,@PathVariable("stockTransactionType") StockTransactionType stockTransactionType, Model model) {
        
        String title = stockTransactionType == StockTransactionType.ADJUSTMENT_ISSUE ? "Adjustment Issue" : 
                stockTransactionType == StockTransactionType.DEPARTMENT_ISSUE? "Department Issue"
                :"";
        if(Utils.empty(title)){
            String msg="Menu Error, Please contact IT Support";
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
            return "redirect:/stocks/issue/"+stockTransactionType;
        }
        model.addAttribute("stockTransactionType", stockTransactionType);
        model.addAttribute("title", title);        
        StockTransaction stockTransaction = stockTransactionService.getByID(id);
        stockTransaction = (stockTransaction == null ? new StockTransaction() : stockTransaction);

        List<StockMovementDescription> stockMovementDescriptions = stockMovementDescriptionService.findAllActive();
        String unitCode = userLogin.getUnitCode();
        List<Location> locations = unitService.getLocationActive(unitCode);
        List<Department> departments = unitService.getDepartmentActive(unitCode);
        if (id > 0) {
            List<StockTransactionLine> stockTransactionLines = stockTransactionService.findStockTransactionLinesByStockTransaction(stockTransaction);
            stockTransaction.setStockTransactionLines(stockTransactionLines);
        }

        stockTransaction.setUnitFrom(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockTransaction.setUnitTo(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockTransaction.setStockTransactionType(stockTransactionType);        
        stockTransaction.setLocations(locations);
        stockTransaction.setDepartments(departments);
        model.addAttribute("stockTransactionTypes", StockTransactionType.values());
        model.addAttribute("stockTransactionStatuses", StockTransactionStatus.values());
        model.addAttribute("stockTransaction", stockTransaction);
        model.addAttribute("stockTransactionLines", stockTransaction.getStockTransactionLines());
        model.addAttribute("stockMovementDescriptions", stockMovementDescriptions);
        return "stocks/issueaction";
    }

    @RequestMapping(value = "/stocks/issueform", method = RequestMethod.POST)
    public String Issueform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute StockTransaction stockTransaction, Model model, RedirectAttributes redirectAttributes,String title) {
        log.info("postlist:::" + stockTransaction.getStockTransactionLines());
        stockTransaction.setUser(userLogin.getUsername());
       
        
        if(previousTransactionService.pastTransactionComplete(userLogin.getUsername(), stockTransaction.getStockTransactionType())){
            log.info("Too Close, transactions");
        redirectAttributes.addFlashAttribute("msg", "setMsg('Previous Transaction Not yet Complete')");
        redirectAttributes.addFlashAttribute("msg", "setError('Previous Transaction Not yet Complete')");
        redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());
        return "redirect:/stocks/issue/";
        }
        
        Location location  = unitService.getLocationById(stockTransaction.getLocationFrom().getId());
        stockTransaction.setLocationTo(location);
        if(stockTransaction.getDepartment()!=null && stockTransaction.getDepartment().getId()!=0){
            Department department  = unitService.getDepartmentById(stockTransaction.getDepartment().getId());
            stockTransaction.setDepartment(department);
        }
        String msg = stockTransactionService.save(stockTransaction);
        log.info("persistlist:::" + stockTransaction.getStockTransactionLines());
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        redirectAttributes.addFlashAttribute("stockTransactionType", stockTransaction.getStockTransactionType());        
        return "redirect:/stocks/issue/" + stockTransaction.getStockTransactionType().name();        
    }   
    
        
    @RequestMapping(value = "/stocks/currentstocks", method = RequestMethod.GET)
    @ResponseBody
    public List<CurrentStock> findCurrentStockInLocation(@RequestParam(value = "Id", required = true) Long Id) {
        List<CurrentStock> currentStock = currentStockService.findAllAvailableByLocation(unitService.getLocationById(Id));
        return currentStock;
    }
    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setAutoGrowCollectionLimit(500000);
}
}
