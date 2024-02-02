package com.example.store.repository.role;

import com.example.store.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByRole(Role.RoleName role);
}
