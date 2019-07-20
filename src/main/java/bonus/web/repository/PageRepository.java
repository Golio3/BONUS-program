package bonus.web.repository;

import bonus.web.controller.page.PageModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PageRepository extends CrudRepository<PageModel, Integer> {

    List<PageModel> findAll();
}
