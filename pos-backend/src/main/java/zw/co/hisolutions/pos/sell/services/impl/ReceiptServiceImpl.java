package zw.co.hisolutions.pos.sell.services.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional; 
import zw.co.hisolutions.pos.sell.entity.Receipt;
import zw.co.hisolutions.pos.sell.entity.ReceiptItem;
import zw.co.hisolutions.pos.sell.entity.dao.ReceiptDao;
import zw.co.hisolutions.pos.sell.entity.dao.ReceiptItemDao;
import zw.co.hisolutions.pos.sell.services.ReceiptService;

/**
 *
 * @author dgumbo
 */
@Service
public class ReceiptServiceImpl implements ReceiptService {

    @Autowired
    private ReceiptDao receiptDao;
    @Autowired
    private ReceiptItemDao receiptItemDao;    
    
    @Override
    public Receipt getReceiptById(long id) {
        return receiptDao.getOne(id) ;
    }

    @Override
    public List<ReceiptItem> getReceiptItemsByReceiptId(long id) {
        return receiptItemDao.findByReceiptId(id) ;
    }

    @Override
    public void cancelReceipt(Receipt receipt, String reason) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    } 
    
    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
    public Receipt save(Receipt receipt, List<ReceiptItem> receiptItemList) {

        if (receiptItemList == null || receiptItemList.isEmpty()) {
            throw new UnsupportedOperationException("Items Cannot Be Empty"); //To change body of generated methods, choose Tools | Templates.
        }

        receiptItemList.stream().forEach(line -> {
            line.setReceipt(receipt);
        }); 
        receipt.setReceiptItems(receiptItemList);

        receiptDao.save(receipt);

        return receipt;
    }

    //@Override
    public JpaRepository<Receipt, Long> getDao() {
        return receiptDao;
    }

}
