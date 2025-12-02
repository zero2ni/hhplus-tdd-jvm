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
    public UserPoint chargePoint(long userId, long amount) {

        // 공통 시간 한 번만 확인하기
        long now = System.currentTimeMillis();

        // 1. 현재 포인트 조회 (없으면 0포인트로 시작)
        UserPoint current = pointRepository.getUserPoint(userId);
        if (current == null) {
            current = UserPoint.empty(userId);
        }

        // 2. 충전 후 포인트 계산
        UserPoint updatePoint = current.charge(amount, now);

        // 3. 충전 히스토리 저장
        pointRepository.saveHistory(new PointHistory(
                0L, userId, amount, TransactionType.CHARGE, now
        ));

        // 4. 변경된 포인트 저장
        return pointRepository.saveUserPoint(updatePoint);
    }

    @Override
    public UserPoint usePoint(long userId, long amount) {

        long now = System.currentTimeMillis();

        // 1. 현재 포인트 조회 (없으면 0포인트)
        UserPoint current = pointRepository.getUserPoint(userId);
        if (current == null) {
            current = UserPoint.empty(userId);
        }

        // 2. 도메인 로직 위임 (잔고 체크 포함)
        UserPoint updated = current.use(amount, now);

        // 3. 사용 히스토리 저장 (amount는 음수!)
        pointRepository.saveHistory(
                new PointHistory(
                        0L,
                        userId,
                        -amount,
                        TransactionType.USE,
                        now
                )
        );

        // 4. 변경된 포인트 저장
        return pointRepository.updateUserPoint(updated);
    }


}
