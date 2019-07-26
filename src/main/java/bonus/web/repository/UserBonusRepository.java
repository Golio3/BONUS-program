package bonus.web.repository;

import bonus.web.model.UserBonusModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserBonusRepository extends CrudRepository<UserBonusModel, Integer> {

    List<UserBonusModel> findAll();

    List<UserBonusModel> findAllByUserIdAndStatus(Integer userId, Integer status);

    UserBonusModel findByUserIdAndBonusIdAndStatus(Integer userId, Integer bonusId, Integer status);
}
