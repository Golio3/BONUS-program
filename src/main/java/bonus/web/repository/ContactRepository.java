package bonus.web.repository;

import bonus.web.model.ContactModel;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<ContactModel, Integer> {
}
