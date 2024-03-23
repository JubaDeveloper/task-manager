package io.github.jubadeveloper.configuration;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "io.github.jubadeveloper")
@EnableJpaAuditing
public class DatabaseConfig {
    @Bean
    public DataSource h2DataSource () {
        return new EmbeddedDatabaseBuilder()
                .setName("jdb2:h2:db:mem;NON_KEYWORDS=USER")
                .setType(EmbeddedDatabaseType.H2)
                .setName("task")
                .addScript("data/schema.sql")
                .addScripts("data/script.sql")
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory () {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(h2DataSource());
        entityManagerFactoryBean.setPackagesToScan("io.github.jubadeveloper");
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
