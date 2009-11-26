/**
 * 
 */
package ar.com.nybble;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author notarip
 *
 */
public class Jugador {

	private String nombre;
	private Fecha fecha;
	private LinkedList<Club> clubs = new LinkedList<Club>();
	private LinkedList<Fecha> fechasDeCambioActividad = new LinkedList<Fecha>();
	private EstadosJugador estado;
	
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
		
		
	}

	public void setNacimiento(Fecha fecha1) {
		this.fecha = fecha1;
		
	}

	public Fecha getFecha() {
		// TODO Auto-generated method stub
		return fecha;
	}

	public void agregarClub(Club club1) {
		clubs.add(club1);
		
	}

	public Club getClubVigente() {
		Iterator<Club> lista = clubs.iterator();
		if (!lista.hasNext()) {
			return null;
		}
		return lista.next();
	}

	public void iniciarActividadProfesional(Fecha fecha2) {
		fechasDeCambioActividad.add(fecha2);
		estado = EstadosJugador.ACTIVO;
	}

	public boolean enActivudad() {
	
		if (estado == EstadosJugador.ACTIVO){
			return true;
		}
		return false;
	}
}