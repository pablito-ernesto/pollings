/**
 * 
 */
package ar.com.nybble.futbol.services;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import ar.com.nybble.futbol.Club;
import ar.com.nybble.futbol.Documento;
import ar.com.nybble.futbol.Jugador;
import ar.com.nybble.futbol.Nacionalidad;
import ar.com.nybble.futbol.common.ContextFactory;
import ar.com.nybble.futbol.dataSource.repositorio.ClubRepositorio;
import ar.com.nybble.futbol.dataSource.repositorio.DocumentoRepositorio;
import ar.com.nybble.futbol.dataSource.repositorio.JugadorRepositorio;
import ar.com.nybble.futbol.dataSource.repositorio.NacionalidadRepositorio;
import ar.com.nybble.futbol.dataSource.util.DataSourceException;
import ar.com.nybble.futbol.dataSource.util.TransactionalProxyFactory;

/**
 * @author notarip
 *
 */
final class AbmJugadorServiceImpl implements AbmJugadorService {
	
	private JugadorRepositorio jugadorRepo;
	private ClubRepositorio clubRepo;
	private DocumentoRepositorio documentoRepo;
	private NacionalidadRepositorio nacionalidadRepo;
	
	public AbmJugadorServiceImpl(JugadorRepositorio jugadorRepo, ClubRepositorio clubRepo,
			DocumentoRepositorio documentoRepo, NacionalidadRepositorio nacionalidadRepo) {
		this.jugadorRepo = jugadorRepo;
		this.clubRepo = clubRepo;
		this.documentoRepo = documentoRepo;
		this.nacionalidadRepo = nacionalidadRepo;
	}

	public AbmJugadorServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	
	public JugadorRepositorio getJugadorRepo() {
		return jugadorRepo;
	}


	public ClubRepositorio getClubRepo() {
		return clubRepo;
	}


	public DocumentoRepositorio getDocumentoRepo() {
		return documentoRepo;
	}


	public NacionalidadRepositorio getNacionalidadRepo() {
		return nacionalidadRepo;
	}


	public void setJugadorRepo(JugadorRepositorio jugadorRepo) {
		this.jugadorRepo = jugadorRepo;
	}


	public void setClubRepo(ClubRepositorio clubRepo) {
		this.clubRepo = clubRepo;
	}


	public void setDocumentoRepo(DocumentoRepositorio documentoRepo) {
		this.documentoRepo = documentoRepo;
	}


	public void setNacionalidadRepo(NacionalidadRepositorio nacionalidadRepo) {
		this.nacionalidadRepo = nacionalidadRepo;
	}

	@Override
	public void crearJugador(String nombre, Date fechaDeNacimiento,
			Nacionalidad nacionalidad, Documento documento) throws DataSourceException {
		Long idNacionalidad = null;
		Nacionalidad nacionalidad2 = null;
		ApplicationContext context = ContextFactory.getInstancia();
		NacionalidadRepositorio nacionRepo = (NacionalidadRepositorio) context.getBean("nacionalidadRepositorio");
		
		//------Rutina de nacionalidad
		for ( Iterator nacionalidades = nacionRepo.findAll().iterator();  nacionalidades != null && nacionalidades.hasNext();) {
				nacionalidad2 = (Nacionalidad) nacionalidades.next();
			if (nacionalidad.equals(nacionalidad2)){
				idNacionalidad = new Long(nacionalidad2.getId());
				nacionalidades = null;
			}
		}
		if (idNacionalidad == null)
			nacionRepo.create(nacionalidad);
		else
			nacionalidad = nacionalidad2;		
		//-----------------------------
		
		//-----Rutina de Documento
		DocumentoRepositorio docuRepo = (DocumentoRepositorio) context.getBean("documentoRepositorio");
		docuRepo.create(documento);
		//-----------------------------
		
		JugadorRepositorio repo =  (JugadorRepositorio) TransactionalProxyFactory.newInstance(jugadorRepo);
		Jugador jugador = new Jugador ();
		jugador.setNombre(nombre.toUpperCase());
		jugador.setFechaNacimiento(fechaDeNacimiento);
		jugador.setNacionalidad(nacionalidad);
		jugador.setDocumento(documento);
		
		try {
			repo.create(jugador);
		} catch (DataSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void modificarJugador (Jugador jugador){
		JugadorRepositorio repo = (JugadorRepositorio) TransactionalProxyFactory.newInstance(jugadorRepo);
		
		try {
			repo.update(jugador);
		} catch (DataSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Jugador buscarJugador(Long id) {
		JugadorRepositorio repo = jugadorRepo;// (JugadorRepositorio) TransactionalProxyFactory.newInstance(jugadorRepo);
		try {
			return (Jugador) repo.findById(id);
		} catch (DataSourceException e) {
			System.out.println("Error a Buscar al Jugador " + id);
			e.printStackTrace();
			return null;
		}

		
	}

	@Override
	public Iterator<Jugador> buscarJugadores() {
		JugadorRepositorio repo = jugadorRepo; //(JugadorRepositorio) TransactionalProxyFactory.newInstance(jugadorRepo);
		try {
			return repo.findAll().iterator();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public Iterator<Jugador> buscarJugadoresPorClub(Long idClub){
		JugadorRepositorio repo = jugadorRepo; //(JugadorRepositorio) TransactionalProxyFactory.newInstance(jugadorRepo);
		List jugadores = new LinkedList<Jugador>();
		Club club = null;
		try {
			club = (Club) clubRepo.findById(idClub);
			return club.getJugadores().iterator();
		} catch (DataSourceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
//		try {
//			for (Iterator iterator = repo.findAll().iterator(); iterator.hasNext();) {
//				Jugador jugador = (Jugador) iterator.next();
//				if (jugador.getClubVigente() != null &&  jugador.getClubVigente().getId() == idClub){
//					jugadores.add(jugador);
//				}	
//			}
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			}
//		return jugadores.iterator(); 
	}

	@Override
	public Iterator<Jugador> buscarJugadoresPorNombre(String nombre){
		JugadorRepositorio repo = jugadorRepo; //(JugadorRepositorio) TransactionalProxyFactory.newInstance(jugadorRepo);
		List jugadores = new LinkedList<Jugador>();
				
		try {
			for (Iterator iterator = repo.findAll().iterator(); iterator.hasNext();) {
				Jugador jugador = (Jugador) iterator.next();
				if (jugador.getNombre().contains(nombre) || (jugador.getNombre().contains(nombre.toUpperCase())) ){
					jugadores.add(jugador);
				}	
			}
		} catch (Exception e) {
		
			e.printStackTrace();
			}
		return jugadores.iterator(); 
	}	
		

}
