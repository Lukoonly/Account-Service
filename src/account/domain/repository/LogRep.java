package account.domain.repository;

import account.domain.entity.LogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRep extends CrudRepository<LogEntity, Long> {
}