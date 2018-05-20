package hu.elte.szgy.lerantmatyas.rest;

import hu.elte.szgy.lerantmatyas.data.Admin;
import hu.elte.szgy.lerantmatyas.data.AdminRepository;

import javax.persistence.EntityExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
@Transactional
public class AdminManager {
	private static Logger log = LoggerFactory.getLogger(AdminManager.class);

	@Autowired
	private AdminRepository adminDao;

    private String printAdmin(Admin a) {
		return "{" +
				" \"felhasznalonev\":\"" + a.getFelhasznalonev() + "\", " +
				" \"nev\":\"" + a.getNev() + "\", " +
				" \"szigszam\":\"" + a.getSzigszam() + "\", " +
				"}";
    }
    
    @GetMapping("/self")
    public String selfAdmin(Authentication auth) {
		Admin a = adminDao.getOne(auth.getName());
		return printAdmin(a); 
	}
    
    @GetMapping("/{userid}")
    public String otherAdmin(@PathVariable("userid") String felhasznalonev, Authentication auth) {
		Admin a = adminDao.getOne(felhasznalonev);
		return printAdmin(a);
	}
    
    @PostMapping("/new")
    public ResponseEntity<Void> createUser(@RequestBody(required=false) Admin a, Authentication auth) {
		log.info( "CREATING NEW ADMIN" );
        if(!a.getJelszo().startsWith( "{" )) a.setJelszo( "{noop}" + a.getJelszo() );
        if(adminDao.existsById( a.getFelhasznalonev() )) {
        	throw new EntityExistsException("Name already used");
        }
        adminDao.save(a);
        log.info( "Creating user: " + a.getFelhasznalonev() );
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
    
    @PostMapping("/delete/{userid}")
	public ResponseEntity<Void> deleteUser(@PathVariable("userid") String felhasznalonev, Authentication auth) {
		Admin a = adminDao.getOne(felhasznalonev);
		adminDao.delete( a );
		log.info( "ADMIN DELETED" );
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }
    
    @PostMapping("/name")
    public ResponseEntity<Void> setSelfName(@RequestBody NameDTO name, Authentication auth) {
		Admin a = adminDao.getOne(auth.getName());
		a.setNev(name.getNew_name());
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}
    
    @PostMapping("/password")
	public ResponseEntity<Void> setSelfpassword(@RequestBody PassDTO pass, Authentication auth) {
    	Admin a = adminDao.getOne(auth.getName());
		a.setJelszo( "{noop}" + pass.getNew_pass() );
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }
}
