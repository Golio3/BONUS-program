package bonus.web.repository;

import bonus.web.model.UsersModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UsersRepository extends CrudRepository<UsersModel, Integer> {

    List<UsersModel> findAll();

    UsersModel findByUsername(String username);

    List<UsersModel> findByRoles(String roles);
}
