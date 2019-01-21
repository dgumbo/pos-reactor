/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.psmi.hms.stocks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.stocks.entity.StockRequest;
import zw.co.psmi.hms.stocks.entity.StockRequestCancellation;
import zw.co.psmi.hms.stocks.entity.WorkFlowType;
import zw.co.psmi.hms.stocks.service.StockRequestCancellationService;
import zw.co.psmi.hms.stocks.service.StockRequestService;

/**
 *
 * @author kinah
 */
@Controller
public class StockRequestCancellationController {
    @Autowired
    StockRequestCancellationService stockRequestCancellationService;
    @Autowired
    StockRequestService stockRequestService;
    //GenericStockType

  
    @RequestMapping(value = "/stocks/requestcancellation/{workFlowType}", method = RequestMethod.GET)
    public String request(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("workFlowType") WorkFlowType workFlowType, Model model, @ModelAttribute StockRequest stockRequest) {
        stockRequest = stockRequest == null ? new StockRequest() : stockRequest;
        stockRequest.setWorkFlowType(workFlowType);
        String title = workFlowType == WorkFlowType.PURCHASEORDER ? "Purchase Order Cancellation" : 
                workFlowType == WorkFlowType.REQUISITION ? "Requisition Cancellation" : 
                workFlowType == WorkFlowType.STOCKREQUEST ? "Stock Request Cancellation" : 
                "";        
        model.addAttribute("workFlowType", workFlowType);
        model.addAttribute("title", title);
        model.addAttribute("stockRequestCancellations", stockRequestCancellationService.getAll(stockRequest));
        model.addAttribute("stockRequest", stockRequest);
        return "stocks/requestcancellation";
    }

    @RequestMapping(value = "/stocks/requestcancellationaction/{Id}/{workFlowType}", method = RequestMethod.GET)
    public String purchaseAction(@PathVariable("workFlowType") WorkFlowType workFlowType, @AuthenticationPrincipal UserLogin userLogin, @PathVariable("Id") Long Id, Model model, @RequestParam(value = "stockRequestId", required = false) Long stockRequestId) {
        String title;
        if (Id > 0) {
            title = workFlowType == WorkFlowType.PURCHASEORDER ? "View Purchase Order Cancellation" : 
                workFlowType == WorkFlowType.REQUISITION ? "View Requisition Cancellation" : 
                workFlowType == WorkFlowType.STOCKREQUEST ? "View Stock Request Cancellation" : 
                "";
        } else {
            title = workFlowType == WorkFlowType.PURCHASEORDER ? "Create New Purchase Order Cancellation" : 
                workFlowType == WorkFlowType.REQUISITION ? "Create New Requisition Cancellation" : 
                workFlowType == WorkFlowType.STOCKREQUEST ? "Create New Stock Request Cancellation" : 
                "";
        }
        StockRequestCancellation stockRequestCancellation = stockRequestCancellationService.getById(Id);
       if(stockRequestId != null && stockRequestId > 0)
          {
          stockRequestCancellation.setStockRequest(stockRequestService.getByID(stockRequestId));
          }
        model.addAttribute("workFlowType", workFlowType);
        model.addAttribute("title", title);
        model.addAttribute("stockRequestCancellation", stockRequestCancellation);
        return "stocks/requestcancellationaction";
    }

    @RequestMapping(value = "/stocks/requestcancellationform/{workFlowType}", method = RequestMethod.POST)
    public String purchaseForm(@PathVariable("workFlowType") WorkFlowType workFlowType, @ModelAttribute StockRequestCancellation request, Model model, RedirectAttributes redirectAttributes) {
        request.setWorkFlowType(workFlowType);
        String msg = stockRequestCancellationService.save(request);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        return "redirect:/stocks/requestcancellation/{workFlowType}";
    }
}
