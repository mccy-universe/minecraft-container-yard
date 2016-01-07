package me.itzg.mccy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import me.itzg.mccy.types.YamlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Geoff Bourne
 * @since 12/30/2015
 */
@Configuration
public class GeneralConfig {

    @Bean
    public HashFunction fileIdHash() {
        return Hashing.md5();
    }

    @Bean
    public HashFunction modPackIdHash() {
        return Hashing.md5();
    }

    @Bean
    public YamlMapper yamlMapper() {
        final YAMLFactory yamlFactory = new YAMLFactory();
        return new YamlMapper(new ObjectMapper(yamlFactory));
    }
}