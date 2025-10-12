package dri.commerce.user.infrastructure.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import dri.commerce.user.domain.entity.Page;
import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.repository.UserRepository;
import dri.commerce.user.domain.valueobject.UserEmail;
import dri.commerce.user.domain.valueobject.UserId;
import dri.commerce.user.infrastructure.entity.UserEntity;
import dri.commerce.user.infrastructure.mapper.UserMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserRepositoryImpl implements UserRepository {

    private static final String DATABASE_NAME = "dri-commerce";
    private static final String COLLECTION_NAME = "users";

    @Inject
    MongoClient mongoClient;

    @Inject
    UserMapper userMapper;

    private MongoCollection<Document> getUserCollection() {
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        return database.getCollection(COLLECTION_NAME);
    }

    @Override
    public UserDomain save(UserDomain user) {
        UserEntity entity = userMapper.toInfrastructure(user);
        Document doc = toDocument(entity);

        getUserCollection().insertOne(doc);

        return userMapper.toDomain(fromDocument(doc));
    }

    @Override
    public UserDomain update(UserDomain user) {
        Document doc = toDocument(userMapper.toInfrastructure(user));
        getUserCollection().replaceOne(
                Filters.eq("_id", new ObjectId(user.id().value())),
                doc);
        return user;
    }

    @Override
    public Optional<UserDomain> findById(UserId id) {
        Document doc = getUserCollection()
                .find(Filters.eq("_id", new ObjectId(id.value())))
                .first();

        if (doc == null) {
            return Optional.empty();
        }

        return Optional.of(userMapper.toDomain(fromDocument(doc)));
    }

    @Override
    public Optional<UserDomain> findByEmail(UserEmail email) {
        Document doc = getUserCollection()
                .find(Filters.eq("email", email.value()))
                .first();

        if (doc == null) {
            return Optional.empty();
        }

        return Optional.of(userMapper.toDomain(fromDocument(doc)));
    }

    @Override
    public List<UserDomain> findAllActive() {
        return getUserCollection()
                .find(Filters.eq("active", true))
                .into(new ArrayList<>())
                .stream()
                .map(this::fromDocument)
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDomain> findAll() {
        return getUserCollection()
                .find()
                .into(new ArrayList<>())
                .stream()
                .map(this::fromDocument)
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDomain> findAll(int page, int pageSize) {
        int skip = (page - 1) * pageSize;

        List<UserDomain> users = getUserCollection()
                .find()
                .skip(skip)
                .limit(pageSize)
                .into(new ArrayList<>())
                .stream()
                .map(this::fromDocument)
                .map(userMapper::toDomain)
                .collect(Collectors.toList());

        long total = count();

        return Page.of(users, total, page, pageSize);
    }

    @Override
    public boolean existsByEmail(UserEmail email) {
        return getUserCollection()
                .countDocuments(Filters.eq("email", email.value())) > 0;
    }

    @Override
    public boolean deleteById(UserId id) {
        return getUserCollection()
                .deleteOne(Filters.eq("_id", new ObjectId(id.value())))
                .getDeletedCount() > 0;
    }

    @Override
    public long countActive() {
        return getUserCollection()
                .countDocuments(Filters.eq("active", true));
    }

    @Override
    public long count() {
        return getUserCollection().countDocuments();
    }

    @Override
    public List<UserDomain> findByNameContaining(String name) {
        return getUserCollection()
                .find(Filters.regex("name", ".*" + name + ".*", "i"))
                .into(new ArrayList<>())
                .stream()
                .map(this::fromDocument)
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    private Document toDocument(UserEntity entity) {
        Document doc = new Document()
                .append("name", entity.name())
                .append("email", entity.email())
                .append("password", entity.password())
                .append("role", entity.role())
                .append("createdAt", entity.createdAt() != null ? toDate(entity.createdAt()) : null)
                .append("updatedAt", entity.updatedAt() != null ? toDate(entity.updatedAt()) : null)
                .append("active", entity.active());

        if (entity.id() != null) {
            doc.append("_id", new ObjectId(entity.id()));
        }

        return doc;
    }

    private UserEntity fromDocument(Document doc) {
        return new UserEntity(
                doc.getObjectId("_id").toHexString(),
                doc.getString("name"),
                doc.getString("email"),
                doc.getString("password"),
                doc.get("role", Integer.class),
                doc.getDate("createdAt") != null ? toLocalDateTime(doc.getDate("createdAt")) : null,
                doc.getDate("updatedAt") != null ? toLocalDateTime(doc.getDate("updatedAt")) : null,
                doc.getBoolean("active"));
    }

    private java.time.LocalDateTime toLocalDateTime(java.util.Date date) {
        return date.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private java.util.Date toDate(java.time.LocalDateTime localDateTime) {
        return java.util.Date.from(
                localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());
    }
}