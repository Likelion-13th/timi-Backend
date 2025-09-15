package likelion13th.shop.login.auth.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion13th.shop.domain.Address;
import likelion13th.shop.domain.User;
import likelion13th.shop.login.auth.dto.JwtDto;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.login.auth.service.JpaUserDetailsManager;
import likelion13th.shop.login.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component

public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final JpaUserDetailsManager jpaUserDetailsManager;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, // 요청
            HttpServletResponse response, // 응답
            Authentication authentication) // 인증된 사용자 정보!
            throws IOException {
        // OAuth2UserServiceImpl에서 만든 attributes 사용
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        String providerId = (String) oAuth2User.getAttributes().get("provider_id");
        String nickname = (String) oAuth2User.getAttributes().get("nickname");

        if (!jpaUserDetailsManager.userExists(providerId)) { // 최초 로그인
            User newUser = User.builder()
                    .providerId(providerId)
                    .usernickname(nickname)
                    .deletable(true)
                    .build();

            // 기본 주소 값
            newUser.setAddress(new Address(
                    "10540",
                    "경기도 고양시 덕양구 항공대학로 76",
                    "한국항공대학교"
            ));

            CustomUserDetails userDetails = new CustomUserDetails(newUser);
            jpaUserDetailsManager.createUser(userDetails);
            log.info("신규 회원 등록이용");
        } else {
            log.info("기존 회원이용");
        }

        // JWT 발급 및 저장
        JwtDto jwt = userService.jwtMakeSave(providerId);

        String frontendRedirectUri = request.getParameter("redirect_uri");
        List<String> authorizeUris = List.of(
                "https://timi-shop.netlify.app/",
                "http://localhost:3000"
        );
        if (frontendRedirectUri != null || authorizeUris.contains(frontendRedirectUri)) {
            frontendRedirectUri = "https://timi-shop.netlify.app/";
        }

        // redirect_uri로 accessToken을 쿼리 파라미터에 담아 리다이렉트
        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendRedirectUri)
                .queryParam("accessToken", jwt.getAccessToken())
                .build().toUriString();
        log.info("리다이렉트 시켜보아요: {}", frontendRedirectUri);

        response.sendRedirect(redirectUrl);
    }
}

/*
1. 왜 필요한가?
- 소셜 로그인을 성공한 후 뒤처리해 주기 위해 필요함.
- 신규 사용자를 가입하고 JWT를 발급한 후 프론트로의 리다이렉트를 위함.

2. 없으면/틀리면?
- 로그인 성공 후에 위의 처리와 같은 것들이 빠지면 프론트 쪽에서는 누가 로그인을 했는지 모름.
- 화이트리스트를 체크하지 않으면 서버가 잘못된 주소로 보내버릴 수가 있고, 보안과 안정성 문제가 생김.
 */
