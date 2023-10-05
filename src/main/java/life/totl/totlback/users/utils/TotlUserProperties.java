package life.totl.totlback.users.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "totl")
@Getter
public class TotlUserProperties {

    @Value("${HOST_URL}")
    private String hostUrl;

}
