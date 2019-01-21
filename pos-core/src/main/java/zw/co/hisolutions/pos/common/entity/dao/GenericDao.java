package zw.co.hisolutions.pos.common.entity.dao;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;  
import zw.co.hisolutions.pos.common.entity.BaseEntity;

/**
 *
 * @author denzil
 * @param <T> 
 * @param <ID> 
 */
@Repository
public interface GenericDao<T extends BaseEntity, ID extends Serializable>  extends JpaRepository<T, Long>{  
     
//    public T getByName(String name); 
}
