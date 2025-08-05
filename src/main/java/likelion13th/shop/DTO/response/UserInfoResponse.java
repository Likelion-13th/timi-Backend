package likelion13th.shop.DTO.response;

import likelion13th.shop.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private Long userId;
    private String providerId;
    private String userName;
    private int recentlyUsed;

    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
                user.getId(),
                user.getProviderId(),
                user.getUserName(),
                user.getRecentlyUsed()
        );
    }
}

/*
UserInfoResponse.java
OrderResponse 패턴 참고하여 사용자 정보에 관한 API 완성함.
정적 팩토리 메서드 사용하여 가독성 높임. 프론트에 필요한 정보 담음.
 */
