package de.igorlueckel.magicmirror.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by Igor Lückel on 22.07.2015.
 */
@Configuration
public class HikariCPConfig {

    @Value("${hikaricp.jdbcurl}")
    private String jdbcUrl;

    @Value("${hikaricp.username}")
    private String jdbcUsername;

    @Value("${hikaricp.password}")
    private String jdbcPassword;

    @Value("${hikaricp.maxpoolsize}")
    private String maxPoolSize;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUsername);
        config.setPassword(jdbcPassword);
        config.setMaximumPoolSize(Integer.parseInt(maxPoolSize));
        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }
}
