package org.charitable.app.domain.port.outbound.db.admin;


import org.charitable.app.domain.entity.admin.Admin;

public interface AdminRepository {
    Admin register(Admin admin);
}
