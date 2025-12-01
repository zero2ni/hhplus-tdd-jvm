package io.hhplus.tdd.point;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    /**
     * 포인트 충전 도메인 로직
     */
    public UserPoint charge(long amount, long now) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }

        return new UserPoint(
                this.id,
                this.point + amount,
                now
        );
    }

    /**
     * 포인트 사용 도메인 로직
     */
    public UserPoint use(long amount, long now) {
        if (amount <= 0) {
            throw new IllegalArgumentException("사용 금액은 0보다 커야 합니다.");
        }
        if (this.point < amount) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }

        return new UserPoint(
                this.id,
                this.point - amount,
                now
        );
    }
}


