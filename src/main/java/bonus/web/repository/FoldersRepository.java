package bonus.web.repository;

import bonus.web.controller.page.FoldersModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FoldersRepository extends CrudRepository<FoldersModel, Integer> {

    List<FoldersModel> findAll();

    FoldersModel findFirstByFolderId(Integer folderId);
}
