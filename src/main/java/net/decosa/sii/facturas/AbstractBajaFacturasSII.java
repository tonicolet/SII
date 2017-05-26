package net.decosa.sii.facturas;

import net.decosa.sii.aeat.CabeceraSiiBaja;
import net.decosa.sii.aeat.PersonaFisicaJuridicaESType;


public abstract class AbstractBajaFacturasSII extends AbstractFacturasSII {

	
	protected CabeceraSiiBaja getCabeceraBajaSII() {
		PersonaFisicaJuridicaESType personaFisicaJuridicaESType = new PersonaFisicaJuridicaESType();
		personaFisicaJuridicaESType.setNIF(cif);
		personaFisicaJuridicaESType.setNombreRazon(nombreEmpresa.toUpperCase());
		
		CabeceraSiiBaja cabeceraSii = new CabeceraSiiBaja();
		cabeceraSii.setIDVersionSii(siiVersion);
		cabeceraSii.setTitular(personaFisicaJuridicaESType);
		
		return cabeceraSii;
	}

}
