
package zw.co.psmi.hms.stocks.controller;

import zw.co.psmi.hms.basic.Pager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.MenuGroupService;
import zw.co.psmi.hms.stocks.entity.GenericStockType;
import zw.co.psmi.hms.stocks.entity.StockSubGroup;
import zw.co.psmi.hms.stocks.entity.StockType;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.stocks.service.StockGroupService;
import zw.co.psmi.hms.stocks.service.StockManufacturerService;
import zw.co.psmi.hms.stocks.service.UnitOfMeasureService;
import static zw.co.psmi.hms.util.Utils.prepairString;


@Controller
@Slf4j
public class GenericStockTypeController {

    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 25;
    private static final int[] PAGE_SIZES = {10, 25, 50, 100, 250};

    private MenuGroupService menuGroupService;
    private GenericStockTypeService genericStockTypeService;
    private StockManufacturerService stockManufacturerService;
    private StockGroupService stockGroupService;
    private UnitOfMeasureService unitOfMeasureService;
    //GenericStockType

    @Autowired
    public GenericStockTypeController(MenuGroupService menuGroupService, GenericStockTypeService genericStockTypeService, StockManufacturerService stockManufacturerService, StockGroupService stockGroupService, UnitOfMeasureService unitOfMeasureService) {
        this.menuGroupService = menuGroupService;
        this.genericStockTypeService = genericStockTypeService;
        this.stockManufacturerService = stockManufacturerService;
        this.stockGroupService = stockGroupService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @RequestMapping(value = "/stocks/genericstocktype", method = RequestMethod.GET)
    public String genericStockType(@AuthenticationPrincipal UserLogin userLogin, Model model,
            @RequestParam(required = false) String name, @RequestParam(required = false) Long stockGroupId,
            @RequestParam("pageSizeSelect") Optional<Integer> pageSize, @RequestParam("page") Optional<Integer> page) {

        //model.addAttribute("genericStockTypes",  genericStockTypeService.findByNameAndStockGroup(prepairString(name), stockGroupId));
        
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        
        Pageable pageable = new PageRequest(evalPage, evalPageSize);
        Page<GenericStockType> genericStockTypePages = genericStockTypeService.findByNameAndStockGroupPageable(pageable, prepairString(name), stockGroupId);
        Pager pager = new Pager(genericStockTypePages.getTotalPages(), genericStockTypePages.getNumber(), BUTTONS_TO_SHOW);
 
        List<GenericStockType> genericStockTypeList = genericStockTypePages.getContent();
                log.info("Generic len:{}",genericStockTypeList.size());
        genericStockTypePages.isFirst();
        genericStockTypePages.isLast();
        genericStockTypePages.getTotalPages();
        
        model.addAttribute("genericStockTypes", genericStockTypePages) ;        
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("pager", pager);
        model.addAttribute("name", name);
        
        model.addAttribute("stockGroups", stockGroupService.findAllActive());
        return "stocks/genericstocktype";
    }

    @RequestMapping(value = "/stocks/genericstocktypeaction/{Id}", method = RequestMethod.GET)
    public String genericStockTypeAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("Id") Long Id, Model model) {

        GenericStockType genericStockType = this.genericStockTypeService.getByID(Id);
        genericStockType = (genericStockType == null ? new GenericStockType() : genericStockType);
        log.info("list:::" + genericStockType.getStockSubGroups());
        genericStockType.setStockTypes(genericStockTypeService.findStockTypeByGenericStockTypeId(Id));
        genericStockType.setStockGroups(stockGroupService.findAllActive());
        if (Id > 0) {
            genericStockType.setStockSubGroupList(stockGroupService.getStockSubGroupByID(genericStockType.getStockGroup().getId()));
        }
        genericStockType.setStockManufacturers(stockManufacturerService.findAllActive());
        genericStockType.setUnitOfMeasures(unitOfMeasureService.findAll());
        model.addAttribute("genericStockType", genericStockType);
        return "stocks/genericstocktypeaction";
    }

    @RequestMapping(value = "/stocks/genericstocktypeform", method = RequestMethod.POST)
    public String genericStockTypeform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute GenericStockType genericStockType, Model model, RedirectAttributes redirectAttributes) {
//        for(StockType a:genericStockType.getStockTypes()){
//            log.info("status:{}:{}",a,a.isActiveStatus());
//        }
        String msg = this.genericStockTypeService.save(genericStockType, genericStockType.getStockTypes());
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        return "redirect:/stocks/genericstocktype";
    }

    @RequestMapping(value = "/stocks/stocksubgroups", method = RequestMethod.GET)
    @ResponseBody
    public     List<StockSubGroup> findAllAgencies(  @RequestParam(value = "Id", required = true) Long Id) {
        List<StockSubGroup> stockSubGroup = stockGroupService.getStockSubGroupByID(Id);
        return stockSubGroup;
    }
 @RequestMapping(value = "/stocks/genericstocktypesearchlist", method = RequestMethod.GET)
    @ResponseBody
    public List<GenericStockType> findAllGenericStockType(@RequestParam(value = "name", required = true) String name) {
        List<GenericStockType> genericStockTypes = genericStockTypeService.findByNameSearch(name);
        return genericStockTypes;
    }
     @RequestMapping(value = "/stocks/stocktypesearchlist", method = RequestMethod.GET)
    @ResponseBody
    public List<StockType> findAllStockType(@RequestParam(value = "name", required = true) String name) {
        List<StockType> stockTypes = genericStockTypeService.findStockTypeByName(name);
        return stockTypes;
    }
    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setAutoGrowCollectionLimit(500000);
}
}
