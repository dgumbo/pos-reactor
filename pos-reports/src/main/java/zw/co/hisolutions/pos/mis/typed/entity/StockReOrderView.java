package zw.co.hisolutions.pos.mis.typed.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class StockReOrderView { 
    private String productCategory;
    private String stockItem;
    private long requiredQuantity;
    private BigDecimal unitCost;
    private BigDecimal orderCost;
    private long currentStock;
    private long totalSales; 
    private Date minSellDate; 
    private double averageDailySales;
    private long currentStockDepletionDays;
    private long minOrderQuantity;
    private long totalSafetyStock;  
} 