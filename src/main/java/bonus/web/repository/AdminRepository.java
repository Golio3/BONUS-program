package bonus.web.repository;

import bonus.web.model.AdminModel;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<AdminModel, Integer> {
}
