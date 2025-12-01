package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository {

    // 유저 포인트 조회
    UserPoint getUserPoint(long userId);

    // 히스토리 조회
    List<PointHistory> getHistories(long userId);

    // 유저 포인트 저장
    UserPoint saveUserPoint(UserPoint userPoint);

    // 히스토리 저장
    PointHistory saveHistory(PointHistory history);

    UserPoint updateUserPoint(UserPoint userPoint);

    UserPoint deleteUserPoint(UserPoint userPoint);


}
