package git_kkalnane.starbucksbackenv2.domain.auth.merchant.dto;


import git_kkalnane.starbucksbackenv2.domain.auth.merchant.dto.response.MerchantLoginResponse;

public record MerchantLoginDto(String accessToken, String refreshToken, String name, String email) {

    public static MerchantLoginDto of(String accessToken, String refreshToken, String name, String email) {
        return new MerchantLoginDto(accessToken, refreshToken, name, email);
    }

    public MerchantLoginResponse toLoginResponse() {
        return new MerchantLoginResponse(name, email);
    }
}
