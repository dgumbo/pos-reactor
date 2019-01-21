
package zw.co.psmi.hms.stocks.controller;

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
import zw.co.psmi.hms.authentication.service.MenuGroupService;
import zw.co.psmi.hms.stocks.entity.StockGroup;
import zw.co.psmi.hms.stocks.service.StockGroupService;


@Controller
public class StockGroupController {
	@Autowired
	private MenuGroupService menuGroupService;
	@Autowired
        StockGroupService stockGroupService;
       //StockGroup
	@RequestMapping(value= "/stocks/stockgroup", method=RequestMethod.GET)
	public String stockGroup(@AuthenticationPrincipal UserLogin userLogin, Model model)
	    {
		
	    
	    
		model.addAttribute("stockGroups", this.stockGroupService.findAll());
		return "stocks/stockgroup";
	    }
	@RequestMapping(value= "/stocks/stockgroupaction/{Id}", method = RequestMethod.GET)
	public String stockGroupAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("Id") Long Id, Model model)
	   {    
		
	    
	    
		StockGroup stockGroup = this.stockGroupService.getByID(Id);
                stockGroup.setStockSubGroups(stockGroupService.getStockSubGroupByID(Id));
		model.addAttribute("stockGroup", stockGroup);
		return "stocks/stockgroupaction";
	   }
	@RequestMapping(value= "/stocks/stockgroupform", method=RequestMethod.POST)
	public String stockGroupform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute StockGroup stockGroup , Model model, RedirectAttributes redirectAttributes)
	   {
		String msg = this.stockGroupService.save(stockGroup, stockGroup.getStockSubGroups());
		redirectAttributes.addFlashAttribute("msg", "setMsg('"+msg+"')");
		return "redirect:/stocks/stockgroup";
	   }
    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setAutoGrowCollectionLimit(500000);
}
    
}
