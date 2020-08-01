package kimosabe.api.repository;

import kimosabe.api.model.Role;
import kimosabe.api.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Optional<Role> findByName(RoleName roleName);
}