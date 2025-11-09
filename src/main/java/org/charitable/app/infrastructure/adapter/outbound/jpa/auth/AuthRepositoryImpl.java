package org.charitable.app.infrastructure.adapter.outbound.jpa.auth;

import jakarta.inject.Singleton;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.organization.Organization;
import org.charitable.app.domain.entity.user.User;
import org.charitable.app.domain.model.auth.AuthStatus;
import org.charitable.app.domain.port.outbound.auth.AuthRepository;
import org.charitable.app.infrastructure.mapper.admin.AdminMapper;
import org.charitable.app.infrastructure.mapper.auth.AuthMapper;
import org.charitable.app.infrastructure.mapper.organization.OrganizationMapper;
import org.charitable.app.infrastructure.mapper.user.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

@Singleton
class AuthRepositoryImpl implements AuthRepository {

    private static final Logger logger = LoggerFactory.getLogger(AuthRepositoryImpl.class);

    private final AuthJpaRepository jpaRepository;

    AuthRepositoryImpl(AuthJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Auth> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(entity -> {
                    return mapToDomain(entity);
                });
    }

    @Override
    public Auth findMyInfo(UUID id) {
         var auth = jpaRepository.findByIdWithRelations(id);

         if(auth.isEmpty()) {
             throw AppException.badRequest("Requested data not found");
         }
         var entity = auth.get();

         Organization orgEntity = null;
         if (entity.getOrganization() != null) {
             orgEntity = Organization.builder()
                     .id(entity.getOrganization().getId())
                     .name(entity.getOrganization().getName())
                     .address(entity.getOrganization().getAddress())
                     .latitude(entity.getOrganization().getLatitude())
                     .longitude(entity.getOrganization().getLongitude())
                     .govtId(entity.getOrganization().getGovtId())
                     .contactNumber(entity.getOrganization().getContactNumber())
                     .build();
         }

        User userEntity = null;
         if(entity.getUser() != null) {
             var user = entity.getUser();
             userEntity = User.builder()
                     .id(user.getId())
                     .firstName(user.getFirstName())
                     .middleName(user.getMiddleName())
                     .lastName(user.getLastName())
                     .latitude(user.getLatitude())
                     .longitude(user.getLongitude())
                     .build();
         }

         return Auth.builder()
                 .id(entity.getId())
                 .email(entity.getEmail())
                 .phone(entity.getPhoneNumber())
                 .status(entity.getStatus())
                 .role(entity.getRole())
                 .organization(orgEntity)
                 .user(userEntity)
                 .build();
    }

    @Override
    public Optional<Auth> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(AuthMapper::mapToDomain);
    }

    @Override
    public Auth save(Auth auth) {

        var entity = AuthEntity.builder()
                .email(auth.getEmail())
                .password(auth.getPassword())
                .phoneNumber(auth.getPhone())
                .role(auth.getRole())
                .status(auth.getStatus())
                .isEmailVerified(auth.getIsEmailVerified())
                .status(auth.getStatus())
                .organization(OrganizationMapper.mapToEntitySafe(auth.getOrganization()))
                .user(UserMapper.mapToSafeEntity(auth.getUser()))
                .admin(AdminMapper.mapToSafeEntity(auth.getAdmin()))
                .build();

        var savedEntity = jpaRepository.save(entity);
        logger.info("Saved user: {}", savedEntity);
        return mapToDomain(savedEntity);
    }


    @Override
    public Auth updateAuthStatus(UUID id, AuthStatus status) {
        var resp = jpaRepository.updateAuthStatus(id, status);

        if(resp.isEmpty()) {
            throw AppException.badRequest("Requested data not found");
        }

        return mapToDomain(resp.get());
    }

    public static Auth mapToDomain(AuthEntity entity) {
//        var auth =  new Auth(
//                entity.getEmail(),
//                entity.getPassword(),
//                entity.getPhoneNumber(),
//                entity.getRole(),
//                entity.getIsEmailVerified(),
//                entity.getStatus(),
//                null,
//                null,
//                null
//        );
//
//        auth.setId(entity.getId());
//        auth.setCreatedAt(entity.getCreatedAt());
//        auth.setUpdatedAt(entity.getUpdatedAt());

        var auth = Auth.builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .phone(entity.getPhoneNumber())
                .role(entity.getRole())
                .isEmailVerified(entity.getIsEmailVerified())
                .status(entity.getStatus())
                .build();
        return auth;
    }
}
