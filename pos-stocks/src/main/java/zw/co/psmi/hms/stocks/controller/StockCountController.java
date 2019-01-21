
package zw.co.psmi.hms.stocks.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
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
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.MenuGroupService;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.entity.BookStock;
import zw.co.psmi.hms.stocks.entity.CurrentStock;
import zw.co.psmi.hms.stocks.entity.StockCount;
import zw.co.psmi.hms.stocks.entity.StockCountLine;
import zw.co.psmi.hms.stocks.service.BookStockService;
import zw.co.psmi.hms.stocks.service.CurrentStockService;
import zw.co.psmi.hms.stocks.service.StockCountService;
import zw.co.psmi.hms.util.Utils;


@Controller
@Slf4j
public class StockCountController {
 
    private MenuGroupService menuGroupService; 
    private StockCountService stockCountService; 
    private BookStockService bookStockService;
    private CurrentStockService currentStockService;
    private UnitService unitService;
    //StockCount

    @Autowired
     public StockCountController(MenuGroupService menuGroupService, StockCountService stockCountService, BookStockService bookStockService, CurrentStockService currentStockService, UnitService unitService) {
        this.menuGroupService = menuGroupService;
        this.stockCountService = stockCountService;
        this.bookStockService = bookStockService;
        this.currentStockService = currentStockService;
        this.unitService = unitService;
    }

    
    @RequestMapping(value = "/stocks/stockcount", method = RequestMethod.GET)
    public String stockCount(@AuthenticationPrincipal UserLogin userLogin, Model model,@ModelAttribute StockCount stockCount) {
        stockCount = stockCount ==null? new StockCount(): stockCount;
         stockCount.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        //model.addAttribute("stockCounts", this.stockCountService.findAll());
        model.addAttribute("stockCounts", this.stockCountService.findAllBySearchCriteria(stockCount));
        model.addAttribute("stockCount", stockCount);
        return "stocks/stockcount";
    }

   

    @RequestMapping(value = "/stocks/stockcountaction/{Id}", method = RequestMethod.GET)
    public String stockCountAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("Id") Long Id, Model model) {
        StockCount stockCount = this.stockCountService.getByID(Id);
        stockCount=(stockCount==null)?new StockCount():stockCount;
        return stockCountActionLocal(userLogin.getUnitCode(),stockCount, model);
    }
    private String stockCountActionLocal(String unitCode, StockCount stockCount, Model model) {
        model.addAttribute("bookStocks", bookStockService.findPendingUpdate(unitCode));
        if(stockCount.getId() > 0)
                {
                stockCount.setStockCountLines(stockCountService.findById(stockCount.getId()));
                }
        model.addAttribute("stockCount", stockCount);
        return "stocks/stockcountaction";
    }

    @RequestMapping(value = "/stocks/stockcountform", method = RequestMethod.POST)
    public String stockCountform(@AuthenticationPrincipal UserLogin userLogin, 
            @ModelAttribute StockCount stockCount, 
            Model model, RedirectAttributes redirectAttributes) {
         String error=stockCountService.isValid(stockCount);
        if(!Utils.empty(error)){
            model.addAttribute("error", error);
            log.info("validation error:{}",error);
            return stockCountActionLocal(userLogin.getUnitCode(), stockCount, model);
        }
        
        stockCount.setUser(userLogin.getUsername());
        stockCount.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        String msg = this.stockCountService.save(stockCount);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        return "redirect:/stocks/stockcount";
    }

    @RequestMapping(value = "/stocks/bookstocklist", method = RequestMethod.GET)//, produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    @ResponseBody
    public List<CurrentStock> findCurrentStockInLocation(@RequestParam(value = "Id", required = true) Long Id) {
        BookStock bookStock = bookStockService.getByID(Id);
        List<CurrentStock> currentStockList = currentStockService.findByBookStock(bookStock);
        currentStockList.stream().forEach(line -> {
            line.setBookStockId(Id);
            line.setQuantity(0);
        });
        List<StockCountLine> countLines = stockCountService.findAllActiveByBookStockId(Id);
        countLines.stream().forEach(c->{
            Optional<CurrentStock> match = currentStockList.stream().filter(cs->cs.getId()==c.getCurrentStock().getId()).findAny();
            if(match.isPresent()){
                match.get().setQuantity2(c.getCount());
            }
        });
        System.out.println("\n\n\n\nId:" + Id + "\n\n");
        return currentStockList;
    }

    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setAutoGrowCollectionLimit(500000);
}
}
