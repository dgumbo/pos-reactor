package zw.co.hisolutions.pos.auth.entity.dao;
  
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 
import zw.co.hisolutions.pos.auth.entity.UserRole;

/**
 *
 * @author dgumbo
 */
@Repository
public interface UserRoleDao extends JpaRepository<UserRole, Long>{
    public UserRole getByRole(String role);
    
}
