package org.charitable.app.infrastructure.adapter.outbound.jpa.organization;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.AuthEntity;
import org.charitable.app.infrastructure.adapter.outbound.jpa.base.BaseEntity;

@Entity
@Table(name = "organization")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationEntity extends BaseEntity {

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column
    @NotNull
    private String address;

    @Column
    private Float latitude;

    @Column
    private Float longitude;

    @Column(name = "govt_id", unique = true, nullable = false)
    @NotNull
    private String govtId;


    @Column(name = "contact_number", nullable = false)
    @NotNull
    private String contactNumber;

    @OneToOne(mappedBy = "organization")
    private AuthEntity auth;
}
