package likelion13th.shop.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13th.shop.DTO.response.ItemResponse;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.service.CategoryService;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@Tag(name = "카테고리", description = "카테고리 관련 API 입니다.")
@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /** 카테고리 별 상품 조회 **/
    @GetMapping("/{categoryId}/items")
    @Operation(summary = "카테고리 별 상품 조회", description = "카테고리 별 상품을 조회합니다." )
    public ApiResponse<?> getItemsByCategory(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<ItemResponse> items = categoryService.getItemsByCategoryId(categoryId);

        // 카테고리가 비어있더라도 성공 응답 + 빈 리스트 반환
        if (items.isEmpty()) {
            return ApiResponse.onSuccess(SuccessCode.CATEGORY_ITEMS_EMPTY, Collections.emptyList());
        }

        // 카테고리가 비어있지 않을 때
        return ApiResponse.onSuccess(SuccessCode.CATEGORY_ITEMS_GET_SUCCESS, items);
    }

}

/*
 CategoryController.java
 OrderController 패턴 참고하여 카테고리 별 상품 조회에 관한 API 완성함.
 @Operation으로 swagger 문서화, ApiResponse로 일관된 응답 형식 유지
*/