package zw.co.hisolutions.pos.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import zw.co.hisolutions.pos.auth.entity.dao.RoleMenuDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service; 
import zw.co.hisolutions.pos.auth.entity.MenuRoles;
import zw.co.hisolutions.pos.auth.service.MenuRolesService;

/**
 *
 * @author dgumbo
 */
@Service
public class MenuRolesServiceImpl implements MenuRolesService{ 
    private final RoleMenuDao roleMenuDao;

    @Autowired
    public MenuRolesServiceImpl(RoleMenuDao roleMenuDao) {
        this.roleMenuDao = roleMenuDao;
    }
    
    
    @Override
    public JpaRepository<MenuRoles, Long> getDao() {
        return roleMenuDao;
    }

    @Override
    public Class getController() {
       return null;
    }
}
