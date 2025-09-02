package likelion13th.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "category")
@NoArgsConstructor
public class Category {

    /** 필드 **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(name = "category_name", nullable = false)
    private String name; // 카테고리 이름

    /** 연관관계 설정 **/
    // Item과의 관계 N:N
    @ManyToMany
    @JsonIgnore //무한 루프 방지  (카테고리 내부에서 items 목록을 JSON 변환에서 제외)
    @JoinTable(name = "category_item", //중간 테이블 자동으로 생성
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    /** db에 직접 넣을 경우에는 필요하지 x **/
    // 생성자로 기본 값 설정
//    public Category(String name) {
//        this.name = name;
//    }

    //양방향 관계 설정
//    public void addItem(Item item) {
//        if (!this.items.contains(item)) {
//            this.items.add(item);
//            if (!item.getCategories().contains(this)) {
//                item.getCategories().add(this);
//            }
//        }
//    }

}

/*
Category.java
Order.java의 패턴을 참고하여 Category 관련 API를 완성함.
필드와 연관관계 설정, 비즈니스 로직 구현
 */
