package bonus.web.repository;

import bonus.web.controller.page.HomeModel;
import org.springframework.data.repository.CrudRepository;

public interface HomeRepository extends CrudRepository<HomeModel, Integer> {
}
