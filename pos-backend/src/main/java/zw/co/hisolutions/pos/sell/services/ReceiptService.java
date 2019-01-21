package zw.co.hisolutions.pos.sell.services;

import java.util.List;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional; 
import zw.co.hisolutions.pos.sell.entity.Receipt;
import zw.co.hisolutions.pos.sell.entity.ReceiptItem;

/**
 *
 * @author dgumbo
 */
public interface ReceiptService {//extends BasicListService<Receipt, ReceiptItem> {
  
    Receipt getReceiptById(long id);

    List<ReceiptItem> getReceiptItemsByReceiptId(long id);

    void cancelReceipt(Receipt receipt, String reason);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
     Receipt save(Receipt receipt, List<ReceiptItem> receiptItems); 

}
