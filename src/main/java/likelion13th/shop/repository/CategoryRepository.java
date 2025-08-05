package likelion13th.shop.repository;

import likelion13th.shop.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long categoryId);

    boolean existsById(Long categoryId);
}

/*
CategoryRepository.java
OrderRepository.java의 패턴을 참고하여 카테고리에 관한 API를 완성함.
JPA Repository 상속 받아 기본 메서드 사용.
 */