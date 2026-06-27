package com.bfhl.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures Jackson so that:
 * <ul>
 *   <li>Private fields annotated with {@code @JsonProperty} are visible for
 *       both serialisation and deserialisation.</li>
 *   <li>{@code is*} getter methods are <em>not</em> auto-detected as
 *       additional properties, preventing naming conflicts with
 *       Lombok-generated {@code isXxx()} methods.</li>
 * </ul>
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder
                // Make private fields visible to Jackson
                .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                // Disable auto-detection of is*/get* methods to avoid naming conflicts
                .visibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
                .visibility(PropertyAccessor.GETTER,    JsonAutoDetect.Visibility.NONE);
    }
}
