
package ar.com.nybble.futbol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ar.com.nybble.futbol.common.exceptions.JugadorSinClubException;
import ar.com.nybble.futbol.common.exceptions.JugadorSinLesionException;



/**
 * @author notarip
 *
 */

//TODO: hacer obligatorio el campo nombre
@Entity
@Table(name = "Jugador")
public class Jugador implements Persona {

	
	private long Id;
	private String nombre = new String(" ");
	private Date fechaNacimiento;
	private Documento documento;
	private Collection<CambioDeClub> clubs = new ArrayList<CambioDeClub>(); 
	private Collection<CambioDeEstado> estados = new ArrayList<CambioDeEstado>();
	private TipoDeLesion tipoDeLesion;
	private Nacionalidad nacionalidad;
	private Club club = null;
	
	
	

	public Jugador() {
		construccionEnComun();
	}
	
	public Jugador(Documento documento) {
		construccionEnComun();
		this.documento = documento;
	}
	
	private void construccionEnComun(){
		getEstados().add(new CambioDeEstado(TipoEstadosJugador.ACTIVO,new Date(), this));
	}
	
	private void setId(long id) {
		Id = id;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return Id;
	}
	
	@Column (name = "NOMBRE")
	public String getNombre() {
		return nombre;
	}

	@Override
	public void setNombre(String nombre) {
		this.nombre = nombre;

	}

	@Override
	public void setFechaNacimiento(Date fecha1) {
		this.fechaNacimiento = fecha1;
	}

	@Temporal(value = TemporalType.DATE)
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void agregarClub(Club club1, Date fecha2) {
		CambioDeClub cambio = new CambioDeClub(club1,fecha2,this);
		this.getCambiosDeClub().add(cambio);
		this.club = club1;
		if (tipoDeLesion == null)
			getEstados().add(new CambioDeEstado(TipoEstadosJugador.ACTIVO,fecha2, this));
		else
			getEstados().add(new CambioDeEstado(TipoEstadosJugador.LESIONADO,fecha2, this));
	}
	
	
	@Transient
	public Club getClubVigente() {
		Collection<CambioDeClub> cambios = getCambiosDeClub();
		CambioDeClub ultimoCambio = null;
		//Collections.sort(cambios);
		if (!cambios.isEmpty()){
			for (Iterator iterator = cambios.iterator(); iterator.hasNext();) {
				ultimoCambio = (CambioDeClub) iterator.next();
			}
			return  ultimoCambio.getClub();
		}	
		else
			return null;
	}


	
	public void iniciarActividadProfesional(Date fecha2) {
		getEstados().add(new CambioDeEstado(TipoEstadosJugador.ACTIVO,fecha2, this));
	}

	@Transient
	public boolean enActividad() {
		if (getEstado() == TipoEstadosJugador.ACTIVO){
			return true;
		}
		return false;
	}

	public void notificarLesion(Date fecha2, TipoDeLesion lesion) {
		getEstados().add(new CambioDeEstado(TipoEstadosJugador.LESIONADO,fecha2, this));
		tipoDeLesion = lesion;
	}


	public void setNacionalidad(Nacionalidad nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	@ManyToOne
	public Nacionalidad getNacionalidad() {
		return nacionalidad;
	}

	@Enumerated (EnumType.STRING)
	public TipoDeLesion getLesion() {
		return tipoDeLesion;
	}

	public void setLesion (TipoDeLesion lesion){
		this.tipoDeLesion = lesion;
	}
	
	public void recuperarActividad(Date fecha2) {
		if (getEstado() != TipoEstadosJugador.LESIONADO)
			throw new JugadorSinLesionException();
		else{
			this.tipoDeLesion  = null;
			//CambioDeEstado cambio =((List<CambioDeEstado>)this.getEstados()).get(getEstados().size()-2);
			CambioDeEstado cambio2 = new CambioDeEstado(TipoEstadosJugador.ACTIVO,fecha2,this);
			getEstados().add(cambio2);
			}
	}

	@Transient
	public TipoEstadosJugador getEstado() {
		Collection<CambioDeEstado> estados = getEstados();
		CambioDeEstado ultimoCambio = null;
		for (Iterator iterator = estados.iterator(); iterator.hasNext();) {
			ultimoCambio = (CambioDeEstado) iterator.next();
		}
		return  ultimoCambio.getEstado();
	}


	public void colgarLosGuantes(Date fecha2) {
		CambioDeEstado cambio = new CambioDeEstado(TipoEstadosJugador.RETIRADO,fecha2,this);
		getEstados().add(cambio);
	}

	@ManyToOne
	@JoinColumn(name= "CLUB_ID")
	public Club getClub() {
		return club;
	}
		
	
	public void desvincularClub(Date fecha2) throws JugadorSinClubException {
		if (getEstado() == TipoEstadosJugador.SIN_CLUB)
			throw new JugadorSinClubException();
		else{
			CambioDeEstado cambio = new CambioDeEstado(TipoEstadosJugador.SIN_CLUB,fecha2,this);
			getEstados().add(cambio);
			this.club.rescindirContratoJugador(this);
			this.club = null;
		}
	}

	@Transient
	public Date getFechaEstadoActual() {
		Collection<CambioDeEstado> estados = getEstados();
		CambioDeEstado ultimoCambio = null;
		if (!estados.isEmpty()){
			for (Iterator iterator = estados.iterator(); iterator.hasNext();) 
				ultimoCambio = (CambioDeEstado) iterator.next();
			return ultimoCambio.getFecha();
		}else
			return null;
	}


	@Transient
	public Object getFechaDeInicioClubActual() {
		Collection<CambioDeClub> cambios = getCambiosDeClub();
		CambioDeClub ultimoCambio = null;
		//Collections.sort(cambios);
		if (!cambios.isEmpty()){
			for (Iterator iterator = cambios.iterator(); iterator.hasNext();) {
				ultimoCambio = (CambioDeClub) iterator.next();
			}
			return  ultimoCambio.getFecha();
		}else
			return null;
	}

	@OneToOne
	public Documento getDocumento() {
		return this.documento;
	}

	@Override
	public void setDocumento(Documento documento) {
		this.documento = documento;
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this.getDocumento().equals(((Jugador) obj).getDocumento()))
			return true;
		return false;
	}
	
	
	public void setCambiosDeClub(Collection<CambioDeClub> cambiosDeClub) {
		this.clubs = cambiosDeClub;
	}
	
	@OneToMany (cascade = CascadeType.ALL, mappedBy = "jugador", fetch = FetchType.LAZY)
	public Collection<CambioDeClub> getCambiosDeClub() {
		return (Collection<CambioDeClub>) clubs;
	}


	public void setEstados(Collection<CambioDeEstado> estados) {
		this.estados = estados;
	}

	@OneToMany (cascade = CascadeType.ALL, mappedBy = "jugador", fetch = FetchType.LAZY)
	public Collection<CambioDeEstado> getEstados() {
		return (Collection<CambioDeEstado>) estados;
	}

	public void setClub(Club club) {
		this.club = club;
	}
	
	@Override
	public String toString() {
		return this.nombre;
	}
	
}
