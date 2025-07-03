package git_kkalnane.starbucksbackenv2.domain.member.service;


import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.member.dto.request.SignUpRequest;
import git_kkalnane.starbucksbackenv2.domain.member.dto.response.SignUpResponse;
import git_kkalnane.starbucksbackenv2.domain.member.event.MemberSignedUpEvent;
import git_kkalnane.starbucksbackenv2.domain.member.repository.MemberRepository;
import git_kkalnane.starbucksbackenv2.global.utils.Encryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final Encryptor encryptor;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * SignUpRequest를 바탕으로 DB에 회원정보를 저장하는 메서드
     *
     * @param request SignUpRequest 객체
     * @return 멤버의 이름을 담은 SignUpResponse 객체
     */
    @Transactional
    public SignUpResponse createMember(SignUpRequest request) {

//        validateDuplicatedEmail(request.email());

        String encryptedPassword = encryptor.encrypt(request.password());

        Member member = Member.builder()
                .name(request.name())
                .nickname(request.nickname())
                .email(request.email())
                .password(encryptedPassword)
                .build();

        Member savedMember = memberRepository.save(member);
        
        eventPublisher.publishEvent(new MemberSignedUpEvent(this, savedMember));

        return new SignUpResponse(savedMember.getName());
    }

    /**
     * 사용자가 입력한 이메일이 이미 존재하는지 검증하는 메서드
     *
     * @param email 회원가입 요청에 포함된 가입자의 이메일
     */
//    private void validateDuplicatedEmail(String email) {
//        if (memberRepository.existsByEmail(email)) {
//            throw new MemberException(MemberErrorCode.EMAIL_ALREADY_EXISTS);
//        }
//    }
//
//    /**
//     * 매개변수로 들어온 멤버 엔티티 식별자(ID)를 바탕으로 사용자 정보를 DB에서 찾아내어 반환하는 메서드
//     *
//     * @param memberId 사용자 엔티티 식별자 (ID)
//     * @return {@link MemberDetailInfo} 객체
//     */
//    public MemberDetailInfo getMemberDetailInfo(Long memberId) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
//
//        return MemberDetailInfo.of(member.getName(), member.getEmail(), member.getNickname());
//    }
//
//    /**
//     * 매개변수로 들어온 멤버 엔티티 식별자(ID)를 바탕으로 멤버의 닉네임을 변경하는 메서드
//     *
//     * @param memberId 사용자 엔티티 식별자 (ID)
//     * @param request {@link UpdateNicknameRequest}
//     */
//    @Transactional
//    public void updateNickname(Long memberId, UpdateNicknameRequest request) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
//
//        member.modifyNickname(request.nickname());
//    }
//
//    /**
//     * 매개변수로 들어온 멤버 엔티티 식별자(ID)를 바탕으로 멤버의 비밀번호를 변경하는 메서드
//     *
//     * @param memberId 사용자 엔티티 식별자 (ID)
//     * @param request {@link UpdatePasswordRequest}
//     */
//    @Transactional
//    public void updatePassword(Long memberId, UpdatePasswordRequest request) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
//
//        String encryptedPassword = encryptor.encrypt(request.password());
//
//        member.modifyPassword(encryptedPassword);
//    }
}
