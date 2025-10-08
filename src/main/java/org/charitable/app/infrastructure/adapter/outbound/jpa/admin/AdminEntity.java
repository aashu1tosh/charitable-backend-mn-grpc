package org.charitable.app.infrastructure.adapter.outbound.jpa.admin;

import io.micronaut.data.annotation.MappedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.charitable.app.infrastructure.adapter.outbound.jpa.base.BaseEntity;

@Entity
@MappedEntity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AdminEntity extends BaseEntity {
    @Column(name="full_name", nullable = false, unique = true)
    @NotNull
    private String fullName;

    @Column(name="middle_name", nullable = true)
    private String middleName;

    @Column(name="last_name", nullable = false)
    @NotNull
    private String lastName;
}
