package likelion13th.shop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@Getter
public class Address {

    /** 필드 **/
    @Column(nullable = false)
    private String zipcode; // 우편번호

    @Column(nullable = false)
    private String address; // 주소

    @Column(name = "address_detail", nullable = false)
    private String addressDetail; // 상세주소

    public Address() {
        this.zipcode = "10540";
        this.address = "경기도 고양시 덕양구 항공대학로 76";
        this.addressDetail = "한국항공대학교";
    }

}

/*
Address.java
Order.java의 패턴을 참고하여 Address 관련 API를 완성함.
필드 설정, User 테이블에 일부 칼럼으로 저장하기 위해서 임베디드로 함
 */