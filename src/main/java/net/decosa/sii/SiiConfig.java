package net.decosa.sii;

import java.io.IOException;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender;

import net.decosa.sii.ws.WSSIIRequest;


@Configuration
@ComponentScan
@PropertySource(value = { "classpath:/sii.properties" })
public class SiiConfig {

	public static final String DEFAULT_DATE_FORMAT			= "dd-MM-yyyy";
	
	public static final Integer MAX_LONG_NOMBRE_RAZON		= 120;
	public static final Integer MAX_LONG_DESCRIPCION_FACTURA= 500;
	public static final Integer MAX_LONG_NUM_SERIE_FACTURA	= 60;
	public static final Integer MAX_LONG_NIF				= 9;
	
	public static final Integer MAX_FACTURAS_POR_ENVIO		= 10000;
	
	@Value("${empresa.key_file}")
	private Resource keyStore;

	@Value("${empresa.key_password}")
	private String keyStorePassword;

	
	
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("net.decosa.sii.aeat");
		return marshaller;
	}

	
	@Bean
	public WSSIIRequest webServiceRequest(Jaxb2Marshaller marshaller) throws Exception {
		WSSIIRequest wsRequest = new WSSIIRequest();

		wsRequest.setMarshaller(marshaller);
		wsRequest.setUnmarshaller(marshaller);

		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(keyStore.getInputStream(), keyStorePassword.toCharArray());

		try {
			keyStore.getInputStream().close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(ks, keyStorePassword.toCharArray());

		HttpsUrlConnectionMessageSender messageSender = new HttpsUrlConnectionMessageSender();
		messageSender.setKeyManagers(keyManagerFactory.getKeyManagers());

		wsRequest.setMessageSender(messageSender);
		return wsRequest;
	}
}
