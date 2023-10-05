package life.totl.totlback.security.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security")
@Getter
public class TotlSecurityProperties {

    @Value("${JWT_EXPIRATION}")
    private String JWT_EXPIRATION;

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

}
