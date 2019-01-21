package zw.co.hisolutions.pos.auth.entity.dao;
  
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zw.co.hisolutions.pos.auth.entity.User;

/**
 *
 * @author dgumbo
 */
@Repository
public interface UserDao extends JpaRepository<User, Long>{
    public User getByUsername(String username) ;
    public User findByUsername(String username) ;
}
