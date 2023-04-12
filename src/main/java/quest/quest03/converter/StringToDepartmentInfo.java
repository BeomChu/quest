package quest.quest03.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import quest.quest03.dto.request.DepartmentInfo;

@Slf4j
@Component
public class StringToDepartmentInfo implements Converter<String, DepartmentInfo> {

    @Override
    public DepartmentInfo convert(String source) {
        log.info("convert source  = {}" ,source);
        String[] split = source.replaceAll("\\s", "").split(",");
        return new DepartmentInfo(split[0],Integer.valueOf(split[1]));
    }
}
