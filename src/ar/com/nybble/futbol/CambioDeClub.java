/**
 * 
 */
package ar.com.nybble.futbol;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author notarip
 *
 */

@Entity
public class CambioDeClub {

	private Date fecha;
	private Club club;
	private Jugador jugador;
	

	public CambioDeClub(Club club, Date fecha, Jugador jugador) {
		this.fecha = fecha;
		this.club = club;
		this.jugador =(jugador);
	}

	
	public Club getClub() {
		return this.club;
	}

	@Temporal(value=TemporalType.DATE)
	@Id
	public Date getFecha() {
		return this.fecha;
	}

	@Id
	@ManyToOne
	@JoinColumn (name = "JUGADOR_ID", nullable = false)
	public Jugador getJugador() {
		return jugador;
	}
	
	

}