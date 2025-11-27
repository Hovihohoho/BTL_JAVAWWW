package iuh.btl_n7_iuh.services;

        import iuh.btl_n7_iuh.entities.Role;
        import iuh.btl_n7_iuh.repositories.RoleRepository;
        import lombok.RequiredArgsConstructor;
        import org.springframework.stereotype.Service;

        import java.util.List;
        import java.util.Optional;

        @Service
        @RequiredArgsConstructor
        public class RoleServices {
            private final RoleRepository roleRepository;

            public List<Role> findAll() {
                return roleRepository.findAll();
            }

            public Optional<Role> findById(Long id) {
                return roleRepository.findById(id);
            }

            public Optional<Role> findByName(String name) {
                return Optional.ofNullable(roleRepository.findByName(name));
            }
        }