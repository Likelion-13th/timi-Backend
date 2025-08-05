package likelion13th.shop.DTO.response;

import likelion13th.shop.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private String postalCode;
    private String address;
    private String detailAddress;

    public static AddressResponse from(Address address) {
        return new AddressResponse(
                address.getPostalCode(),
                address.getAddress(),
                address.getDetailAddress()
        );
    }
}

/*
AddressResponse.java
OrderResponse 패턴 참고하여 사용자의 주소에 관한 API 완성함.
정적 팩토리 메서드 사용하여 가독성 높임. 프론트에 필요한 정보 담음.
 */
