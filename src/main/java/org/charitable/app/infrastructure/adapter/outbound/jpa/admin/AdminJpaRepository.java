package org.charitable.app.infrastructure.adapter.outbound.jpa.admin;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.UUID;

@Repository
public interface AdminJpaRepository extends CrudRepository<AdminEntity, UUID> {

}
