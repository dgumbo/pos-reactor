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
@Profile("prod")
public class StockReOrderHqlProdImpl implements StockReOrderHql {

    @PersistenceContext
    EntityManager entityManager; 
    
    @Override
    public TypedQuery<StockReOrderView> createNQuery(String stockItemName, Date dateBackAgo, int daysBackAgo, long leadDays) {
        StringBuilder queryString = new StringBuilder("Select new " + StockReOrderView.class.getName() + " ( ");
        queryString.append("\n pc.name As productCategory, sti.name As stockItem "
                + "\n  "
                + "\n 	, CEILING((( "
                + "\n 		Sum(si.quantity) /  "
                + "\n 		Case  "
                + "\n 			When DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) = 0 Then :daysBackAgo "
                + "\n 			When DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) < :daysBackAgo Then DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) "
                + "\n 			Else :daysBackAgo "
                + "\n 		End ) "
                + "\n 	  * :leadDays) "
                + "\n 	  - cs.quantity ) As requiredQuantity "
                + "\n  "
                + "\n 	, Coalesce(sti.lastReceiptCostRate, 0) As unitCost "
                + "\n  "
                + "\n 	, Coalesce(sti.lastReceiptCostRate, 0)  "
                + "\n 	  * CEILING((( "
                + "\n 		Sum(si.quantity) /  "
                + "\n 		Case  "
                + "\n 			When DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) = 0 Then :daysBackAgo "
                + "\n 			When DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) < :daysBackAgo Then DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) "
                + "\n 			Else :daysBackAgo "
                + "\n 		End ) "
                + "\n 	  * :leadDays) "
                + "\n 	  - cs.quantity ) As orderCost "
                + "\n 	  "
                + "\n 	, cs.quantity As currentStock "
                + "\n  "
                + "\n 	, CEILING( "
                + "\n 		Sum(si.quantity) /  "
                + "\n 		Case  "
                + "\n 			When DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) = 0 Then :daysBackAgo "
                + "\n 			When DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) < :daysBackAgo Then DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) "
                + "\n 			Else :daysBackAgo "
                + "\n 		End ) As averageDailySales  "
                + "\n  "
                + "\n 	, FLOOR( "
                + "\n 		cs.quantity /  "
                + "\n 		Case When Sum(si.quantity) = 0 Then 1 Else Sum(si.quantity) End /  "
                + "\n 		Case  "
                + "\n 			When DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) = 0 Then :daysBackAgo "
                + "\n 			When DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) < :daysBackAgo Then DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) "
                + "\n 			Else :daysBackAgo "
                + "\n 		End ) As currentStockDepletionDays  "
                + "\n  "
                + "\n 	, Coalesce(moq.minOrderQuantity, 1) As minOrderQuantity "
                + "\n  "
                + "\n 	, CEILING( "
                + "\n 		Sum(si.quantity) /  "
                + "\n 		Case  "
                + "\n 			When DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) = 0 Then :daysBackAgo "
                + "\n 			When DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) < :daysBackAgo Then DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) "
                + "\n 			Else :daysBackAgo "
                + "\n 		End "
                + "\n 	  * :leadDays ) As totalSafetyStock ");
        queryString.append("\n ) ");
        //        queryString.append("\n From Sell s "
//                + "\n Join SellItem si On si.sell.id = s.id "
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
        queryString.append("\n Having cs.quantity < CEILING( "
                + "\n 		Sum(si.quantity) /  "
                + "\n 		Case  "
                + "\n 			When DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) = 0 Then :daysBackAgo "
                + "\n 			When DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) < :daysBackAgo Then DateDiff( Day, Min(s.sellDate), CURRENT_TIMESTAMP ) "
                + "\n 			Else :daysBackAgo "
                + "\n 		End ) "
                + "\n 	  * :leadDays ");
        queryString.append(" Order By currentStockDepletionDays Asc, cs.quantity Asc ");
        TypedQuery<StockReOrderView> qry = entityManager.createQuery(queryString.toString(), StockReOrderView.class);
        if (stockItemName != null && !stockItemName.trim().equalsIgnoreCase("") && !stockItemName.trim().equalsIgnoreCase("all")) {
            qry.setParameter("stockItemName", "%" + stockItemName + "%");
        }
        qry.setParameter("dateBackAgo", dateBackAgo);
        qry.setParameter("daysBackAgo", daysBackAgo);
        qry.setParameter("leadDays", leadDays);
        return qry;
    }

}
