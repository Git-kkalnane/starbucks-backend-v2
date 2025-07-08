package git_kkalnane.starbucksbackenv2.domain.cart.controller;

import git_kkalnane.starbucksbackenv2.domain.cart.common.success.CartSuccessCode;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.DeleteCartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.ModifyCartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.CartItemResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.ModifyCartItemResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.service.CartService;
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
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(
            summary = "장바구니에 상품 추가",
            description = "로그인한 사용자의 장바구니에 상품을 추가합니다. 이미 담긴 동일한 상품(옵션 포함)의 경우 수량이 더해집니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장바구니 상품 추가 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 유효성 오류 (예: 수량이 0 이하)"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 또는 상품")
    })
    @PostMapping(value = "/addItem")
    public ResponseEntity<SuccessResponse> addItem(
            @Parameter(description = "추가할 상품의 ID와 수량, 옵션 등의 정보") @RequestBody CartItemDto cartItemDto,
            @RequestAttribute(name = "memberId") Long memberId) {

        CartItemResponse response = cartService.addCartItem(memberId, cartItemDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(CartSuccessCode.CART_SUCCESS_CODE, response));
    }

    @Operation(
            summary = "장바구니 수량 수정",
            description = "로그인한 사용자의 장바구니에서 상품 수량을 수정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장바구니 수량 수정 성공")
    })
    @PutMapping(value = "/modifyItem")
    public ResponseEntity<SuccessResponse> updateItem( @RequestBody ModifyCartItemDto modifyCartItemDto,
                                                       @RequestAttribute(name = "memberId") Long memberId

    ) {
        ModifyCartItemResponse modifyCartItemResponse = cartService.modifyCartItem(modifyCartItemDto, memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(CartSuccessCode.CART_SUCCESS_MODIFIED, modifyCartItemResponse));
    }
    @Operation(
            summary = "장바구니에서 상품 삭제",
            description = "로그인한 사용자의 장바구니에 상품을 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장바구니 상품 삭제 성공")
    })

    @DeleteMapping(value = "deleteItem")
    public ResponseEntity<SuccessResponse> deleteItem(
            @RequestBody DeleteCartItemDto deleteCartItemDto,
            @RequestAttribute(name = "memberId") Long memberId) {

        cartService.deleteCartItem(deleteCartItemDto, memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(CartSuccessCode.CART_SUCCESS_DELETED));
    }


}
