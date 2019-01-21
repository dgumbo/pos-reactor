package zw.co.hisolutions.pos.dayend.entity.dao;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 
import zw.co.hisolutions.pos.dayend.entity.DayEnd;

/**
 *
 * @author kinah
 */
@Repository
public interface DayEndDao extends JpaRepository<DayEnd, Long> { 
    
    public List<DayEnd> findByModifiedByUserContainingIgnoreCaseAndDayEndDateBetweenOrderByIdDesc(String username, Date startDate, Date dayEndDate);

    public List<DayEnd> findByDayEndDateBetweenOrderByIdDesc(Date startDate, Date dayEndDate);
    
}
