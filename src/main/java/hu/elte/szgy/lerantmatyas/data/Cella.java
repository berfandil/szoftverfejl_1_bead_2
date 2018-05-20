package hu.elte.szgy.lerantmatyas.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cella")
public class Cella implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id 
	private int cellaszam;
    private int maximalisferohely;
    private int szabadferohely;

	public int getCellaszam() { return cellaszam; }
	public void setCellaszam(int cellaszam) { this.cellaszam = cellaszam; }

	public int getMaximalisferohely() { return maximalisferohely; }
	public void setmMximalisferohely(int maximalisferohely) { this.maximalisferohely = maximalisferohely; }

	public int getSzabadferohely() { return szabadferohely; }
	public void setSzabadferohely(int szabadferohely) { this.szabadferohely = szabadferohely; }
	
    public Cella(int cellaszam, int maximalisferohely) {
    	this.cellaszam = cellaszam;
    	this.maximalisferohely = maximalisferohely;
    	this.szabadferohely = maximalisferohely;
    }
}
