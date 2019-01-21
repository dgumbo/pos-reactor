package zw.co.hisolutions.pos.mis.typed.dao;

import java.util.Date;
import java.util.List;  
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Service; 
import zw.co.hisolutions.pos.mis.typed.entity.DayendCashRegisterView; 

/**
 *
 * @author dgumbo
 */
@Service
public class DayendCashRegisterViewDao {

    @PersistenceContext
    EntityManager entityManager; 
    
    public List<DayendCashRegisterView> getReceiptedAmountData( Date fromDate, Date endDate) {

        StringBuilder queryString = new StringBuilder(" Select new " + DayendCashRegisterView.class.getName() + " ( ");
 
        queryString.append(" r.receiptDate as Date ");
        queryString.append(" , ri.amount as RecieptAmount ");
        queryString.append(" , 0 as DayendAmount ");
        queryString.append(" , pt.name as PaymentType  ");
        queryString.append(" , c.name as Currency ");  
        queryString.append(" , 0 as Variance "); 
        queryString.append(" , 0 as percentageVariance ");   
        queryString.append(" ) ");  
          
        
        queryString.append(" From Receipt r "); 
        queryString.append(" Join ReceiptItem ri On r.id = ri.receipt.id ");    
         
        queryString.append(" Join ri.paymentType pt "); 
        queryString.append(" Join ri.currency c ");  
        
        queryString.append(" Where r.receiptDateTime >= :fromDate ");
        queryString.append(" And r.receiptDateTime <= :endDate ");
  
         
        TypedQuery<DayendCashRegisterView> qry = entityManager.createQuery(queryString.toString(), DayendCashRegisterView.class);
         
        
        qry.setParameter("fromDate", fromDate);
        qry.setParameter("endDate", endDate);
         
        
        List<DayendCashRegisterView> resultList = qry.getResultList();
        System.out.println("fromDate : " + fromDate);
        System.out.println("endDate : " + endDate);
        System.out.println("resultList.size() : " + resultList.size());
        return resultList;
    }

//    public List<DayendCashRegisterView> getDeclaredAmount(String unitCode, String user, Date fromDate, Date endDate) {
//
//        StringBuilder queryString = new StringBuilder(" Select new zw.co.psmi.hms.reports.typedqueries.entity.DayendCashRegisterView ( ");
// 
//        queryString.append("   u.unit_code as 'Unit Code', u.name as 'Unit Name' "); 
//        queryString.append(" , r.receipt_date as 'Date' ");
//        queryString.append(" , Coalesce(usr.first_name + ' ' + usr.last_name, r.username) as 'User' ");
//        queryString.append(" , r.amount as 'Reciept Amount' ");
//        queryString.append(" , 0 as 'Dayend Amount' ");
//        queryString.append(" , pt.name as 'Payment Type'  ");
//        queryString.append(" , c.name as 'Currency' ");  
//        queryString.append(" , 0 as 'Variance' "); 
//        queryString.append(" , 0 'percentageVariance' ");  
//          
//        
//        queryString.append(" From Receipt r "); 
//        queryString.append(" Join ReceiptItem ri On r.id = ri.receipt.id ");    
//         
//        queryString.append(" Join ri.paymentType pt "); 
//        queryString.append(" Join ri.currency c "); 
//        
//        queryString.append(" Join r.unit u ");
//        
//        queryString.append(" Join User usr On r.username = usr.username "); 
//        
//        queryString.append(" Where r.receiptDateTime >= :fromDate ");
//        queryString.append(" And r.receiptDateTime <= :endDate ");
//
//        if (unitCode != null && !Utils.empty(unitCode) && !unitCode.trim().equalsIgnoreCase("") && !unitCode.trim().equalsIgnoreCase("all")) {
//            queryString.append(" And u.unitCode like :unitCode ");
//        }
//
//        if (user != null && !Utils.empty(user) && !user.trim().equalsIgnoreCase("") && !user.trim().equalsIgnoreCase("all")) {
//            queryString.append(" And Coalesce(usr.first_name + ' ' + usr.last_name, r.username) like like '%' + :user  + '%' ");
//        } 
//         
//        
//        TypedQuery<DayendCashRegisterView> qry = entityManager.createQuery(queryString.toString(), DayendCashRegisterView.class);
//        
//        
//        qry.setParameter("fromDate", fromDate);
//        qry.setParameter("endDate", endDate);
//        
//        if (!Utils.empty(unitCode) && !unitCode.trim().equalsIgnoreCase("") && !unitCode.trim().equalsIgnoreCase("all")) {
//            qry.setParameter("unitCode", unitCode);
//        }
//
//        if (!Utils.empty(user) && !user.trim().equalsIgnoreCase("") && !user.trim().equalsIgnoreCase("all")) {
//            qry.setParameter("user", user);
//        } 
//
//        return qry.getResultList();
//    }

}
