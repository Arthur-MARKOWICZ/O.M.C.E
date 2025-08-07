package OMCE.OMCE.User.repository;

import OMCE.OMCE.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    User findByTokenRedefinicao(String token);
}
