package git_kkalnane.starbucksbackenv2.domain.item.dto.response;

import git_kkalnane.starbucksbackenv2.domain.item.dto.BeverageItemDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * 음료 목록 전체를 담아 페이지네이션 정보와 함께 반환하는 DTO
 * <br><br>
 * 음료 리스트와 총 개수 및 페이지네이션 정보를 포함한다.
 */
@Builder
@Getter
public class BeveragePaginationResponse {

    /**
     * 조회된 음료 리스트
     */
    private List<BeverageItemDto> beverages;

    /**
     * 전체 음료의 총 개수
     */
    private long totalCount;

    /**
     * 현재 페이지 번호 (0부터 시작)
     */
    private int currentPage;

    /**
     * 전체 페이지 수
     */
    private int totalPages;

    /**
     * 한 페이지당 아이템 개수
     */
    private int pageSize;
}
