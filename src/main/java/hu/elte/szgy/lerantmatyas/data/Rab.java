package hu.elte.szgy.lerantmatyas.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="rab") 
public class Rab implements Serializable { 
	private static final long serialVersionUID = 1L;
	@Id 
	private int szigszam;
    private String nev;
    private Date bekerulesdatuma;
    private int buntetesiido;
    private int cellaszam;

	public int getSzigszam() { return szigszam; }
	public void setSzigszam(int szigszam) { this.szigszam = szigszam; }
	
	public String getNev() { return nev; }
	public void setNev(String nev) { this.nev = nev; }
	
	public Date getBekerulesdatuma() { return bekerulesdatuma; }
	
	public int getBuntetesiido() { return buntetesiido; }
	public void setBuntetesiido(int buntetesiido) { this.buntetesiido = buntetesiido; }
	
	public int getCellaszam() { return cellaszam; }
	public void setCellaszam(int cellaszam) { this.cellaszam = cellaszam; }
	
	public Date getSzabadulasdatuma() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(bekerulesdatuma);
		cal.add(Calendar.DATE, buntetesiido);
		return cal.getTime();
	}
	
	private Date Today() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
    public Rab(int szigszam, String nev, int buntetesiido, int cellaszam) {
    	this.szigszam = szigszam;
    	this.nev = nev;
    	this.bekerulesdatuma = Today();
    	this.buntetesiido = buntetesiido;
    	this.cellaszam = cellaszam;
    }
}
