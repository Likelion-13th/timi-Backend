package likelion13th.shop.repository;

import likelion13th.shop.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}

/*
ItemRepository.java
OrderRepository.java의 패턴을 참고하여 item에 관한 API를 완성함.
JPA Repository 상속 받아 기본 메서드 사용.
 */
