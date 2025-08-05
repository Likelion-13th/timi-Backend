package likelion13th.shop.service;

import jakarta.transaction.Transactional;
import likelion13th.shop.DTO.response.ItemResponse;
import likelion13th.shop.domain.Category;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /** 카테고리 별 상품 조회 **/
    @Transactional
    public List<ItemResponse>getItemsByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GeneralException(ErrorCode.CATEGORY_NOT_FOUND));

        return category.getItems().stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
    }
}

/*
CategoryService.java
OrderService의 패턴을 참고하여 category 관련 API를 완성함.
의존성 주입 및 비즈니스 로직 구현
 */