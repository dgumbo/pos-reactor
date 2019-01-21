
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
import zw.co.psmi.hms.stocks.entity.Approval;
import zw.co.psmi.hms.stocks.entity.ApprovalStatus;
import zw.co.psmi.hms.stocks.entity.WorkFlowType;
import zw.co.psmi.hms.stocks.service.ApprovalService;



@Controller
public class ApprovalController {
 
    private MenuGroupService menuGroupService;
    private ApprovalService approvalService;
    //Approval

    @Autowired
    public ApprovalController(MenuGroupService menuGroupService, ApprovalService approvalService) {
        this.menuGroupService = menuGroupService;
        this.approvalService = approvalService;
    }      
    
    @RequestMapping(value = "/stocks/approvalaction/{Id}/{action}/{workFlowType}", method = RequestMethod.GET)
    public String approvalAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("Id") Long Id,  @PathVariable("action") int action,  @PathVariable("workFlowType") WorkFlowType workFlowType, Model model) {

        Approval approval = this.approvalService.getByID(Id);
        if (approval == null) {
            approval = new Approval();
        } 
        if(action == 1)
           {
           approval.setApprovalStatus(ApprovalStatus.ACCEPTED);
           model.addAttribute("action", "Accept Action");
           }
        if(action == 2)
           {
           approval.setApprovalStatus(ApprovalStatus.REJECTED);
           model.addAttribute("action", "Reject Action");
           }
        model.addAttribute("approval", approval);
        model.addAttribute("workFlowType", workFlowType);
        return "stocks/approvalaction";
    }

    @RequestMapping(value = "/stocks/approvalform/{workFlowType}", method = RequestMethod.POST)
    public String approvalform(  @PathVariable("workFlowType") WorkFlowType workFlowType, @ModelAttribute Approval approval, Model model, RedirectAttributes redirectAttributes) {
        String msg = this.approvalService.save(approval);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
         return "redirect:/stocks/request/{workFlowType}";
    }
    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setAutoGrowCollectionLimit(500000);
}
}
