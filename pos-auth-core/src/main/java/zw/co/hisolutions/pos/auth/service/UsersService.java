package zw.co.hisolutions.pos.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.auth.entity.UserRole;
import zw.co.hisolutions.pos.auth.entity.User;

/**
 *
 * @author dgumbo
 */
public interface UsersService extends GenericService<User, Long>, UserDetailsService{
    User createUser(User user);
    UserRole createAuthority(UserRole authority);

    public User findUserByUsername(String username);
}
