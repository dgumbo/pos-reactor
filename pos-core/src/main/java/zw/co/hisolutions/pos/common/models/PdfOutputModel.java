package zw.co.hisolutions.pos.common.models;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
  
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdfOutputModel {
    private static final long serialVersionUID = 47L;
    private String reportName;
    private Map<String, Object> parameter;
    private List<Map<String, String>> fileldslist;
    private byte[] template;
}
