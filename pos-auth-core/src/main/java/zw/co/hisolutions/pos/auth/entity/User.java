package zw.co.hisolutions.pos.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.Date;
import java.util.List; 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import zw.co.hisolutions.pos.common.entity.BaseEntity;

/**
 *
 * @author dgumbo
 */
@Entity(name="users")
@Data
@AllArgsConstructor 
//@NoArgsConstructor 
public class User extends BaseEntity  implements UserDetails{
    
    public User(){
    }
     
    public User(String username, Date creationDate, Collection<? extends GrantedAuthority> authorities) {
        this.username = username; 
        this.creationDate=creationDate;
        //this.roles = authorities;
    }
    
    @Column(unique = true)
    private String username;     
    private String password;
    private boolean enabled ; 
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastPasswordResetDate ;
    
    @Transient
    //@JsonIgnore
    private String jwtToken ;
    
    @Transient
    //@JsonIgnore
    private Date creationDate ;
      
    @ManyToMany( fetch = FetchType.EAGER )
    @OrderColumn 
    private List<UserRole> roles; 
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {        
        return roles; 
    }  
 
    public void setAuthorities(List<UserRole> roles) {        
        //this. roles = roles; 
    }  
     
    @JsonIgnore
    @Override
    public String getPassword() {
       return password;
    }

    @JsonProperty
    public void setPassword(String password) {
       this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
       return activeStatus;
    }

    @Override
    public boolean isAccountNonLocked() {
       return activeStatus;
    }

    @Override
    public boolean isCredentialsNonExpired() {
       return enabled;
    }

}
