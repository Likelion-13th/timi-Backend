package likelion13th.shop.login.auth.dto;

import lombok.Builder;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;

@Builder
@ToString
@Getter
@Setter

public class JwtDto {
    private String accessToken; // 인증, 인가에 사용
    private String refreshToken; // 재발급 요청에 사용

    public JwtDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken; // 필드 주입
        this.refreshToken = refreshToken; // 상동
    }

}

/*
1. 왜 필요한가?
- 로그인 또는 재발급 후에 accessToken과 refreshToken을 한 번에 담아서
  프론트에게 전달하기 위해 필요함.

2. 없으면/틀리면?
- 토큰을 각각 따로 전달하게 되면 파싱 실수와 필드명 오타로 오류가 쉽게 나는 문제가 있음.
- 프론트랑 키가 맞지 않아서 오류가 발생할 수도 있을 것 가틈..!

 */