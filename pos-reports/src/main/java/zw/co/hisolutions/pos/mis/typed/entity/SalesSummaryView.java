package zw.co.hisolutions.pos.mis.typed.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 *
 * @author dgumbo
 */
@Data
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
public class SalesSummaryView {     
    private Date sellDate;
    private String productCategory; 
    private String stockItem; 
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice; 
   // private String createUsername; 
    private Date sellDatetime;  
}
    
