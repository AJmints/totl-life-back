package life.totl.totlback.logs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

//@Configuration
//@PropertySource({"classpath:application.properties"})
//@EnableJpaRepositories(
////        entityManagerFactoryRef = "usersEntityManager",
////        transactionManagerRef = "usersTransactionManager",
//        basePackages = "life.totl.totlback.logs.repositories")
public class PersistenceLogBaleAutoConfiguration {

//    @Bean
//    @ConfigurationProperties(prefix = "spring.second-datasource")
//    public DataSource logBaleDataSource() {
//        return DataSourceBuilder.create().build();
//    }

}
