package zw.co.hisolutions.pos.sell.entity;

import lombok.Data;
/**
 *
 * @author dgumbo
 */
@Data
public class SellResult {
    private TransactionStatus transactionStatus ;
    private Sell sell;
    private Receipt receipt;
}
