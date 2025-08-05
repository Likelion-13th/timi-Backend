package likelion13th.shop.service;

import jakarta.transaction.Transactional;
import likelion13th.shop.DTO.response.UserInfoResponse;
import likelion13th.shop.DTO.response.UserMileageResponse;
import likelion13th.shop.DTO.response.AddressResponse;
import likelion13th.shop.domain.Address;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAddressService{
    private final UserRepository userRepository;

    /** 내 정보 조회 **/
    @Transactional
    public UserInfoResponse getUserInfo (CustomUserDetails customUserDetails) {
        User user = userRepository.findById(customUserDetails.getUserId()) // 유저 정보 조회
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_INFO_NOT_FOUND));

        return UserInfoResponse.from(user);
    }

    /** 내 마일리지 조회 **/
    @Transactional
    public UserMileageResponse getUserMileage (CustomUserDetails customUserDetails) {
        User user = userRepository.findById(customUserDetails.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_INFO_NOT_FOUND));

        return UserMileageResponse.from(user);
    }

    /** 내 주소 조회 **/
    @Transactional
    public AddressResponse getUserAddress (CustomUserDetails customUserDetails) {
        User user = userRepository.findById(customUserDetails.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_INFO_NOT_FOUND));

        Address address = user.getAddress();
        if (address == null) throw new GeneralException(ErrorCode.ADDRESS_NOT_FOUND);
        return AddressResponse.from(user.getAddress());

    }
}

/*
UserAddressService.java
OrderService 패턴 참고하여 사용자 정보에 관한 API 완성함.
의존성 주입 및 비즈니스 로직 구현
 */
