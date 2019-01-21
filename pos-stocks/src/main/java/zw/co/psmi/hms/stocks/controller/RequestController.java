package zw.co.psmi.hms.stocks.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.entity.Location;
import zw.co.psmi.hms.authentication.entity.Unit;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.MenuGroupService;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.entity.Approval;
import zw.co.psmi.hms.stocks.entity.ApprovalStatus;
import zw.co.psmi.hms.stocks.entity.GenericStockType;
import zw.co.psmi.hms.stocks.entity.StockRequest;
import zw.co.psmi.hms.stocks.entity.WorkFlowType;
import zw.co.psmi.hms.stocks.service.ApprovalService;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.stocks.service.StockGroupService;
import zw.co.psmi.hms.stocks.service.StockManufacturerService;
import zw.co.psmi.hms.stocks.service.StockRequestService;
import zw.co.psmi.hms.stocks.service.StockSupplierService;
import zw.co.psmi.hms.stocks.service.UnitOfMeasureService;
import zw.co.psmi.hms.util.Utils;

@Controller
@Slf4j
public class RequestController {

    private MenuGroupService menuGroupService;
    StockRequestService stockRequestService;
    StockManufacturerService stockManufacturerService;
    StockGroupService stockGroupService;
    UnitService unitService;
    UnitOfMeasureService unitOfMeasureService;
    GenericStockTypeService genericStockTypeService;
    StockSupplierService stockSupplierService;
    ApprovalService approvalService;
    //GenericStockType

    @Autowired
    public RequestController(MenuGroupService menuGroupService, StockRequestService stockRequestService, StockManufacturerService stockManufacturerService, StockGroupService stockGroupService, UnitService unitService, UnitOfMeasureService unitOfMeasureService, GenericStockTypeService genericStockTypeService, StockSupplierService stockSupplierService, ApprovalService approvalService) {
        this.menuGroupService = menuGroupService;
        this.stockRequestService = stockRequestService;
        this.stockManufacturerService = stockManufacturerService;
        this.stockGroupService = stockGroupService;
        this.unitService = unitService;
        this.unitOfMeasureService = unitOfMeasureService;
        this.genericStockTypeService = genericStockTypeService;
        this.stockSupplierService = stockSupplierService;
        this.approvalService = approvalService;
    }

    @RequestMapping(value = "/stocks/request/{workFlowType}", method = RequestMethod.GET)
    public String request(@AuthenticationPrincipal UserLogin userLogin, RedirectAttributes redirectAttributes, @PathVariable("workFlowType") WorkFlowType workFlowType, Model model, @ModelAttribute StockRequest stockRequest) {
        stockRequest = stockRequest == null ? new StockRequest() : stockRequest;
        stockRequest = stockRequestService.updateLocationsfromCurrentLoginUnit(stockRequest);
        stockRequest.setWorkFlowType(workFlowType);
        stockRequest.setRequestUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockRequest.setLocations(unitService.getLocationActive(userLogin.getUnitCode()));
        String title = workFlowType == WorkFlowType.PURCHASEORDER ? "Purchase Order"
                : workFlowType == WorkFlowType.REQUISITION ? "Requisition"
                        : workFlowType == WorkFlowType.STOCKREQUEST ? "Stock Request"
                                : workFlowType == WorkFlowType.STOCKRETURN ? "Stock Return"
                                        : workFlowType == WorkFlowType.GRVRETURN ? "Return Grv"
                                                : "";
        if (Utils.empty(title)) {
            String msg = "Menu Error, Please contact IT Support";
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
            return "redirect:/home";
        }
        model.addAttribute("workFlowType", workFlowType);
        model.addAttribute("title", title);
        model.addAttribute("stockRequests", stockRequestService.findAllBySearchCriteria(stockRequest));
        model.addAttribute("stockRequest", stockRequest);
        return "stocks/request";
    }
    @RequestMapping(value = "/stocks/requestlob/{workFlowType}", method = RequestMethod.GET)
    public String requestLob(@AuthenticationPrincipal UserLogin userLogin, RedirectAttributes redirectAttributes, @PathVariable("workFlowType") WorkFlowType workFlowType, Model model, @ModelAttribute StockRequest stockRequest) {
        stockRequest = stockRequest == null ? new StockRequest() : stockRequest;
        stockRequest.setLob(unitService.getByUnitCode(userLogin.getUnitCode()).getLob());
        stockRequest.setWorkFlowType(workFlowType);
        String title = workFlowType == WorkFlowType.PURCHASEORDER ? "Purchase Order"
                : workFlowType == WorkFlowType.REQUISITION ? "Requisition"
                        : workFlowType == WorkFlowType.STOCKREQUEST ? "Stock Request"
                                : workFlowType == WorkFlowType.STOCKRETURN ? "Stock Return"
                                        : workFlowType == WorkFlowType.GRVRETURN ? "Return Grv"
                                                : "";
        if (Utils.empty(title)) {
            String msg = "Menu Error, Please contact IT Support";
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
            return "redirect:/home";
        }
        model.addAttribute("workFlowType", workFlowType);
        model.addAttribute("title", title);
        model.addAttribute("stockRequests", stockRequestService.findAllBySearchCriteriaWithLob(stockRequest));
        model.addAttribute("stockRequest", stockRequest);
        return "stocks/request";
    }

    @RequestMapping(value = "/stocks/requestselect/{workFlowType}", method = RequestMethod.GET)
    public String requestSelect(@AuthenticationPrincipal UserLogin userLogin, RedirectAttributes redirectAttributes, @PathVariable("workFlowType") WorkFlowType workFlowType, Model model, @ModelAttribute StockRequest stockRequest) {
        if (workFlowType != WorkFlowType.STOCKREQUEST) {
            redirectAttributes.addFlashAttribute("msg", "setError('Wrong Request Type')");
            return "redirect:/stocks/request/STOCKREQUEST";
        }
        stockRequest = stockRequest == null ? new StockRequest() : stockRequest;
        stockRequest = stockRequestService.updateLocationsfromCurrentLoginUnit(stockRequest);
        stockRequest.setRequestUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        stockRequest.setWorkFlowType(workFlowType);
        stockRequest.setLocations(unitService.getLocationActive(userLogin.getUnitCode()));
        String title = workFlowType == WorkFlowType.PURCHASEORDER ? "Purchase Order"
                : workFlowType == WorkFlowType.REQUISITION ? "Requisition"
                        : workFlowType == WorkFlowType.STOCKREQUEST ? "Stock Request"
                                : workFlowType == WorkFlowType.STOCKRETURN ? "Stock Return"
                                        : workFlowType == WorkFlowType.GRVRETURN ? "Return Grv"
                                                : "";
        if (Utils.empty(title)) {
            String msg = "Menu Error, Please contact IT Support";
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
            return "redirect:/home";
        }
        model.addAttribute("workFlowType", workFlowType);
        model.addAttribute("title", title);
        model.addAttribute("stockRequests", stockRequestService.findByApprovalStatusAndRequestDateBetween(stockRequest));
        model.addAttribute("stockRequest", stockRequest);
        return "stocks/requestselect";
    }

    @PostMapping(value = "/stocks/selectedrequests/{workFlowType}")
    public String processSelectedRequests(RedirectAttributes redirectAttributes, @PathVariable("workFlowType") WorkFlowType workFlowType, @AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute StockRequest stockRequest) {
        if (workFlowType != WorkFlowType.STOCKREQUEST) {
            redirectAttributes.addFlashAttribute("msg", "setError('Wrong Request Type')");
            return "redirect:/stocks/request/STOCKREQUEST";
        }
        stockRequest = stockRequestService.createConsolidatedRequest(stockRequest.getStockRequestsIdList());
        if (stockRequest == null) {
            redirectAttributes.addFlashAttribute("msg", "setError('Request selection failed')");
            return "redirect:/stocks/request/STOCKREQUEST";
        }
        String title="Create New Requisition";
        stockRequest.setWorkFlowType(WorkFlowType.REQUISITION);
        model.addAttribute("workFlowType", workFlowType);
        model.addAttribute("title", title);
        stockRequest.setLocations(unitService.getLocationActive(userLogin.getUnitCode()));  
        stockRequest.setStockSuppliers(stockSupplierService.findAllActive());
        stockRequest.setIssueUnits(unitService.getActive());
            stockRequest.setEditable(true);
        model.addAttribute("stockRequest", stockRequest);
        return "stocks/requestaction";
    }

    @RequestMapping(value = "/stocks/requestaction/{Id}/{workFlowType}", method = RequestMethod.GET)
    public String purchaseAction(RedirectAttributes redirectAttributes, @PathVariable("workFlowType") WorkFlowType workFlowType, @AuthenticationPrincipal UserLogin userLogin, @PathVariable("Id") Long Id, Model model) {
        String title;
        if (Id > 0) {
            title = workFlowType == WorkFlowType.PURCHASEORDER ? "View Purchase Order"
                    : workFlowType == WorkFlowType.REQUISITION ? "View Requisition"
                            : workFlowType == WorkFlowType.STOCKREQUEST ? "View Stock Request"
                                    : workFlowType == WorkFlowType.STOCKRETURN ? "View Stock Request"
                                            : "";
        } else {
            title = workFlowType == WorkFlowType.PURCHASEORDER ? "Create New Purchase Order"
                    : workFlowType == WorkFlowType.REQUISITION ? "Create New Requisition"
                            : workFlowType == WorkFlowType.STOCKREQUEST ? "Create New Stock Request"
                                    : workFlowType == WorkFlowType.STOCKRETURN ? "Create New Stock Return"
                                            : "";
        }
        if (Utils.empty(title)) {
            String msg = "Menu Error, Please contact IT Support";
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
            return "redirect:/home";
        }
        model.addAttribute("workFlowType", workFlowType);
        model.addAttribute("title", title);
        StockRequest stockRequest = stockRequestService.getByID(Id);
        stockRequest.setLocations(unitService.getLocationActive(userLogin.getUnitCode()));
        stockRequest.setStockRequestItems(stockRequestService.getStockRequestItemsByStockRequestId(Id));
        stockRequest.setStockSuppliers(stockSupplierService.findAllActive());
        stockRequest.setIssueUnits(unitService.getActive());
        if (Id > 0 && WorkFlowType.STOCKREQUEST == workFlowType) {
            stockRequest.setIssueLocations(unitService.getLocationActive(stockRequest.getIssueUnit().getUnitCode()));
        } else {

        }
        if (Id == 0) {
            stockRequest.setEditable(true);
        }
        if (Id > 0
                && stockRequest.getCreatedByUser().contentEquals(userLogin.getUsername())
                && (stockRequest.getApprovalStatus() == ApprovalStatus.PENDING
                || stockRequest.getApprovalStatus() == ApprovalStatus.CREATED)) {
            Optional<Approval> r = approvalService.findByStockRequest(stockRequest).stream().filter(e
                    -> (e.getApprovalStatus() == ApprovalStatus.ACCEPTED || e.getApprovalStatus() == ApprovalStatus.REJECTED))
                    .findAny();
            log.info("editable1::");
            if (!r.isPresent()) {
                log.info("editable2::true");
                stockRequest.setEditable(true);
            }
            log.info("editable3::complete");
        }
        if (Id > 0
                && (stockRequest.getApprovalStatus() == ApprovalStatus.ACCEPTED)) {
            List<Approval> r = approvalService.findByStockRequest(stockRequest);
            log.info("cancellable1::");
            if (!r.isEmpty() && r.get(r.size() - 1).getCreatedByUser().contentEquals(userLogin.getUsername())) {
                log.info("cancellable2::true");
                stockRequest.setCancellable(true);
            }
            log.info("cancellable3::complete");
        }
        model.addAttribute("stockRequest", stockRequest);
        return "stocks/requestaction";
    }

    @RequestMapping(value = "/stocks/requestaction/{Id}/{workFlowType}/{orgId}", method = RequestMethod.GET)
    public String purchaseActionGrvReturn(RedirectAttributes redirectAttributes, @PathVariable("workFlowType") WorkFlowType workFlowType,
            @AuthenticationPrincipal UserLogin userLogin, @PathVariable("Id") Long Id, @PathVariable("orgId") Long orgId, Model model) {
        String title;
        if (Id > 0) {
            title
                    = workFlowType == WorkFlowType.GRVRETURN ? "View GRV Return"
                            : "";
        } else {
            title
                    = workFlowType == WorkFlowType.GRVRETURN ? "Create New GRV Return"
                            : "";
        }
        if (Utils.empty(title)) {
            String msg = "Menu Error, Please contact IT Support";
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
            return "redirect:/home";
        }
        model.addAttribute("workFlowType", workFlowType);
        model.addAttribute("title", title);
        StockRequest stockRequest = Id == 0 ? stockRequestService.getByID(orgId) : stockRequestService.getByID(Id);
        stockRequest.setLocations(unitService.getLocationActive(userLogin.getUnitCode()));
        stockRequest.setStockRequestItems(stockRequestService.getStockRequestItemsByStockRequestId(Id));
        stockRequest.setStockSuppliers(stockSupplierService.findAllActive());
        stockRequest.setIssueUnits(unitService.getActive());
        if (Id == 0) {
            stockRequest.setEditable(true);
        }
        if (Id > 0
                && stockRequest.getCreatedByUser().contentEquals(userLogin.getUsername())
                && (stockRequest.getApprovalStatus() == ApprovalStatus.PENDING
                || stockRequest.getApprovalStatus() == ApprovalStatus.CREATED)) {
            Optional<Approval> r = approvalService.findByStockRequest(stockRequest).stream().filter(e
                    -> (e.getApprovalStatus() == ApprovalStatus.ACCEPTED || e.getApprovalStatus() == ApprovalStatus.REJECTED))
                    .findAny();
            log.info("editable1::");
            if (!r.isPresent()) {
                log.info("editable2::true");
                stockRequest.setEditable(true);
            }
            log.info("editable3::complete");
        }
        if (Id > 0
                && (stockRequest.getApprovalStatus() == ApprovalStatus.ACCEPTED)) {
            List<Approval> r = approvalService.findByStockRequest(stockRequest);
            log.info("cancellable1::");
            if (!r.isEmpty() && r.get(r.size() - 1).getCreatedByUser().contentEquals(userLogin.getUsername())) {
                log.info("cancellable2::true");
                stockRequest.setCancellable(true);
            }
            log.info("cancellable3::complete");
        }
        model.addAttribute("stockRequest", stockRequest);
        return "stocks/requestaction";
    }

    @RequestMapping(value = "/stocks/requestform/{workFlowType}", method = RequestMethod.POST)
    public String purchaseForm(@PathVariable("workFlowType") WorkFlowType workFlowType, @ModelAttribute StockRequest request, Model model, RedirectAttributes redirectAttributes) {
        request.setWorkFlowType(workFlowType);
        String msg = stockRequestService.save(request, request.getStockRequestItems());
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        return "redirect:/stocks/request/{workFlowType}";
    }

    @RequestMapping(value = "/stocks/requestlist/{workFlowType}", method = RequestMethod.GET)
    public String stockrequestlist(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("workFlowType") WorkFlowType workFlowType, Model model, @ModelAttribute StockRequest stockRequest) {
        stockRequest = stockRequest == null ? new StockRequest() : stockRequest;
        stockRequest.setWorkFlowType(workFlowType);
        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
        stockRequest.setIssueUnit(unit);
        stockRequest.setRequestUnit(unit);
        stockRequest.setApprovalStatus(ApprovalStatus.ACCEPTED);
        String title = workFlowType == WorkFlowType.PURCHASEORDER ? "Purchase Order  Approved List" : "Stock Request Approved List";
        title = workFlowType == WorkFlowType.REQUISITION ? "Requisition Approved List" : title;
        log.info("issue unit set:{}", workFlowType == WorkFlowType.STOCKREQUEST);
        model.addAttribute("stockRequests", stockRequestService.findAllBySearchCriteriaAndApprovalStatus(stockRequest));
        stockRequest.setLocations(unitService.getLocationActive(userLogin.getUnitCode()));
        model.addAttribute("workFlowType", workFlowType);
        model.addAttribute("title", title);
        return "stocks/requestlist";
    }

    @RequestMapping(value = "/stocks/locations", method = RequestMethod.GET)
    @ResponseBody
    public List<Location> findAllLocations(@RequestParam(value = "Id", required = true) Long Id) {
        List<Location> location = unitService.getLocationsByUnitID(Id);
        return location;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(500000);
    }
}
