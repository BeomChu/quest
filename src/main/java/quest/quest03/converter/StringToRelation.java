package quest.quest03.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import quest.quest03.dto.request.Relation;

@Slf4j
@Component
public class StringToRelation implements Converter<String, Relation> {
    @Override
    public Relation convert(String source) {
        log.info("convert source  = {}" ,source);
        String[] split = source.replaceAll("\\s", "").split(">");
        return new Relation(split[0], split[1]);
    }
}
