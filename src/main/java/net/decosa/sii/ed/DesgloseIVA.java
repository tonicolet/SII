package net.decosa.sii.ed;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class DesgloseIVA {

	private Integer tipoImpositivo;
	private Double baseImponible;
	
	
	public void setBaseImponible(Integer baseImponible) {
		setBaseImponible(baseImponible.doubleValue());
	}
	
	public void setBaseImponible(Double baseImponible) {
		this.baseImponible = baseImponible;
	}
	
}
