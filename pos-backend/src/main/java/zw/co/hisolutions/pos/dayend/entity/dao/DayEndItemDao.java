package zw.co.hisolutions.pos.dayend.entity.dao;
 
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 
import zw.co.hisolutions.pos.dayend.entity.DayEndItem;

/**
 *
 * @author kinah
 */
@Repository
public interface DayEndItemDao extends JpaRepository<DayEndItem, Long> {

    public List<DayEndItem> findByDayEndId(Long id);
    
}
