package serv.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dbase.DbConfiguration;

@Configuration
public class ServiceConfiguration
{
    @Autowired
    private DbConfiguration dbConfiguration;
    
    @Bean
    public Services services()
    {
        return new Services(dbConfiguration.sessionTool());
    }
}
