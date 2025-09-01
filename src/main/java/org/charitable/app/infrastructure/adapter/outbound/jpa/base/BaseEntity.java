package org.charitable.app.infrastructure.adapter.outbound.jpa.base;

import java.time.Instant;
import java.util.UUID;

import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.GeneratedValue.Type;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedEntity
public abstract class BaseEntity {

    @Id
    @GeneratedValue(Type.UUID)
    private UUID id;

    @DateCreated
    private Instant createdAt;

    @DateUpdated
    private Instant updatedAt;

    private Instant deletedAt;
}
