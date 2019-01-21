
package zw.co.hisolutions.pos.client.printer.service;

import java.util.List;
import java.util.Map;



@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class PdfOutputModel {
    private static final long serialVersionUID = 47L;
    private String reportName;
    private Map<String, Object> parameter;
    private List<Map<String, String>> fileldslist;
    private byte[] template;
}
