
package zw.co.psmi.hms.stocks.controller;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.basic.BasePdfOutputService;
import zw.co.psmi.hms.basic.OutputType;
import zw.co.psmi.hms.basic.PdfOutput;
import zw.co.psmi.hms.stocks.service.StocksPdfOutputService;




@Controller
@Slf4j
public class StocksPdfOutputController {

    private StocksPdfOutputService pdfOutputService;
    private BasePdfOutputService basePdfOutputService;

    @Autowired    
    public StocksPdfOutputController(StocksPdfOutputService pdfOutputService, BasePdfOutputService basePdfOutputService) {
        this.pdfOutputService = pdfOutputService;
        this.basePdfOutputService = basePdfOutputService;
    }

    @RequestMapping(value = "/stocks/output/{id}/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody
    byte[] outputReport(@PathVariable Long id,@PathVariable OutputType type) {
        return basePdfOutputService.outputReport(id, type);
    }


    @RequestMapping(value = "/stocks/outputpopup", method = RequestMethod.GET)
    public @ResponseBody
    String outputReport(@RequestParam(value = "url", required = false) String url) {
        return "<div id=\"100\" class=\"pop\"><div class=\"panel panel-default\" >"
                + "<div class=\"panel-heading\">PrintOut</div>"
                + "<div class=\"panel-body\" >"
                + "<div class=\"row\">"
                + " <div class=\"col-lg-12 col-md-12 col-sm-12\">"
                + " <object class='printable' data=\"" + url + "\" style='width:100%'  height='500px'  type='application/pdf'>"
                + " <embed th:src='" + url + "'  style='width:100%'  height='500px' type='application/pdf' /></div>"
                + " </object>"
                + "</div> "
                + "<div class=\"row\">"
                + "<div class=\"col-lg-12 col-md-12 col-sm-12\" align=\"center\" ><input class=\"btn btn-default\" type='button' onclick=\"dropdownhide(100)\" value=\"Cancel\"  /></div> "
                + "</div></div></div>";
    }    

    @RequestMapping(value = "/stocks/pdfoutputs", method = RequestMethod.GET)
    public String pdfOutputs(@AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute PdfOutput pdfOutput) {
        pdfOutput = pdfOutput == null ? new PdfOutput() : pdfOutput;
        model.addAttribute("pdfOutputss", pdfOutputService.findAll());
        //model.addAttribute("pdfOutputss", pdfOutputService.findByAllBYSearchCriteria(pdfOutput));
        return "stocks/pdfoutputs";
    }

    @RequestMapping(value = "/stocks/pdfoutputsaction/{Id}", method = RequestMethod.GET)
    public String pdfOutputsaction(@AuthenticationPrincipal UserLogin userLogin, Model model, @PathVariable("Id") Long Id) {
        PdfOutput pdfOutput = pdfOutputService.getByID(Id);
        pdfOutput = pdfOutput == null ? new PdfOutput() : pdfOutput;
        model.addAttribute("pdfOutput", pdfOutput);
        return "stocks/pdfoutputsaction";

    }

    @RequestMapping(value = "/stocks/pdfoutputsForm", method = RequestMethod.POST)
    public String fileUpload(@RequestParam("file") MultipartFile file, Model model, @ModelAttribute PdfOutput pdfOutput, RedirectAttributes redirectAttributes)
            throws IOException {

        // Save 
        if (!file.getOriginalFilename().isEmpty() ) {
            pdfOutput.setJasperData(file.getBytes());
            String msg = pdfOutputService.save(pdfOutput);
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
            return "redirect:/stocks/pdfoutputs";
        } else {
            redirectAttributes.addFlashAttribute("msg", "setMsg(please select Valid file for upload)");
            return "redirect:/stocks/pdfoutputs";
        }

    }
    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setAutoGrowCollectionLimit(500000);
}
}
                                          