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
public class StockOutView {
    private String productCategory;
    private String stockItem;
    private long quantity;
    private BigDecimal lrcr;
    private BigDecimal wac;
    private BigDecimal sellingPrice;
    private String currency;
}
