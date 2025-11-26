package io.hhplus.tdd.point.svc;

import io.hhplus.tdd.point.UserPoint;

public interface PointService {

    // 1) 특정 유저의 현재 포인트 조회
    UserPoint getPoint(long id);

}
