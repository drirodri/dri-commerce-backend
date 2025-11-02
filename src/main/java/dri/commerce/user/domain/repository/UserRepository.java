package dri.commerce.user.domain.repository;

import java.util.List;
import java.util.Optional;

import dri.commerce.user.domain.entity.Page;
import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.valueobject.UserEmail;
import dri.commerce.user.domain.valueobject.UserId;

public interface UserRepository {

    UserDomain save(UserDomain user);

    UserDomain update(UserDomain user);

    Optional<UserDomain> findById(UserId id);

    Optional<UserDomain> findByEmail(UserEmail email);

    List<UserDomain> findAllActive();

    List<UserDomain> getAllUsers();

    Page<UserDomain> findAll(int page, int pageSize);

    boolean existsByEmail(UserEmail email);

    boolean deleteById(UserId id);

    long countActive();

    long count();

    List<UserDomain> findByNameContaining(String name);
}
