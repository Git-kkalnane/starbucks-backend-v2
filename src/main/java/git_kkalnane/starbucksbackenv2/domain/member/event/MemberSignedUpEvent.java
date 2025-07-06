package git_kkalnane.starbucksbackenv2.domain.member.event;

import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import org.springframework.context.ApplicationEvent;

public class MemberSignedUpEvent extends ApplicationEvent {
    private final Member member;

    public MemberSignedUpEvent(Object source, Member member) {
        super(source);
        this.member = member;
    }

    public Member getMember() {
        return member;
    }
}
