package net.decosa.sii.ed;

import lombok.Getter;
import lombok.Setter;
import net.decosa.sii.SiiConfig;
import net.decosa.sii.aeat.PersonaFisicaJuridicaType;
import net.decosa.sii.util.StringUtils;


@Getter @Setter
public class Contraparte {
	
	private String nombreRazon;
	private String nif;
	
	
	public Contraparte(String nombreRazon, String nif) {
		this.nombreRazon = nombreRazon;
		this.nif = nif;
	}
	
	
	public PersonaFisicaJuridicaType toPersonaFisicaJuridicaType() {
		PersonaFisicaJuridicaType contraparte = new PersonaFisicaJuridicaType();
		contraparte.setNIF(nif);
		contraparte.setNombreRazon(StringUtils.limit(StringUtils.removeSpecialChars(nombreRazon), SiiConfig.MAX_LONG_NOMBRE_RAZON));
		
		return contraparte;
	}
}
