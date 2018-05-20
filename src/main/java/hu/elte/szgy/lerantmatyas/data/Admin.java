package hu.elte.szgy.lerantmatyas.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="admin")
public class Admin implements Serializable { 
	private static final long serialVersionUID = 1L;
	
	private String nev;
	private int szigszam;
	@Id
    private String felhasznalonev;
    private String jelszo;
    
    public String getNev() { return this.nev; }
    public void setNev(String nev) { this.nev = nev; }
    
    public int getSzigszam() { return this.szigszam; }
    
	public String getFelhasznalonev() { return this.felhasznalonev; }
    public void setFelhasznalonev(String felhasznalonev) { this.felhasznalonev = felhasznalonev; }
    
    public String getJelszo() { return this.jelszo; }
    public void setJelszo(String jelszo) { this.jelszo = jelszo; }
    
    public Admin(String nev, int szigszam, String felhasznalonev, String jelszo) {
    	this.nev = nev;
    	this.szigszam = szigszam;
    	this.felhasznalonev = felhasznalonev;
    	this.jelszo = jelszo;
    }
}

