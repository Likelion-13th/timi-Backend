package likelion13th.shop.domain;

import jakarta.persistence.*;
import likelion13th.shop.domain.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseEntity {

    /** 필드 **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String providerId; // 소셜 로그인 식별자

    @Column(nullable = false)
    private String userName; // 사용자명

    // (기본값 0, 비즈니스 메서드로만 관리)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private int maxMileage = 0; // 사용 가능한 마일리지

    // (기본값 0, 비즈니스 메서드로만 관리)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private int recentlyUsed = 0; // 최근 결제 금액

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "postalCode", column = @Column(name = "postalCode", nullable = false)),
            @AttributeOverride(name = "address", column = @Column(name = "address", nullable = false)),
            @AttributeOverride(name = "detailAddress", column = @Column(name = "detailAddress", nullable = false))
    })
    private Address address;

    /** 연관관계 설정 **/
    // Order과의 관계 1:N
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    /** 생성자 및 비즈니스 로직 등등 **/
    // 내부 생성자 메서드 -> 필수 값만으로도 객체 생성
    private User(String providerId, String userName, Address address) {
        this.providerId = providerId;
        this.userName = userName;
        this.address = address;
        this.maxMileage = 0;
        this.recentlyUsed = 0;
    }

    // 정적 팩토리 메서드 -> 객체 생성의 진입점
    public static User create(String providerId, String userName, Address address) {
        User user = new User(providerId, userName, address);
        user.providerId = providerId;
        user.userName = userName;
        return user;
    }

    // 주문 추가 메서드
    public void addOrder(Order order) {
        this.orders.add(order);
        order.setUser(this);
    }

    // 마일리지 사용
    public void useMileage(int mileage) {
        if (mileage < 0) {
            throw new IllegalArgumentException("사용할 마일리지는 0보다 커야 합니다.");
        }
        if (this.maxMileage < mileage) {
            throw new IllegalArgumentException("마일리지가 부족합니다.");
        }
        this.maxMileage -= mileage;
    }

    // 마일리지 적립
    public void addMileage(int mileage) {
        if (mileage < 0) {
            throw new IllegalArgumentException("적립할 마일리지는 0보다 커야 합니다.");
        }
        this.maxMileage += mileage;
    }

    // 총 결제 금액 업데이트
    public void updateRecentTotal(int amount) {
        int newTotal = this.recentlyUsed + amount;
        if (newTotal < 0) {
            throw new IllegalArgumentException("총 결제 금액은 음수가 될 수 없습니다.");
        }
        this.recentlyUsed = newTotal;
    }
}

/*
User.java
Order.java의 패턴을 참고하여 User 관련 API를 완성함.
필드와 연관관계 설정, 비즈니스 로직 구현
 */

