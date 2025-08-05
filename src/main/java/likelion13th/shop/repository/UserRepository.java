package likelion13th.shop.repository;

import likelion13th.shop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // userId로 사용자 조회
    Optional<User> findById(Long userId);

    boolean existsById(Long userId);
}

/*
UserRepository.java
OrderRepository.java의 패턴을 참고하여 사용자 정보에 관한 API를 완성함.
JPA Repository 상속 받아 기본 메서드 사용.
 */