package git_kkalnane.starbucksbackenv2.domain.cart.controller;

import git_kkalnane.starbucksbackenv2.domain.cart.common.success.CartSuccessCode;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.FavoriteCartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.FavoriteSimpleDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.favorite.FavoriteCartItemResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.favorite.FavoriteSimpleResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.service.favorite.FavoriteCartService;
import git_kkalnane.starbucksbackenv2.global.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteCartController {

    private final FavoriteCartService favoriteCartService;

    @Operation(
            summary = "나의 음료 상품 추가",
            description = "로그인한 사용자의 장바구니에 나의 음료을 추가합니다. 이미 담긴 동일한 상품(옵션 포함)의 경우 수량이 더해집니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장바구니 상품 추가 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 유효성 오류 (예: 수량이 0 이하)"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 또는 상품")
    })
    @PostMapping(value = "/addItem")
    public ResponseEntity<SuccessResponse> addItem(
            @Parameter(description = "추가할 상품의 ID와 수량, 옵션 등의 정보") @RequestBody FavoriteSimpleDto favoriteSimpleDto,
            @RequestAttribute(name = "memberId") Long memberId) {

        FavoriteSimpleResponse response = favoriteCartService.createFavoriteCartItem(favoriteSimpleDto, memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(CartSuccessCode.CART_SUCCESS_CODE, response));
    }

    @Operation(
            summary = "나의 음료 상품 추가",
            description = "로그인한 사용자의 장바구니에 나의 음료을 추가합니다. 이미 담긴 동일한 상품(옵션 포함)의 경우 수량이 더해집니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장바구니 상품 추가 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 유효성 오류 (예: 수량이 0 이하)"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 또는 상품")
    })
    @PostMapping(value = "/addItem/option")
    public ResponseEntity<SuccessResponse> addItem(
            @Parameter(description = "추가할 상품의 ID와 수량, 옵션 등의 정보") @RequestBody FavoriteCartItemDto favoriteCartItemDto,
            @RequestAttribute(name = "memberId") Long memberId) {

        FavoriteCartItemResponse response = favoriteCartService.createFavoriteCartItemWithOption(memberId, favoriteCartItemDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(CartSuccessCode.CART_SUCCESS_CODE, response));
    }


    @Operation(
            summary = "나의 음료에서 상품 삭제",
            description = "로그인한 사용자의 나의 음료에 상품을 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 삭제 성공")
    })
    @DeleteMapping(value = "deleteItem")
    public ResponseEntity<SuccessResponse> deleteItem(
            @RequestAttribute(name = "memberId") Long memberId,
            @RequestParam Long cartItemId) {

        favoriteCartService.deleteCartItem(cartItemId, memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(CartSuccessCode.CART_SUCCESS_DELETED));
    }
}
