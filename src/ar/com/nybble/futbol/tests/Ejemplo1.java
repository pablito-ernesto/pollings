/**
 * 
 */
package ar.com.nybble.futbol.tests;


import ar.com.nybble.futbol.Club;
import ar.com.nybble.futbol.Jugador;
import ar.com.nybble.futbol.Nacionalidad;
import ar.com.nybble.futbol.dataSource.repositorio.ClubRepositorioImpl;
import ar.com.nybble.futbol.dataSource.repositorio.JugadorRepositorioImpl;
import ar.com.nybble.futbol.dataSource.repositorio.NacionalidadRepositorioImpl;
import ar.com.nybble.futbol.dataSource.util.DataSourceException;
import ar.com.nybble.futbol.dataSource.util.HibernateUtil;



/**
 * @author notarip
 *
 */
public final class Ejemplo1 {

	/**
	 * @param args
	 * @throws DataSourceException
	 * Antes de comitear una entidad hay que comitear las que la componen
	 *  
	 */
	public static void main(String[] args) throws DataSourceException {
		try {
			HibernateUtil.currentSession("hibernate.cfg.xml");
		} catch (DataSourceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HibernateUtil.openSession("hibernate.cfg.xml");
		
		JugadorRepositorioImpl jugaRepo = new JugadorRepositorioImpl();
		ClubRepositorioImpl clubRepo = new ClubRepositorioImpl();
		NacionalidadRepositorioImpl nacionalidadRepo = new NacionalidadRepositorioImpl();  
		
		Club club = (Club) clubRepo.findById(new Long(1));
		Jugador pablo = (Jugador) jugaRepo.findById(new Long(5));
		Nacionalidad nacionalidad = (Nacionalidad) nacionalidadRepo.findById(new Long(1));
		
		 
		
		//Jugador pablo = new Jugador();
		//pablo.agregarClub(club, new Date());
		//pablo.setNombre("Pablo E. Notari");
		pablo.setNacionalidad(nacionalidad);
		
		
		try {
			HibernateUtil.beginTransaction();
			jugaRepo.create(pablo);
		} catch (DataSourceException e1) {
			e1.printStackTrace();
		}
		
		
		try {
			HibernateUtil.commitTransaction();
			HibernateUtil.closeSession("hibernate.cfg.xml");
			System.out.println("la cerro");
		} catch (DataSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
