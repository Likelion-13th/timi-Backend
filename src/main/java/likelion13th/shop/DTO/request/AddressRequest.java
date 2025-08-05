package likelion13th.shop.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddressRequest {
    // 기본 주소
    private Long addressId;
    private String postalCode;
    private String detailAddress;
}

/*
AddressRequest.java
OrderCreateRequest의 패턴을 참고해 사용자의 주소 관련 API를 완성함.
 */
