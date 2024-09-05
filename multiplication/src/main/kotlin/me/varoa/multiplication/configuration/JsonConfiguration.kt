package me.varoa.multiplication.configuration

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JsonConfiguration {
    @Bean
    fun hibernateModule(): Module = Hibernate5JakartaModule()
}
