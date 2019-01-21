
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
import zw.co.psmi.hms.authentication.service.MenuGroupService;
import zw.co.psmi.hms.basic.Pager;
import zw.co.psmi.hms.stocks.entity.StockManufacturer;
import zw.co.psmi.hms.stocks.service.StockManufacturerService;
import static zw.co.psmi.hms.util.Utils.prepairString;


@Controller
public class StockManufacturerController {
    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 25;
    private static final int[] PAGE_SIZES = {10, 25, 50, 100, 250};
	@Autowired
	private StockManufacturerService stockManufacturerService;
       //Manufacturer
	@RequestMapping(value= "/stocks/stockmanufacturer", method=RequestMethod.GET)
	public String manufacturer(Model model, @RequestParam(required = false) String name,
            @RequestParam("pageSizeSelect") Optional<Integer> pageSize, @RequestParam("page") Optional<Integer> page
    ) {
        
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        
        Pageable pageable = new PageRequest(evalPage, evalPageSize);
        Page<StockManufacturer> stockManufacturerPages = stockManufacturerService.findByNamePageable(pageable, prepairString(name));
        Pager pager = new Pager(stockManufacturerPages.getTotalPages(), stockManufacturerPages.getNumber(), BUTTONS_TO_SHOW);
 
        List<StockManufacturer> stockManufacturerList = stockManufacturerPages.getContent();
        
        stockManufacturerPages.isFirst();
        stockManufacturerPages.isLast();
        stockManufacturerPages.getTotalPages();
        
        model.addAttribute("stockManufacturers", stockManufacturerPages) ;        
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("pager", pager);
        model.addAttribute("name", name);
		return "stocks/stockmanufacturer";
	    }
	@RequestMapping(value= "/stocks/stockmanufactureraction/{Id}", method = RequestMethod.GET)
	public String manufacturerAction(@PathVariable("Id") Long Id, Model model)
	   {   StockManufacturer manufacturer = this.stockManufacturerService.getByID(Id);
		model.addAttribute("manufacturer", manufacturer);
		return "stocks/stockmanufactureraction";
	   }
	@RequestMapping(value= "/stocks/stockmanufacturerform", method=RequestMethod.POST)
	public String manufacturerform(@ModelAttribute StockManufacturer manufacturer , Model model, RedirectAttributes redirectAttributes)
	   {
		String msg = this.stockManufacturerService.save(manufacturer);
		redirectAttributes.addFlashAttribute("msg", "setMsg('"+msg+"')");
		return "redirect:/stocks/stockmanufacturer";
	   }
    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setAutoGrowCollectionLimit(500000);
}
    
}
