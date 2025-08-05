package likelion13th.shop.DTO.response;

import likelion13th.shop.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    private Long itemId;
    private String itemName;
    private int itemPrice;
    private int itemLeft;
    private String itemImg;
    private String itemStatus;

    public static ItemResponse from(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getItemName(),
                item.getItemPrice(),
                item.getItemLeft(),
                item.getItemImg(),
                item.getItemStatus()
        );
    }
}

/*
ItemResponse.java
OrderResponse 패턴 참고하여 아이템에 관한 API 완성함.
정적 팩토리 메서드 사용하여 가독성 높임. 프론트에 필요한 정보 담음.
 */