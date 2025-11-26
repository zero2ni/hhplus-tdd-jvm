package io.hhplus.tdd.point.svc;

import io.hhplus.tdd.point.UserPoint;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PointServiceImpl implements PointService {

    // 유저별 현재 포인트 저장소 (userId -> UserPoint)
    private final Map<Long, UserPoint> pointStore = new ConcurrentHashMap<>();

    @Override
    public UserPoint getPoint(long id) {
        // 없으면 0포인트를 기본으로 리턴
        UserPoint userPoint = pointStore.get(id);
        if (userPoint == null) {
            long now = System.currentTimeMillis();
            return new UserPoint(id, 0L, now);
        }
        return userPoint;
    }
}
