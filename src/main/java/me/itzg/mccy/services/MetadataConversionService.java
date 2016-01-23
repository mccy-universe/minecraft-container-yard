package me.itzg.mccy.services;

import me.itzg.mccy.model.EnvironmentVariable;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Geoff Bourne
 * @since 1/22/2016
 */
@Service
public class MetadataConversionService {

    @Autowired
    private ConversionService conversionService;

    public void fillFromEnv(List<String> env, Object target) {
        Map<String, String> envMap = extractEnvListToMap(env);

        final BeanWrapper propertyAccess = PropertyAccessorFactory.forBeanPropertyAccess(target);

        ReflectionUtils.doWithLocalFields(target.getClass(), f -> {
            final EnvironmentVariable[] annos = f.getAnnotationsByType(EnvironmentVariable.class);

            final Optional<String> evValue = Stream.of(annos)
                    .map(evAnno -> envMap.get(evAnno.value()))
                    .filter(val -> val != null)
                    .findFirst();

            if (evValue.isPresent()) {
                propertyAccess.setPropertyValue(f.getName(),
                        conversionService.convert(evValue.get(), f.getType()));
            }
        });

    }

    private static Map<String, String> extractEnvListToMap(List<String> env) {
        if (env == null) {
            return null;
        }

        return env.stream()
                .map(e -> e.split("=", 2))
                .collect(Collectors.toMap(parts -> parts[0], parts -> parts[1]));
    }

}
