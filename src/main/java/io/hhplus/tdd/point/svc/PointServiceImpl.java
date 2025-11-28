package io.hhplus.tdd.point.svc;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
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

    /*
    * 포인트 조회 후 저장
    * */
    @Override
    public UserPoint saveCharge(long userId, long amount) {

        // 1. 현재 포인트 조회 (없으면 0포인트로 시작)
        UserPoint current = pointRepository.getUserPoint(userId);
        if (current == null) {
            current = UserPoint.empty(userId);
        }

        // 2. 충전 후 포인트 계산
        long newPoint = current.point() + amount;
        UserPoint updated = new UserPoint(
                userId,
                newPoint,
                System.currentTimeMillis()
        );

        // 3. 충전 히스토리 저장
        PointHistory history = new PointHistory(
                0L,                                     // id는 DB에서 채운다고 보고 0으로 둬도 됨
                userId,
                amount,
                TransactionType.CHARGE,
                System.currentTimeMillis()
        );
        pointRepository.saveHistory(history);

        // 4. 변경된 포인트 저장
        UserPoint saved = pointRepository.saveUserPoint(updated);

        // 5. 결과 반환
        return saved;
    }

    @Override
    public UserPoint usePoint(long userId, long amount) {
        return null;
    }


}
