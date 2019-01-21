package zw.co.hisolutions.pos.auth.entity.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.hisolutions.pos.auth.entity.MenuRoles;

/**
 *
 * @author dgumbo
 */
public interface RoleMenuDao extends JpaRepository<MenuRoles, Long>{
    
}
