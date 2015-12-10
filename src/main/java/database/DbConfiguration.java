package database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
 
//allows configurations through annotations
@Configuration
@EnableTransactionManagement
public class DbConfiguration
{
	//Db is defined
    public static final String DB_PROPERTIES_PATH = "/db.properties";
    public static final String DRIVER_CLASS_NAME = "driverClassName";
    public static final String URL = "url";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    
    @Bean
    public DataSource dataSource()
    {
    	//defines destination
        InputStream inputStream = DbConfiguration.class.getResourceAsStream(DB_PROPERTIES_PATH);
        Properties properties = new Properties();
        try
        {
            properties.load(inputStream);
        }catch (IOException e) 
        {
            throw new RuntimeException(e);
        }
        
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(properties.getProperty(DRIVER_CLASS_NAME));
        dataSource.setUrl(properties.getProperty(URL));
        dataSource.setUsername(properties.getProperty(USERNAME));
        dataSource.setPassword(properties.getProperty(PASSWORD));
        dataSource.setValidationQuery("select 1");
        dataSource.setTestOnBorrow(true);
        return dataSource;
    }
    
    @Bean
    public SessionFactory sessionFactory()
    {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setDataSource(this.dataSource());
        factoryBean.setPackagesToScan("database");
        {
        	//hibernate properties are set
            Properties properties = new Properties();
            factoryBean.setHibernateProperties(properties);
            properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
            properties.setProperty("hibernate.hbm2ddl.auto", "update");
            properties.setProperty("hibernate.id.new_generator_mappings", "true");
            properties.setProperty("hibernate.c3p0.min_size", "3");
            properties.setProperty("hibernate.c3p0.max_size", "50");
            properties.setProperty("hibernate.c3p0.timeout", "1800");
            properties.setProperty("hibernate.c3p0.max_statements", "50");
            properties.setProperty("hibernate.c3p0.idle_test_period", "28680");
            properties.setProperty("hibernate.autoReconnect", "true");
        }
        
        try
        {
            factoryBean.afterPropertiesSet();
        }catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return factoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager()
    {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(this.sessionFactory());
        return transactionManager;
    }    
    
    @Bean
    public SessionTool sessionTool()
    {
        return new SessionTool(this.sessionFactory());
    }
}
