package zw.co.hisolutions.pos.backend.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zw.co.hisolutions.pos.sell.entity.Receipt;

/**
 *
 * @author dgumbo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartHolder {

    private List<CartItemHolder> items;
    private long itemsCount;
    private BigDecimal totalAmount;
    private Receipt payment;

    private Date dateSaleDate;
    private boolean dateSale;
}
