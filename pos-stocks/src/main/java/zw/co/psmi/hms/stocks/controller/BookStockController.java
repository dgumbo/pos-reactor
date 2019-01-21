
package zw.co.psmi.hms.stocks.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.dao.LocationDao;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.entity.BookStock;
import zw.co.psmi.hms.stocks.entity.BookStockLine;
import zw.co.psmi.hms.stocks.entity.BookStockLocation;
import zw.co.psmi.hms.stocks.entity.BookStockLockView;
import zw.co.psmi.hms.stocks.entity.StockTransaction;
import zw.co.psmi.hms.stocks.entity.StockTransactionStatus;
import zw.co.psmi.hms.stocks.service.BookStockService;
import zw.co.psmi.hms.stocks.service.CurrentStockService;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.stocks.service.StockCountService;
import zw.co.psmi.hms.stocks.service.StockTransactionService;


@Controller
@Slf4j
public class BookStockController {
@Autowired
    private StockTransactionService stockTransactionService;
    private UnitService unitService;
    private BookStockService bookStockService;
    private GenericStockTypeService genericStockTypeService;
    private LocationDao locationDao;
    private CurrentStockService currentStockService;
    private StockCountService stockCountService;

    @Autowired
    public BookStockController(StockCountService stockCountService,BookStockService bookStockService, GenericStockTypeService genericStockTypeService, UnitService unitService, LocationDao locationDao,CurrentStockService currentStockService) {
        this.unitService = unitService;
        this.bookStockService = bookStockService;
        this.genericStockTypeService = genericStockTypeService;
        this.locationDao = locationDao;
        this.currentStockService = currentStockService;
        this.stockCountService=stockCountService;
    } 
    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setAutoGrowCollectionLimit(500000);
}

    @RequestMapping(value = "/stocks/bookstock", method = RequestMethod.GET)
    public String bookstock(@AuthenticationPrincipal UserLogin userLogin, Model model,@ModelAttribute BookStock bookStock) {   
        bookStock = bookStock == null? new BookStock(): bookStock;
        bookStock.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        model.addAttribute("bookStocks", bookStockService.findByAllBYSearchCriteria(bookStock));
        model.addAttribute("bookStock",bookStock);
        return "stocks/bookstock";
    }
    
    @RequestMapping(value = "/stocks/bookstockcancel", method = RequestMethod.GET)
    public String bookstockCancel(@AuthenticationPrincipal UserLogin userLogin, Model model,@ModelAttribute BookStock bookStock) {   
        bookStock = bookStock == null? new BookStock(): bookStock;
        bookStock.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        model.addAttribute("bookStocks", bookStockService.findByAllBYSearchCriteria(bookStock));
        model.addAttribute("bookStock",bookStock);
        return "stocks/bookstockcancel";
    }
    
    @RequestMapping(value = "/stocks/bookstockcancelaction", method = RequestMethod.POST)
    public String bookstockCancelAction(@AuthenticationPrincipal UserLogin userLogin, 
            @RequestParam("id") Long Id,
            @RequestParam("reason") String reason, RedirectAttributes redirectAttributes) {
        if (Id > 0) {
            BookStock bookStock = bookStockService.getByID(Id);
            if(bookStock.getBookStockStatus() != StockTransactionStatus.CREATED)
                {
                redirectAttributes.addFlashAttribute("msg", "setError('Failed to Cancel Book Stock')");
                return "redirect:/stocks/bookstockcancel";
                }
            bookStock.setBookStockStatus(StockTransactionStatus.REVERSED);
            bookStock.setEndDate(new Date());
            bookStock.setNotes(bookStock.getNotes()+"\n"+reason+"\nCancelled By:"+userLogin.getUsername()+":"+userLogin.getFullname());
            String msg = bookStockService.save(bookStock, null);
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
            return "redirect:/stocks/bookstockcancel";
        }        
         redirectAttributes.addFlashAttribute("msg", "setError('Failed to Cancel Book Stock')");
         return "redirect:/stocks/bookstockcancel";
         
    }    
    @RequestMapping(value = "/stocks/bookstockaction/{Id}", method = RequestMethod.GET)
    public String bookstockAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("Id") Long Id, Model model) {
        BookStock bookStock = bookStockService.getByID(Id);
        bookStock = bookStock==null?new BookStock():bookStock;
        bookStock.setBookStockLocations(bookStockService.getBookStocViewByLocation(currentStockService.getBookStockLockView()));        
        if (Id > 0) {
           List<BookStockLine> lineList = currentStockService.getBookStockLinesByBookStock(bookStock);
           List<BookStockLocation> viewList = bookStockService.getBookStocViewByLocation(
                   currentStockService.getBookStockLockViewByBookStockLines(lineList));
           bookStock.setBookStockLocations(viewList);
        }        
        model.addAttribute("bookStock", bookStock);
        return "stocks/bookstockaction";
    }    

    @RequestMapping(value = "/stocks/bookstockform", method = RequestMethod.POST)
    public String genericStockTypeform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute BookStock bookStock, Model model, RedirectAttributes redirectAttributes) {
        bookStock.setUser(userLogin.getUsername());
        bookStock.setBookStockDate(new Date());
        bookStock.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
    List<BookStockLockView> bookStockLockViews = bookStock.getBookStockLocations().stream().map(l->l.getStocks()).flatMap(l->l.stream()).filter(line-> line.isLock()).collect(Collectors.toList());
       if(bookStockLockViews.size() > 0)
           {
            List<BookStockLine> bookStockLines = bookStockService.getBookStockLinesList(bookStockLockViews);
            bookStock.setBookStockLines(bookStockLines);
            String msg = this.bookStockService.save(bookStock, bookStockLines);
            List<StockTransaction> lockTransactionFromBookStock = stockTransactionService.createTransactionByGroupingLocation(bookStock.getId());
            for(StockTransaction a:lockTransactionFromBookStock){
        stockTransactionService.save(a);
            }
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
           }
        else
           {
           redirectAttributes.addFlashAttribute("msg", "setError('No Stock Generics selected for any location')");
           }
        
        return "redirect:/stocks/bookstock";
    }

    
   @RequestMapping(value = "/stocks/bookstockstockcountedlist", method = RequestMethod.GET)
    public String bookstocklist(@AuthenticationPrincipal UserLogin userLogin, Model model,@ModelAttribute BookStock bookStock) {   
        bookStock = bookStock == null? new BookStock(): bookStock;
        bookStock.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        model.addAttribute("bookStocks", bookStockService.findByAllBySearchCriteriaAndStockCounted(bookStock));
        model.addAttribute("bookStock",bookStock);
        return "stocks/bookstockstockcountedlist";
    }
    
}
