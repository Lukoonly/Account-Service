package account.domain.repository;

import account.domain.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface AccountRep extends CrudRepository<Account, Long> {

    Optional<Account> findUserByEmailIgnoreCase(String email);

    long count();

    void deleteAccountByEmail(String email);
}