package account.service;

import account.domain.entity.LogEntity;
import account.domain.entity.LogEvents;
import account.domain.repository.LogRep;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
public class LogService {

    private LogRep logRepository;

    public void saveLog(LogEvents action, String subject, String object, String path) {
        logRepository.save(LogEntity.builder()
                .action(action)
                .subject(subject)
                .object(object)
                .path(path).build());
    }

    public List<LogEntity> showAllLogs() {
        List<LogEntity> result = new ArrayList<>();
        logRepository.findAll().forEach(result::add);
        return result;
    }
}
