package zw.co.hisolutions.pos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import zw.co.hisolutions.pos.init.services.InitStockLedgerReport;
import zw.co.hisolutions.pos.masters.service.CurrencyService;
import zw.co.hisolutions.pos.reports.ReportsModuleComponent;

@SpringBootApplication(scanBasePackages = {"zw.co.hisolutions.pos"})
@EnableJpaRepositories("zw.co.hisolutions.pos")
@EnableTransactionManagement
@ReportsModuleComponent
//@StorageServicesComponent 
//@JwtAuthModuleComponent
//@StorageServicesComponent
public class PosBackendApplication {

    //@SuppressWarnings("resource")
    public static void main(String[] args) {
        SpringApplication.run(PosBackendApplication.class, args);
    }

    @Autowired
    CurrencyService currencyService;

    @Bean
    CommandLineRunner setEcocashRate() {
        org.hibernate.dialect.SQLServer2012Dialect SQLServer2012Dialect;
        org.hibernate.dialect.SQLServer2012Dialect SQLServer2016Dialect;
        com.microsoft.sqlserver.jdbc.SQLServerDriver sqlDriver;

        return (args) -> {
            currencyService.updateStaticExchangeRates();
        };

    }

    @Bean
    CommandLineRunner testForEnvironmentVariables(Environment environment) {
        // com.microsoft.sqlserver.jdbc.SQLServerDriver sqlDriver;
        com.mysql.cj.jdbc.Driver mySqlDriver;

        return (args) -> {
            System.out.println("\n");
            System.out.println("SPRING_PROFILES_ACTIVE-: " + environment.getProperty("SPRING_PROFILES_ACTIVE"));
            System.out.println("\n");
        };
    }

    @Autowired
    InitStockLedgerReport initStockLedgerReport;

    @Bean
    CommandLineRunner clrInitStockLedgerReport() {
        initStockLedgerReport.initReport();

        return (args) -> {
            System.out.println("\n");
            System.out.println("STOCK LEDGER REPORT INITIALIZED");
            System.out.println("\n");
        };
    }
}
