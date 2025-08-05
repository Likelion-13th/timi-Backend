package likelion13th.shop.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13th.shop.DTO.response.UserInfoResponse;
import likelion13th.shop.DTO.response.UserMileageResponse;
import likelion13th.shop.DTO.response.AddressResponse;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.login.auth.service.UserService;
import likelion13th.shop.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "사용자", description ="사용자 정보 및 주소 관련 API 입니다.")
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserInfoController {
    private final UserService userService;
    private final UserAddressService userAddressService;

    /** 내 정보 조회 **/
    @GetMapping("/profile")
    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
    public ApiResponse<?> getMyProfile (
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        UserInfoResponse userInfo = userAddressService.getUserInfo(customUserDetails);
        return ApiResponse.onSuccess(SuccessCode.USER_INFO_GET_SUCCESS, userInfo);
    }

    /** 내 마일리지 조회 **/
    @GetMapping("/mileage")
    @Operation(summary = "내 마일리지 조회", description = "로그인한 사용자의 마일리지를 조회합니다.")
    public ApiResponse<?> getMyMileage (
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        UserMileageResponse mileage = userAddressService.getUserMileage(customUserDetails);
        return ApiResponse.onSuccess(SuccessCode.USER_MILEAGE_GET_SUCCESS, mileage);
    }

    /** 내 주소 조회 **/
    @GetMapping("/address")
    @Operation(summary = "내 주소 조회", description = "로그인한 사용자의 주소를 조회합니다.")
    public ApiResponse<?> getMyAddress (
            @AuthenticationPrincipal CustomUserDetails customUserDetails

    ) {
        AddressResponse address = userAddressService. getUserAddress(customUserDetails);
        return ApiResponse.onSuccess(SuccessCode.ADDRESS_GET_SUCCESS, address);
    }
}

/*
 UserInfoController.java
 OrderController 패턴 참고하여 사용자 정보에 관한 API 완성함.
 @operation으로 swagger 문서화, ApiResponse로 일관된 응답 형식 유지
 */