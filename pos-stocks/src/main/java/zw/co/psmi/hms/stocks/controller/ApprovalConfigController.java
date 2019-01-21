
package zw.co.psmi.hms.stocks.controller;

import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
import zw.co.psmi.hms.authentication.service.RoleService;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.entity.ApprovalConfig;
import zw.co.psmi.hms.stocks.entity.ApprovalTemplate;
import zw.co.psmi.hms.stocks.entity.WorkFlowType;
import zw.co.psmi.hms.stocks.service.ApprovalConfigService;


@Controller 
@Slf4j
public class ApprovalConfigController {

    private MenuGroupService menuGroupService;
    ApprovalConfigService approvalConfigService;
    UnitService unitService;
    RoleService roleService;
    //ApprovalConfig

    @Autowired
    public ApprovalConfigController(MenuGroupService menuGroupService, ApprovalConfigService approvalConfigService, UnitService unitService, RoleService roleService) {
        this.menuGroupService = menuGroupService;
        this.approvalConfigService = approvalConfigService;
        this.unitService = unitService;
        this.roleService = roleService;
    }

        @RequestMapping(value = "/stocks/approvalconfig", method = RequestMethod.GET)
    public String approvalConfig(@AuthenticationPrincipal UserLogin userLogin, Model model) {
        model.addAttribute("approvalConfigs", this.approvalConfigService.findAll());
        return "stocks/approvalconfig";
    }
 
    @RequestMapping(value = "/stocks/approvalconfigaction/{Id}", method = RequestMethod.GET)
    public String approvalConfigAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("Id") Long Id, Model model) {
        ApprovalConfig approvalConfig = this.approvalConfigService.getByID(Id);
        approvalConfig.setApprovalTemplates(approvalConfigService.getApprovalTemplateByApprovalConfigId(Id));
        List<WorkFlowType> workFlowType = Arrays.asList(WorkFlowType.values());
        approvalConfig.setWorkFlowTypes(workFlowType);
        approvalConfig.setUnits(unitService.getActive());
        approvalConfig.setRoles(roleService.getActive());
        model.addAttribute("approvalConfig", approvalConfig);
        return "stocks/approvalconfigaction";
    }

    @RequestMapping(value = "/stocks/approvalconfigform", method = RequestMethod.POST)
    public String approvalConfigform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute ApprovalConfig approvalConfig, Model model, RedirectAttributes redirectAttributes) {
        String msg = this.approvalConfigService.save(approvalConfig, approvalConfig.getApprovalTemplates());
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        return "redirect:/stocks/approvalconfig";
    }
    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setAutoGrowCollectionLimit(500000);
}

}
