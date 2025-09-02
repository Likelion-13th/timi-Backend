package likelion13th.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import likelion13th.shop.domain.entity.BaseEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseEntity {

    /** 필드 **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(nullable = false)
    private int price; // 상품 가격

    @Column(nullable = false)
    private String itemName; // 상품명

    @Column(nullable = false)
    private String imagePath; // 상품 이미지

    @Column(nullable = false)
    private  String brand;

    @Column(nullable = false)
    private boolean isNew = false; // 상품 상태 (New인지 아닌지)

    /** 연관관계 설정 **/
    //Category와의 관계 N:N
    @ManyToMany(mappedBy = "items")
    private  List<Category> categories = new ArrayList<>();

    /** Order과 일대다 연관관계 설정
     * -> Item에서 Order의 목록을 볼 일이 없으므로 단방향 처리 **/
}

/*
Item.java
Order.java의 패턴을 참고하여 Item 관련 API를 완성함.
필드와 연관관계 설정, 비즈니스 로직 구현
 */

