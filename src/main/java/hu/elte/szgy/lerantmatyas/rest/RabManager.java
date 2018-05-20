package hu.elte.szgy.lerantmatyas.rest;

import hu.elte.szgy.lerantmatyas.data.Rab;
import hu.elte.szgy.lerantmatyas.data.RabRepository;
import hu.elte.szgy.lerantmatyas.data.Cella;
import hu.elte.szgy.lerantmatyas.data.CellaRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
@RequestMapping("prisoner")
@Transactional
public class RabManager {
	private static Logger log = LoggerFactory.getLogger(RabManager.class);

	@Autowired
	private RabRepository rabDao;
	@Autowired
	private CellaRepository cellaDao;
    
	@GetMapping("/{personalid}")
    public String printRab(@PathVariable("personalid") int szigszam, Authentication auth) {
		Rab r = rabDao.getOne(szigszam);
		return "{" +
			   " \"name\":\"" + r.getNev() + "\", " +
			   " \"personal id\":\"" + r.getSzigszam() + "\", " +
			   " \"first day in:\":\"" + r.getBekerulesdatuma() + "\", " +
			   " \"sentence\":\"" + r.getBuntetesiido() + "\", " +
			   " \"first day out\":\"" + r.getSzabadulasdatuma() + "\", " +
			   " \"cell\":\"" + r.getCellaszam() + "\", " +
			   "}";
	}
	
	@GetMapping("/releaseinfo/date")
	public String printReleaseInfoForDate(@RequestBody Date date, Authentication auth) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date datum = cal.getTime();
		String releaseInfo = "Releases on " + datum + ":\n";
		List<Rab> rabok = rabDao.findAll();
		for (Rab rab : rabok)
			if (rab.getSzabadulasdatuma() == datum)
				releaseInfo += "  [ personal id: " + rab.getSzigszam() + " | " + 
							   "name: " + rab.getNev() + " | " +
							   "cell: " + rab.getCellaszam() + "]\n";
		return releaseInfo;
	}
	
	@GetMapping("/releaseinfo/interval")
	public String printReleaseInfoForInterval(@RequestBody Date date, @RequestBody int num_days, Authentication auth) {
		String releaseInfo = "BEGIN RELEASES LIST\n";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		for (int i = 0; i < num_days; i += 1)
		{
			cal.add(Calendar.DATE, 1);
			releaseInfo += printReleaseInfoForDate(cal.getTime(), auth);
		}
		releaseInfo += "END RELEASES LIST\n";
		return releaseInfo;
	}
	
	@PostMapping("/new")
	public ResponseEntity<Void> createRab(@RequestBody(required=false) Rab r, Authentication auth) {
		log.info( "CREATING NEW PRISONER" );
        if(rabDao.existsById( r.getSzigszam() )) {
        	throw new EntityExistsException("Rab already inside");
        }
        rabDao.save(r);
        log.info( "New prisoner registered: " + r.getSzigszam() );
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
	
	@PostMapping("/delete/{personalid}")
	public ResponseEntity<Void> deleteRab(@PathVariable("personalid") int szigszam, Authentication auth) {
		Rab r = rabDao.getOne(szigszam);
		int cellaszam = r.getCellaszam();
		Cella c = cellaDao.getOne(cellaszam);
		
		rabDao.delete( r );
		c.setSzabadferohely(c.getSzabadferohely() + 1);
		
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }
	
	@PostMapping("/update/sentence/{personalid}")
	public ResponseEntity<Void> setRabSentence(@PathVariable("personalid") int szigszam, @RequestBody int uj_buntetesiido, Authentication auth) {
		Rab r = rabDao.getOne(szigszam);
		r.setBuntetesiido(uj_buntetesiido);
		Date szabadulasdatuma = r.getSzabadulasdatuma();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date today = cal.getTime();
		
		if (szabadulasdatuma == today)
			deleteRab(r.getSzigszam(), auth);
		
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}

    @PostMapping("/update/cell/{personalid}")
    public ResponseEntity<Void> setRabCella(@PathVariable("personalid") int szigszam, @RequestBody int uj_cellaszam, Authentication auth) {
    	Rab r = rabDao.getOne(szigszam);
    	int regi_cellaszam = r.getCellaszam();
    	if (regi_cellaszam != uj_cellaszam)
    	{
    		Cella uj_c = cellaDao.getOne(uj_cellaszam);
    		if (uj_c.getSzabadferohely() > 0)
    		{
    			
    			Cella regi_c = cellaDao.getOne(regi_cellaszam);
    			regi_c.setSzabadferohely(regi_c.getSzabadferohely() + 1);
    			uj_c.setSzabadferohely(uj_c.getSzabadferohely() - 1);
    			r.setCellaszam(uj_cellaszam);
    		}
    	}
    	return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }
}
