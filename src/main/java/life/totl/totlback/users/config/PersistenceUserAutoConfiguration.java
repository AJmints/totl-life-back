package life.totl.totlback.users.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

//@Configuration
//@PropertySource({"classpath:application.properties"})
//@EnableJpaRepositories(
////        entityManagerFactoryRef = "entityManagerFactory",
////        transactionManagerRef = "userTransactionManager",
//        basePackages = "life.totl.totlback.users.repository")
public class PersistenceUserAutoConfiguration {

//    @Primary
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource userEntityDataSource() {
//        return DataSourceBuilder.create().build();
//    }

}
