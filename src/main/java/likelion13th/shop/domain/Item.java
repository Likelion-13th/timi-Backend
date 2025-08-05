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
@Table(name = "items")
@NoArgsConstructor
public class Item extends BaseEntity {

    /** 필드 **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(nullable = false)
    private int itemPrice; // 상품 가격

    @Column(nullable = false)
    private int itemLeft; // 상품 잔여 수량

    @Column(nullable = false)
    private String itemName; // 상품명

    @Column(nullable = false)
    private String itemImg; // 상품 이미지

    @Column(nullable = false)
    private String itemStatus; // 상품 상태 (New인지 아닌지)

    /** 연관관계 설정 **/
    // Order과의 관계 1:N
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    //Category와의 관계 N:N
    @ManyToMany
    @JoinTable(
            name = "ItemCategory", // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "item_id"), // 현재 테이블의 FK
            inverseJoinColumns = @JoinColumn(name = "category_id") // 반대 테이블의 FK
    )
    private List<Category> categories = new ArrayList<>();

    /** 생성자 및 비즈니스 로직 등등 **/
    // 내부 생성자 메서드 -> 필수 값만으로도 객체 생성
    private Item(String itemName, int itemPrice, int itemLeft, String itemImg, String itemStatus) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemLeft = itemLeft;
        this.itemImg = itemImg;
        this.itemStatus = itemStatus;
    }
    // 정적 팩토리 메서드 -> 객체 생성의 진입점
    public static Item create(String itemName, int itemPrice, int itemLeft, String itemImg, String itemStatus) {
        Item item = new Item(itemName, itemPrice, itemLeft, itemImg, itemStatus);
        item.itemName = itemName;
        item.itemPrice = itemPrice;
        item.itemLeft = itemLeft;
        item.itemImg = itemImg;
        item.itemStatus = itemStatus;
        return item;
    }

    // 상품 가격
    public int getPrice() {
        return this.itemPrice;
    }
}

/*
Item.java
Order.java의 패턴을 참고하여 Item 관련 API를 완성함.
필드와 연관관계 설정, 비즈니스 로직 구현
 */

