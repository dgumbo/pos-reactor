package zw.co.psmi.hms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "zw.co.psmi.hms")
@EnableJpaAuditing
@EnableAspectJAutoProxy
@EnableConfigServer
@Configuration
@EnableAsync 
@EnableScheduling
@EnableHazelcastHttpSession(maxInactiveIntervalInSeconds = 3600)
@EnableWebSecurity
@Import({AsyncConfiguration.class,WebSecurityConfig.class,UserMenuConfig.class,SessionConfig.class})
public class StocksApplication  extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(StocksApplication.class);
	}

    

	public static void main(String[] args) {
       // ConfigurableApplicationContext c = 
                SpringApplication.run(StocksApplication.class, args);
//            CurrentStockService instance=   c.getBean(CurrentStockService.class);
//        List<BookStockLockView> result = instance.getBookStockLockView();
//            System.err.println("res::"+result.size());
//            System.err.println("res::"+result);
	}
          @Bean
  public EmbeddedServletContainerFactory embeddedServletContainerFactory() {
              UndertowEmbeddedServletContainerFactory factory=new UndertowEmbeddedServletContainerFactory();
    factory.setIoThreads(20);
    factory.setWorkerThreads(100);
    factory.addBuilderCustomizers(
        builder -> builder.setServerOption(io.undertow.UndertowOptions.MAX_PARAMETERS, 500000),
        builder -> builder.setServerOption(io.undertow.UndertowOptions.ENABLE_HTTP2, true),
        builder -> builder.setServerOption(io.undertow.UndertowOptions.ENABLE_STATISTICS, true),
        builder -> builder.setServerOption(io.undertow.UndertowOptions.RECORD_REQUEST_START_TIME, true));

    return factory;
  }
}
