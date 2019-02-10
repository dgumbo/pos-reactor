package zw.co.hisolutions.pos.mis.typed.service.hqlGen.impl;

import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.mis.typed.entity.StockReOrderView; 
import zw.co.hisolutions.pos.mis.typed.service.hqlGen.StockReOrderHql;

/**
 *
 * @author dgumbo
 */
@Service
@Profile("dev")
public class StockReOrderHqlDevImpl implements StockReOrderHql{
   
    @PersistenceContext
    EntityManager entityManager;   

    @Override
    public TypedQuery<StockReOrderView> createNQuery(String stockItemName, Date dateBackAgo) {
        StringBuilder queryString = new StringBuilder("Select new " + StockReOrderView.class.getName() + " ( ");
        queryString.append("\n pc.name As productCategory, sti.name As stockItem " 
                + "\n 	, cs.quantity * 0 As requiredQuantity " 
                + "\n 	, Coalesce(sti.lastReceiptCostRate, 0) As unitCost "  
                + "\n 	, Coalesce(sti.lastReceiptCostRate, 0) * 0 As orderCost " 
                + "\n 	, cs.quantity As currentStock " 
                + "\n 	, Sum(si.quantity) As totalSales  "
                + "\n 	, Min(s.sellDate) As minSellDate  " 
                + "\n 	, 0.0 As averageDailySales  " 
                + "\n 	, cs.quantity * 0 As currentStockDepletionDays  " 
                + "\n 	, Coalesce(moq.minOrderQuantity, 1) As minOrderQuantity " 
                + "\n 	, cs.quantity * 0 As totalSafetyStock ");
        queryString.append("\n ) "); 
        queryString.append("\n From SellItem si "
                + "\n Join si.sell s "
                + "\n Join si.product sti "
                + "\n Join sti.productCategory pc  "
                + "\n Join CurrentStock cs On cs.stockItem.id = sti.id And cs.stockStatus = 'AVAILABLE' "
                + "\n Left Join MinimumOrderQuantity moq On moq.stockItem.id = sti.id ");
        queryString.append("\n Where s.sellDate >= :dateBackAgo ");
        if (stockItemName != null && !stockItemName.trim().equalsIgnoreCase("") && !stockItemName.trim().equalsIgnoreCase("all")) {
            queryString.append("\n And sti.name like :stockItemName ");
        }
        queryString.append("\n Group By cs.quantity, Coalesce(moq.minOrderQuantity, 1), pc.name, sti.name, Coalesce(sti.lastReceiptCostRate, 0) ");

        TypedQuery<StockReOrderView> qry = entityManager.createQuery(queryString.toString(), StockReOrderView.class);
        if (stockItemName != null && !stockItemName.trim().equalsIgnoreCase("") && !stockItemName.trim().equalsIgnoreCase("all")) {
            qry.setParameter("stockItemName", "%" + stockItemName + "%");
        }
        qry.setParameter("dateBackAgo", dateBackAgo);
            
        return qry;
    }

}
