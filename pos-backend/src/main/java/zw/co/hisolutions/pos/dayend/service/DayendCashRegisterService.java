package zw.co.hisolutions.pos.dayend.service;

import java.util.Date;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 

/**
 *
 * @author dgumbo
 */ 
public interface DayendCashRegisterService { 
    public XSSFWorkbook getWorkBook(String unitCode, String username, Date fromDate, Date endDate) ;
  
}
