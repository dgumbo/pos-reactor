package zw.co.psmi.hms.bill.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import zw.co.psmi.hms.basic.BasePdfOutputService;
import zw.co.psmi.hms.basic.OutputType;

@Controller
@Slf4j
public class BillPdfOutputController {
    @Autowired
    private BasePdfOutputService basePdfOutputService;

    @RequestMapping(value = "/bill/pdfoutput/{id}/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody
    byte[] billOutputReport(@PathVariable Long id, @PathVariable OutputType type) {
        return basePdfOutputService.outputReport(id, type);
    }

    @RequestMapping(value = "/bill/pdfoutput/{id}", method = RequestMethod.GET)
    public String billOutputReport(@PathVariable(value = "id") Long id) {
       /* String html = "<div id=\"100\" class=\"pop\"><div class=\"panel panel-primary\">\n"
                + "        <div class=\"panel-heading\">\n"
                + "             <h3 class=\"panel-title\">Print Out</h3>\n"
                + "        </div>\n"
                + "            <div class=\"panel-body\"><div class=\"scroll4\"> \n"
                + "                     <ul class=\"nav nav-tabs nav-justified\">\n"
                + "                         <li><a data-toggle=\"pill\"  href=\"#REFUND\" style='color:#007fff'>REFUND</a></li>\n"
                + "                         <li><a data-toggle=\"pill\"  href=\"#INVOICE\" style='color:#007fff'>INVOICE</a></li>\n"
                + "                      </ul>\n"
                + "\n"
                + "                      <div class=\"tab-content\">\n"
                + "                         <div id=\"INVOICE\" class=\"tab-pane fade\">\n"
                + "                             <object class='printable' data=\"/bill/pdfoutput/" + id + "/REFUND\" style='width:100%'  height='500px'  type='application/pdf'>"
                + "                             <embed src='/bill/pdfoutput/" + id + "/REFUND'  style='width:100%'  height='500px' type='text/plain' /></object>\n"
                + "                        </div>"
                + "                    </div>"
                + "                      <div class=\"tab-content\">\n"
                + "                         <div id=\"INVOICE\" class=\"tab-pane fade\">\n"
                + "                             <object class='printable' data=\"/bill/pdfoutput/" + id + "/INVOICE\" style='width:100%'  height='500px'  type='application/pdf'>"
                + "                             <embed src='/bill/pdfoutput/" + id + "/INVOICE'  style='width:100%'  height='500px' type='text/plain' /></object>\n"
                + "                        </div>\n"
                + "                    </div>\n"
                + "           </div>\n"
                + "           <div class='row'>\n"
                + "              <div class='col-xs-12 col-md-12 col-lg-12' align='center'><input class='btn btn-success' type='button' name='button2' id='button' value='Close' onclick='dropdownhide(100)'></div>\n"
                + "           </div>"
                + " </div>";*/
        return "";
    }
}
