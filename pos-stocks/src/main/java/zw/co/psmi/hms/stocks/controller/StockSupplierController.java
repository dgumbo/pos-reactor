
package zw.co.psmi.hms.stocks.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import zw.co.psmi.hms.basic.Pager;
import zw.co.psmi.hms.stocks.entity.StockSupplier;
import zw.co.psmi.hms.stocks.service.StockSupplierService;
import static zw.co.psmi.hms.util.Utils.prepairString;


@Controller
public class StockSupplierController {
    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 25;
    private static final int[] PAGE_SIZES = {10, 25, 50, 100, 250};
    @Autowired
    private StockSupplierService stockSupplierService;
    //StockSupplier

    @RequestMapping(value = "/stocks/stocksupplier", method = RequestMethod.GET)
    public String stockSupplier(Model model, @RequestParam(required = false) String name,
            @RequestParam("pageSizeSelect") Optional<Integer> pageSize, @RequestParam("page") Optional<Integer> page
    ) {
        
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        
        Pageable pageable = new PageRequest(evalPage, evalPageSize);
        Page<StockSupplier> stockSupplierPages = stockSupplierService.findByNamePageable(pageable, prepairString(name));
        Pager pager = new Pager(stockSupplierPages.getTotalPages(), stockSupplierPages.getNumber(), BUTTONS_TO_SHOW);
 
        List<StockSupplier> stockSupplierList = stockSupplierPages.getContent();
        
        stockSupplierPages.isFirst();
        stockSupplierPages.isLast();
        stockSupplierPages.getTotalPages();
        
        model.addAttribute("stockSuppliers", stockSupplierPages) ;        
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("pager", pager);
        model.addAttribute("name", name);
        return "stocks/stocksupplier";
    }

    @RequestMapping(value = "/stocks/stocksupplieraction/{Id}", method = RequestMethod.GET)
    public String stockSupplierAction(@PathVariable("Id") Long Id, Model model) {

        StockSupplier stockSupplier = this.stockSupplierService.getByID(Id);
        if (stockSupplier == null) {
            stockSupplier = new StockSupplier();
        }
        model.addAttribute("stockSupplier", stockSupplier);
        return "stocks/stocksupplieraction";
    }

    @RequestMapping(value = "/stocks/stocksupplierform", method = RequestMethod.POST)
    public String stockSupplierform(@ModelAttribute StockSupplier stockSupplier, Model model, RedirectAttributes redirectAttributes) {
        String msg = this.stockSupplierService.save(stockSupplier);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        return "redirect:/stocks/stocksupplier";
    }

    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setAutoGrowCollectionLimit(500000);
}
}
