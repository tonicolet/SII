package net.decosa.sii.facturas;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.decosa.sii.util.StringUtils;


@Getter @Setter @ToString
public class RespuestaAlta {
	
	private String estadoEnvio;
	private Date fechaPresentacion;
	private String csv;
	private List<RespuestaAltaDetalle> respuestaDetalles;
	private Integer numeroFacturasCorrectas;
	private Integer numeroFacturasIncorrectas;
	
	
	public RespuestaAlta() {
		this.respuestaDetalles = new ArrayList<RespuestaAltaDetalle>();
		this.numeroFacturasCorrectas = 0;
		this.numeroFacturasIncorrectas = 0;
	}
	
	
	public void addRespuestaAltaDetalle(RespuestaAltaDetalle respuestaAltaDetalle) {
		if (!StringUtils.isBlank(respuestaAltaDetalle.getDescripcionError()))
			numeroFacturasIncorrectas++;
		else
			numeroFacturasCorrectas++;
		
		respuestaDetalles.add(respuestaAltaDetalle);
	}
}
