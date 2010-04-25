package ar.com.nybble.futbol.view;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;

import ar.com.nybble.futbol.Club;
import ar.com.nybble.futbol.Jugador;
import ar.com.nybble.futbol.services.AbmClubService;
import ar.com.nybble.futbol.services.AbmJugadorService;
import ar.com.nybble.futbol.services.common.SpringContext;

/**
 * @author Administrador
 */
public class ConsultaClubs extends WebPage {
	
	
	private static final String POR_CLUB = new String("Por Club");
	private static final String POR_JUGADOR = new String("Por Jugador");
	private static final List<String> CRITERIOS = Arrays.asList(new String[] {POR_CLUB,
    POR_JUGADOR});
	
	Form formBusqueda = new Form("formBusqueda");
	TextField busqueda = new TextField("busqueda", new Model(""));
	RadioChoice<String> radioCriterioGroup = new RadioChoice<String>("radioChoise",new Model("") ,CRITERIOS);
	
	Label resultado = new Label("resultado", new Model(""));
	Label criterio = new Label("criterio", new Model(""));
	
	Form formResultados = new Form("formResultados");
	
	
	
	
	public ConsultaClubs() {
		formBusqueda.add(busqueda);
		formBusqueda.add(radioCriterioGroup);
		formBusqueda.add(resultado);
		formBusqueda.add(criterio);
		
		formBusqueda.add(new Button("buscar"){
			@Override
			public void onSubmit() {
				String busquedaTxt = (String) busqueda.getModelObject();
				String criterioTxt = radioCriterioGroup.getDefaultModelObjectAsString();
				consultarYModelar(busquedaTxt,criterioTxt);
				resultado.setDefaultModelObject(busquedaTxt);
				criterio.setDefaultModelObject(criterioTxt);
			}
		});
		add(formBusqueda);
		add(formResultados);
		formResultados.add(new Label("itemResultado"));
	}
	
	private void consultarYModelar(String busquedaTxt, String criterioTxt) {
		AbmClubService abmClub = (AbmClubService) SpringContext.context.getBean("AbmClubService");
		AbmJugadorService abmJugador = (AbmJugadorService) SpringContext.context.getBean("AbmJugadorService");
		List resultado = new LinkedList<Jugador>();
		
		if (criterioTxt == POR_CLUB){
			for (Iterator iterator = abmClub.buscarClubsPorNombre(busquedaTxt);; iterator.hasNext()) {
				Club club = (Club) iterator.next();
				for (Iterator iterator2 = abmJugador.buscarJugadoresPorClub(club.getId()); iterator2.hasNext();) {
					Jugador jugador = (Jugador) iterator2.next();
					resultado.add(jugador);
				}
			}
		}
		for (Iterator iterator = resultado.iterator(); iterator.hasNext();) {
			Jugador jugador = (Jugador) iterator.next();
			formResultados.add(new Label("itemResultado", jugador.toString()));
			System.out.println(jugador.toString());
		}
		
		
	}
	
	
	
	
	
}
