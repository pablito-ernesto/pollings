/**
 * 
 */
package ar.com.nybble.futbol;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * @author notarip
 *
 */
@Entity
public class Club {

	private String nombre;
	private long Id;

	
	public Club() {
		// TODO Auto-generated constructor stub
	}
	
	public Club(String string) {
		this.setNombre(string);
	}

	public void setId(long id) {
		Id = id;
	}
	
	@Id 
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	public long getId() {
		return Id;
	}
	
	
	@Override
	public String toString() {
		return this.getNombre();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this.getNombre() == obj.toString())
			return true;
		return false;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}


}