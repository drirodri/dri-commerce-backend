package dri.commerce.user.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dri.commerce.user.domain.entity.Page;
import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.repository.UserRepository;
import dri.commerce.user.domain.valueobject.UserEmail;
import dri.commerce.user.domain.valueobject.UserId;
import dri.commerce.user.infrastructure.entity.UserEntity;
import dri.commerce.user.infrastructure.mapper.UserMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserRepositoryImpl implements PanacheRepositoryBase<UserEntity, String>, UserRepository {

    @Inject
    UserMapper userMapper;

    @Override
    @Transactional
    public UserDomain save(UserDomain user) {
        UserEntity entity = userMapper.toInfrastructure(user);
        persist(entity);
        return userMapper.toDomain(entity);
    }

    @Override
    @Transactional
    public UserDomain update(UserDomain user) {
        UserEntity entity = userMapper.toInfrastructure(user);
        getEntityManager().merge(entity);
        return user;
    }

    @Override
    public Optional<UserDomain> findById(UserId id) {
        return findByIdOptional(id.value())
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<UserDomain> findByEmail(UserEmail email) {
        return find("email", email.value()).firstResultOptional()
                .map(userMapper::toDomain);
    }

    @Override
    public List<UserDomain> findAllActive() {
        return find("active", true)
                .list()
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDomain> getAllUsers() {
        return listAll()
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDomain> findAll(int page, int pageSize) {
        List<UserDomain> users = find("ORDER BY createdAt DESC")
                .page(page - 1, pageSize)
                .list()
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());

        return Page.of(users, total(), page, pageSize);
    }

    @Override
    public boolean existsByEmail(UserEmail email) {
        return count("email", email.value()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(UserId id) {
        return delete("id", id.value()) > 0;
    }

    @Override
    public long countActive() {
        return count("active", true);
    }

    @Override
    public long count() {
        return listAll().size();
    }

    private long total() {
        return listAll().size();
    }

    @Override
    public List<UserDomain> findByNameContaining(String name) {
        return find("LOWER(name) LIKE LOWER(?1)", "%" + name + "%")
                .list()
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }
}