package quest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import quest.quest03.converter.StringToDepartmentInfo;
import quest.quest03.converter.StringToRelation;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToDepartmentInfo());
        registry.addConverter(new StringToRelation());
    }
}
