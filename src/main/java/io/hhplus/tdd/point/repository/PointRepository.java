package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository {

    // 유저 포인트 조회
    UserPoint getUserPoint(long userId);

    List<PointHistory> getHistories(long userId);
}
