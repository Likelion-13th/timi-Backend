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
@Table(name = "categories")
@NoArgsConstructor
public class Category extends BaseEntity {

    /** 필드 **/
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(nullable = false)
    private String categoryName; // 카테고리 이름

    /** 연관관계 설정 **/
    // Item과의 관계 N:N
    @ManyToMany (mappedBy = "categories")
    private List<Item> items = new ArrayList<>();

    /** 생성자 및 비즈니스 로직 등등 **/
    // 내부 생성자 메서드 -> 필수 값만으로도 객체 생성
    private Category(String categoryName) {
        this.categoryName = categoryName;
    }

    // 정적 팩토리 메서드 -> 객체 생성의 진입점
    public static Category create(String categoryName) {
        Category category = new Category(categoryName);
        category.categoryName = categoryName;
        return category;
    }

}

/*
Category.java
Order.java의 패턴을 참고하여 Category 관련 API를 완성함.
필드와 연관관계 설정, 비즈니스 로직 구현
 */
