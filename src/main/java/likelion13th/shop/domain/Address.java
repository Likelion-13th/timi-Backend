package likelion13th.shop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Address {

    /** 필드 **/
    @Column(nullable = false)
    private String postalCode; // 우편번호

    @Column(nullable = false)
    private String address; // 주소

    @Column(nullable = false)
    private String detailAddress; // 상세주소

}

/*
Address.java
Order.java의 패턴을 참고하여 Address 관련 API를 완성함.
필드 설정, User 테이블에 일부 칼럼으로 저장하기 위해서 임베디드로 함
 */