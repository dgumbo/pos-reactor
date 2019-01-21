package zw.co.hisolutions.pos.dayend.service;

import java.util.List; 
import zw.co.hisolutions.pos.dayend.entity.DayEnd;
import zw.co.hisolutions.pos.dayend.entity.DayEndItem;

/**
 *
 * @author kinah
 */
public interface DayEndService {

    public List<DayEnd> getAllBySearch(DayEnd dayEnd);

    public DayEnd getByID(Long id);

    public DayEnd save(DayEnd dayEnd);
    
    public List<DayEndItem> getByDayEndId(Long id);

    public List<DayEnd> findAll();
    
}
