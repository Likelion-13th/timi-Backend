package likelion13th.shop.login.auth.utils;

import likelion13th.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor

public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("// 카카오 OAuth2 로그인 시도");

        // 카카오 고유 ID
        String providerId = oAuth2User.getAttributes().get("id").toString();

        @SuppressWarnings("unchecked")
        Map<String, Object> properties =
                (Map<String, Object>) oAuth2User.getAttributes().getOrDefault("properties", Collections.emptyMap());
        // nickname 추출
        String nickname = properties.getOrDefault("nickname", "카카오 사용자").toString();

        Map<String, Object> extendedAttributes = new HashMap<>(oAuth2User.getAttributes());
        // 키 표준화
        extendedAttributes.put("provider_id", providerId);
        extendedAttributes.put("nickname", nickname);

        return new DefaultOAuth2User(
                // 인증 성공 시에 기본 권한 부여
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                extendedAttributes,
                "provider_id"
        );
    }

}

/*
1. 왜 필요한가?
- 카카오에서 받아오는 attributes를 우리의 서비스에서 쓰기 편하게 해주기 위해서 필요함.
(id -> provider_id, properties.nickname -> nickname)
- 이렇게 표준화를 해두면 이후에도 컨트롤러나 도메인 등에서 동일한 키로 동작할 수 있기 때문.

2. 없으면/틀리면?
- 없으면 카카오에서 주는 응답이랑 우리 서비스가 기대하는 게 달라서 향후의 동작에서 문제가 생길 수 있다.
- 닉네임 키가 일관되지 않아 화면/로그/저장 로직에서 속성명이 뒤섞일 수 있고,
로그인 후 세션 및 보안 쪽에서 사용자 식별이 실패할 가능성이 있다.
 */
