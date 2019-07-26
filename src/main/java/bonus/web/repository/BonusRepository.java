package bonus.web.repository;

import bonus.web.model.BonusModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BonusRepository extends CrudRepository<BonusModel, Integer> {

    List<BonusModel> findAll();
}
