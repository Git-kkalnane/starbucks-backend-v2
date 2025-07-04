package git_kkalnane.starbucksbackenv2.domain.auth.merchant.dto.response;

public record MerchantLoginResponse(String name, String email) {

    public static MerchantLoginResponse of(String name, String email) {
        return new MerchantLoginResponse(name, email);
    }
}
