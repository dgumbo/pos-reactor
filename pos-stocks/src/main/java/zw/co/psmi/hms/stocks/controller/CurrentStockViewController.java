
package zw.co.psmi.hms.stocks.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import zw.co.psmi.hms.authentication.dao.LocationDao;
import zw.co.psmi.hms.authentication.entity.Unit;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.entity.CurrentStockView;
import zw.co.psmi.hms.stocks.entity.StockStatus;
import zw.co.psmi.hms.stocks.service.CurrentStockService;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.stocks.service.StockGroupService;
import zw.co.psmi.hms.stocks.service.StockManufacturerService;


@Controller
@Slf4j
public class CurrentStockViewController {
    
    private UnitService unitService;
    private GenericStockTypeService genericStockTypeService;
    private LocationDao locationDao;
    private CurrentStockService currentStockService;
    private StockManufacturerService stockManufacturerService; 
    private StockGroupService stockGroupService;
    @Autowired
    public CurrentStockViewController(GenericStockTypeService genericStockTypeService, UnitService unitService, LocationDao locationDao,CurrentStockService currentStockService,StockManufacturerService stockManufacturerService,StockGroupService stockGroupService) {
        this.unitService = unitService;
        this.genericStockTypeService = genericStockTypeService;
        this.locationDao = locationDao;
        this.currentStockService = currentStockService;
        this.stockManufacturerService=stockManufacturerService;
        this.stockGroupService=stockGroupService;
    }
 
    @RequestMapping(value = "/stocks/currentstockview", method = RequestMethod.GET)
    public String currentstockview(@AuthenticationPrincipal UserLogin userLogin, Model model,@RequestParam(required = false) String name, @RequestParam(required = false) Long locationId,
    @RequestParam(required = false) Long manufacturerId,@RequestParam(required = false) StockStatus stockStatus,@RequestParam(required = false) Long stockGroupId,@RequestParam(required = false) Long stockSubGroupId) {
        
        List<CurrentStockView> currentStockViewList = currentStockService.getCurrentStockView(name,locationId,manufacturerId,stockGroupId,stockSubGroupId,stockStatus);         
            String unitCode = userLogin.getUnitCode();
            model.addAttribute("currentStockViewList",currentStockViewList);
            model.addAttribute("locations", unitService.getLocationActive(unitCode));
            model.addAttribute("manufacturers", stockManufacturerService.findAllActive());
            model.addAttribute("stockStatus", Arrays.asList(StockStatus.values()).stream().filter(s->s!=StockStatus.MERGED&&s!=StockStatus.SPLIT).collect(Collectors.toList()));
            model.addAttribute("stockGroups", stockGroupService.findAllActive());
        
        return "stocks/currentstockview";
    }    
    @RequestMapping(value = "/stocks/currentstockviewotherunits", method = RequestMethod.GET)
    public String currentstockviewPsp(@AuthenticationPrincipal UserLogin userLogin, Model model,@RequestParam(required = false) String name
            , @RequestParam(required = false) Long unitId, @RequestParam(required = false) Long locationId,
    @RequestParam(required = false) Long manufacturerId,@RequestParam(required = false) StockStatus stockStatus,@RequestParam(required = false) Long stockGroupId,@RequestParam(required = false) Long stockSubGroupId) {
        unitId = unitId == null? 0L:unitId;
        Unit unit  = unitService.getByID(unitId);
        String unitCode=unit==null?"":unit.getUnitCode();
        List<CurrentStockView> currentStockViewList = currentStockService.getCurrentStockView(name,unitCode,locationId,manufacturerId,stockGroupId,stockSubGroupId,stockStatus);         
            model.addAttribute("currentStockViewList",currentStockViewList);
            model.addAttribute("units", unitService.getActive());
            model.addAttribute("manufacturers", stockManufacturerService.findAllActive());
            model.addAttribute("stockStatus", Arrays.asList(StockStatus.values()).stream().filter(s->s!=StockStatus.MERGED&&s!=StockStatus.SPLIT).collect(Collectors.toList()));
            model.addAttribute("stockGroups", stockGroupService.findAllActive());
        
        return "stocks/currentstockviewpsp";
    }    
    @RequestMapping(value = "/stocks/currentstockview_csv", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity currentstockview2(@AuthenticationPrincipal UserLogin userLogin, Model model,@RequestParam(required = false) String name, @RequestParam(required = false) Long locationId,
    @RequestParam(required = false) Long manufacturerId,@RequestParam(required = false) StockStatus stockStatus,@RequestParam(required = false) Long stockGroupId,@RequestParam(required = false) Long stockSubGroupId) {
        
        List<CurrentStockView> currentStockViewList = currentStockService.getCurrentStockView
        (name,locationId,manufacturerId,stockGroupId,stockSubGroupId,stockStatus);         
            
        StringBuilder builder = new StringBuilder("Current Stock View");
        builder.append("\n");
        builder.append("\n");
        builder.append("\n");
        builder.append("Filters::");
        builder.append("\n");
        if(name!=null){
        builder.append("name,").append(name).append("\n");
        }
        if(locationId!=null&&locationId>0l){
        builder.append("location,").append(locationDao.findOne(locationId).getLocationName().getName()).append("\n");
        }
        if(manufacturerId!=null&&manufacturerId>0l){
        builder.append("manufacturer,").append(stockManufacturerService.getByID(manufacturerId).getName()).append("\n");
        }
        if(stockGroupId!=null&&stockGroupId>0l){
        builder.append("stockGroup,").append(stockGroupService.getByID(locationId).getName()).append("\n");
        }
        if(stockSubGroupId!=null&&stockSubGroupId>0l){
        builder.append("Stock Subgroup,").append(stockGroupService.getStockSubGroupBySubGroupId(stockSubGroupId).getName()).append("\n");
        }
        if(stockStatus!=null){
        builder.append("Stock Status,").append(stockStatus).append("\n");
        }
        builder.append("\n");
        builder.append("\n");
        builder.append("\n");
        builder.append("\n");
        builder.append("\n")
                .append("Unit,Location,Manufacturer,Generic Stock Type,Unit Of Measure,"
                        + "Weighted Average Cost,Last Receipt Cost,Status,Batch Number,Expiry Date,Quantity");
        for (CurrentStockView row : currentStockViewList) {
            builder.append("\n");
        builder.append(row.getUnit().getName()).append(",");
        builder.append(row.getLocation().getLocationName().getName()).append(",");
        builder.append(row.getStockType().getStockManufacturer().getName()).append(",");
        builder.append(row.getStockType().getGenericStockType().getName()).append(",");
        builder.append(row.getStockType().getUnitOfMeasure().getName()).append(",");
        builder.append(row.getStockType().getWeightedAverageCost()).append(",");
        builder.append(row.getStockType().getLastReceiptCostRate()).append(",");
        builder.append(row.getStockStatus()).append(",");
        builder.append(row.getBatchNumber()).append(",");
        builder.append(row.getExpiryDate()).append(",");
        builder.append(row.getQuantity());
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-disposition", "attachment; filename=CurrentStockViewFiltered.csv");
        responseHeaders.add("Content-Type","text/csv");
        ResponseEntity respEntity = new ResponseEntity(builder.toString(), responseHeaders,HttpStatus.OK);
        return respEntity;
    }
    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setAutoGrowCollectionLimit(500000);
}
}
