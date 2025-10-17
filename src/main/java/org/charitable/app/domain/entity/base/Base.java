package org.charitable.app.domain.entity.base;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public abstract class Base {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
}
