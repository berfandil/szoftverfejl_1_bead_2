package hu.elte.szgy.lerantmatyas.rest;


import hu.elte.szgy.lerantmatyas.data.Admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SingsingAdminPrincipal implements UserDetails {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Admin admin;
	private List<GrantedAuthority> auths=new ArrayList<GrantedAuthority>(5);
 
    public SingsingAdminPrincipal(Admin admin) {
        this.admin = admin;
    }

    public java.util.Collection<? extends GrantedAuthority> getAuthorities() { return auths; }
	public java.lang.String getUsername() { return admin.getFelhasznalonev(); }
	public java.lang.String getPassword() { return admin.getJelszo(); }
	public String getSingsingFelhasznalonev() { return admin.getFelhasznalonev(); }

	public boolean isEnabled() { return true; }
	public boolean isCredentialsNonExpired() { return true; }
	public boolean isAccountNonExpired() { return true; }
	public boolean isAccountNonLocked() { return true; }
}
