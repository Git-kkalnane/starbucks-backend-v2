package git_kkalnane.starbucksbackenv2.domain.pointcard.common;

public enum PointTransactionType {
    DEPOSIT,
    ERROR,
    WITHDRAWAL;

    public String getKorean() {
        switch (this) {
            case DEPOSIT:
                return "입금 되었습니다.";
            case ERROR:
                return "에러가 발생했습니다.";
            case WITHDRAWAL:
                return "출금 되었습니다.";
            default:
                return "알 수 없는 상태입니다.";
        }
    }
}
