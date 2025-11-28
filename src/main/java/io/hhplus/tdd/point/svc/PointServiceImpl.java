package io.hhplus.tdd.point.svc;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.repository.PointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;

    public PointServiceImpl(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Override
    public UserPoint getPoint(long userId) {
        // 없으면 0포인트를 기본으로 리턴
        return pointRepository.getUserPoint(userId);
    }

    @Override
    public List<PointHistory> getHistories(long userId) {
        // 없으면 빈 리스트를 리턴 해야 함 (그럼 이게 맞나?)
        return pointRepository.getHistories(userId);
    }


}
