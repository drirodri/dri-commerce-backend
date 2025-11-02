package dri.commerce.user.infrastructure.entity;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false, unique = true)
    public String email;

    @Column(nullable = false)
    public String password;

    @Column(nullable = false)
    public Integer role;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @Column(nullable = false)
    public boolean active = true;

    public UserEntity() {
    }

    public UserEntity(String id, String name, String email, String password, Integer role,
            LocalDateTime createdAt, LocalDateTime updatedAt, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = active;
    }
}
