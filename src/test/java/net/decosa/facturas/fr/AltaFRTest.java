package net.decosa.facturas.fr;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.decosa.sii.SiiConfig;
import net.decosa.sii.ed.Contraparte;
import net.decosa.sii.ed.DesgloseIVAFR;
import net.decosa.sii.ed.FacturaEmitida;
import net.decosa.sii.ed.FacturaRecibida;
import net.decosa.sii.ed.IDFactura;
import net.decosa.sii.ed.PeriodoImpositivo;
import net.decosa.sii.facturas.fr.AltaFR;
import net.decosa.sii.util.DateUtils;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SiiConfig.class)
public class AltaFRTest {

	@Autowired
	private AltaFR altaFR;
	
	private List<FacturaRecibida> facturasRecibidas;
	
	
	@Before
	public void inicializaDatos() {
		facturasRecibidas = new ArrayList<FacturaRecibida>();
		
		// Factura 1
		FacturaRecibida f = new FacturaRecibida();
		f.setPeriodoImpositivo(new PeriodoImpositivo(2017, 1));
		f.setIdFactura(new IDFactura("01", DateUtils.parse("15-01-2105"), "A84532501"));
		f.setContraparte(new Contraparte("EMPRESAXXX", "A84532501"));
		
		f.setTipoFactura(FacturaEmitida.TIPO_FACTURA_F1);
		f.setFechaOperacion(DateUtils.parse("01-03-2016"));
		f.setFechaRegContable(DateUtils.parse("01-03-2017"));
		f.setClaveRegimenEspecialOTrascendencia("01");
		f.setImporteTotal(26.7);
		f.setDescripcionOperacion("ARQ#");
		
		// Detalles IVA
		List<DesgloseIVAFR> desglosesIVA = new ArrayList<DesgloseIVAFR>();
		
		DesgloseIVAFR desgloseIVA = new DesgloseIVAFR();
		desgloseIVA.setBaseImponible(800);
		desgloseIVA.setTipoImpositivo(21);
		desgloseIVA.setCuotaSoportada(168);
		desgloseIVA.setIsp(true);
		desglosesIVA.add(desgloseIVA);
		
		desgloseIVA = new DesgloseIVAFR();
		desgloseIVA.setBaseImponible(118.5);
		desgloseIVA.setTipoImpositivo(21);
		desgloseIVA.setCuotaSoportada(31.5);
		desglosesIVA.add(desgloseIVA);
		
		f.setDetallesIVA(desglosesIVA);
		facturasRecibidas.add(f);
	}
	
	
	@Test
	public void altaFRTest() {
		try {
			altaFR.setFacturas(facturasRecibidas);
			System.out.println(altaFR.getXML());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
