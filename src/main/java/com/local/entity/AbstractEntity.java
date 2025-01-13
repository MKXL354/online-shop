package com.local.entity;

import com.local.util.validation.PersistenceValidationListener;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@EntityListeners(PersistenceValidationListener.class)
@MappedSuperclass
public class AbstractEntity {
    @Id
    @GeneratedValue
    protected Long id;

    @Version
    protected Long version;

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
