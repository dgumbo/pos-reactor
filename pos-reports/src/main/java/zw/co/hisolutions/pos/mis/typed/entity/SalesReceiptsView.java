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
public class SalesReceiptsView {   
    private Date date;      
    private BigDecimal amount; 
    private String paymentType;
    private String currency; 
    private BigDecimal convertedAmount; 
    private Date dateTime;      
}
    
