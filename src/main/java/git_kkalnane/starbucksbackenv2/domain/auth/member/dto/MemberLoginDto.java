package git_kkalnane.starbucksbackenv2.domain.auth.member.dto;

import git_kkalnane.starbucksbackenv2.domain.auth.member.dto.response.MemberLoginResponse;

public record MemberLoginDto(String accessToken, String refreshToken, String name, String nickname, String email) {

    public static MemberLoginDto of(String accessToken, String refreshToken, String name, String nickname, String email) {
        return new MemberLoginDto(accessToken, refreshToken, name, nickname, email);
    }

    public MemberLoginResponse toLoginResponse() {
        return new MemberLoginResponse(name, nickname, email);
    }
}
