package org.charitable.app.infrastructure.mapper.admin;

import org.charitable.app.domain.admin.entity.Admin;
import org.charitable.app.domain.entity.user.User;
import org.charitable.app.infrastructure.adapter.outbound.jpa.admin.AdminEntity;
import org.charitable.app.infrastructure.adapter.outbound.jpa.user.UserEntity;
import org.charitable.app.infrastructure.mapper.auth.AuthMapper;

public class AdminMapper {

    public static Admin mapToDomain(AdminEntity adminEntity) {
        if (adminEntity == null) return null;

        return getAdmin(adminEntity);
    }


    public static AdminEntity mapToSafeEntity(Admin admin) {
        if (admin == null) return null;

        var entity = new AdminEntity(
                admin.getFirstName(),
                admin.getMiddleName(),
                admin.getLastName(),
                AuthMapper.mapToEntity(admin.getAuth())
        );
        entity.setId(admin.getId());
        entity.setCreatedAt(admin.getCreatedAt());
        entity.setUpdatedAt(admin.getUpdatedAt());
        return entity;
    }

    private static Admin getAdmin(AdminEntity adminEntity) {
        var admin = new Admin(
                adminEntity.getFirstName(),
                adminEntity.getMiddleName(),
                adminEntity.getLastName(),
                AuthMapper.mapToDomain(adminEntity.getAuth())
        );
        admin.setId(adminEntity.getId());
        admin.setCreatedAt(adminEntity.getCreatedAt());
        admin.setUpdatedAt(adminEntity.getUpdatedAt());
        return admin;
    }
}
