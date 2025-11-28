package io.hhplus.tdd.point.svc;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;

import java.util.List;

public interface PointService {

    // 1) 특정 유저의 현재 포인트 조회
    UserPoint getPoint(long userId);

    // 2) 유저의 포인트 내역 조회
    List<PointHistory> getHistories(long userId);
}
