package likelion13th.shop.login.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.dto.JwtDto;
import likelion13th.shop.login.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 관련 API (토큰 재발급, 로그아웃)입니다.")
public class UserController {
    private final UserService userService;

    // 토큰 재발급
    @Operation(summary = "토큰 재발급",description = "리프레시 토큰으로 새로운 Access token 발급")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 이상"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "유효하지 않음")
    })
    @PostMapping("reissue")
    public ApiResponse<JwtDto> reissue(HttpServletRequest request) {
        try {
            JwtDto jwt = userService.reissue(request);
            return ApiResponse.onSuccess(SuccessCode.USER_REISSUE_SUCCESS, jwt);
        } catch (GeneralException e) {
            throw e;
        } catch(Exception e) {
            throw  new GeneralException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 로그아웃
    @Operation(summary = "로그아웃",description = "로그아웃 api입니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 이상"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "유효하지 않음")
    })
    @DeleteMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        userService.logout(request);
        return ApiResponse.onSuccess(SuccessCode.USER_LOGOUT_SUCCESS, null);
    }

}
