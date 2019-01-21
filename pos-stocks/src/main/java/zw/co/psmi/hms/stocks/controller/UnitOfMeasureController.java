
package zw.co.psmi.hms.stocks.controller;

import java.util.List;
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
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.MenuGroupService;
import zw.co.psmi.hms.stocks.entity.UnitOfMeasure;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.stocks.service.UnitOfMeasureService;


@Controller
public class UnitOfMeasureController { 
    private MenuGroupService menuGroupService;
    private UnitOfMeasureService unitOfMeasureService;
    @Autowired
    private GenericStockTypeService genericStockTypeService;
    //UnitOfMeasure

    @Autowired
    public UnitOfMeasureController(MenuGroupService menuGroupService, UnitOfMeasureService unitOfMeasureService) {
        this.menuGroupService = menuGroupService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @RequestMapping(value = "/stocks/unitofmeasure", method = RequestMethod.GET)
    public String unitOfMeasure(@AuthenticationPrincipal UserLogin userLogin, Model model) {

        model.addAttribute("unitOfMeasures", this.unitOfMeasureService.findAll());
        return "stocks/unitofmeasure";
    }
    @RequestMapping(value = "/stocks/unitofmeasurebygenericid", method = RequestMethod.GET)
    @ResponseBody
    public List<UnitOfMeasure> unitofmeasurebygenericid(@RequestParam long id) {
        return genericStockTypeService.findByGenericStockTypeID(id);
    }

    @RequestMapping(value = "/stocks/unitofmeasureaction/{Id}", method = RequestMethod.GET)
    public String unitOfMeasureAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("Id") Long Id, Model model) {

        UnitOfMeasure unitOfMeasure = this.unitOfMeasureService.getByID(Id);
        if (unitOfMeasure == null) {
            unitOfMeasure = new UnitOfMeasure();
        }
        unitOfMeasure.setUnitOfMeasures(unitOfMeasureService.findByActiveStatusTrue());
        model.addAttribute("unitOfMeasure", unitOfMeasure);
        return "stocks/unitofmeasureaction";
    }

    @RequestMapping(value = "/stocks/unitofmeasureform", method = RequestMethod.POST)
    public String unitOfMeasureform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute UnitOfMeasure unitOfMeasure, Model model, RedirectAttributes redirectAttributes) {
        String msg = this.unitOfMeasureService.save(unitOfMeasure);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        return "redirect:/stocks/unitofmeasure";
    }

    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setAutoGrowCollectionLimit(500000);
}
}
