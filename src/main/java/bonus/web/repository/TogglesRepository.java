package bonus.web.repository;

import bonus.web.controller.page.TogglesModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TogglesRepository extends CrudRepository<TogglesModel, Integer> {

    List<TogglesModel> findAll();
}
