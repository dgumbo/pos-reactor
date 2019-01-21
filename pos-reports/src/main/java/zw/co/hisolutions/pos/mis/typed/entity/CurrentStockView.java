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
public class CurrentStockView {
    private String productCategory;
    private String stockItem;
    private long quantity;
    private BigDecimal lrcr;
    private BigDecimal wac;
    private BigDecimal lrcrTotalValue;
    private BigDecimal wacTotalValue;
    private BigDecimal sellingPrice;
    private BigDecimal totalSellingPrice;
    private String currency;
    private String batchNumber;
    private Date expiryDate;
    private Object stockStatus;
    private String consignment;
}
