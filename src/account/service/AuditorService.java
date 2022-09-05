package account.service;

import account.domain.entity.Account;
import account.domain.entity.LogEntity;
import account.domain.repository.LogRep;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuditorService {

    LogRep logRep;

    public List<LogEntity> getEventsList() {
        List<LogEntity> result = new ArrayList<>();
        logRep.findAll().forEach(result::add);
        return result;
    }
}
