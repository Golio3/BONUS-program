package bonus.web.repository;

import bonus.web.controller.page.PageTreeModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PageTreeRepository extends CrudRepository<PageTreeModel, Integer> {

    List<PageTreeModel> findAll();

    List<PageTreeModel> findAllByType(int type);

    List<PageTreeModel> findAllByOrderByPosition();

    PageTreeModel findFirstByTreeId(String treeId);
}
