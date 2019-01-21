
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
import zw.co.psmi.hms.stocks.entity.StockMovementDescription;
import zw.co.psmi.hms.stocks.service.StockMovementDescriptionService;


@Controller
public class StockMovementDescriptionController {
	@Autowired
	private MenuGroupService menuGroupService;
	@Autowired
        StockMovementDescriptionService stockMovementDescriptionService;
       //StockMovementDescription
	@RequestMapping(value= "/stocks/stockmovementdescription", method=RequestMethod.GET)
	public String stockMovementDescription(@AuthenticationPrincipal UserLogin userLogin, Model model)
	    {
		
	    
	    
		model.addAttribute("stockMovementDescriptions", this.stockMovementDescriptionService.findAll());
		return "stocks/stockmovementdescription";
	    }
	@RequestMapping(value= "/stocks/stockmovementdescriptionaction/{Id}", method = RequestMethod.GET)
	public String stockMovementDescriptionAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("Id") Long Id, Model model)
	   {    
		
	    
	    
		StockMovementDescription stockMovementDescription = this.stockMovementDescriptionService.getByID(Id);
		model.addAttribute("stockMovementDescription", stockMovementDescription);
		return "stocks/stockmovementdescriptionaction";
	   }
	@RequestMapping(value= "/stocks/stockmovementdescriptionform", method=RequestMethod.POST)
	public String stockMovementDescriptionform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute StockMovementDescription stockMovementDescription , Model model, RedirectAttributes redirectAttributes)
	   {
		String msg = this.stockMovementDescriptionService.save(stockMovementDescription);
		redirectAttributes.addFlashAttribute("msg", "setMsg('"+msg+"')");
		return "redirect:/stocks/stockmovementdescription";
	   }
    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setAutoGrowCollectionLimit(500000);
}
    
}
