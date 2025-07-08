package git_kkalnane.starbucksbackenv2.domain.notification.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NotificationsResponse {
    private List<NotificationResponse> notifications;
    private long total;
    private int page;
    private int pageSize;
    private int totalPages;
}
