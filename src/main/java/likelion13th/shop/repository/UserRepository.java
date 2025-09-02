package likelion13th.shop.repository;

import likelion13th.shop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // user_id 기반 사용자 찾기 (feature/4)
    Optional<User> findById(Long userId);

    boolean existsById(Long userId);

    // providerId(카카오 고유 ID) 기반 조회 (feature/4)
    Optional<User> findByProviderId(String providerId);

    boolean existsByProviderId(String providerId);

    // usernickname(닉네임) 기반 사용자 찾기 (develop)
    //List<User> findByUsernickname(String usernickname);

    // 향후 필요 시 사용할 수 있도록 주석 유지
    //Optional<User> findByKakaoId(String kakaoId);
}

/*
UserRepository.java
OrderRepository.java의 패턴을 참고하여 사용자 정보에 관한 API를 완성함.
JPA Repository 상속 받아 기본 메서드 사용.
 */