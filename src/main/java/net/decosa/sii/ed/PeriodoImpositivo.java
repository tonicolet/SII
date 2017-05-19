package net.decosa.sii.ed;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import net.decosa.sii.util.DateUtils;


@Getter @Setter
public class PeriodoImpositivo {
	
	private Integer ejercicio;
	private Integer periodo;

	
	public PeriodoImpositivo(Date fecha) {
		this(DateUtils.getAnyo(fecha), DateUtils.getMes(fecha));
	}
	
	
	public PeriodoImpositivo(Integer ejercicio, Integer periodo) {
		this.ejercicio = ejercicio;
		this.periodo = periodo;
	}

	
	public net.decosa.sii.aeat.RegistroSii.PeriodoImpositivo toPeriodoImpositivoSII() {
		net.decosa.sii.aeat.RegistroSii.PeriodoImpositivo pi = new net.decosa.sii.aeat.RegistroSii.PeriodoImpositivo();
		pi.setEjercicio("" + ejercicio);
		pi.setPeriodo("" + ((periodo < 10) ? "0" + periodo : "" + periodo));
		
		return pi;
	}
}
