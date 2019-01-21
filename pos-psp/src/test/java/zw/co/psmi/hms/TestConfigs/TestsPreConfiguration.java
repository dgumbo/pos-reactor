
package zw.co.psmi.hms.TestConfigs;

import javax.sql.DataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan(basePackages = {"zw.co.psmi.hms", "zw.co.psmi.hms.*"})//.authentication.service.impl"})
@EnableJpaRepositories(basePackages = {"zw.co.psmi.hms", "zw.co.psmi.hms.*"})//.authentication.dao.UserDao"})//, entityManagerFactoryRef="entityManagerFactory") 
public class TestsPreConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        //-- Return the correct data source depending on prefered environemnt  --//
        //-- Warning may drop and create existing tables  --//
        
        return dataSource_MySQL();
        //return dataSource_SQLServer();
    }

    private static final DataSource dataSource_MySQL() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/hmsdev");
        dataSource.setUsername("hms");
        dataSource.setPassword("hms");
        return dataSource;
    }

    private static final DataSource dataSource_SQLServer() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://192.200.10.110;databaseName=HMSDevTest");
        dataSource.setUsername("HISAdmin");
        dataSource.setPassword("5p1tf1r3");
        return dataSource;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws ClassNotFoundException {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

        DataSource data_source = dataSource();
        entityManagerFactory.setDataSource(data_source);//" ref="dataSource" />

        entityManagerFactory.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", "create-drop");
        //entityManagerFactory.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", "update");

        String hibernate_dialect = "";
        try {
            String driverClassName = data_source.getConnection().getMetaData().getURL();
            if (driverClassName.toLowerCase().contains("sqlserver")) {
                hibernate_dialect = "org.hibernate.dialect.SQLServerDialect";
            } else if (driverClassName.toLowerCase().contains("mysql")) {
                hibernate_dialect = "org.hibernate.dialect.MySQLDialect";
            }
        } catch (SQLException ex) {
            Logger.getLogger(TestsPreConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
        entityManagerFactory.getJpaPropertyMap().put("hibernate.dialect", hibernate_dialect);

        entityManagerFactory.getJpaPropertyMap().put("hibernate.show_sql", "true");
        entityManagerFactory.getJpaPropertyMap().put("hibernate.format_sql", "true");

        entityManagerFactory.setPersistenceProviderClass(HibernatePersistenceProvider.class); // org.hibernate.jpa.HibernatePersistenceProvider

        entityManagerFactory.setPackagesToScan("zw.co.psmi.hms");// To Scan All
//         entityManagerFactory.setPackagesToScan(new String[] {
//            "zw.co.psmi.hms.authentication.entity",
//            "zw.co.psmi.hms.stocks.entity",
//            "zw.co.psmi.hms.logging.entity",
//            "zw.co.psmi.hms.master.entity"
//        });// To can Selected

        return entityManagerFactory;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager jpaTransactionManager() throws ClassNotFoundException { // TODO: Really need this?
        final JpaTransactionManager transactionManager = new JpaTransactionManager(); // http://stackoverflow.com/questions/26562787/hibernateexception-couldnt-obtain-transaction-synchronized-session-for-current
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

}
