package account.api.mapper;

import account.api.dto.ou.LogDTO;
import account.domain.entity.LogEntity;
import org.springframework.stereotype.Component;

@Component
public class LogMapper {

    public LogDTO toLogDTO(LogEntity logEntity) {
        return LogDTO.builder()
                .action(logEntity.getAction())
                .id(logEntity.getId())
                .date(logEntity.getDate())
                .subject(logEntity.getSubject())
                .path(logEntity.getPath())
                .object(logEntity.getObject())
                .build();
    }
}
