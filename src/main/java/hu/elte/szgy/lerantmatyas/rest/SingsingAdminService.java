package hu.elte.szgy.lerantmatyas.rest;

import hu.elte.szgy.lerantmatyas.data.Admin;
import hu.elte.szgy.lerantmatyas.data.AdminRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

public class SingsingAdminService  implements UserDetailsService {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger( SingsingAdminService.class );
	
    @Autowired
    private AdminRepository adminRepository;
 
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String felhasznalonev) {
    	logger.info("Authenticating " + felhasznalonev);
        Admin admin = adminRepository.findById(felhasznalonev).get();
    	logger.info("User data " + admin.getJelszo());
        return new SingsingAdminPrincipal(admin);
    }
}
