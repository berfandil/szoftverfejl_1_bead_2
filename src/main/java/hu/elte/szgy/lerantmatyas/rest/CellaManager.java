package hu.elte.szgy.lerantmatyas.rest;

import hu.elte.szgy.lerantmatyas.data.Cella;
import hu.elte.szgy.lerantmatyas.data.CellaRepository;
import hu.elte.szgy.lerantmatyas.data.Rab;
import hu.elte.szgy.lerantmatyas.data.RabRepository;

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
@RequestMapping("cell")
@Transactional
public class CellaManager {
	private static Logger log = LoggerFactory.getLogger(CellaManager.class);

	@Autowired
	private CellaRepository cellaDao;
	@Autowired
	private RabRepository rabDao;
    
    @GetMapping("/{cellid}")
    public String printCella(@PathVariable("cellid") int cellaszam, Authentication auth) {
		Cella c = cellaDao.getOne(cellaszam);
		return "{" +
			   " \"cell number\":\"" + c.getCellaszam() + "\", " +
			   " \"max places\":\"" + c.getMaximalisferohely() + "\", " +
			   " \"free spaces\":\"" + c.getSzabadferohely() + "\", " +
			   "}";
	}

    @GetMapping("/fullnessinfo/today")
    public String printFullnessInfoForDate(@RequestBody Date date, Authentication auth) {
    	Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date datum = cal.getTime();
		String releaseInfo = "Fullness on " + datum + ":\n";
		List<Cella> cellak = cellaDao.findAll();
		for (Cella cella : cellak)
		{
			int max_ferohely = cella.getMaximalisferohely();
			int foglalt_helyek = max_ferohely - cella.getSzabadferohely();
			List<Rab> rabok = rabDao.findAll();
			for (Rab rab : rabok)
				if (rab.getCellaszam() == cella.getCellaszam() && rab.getSzabadulasdatuma().before(datum))
					foglalt_helyek -= 1;
			releaseInfo += "  [ cell id: " + cella.getCellaszam() + " | " +
						   foglalt_helyek + " / " + max_ferohely + 
						   " | " + (foglalt_helyek / max_ferohely * 100) + "% ]\n";
		}
		return releaseInfo;
    }
    
    @GetMapping("/fullnessinfo/date")
    public String printFullnessInfoForInterval(@RequestBody Date date, @RequestBody int num_days, Authentication auth) {
		String releaseInfo = "BEGIN FULLNESS LIST\n";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		for (int i = 0; i < num_days; i += 1)
		{
			cal.add(Calendar.DATE, 1);
			releaseInfo += printFullnessInfoForDate(cal.getTime(), auth);
		}
		releaseInfo += "END FULLNESS LIST\n";
		return releaseInfo;
	}

    @PostMapping("/new")
    public ResponseEntity<Void> createCella(@RequestBody(required=false) Cella c, Authentication auth) {
		log.info( "CREATING NEW CELL" );
        if(cellaDao.existsById( c.getCellaszam() )) {
        	throw new EntityExistsException("Cella already existing");
        }
        cellaDao.save(c);
        log.info( "New cell registered: " + c.getCellaszam() );
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
    
    @PostMapping("/delete/*")
    public ResponseEntity<Void> deleteCella(@PathVariable("personalid") int cellaszam, Authentication auth) {
		Cella c = cellaDao.getOne(cellaszam);
		if (c.getSzabadferohely() < c.getMaximalisferohely())
		{
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		cellaDao.delete( c );
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }
    
    @PostMapping("/update/places/*")
    public ResponseEntity<Void> setCellaMax(@PathVariable("personalid") int cellaszam, @RequestBody int uj_max, Authentication auth) {
		Cella c = cellaDao.getOne(cellaszam);
		int foglalt_helyek = c.getMaximalisferohely() - c.getSzabadferohely();
		if (uj_max < foglalt_helyek)
		{
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		c.setmMximalisferohely(uj_max);
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}
    
    @PostMapping("/reorder")
    public ResponseEntity<Void> cellaReorder(Authentication auth) {
    	List<Cella> cellak = cellaDao.findAll();
    	
    	// TODO
    	
    	return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }
}
