package net.decosa.sii;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan
public class SiiApplication {
	
	public static void main(String... args) {
		
		ApplicationContext ctx = 
		          new AnnotationConfigApplicationContext(SiiApplication.class);
		
		SiiApplication siiApplication = (SiiApplication) ctx.getBean("siiApplication");
		siiApplication.run(args);
		
		((ConfigurableApplicationContext)ctx).close();
    }
	
	
	public void run(String... args) {
		System.exit(0);
	}
}
