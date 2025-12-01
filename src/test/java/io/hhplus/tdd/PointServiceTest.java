package io.hhplus.tdd;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.repository.PointRepository;
import io.hhplus.tdd.point.svc.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SpringBootTest
public class PointServiceTest {

    private static final Logger log = LoggerFactory.getLogger(PointServiceTest.class);

    @MockBean
    private PointRepository pointRepository;

    @Autowired
    private PointService pointService;

    /*
    * ID가 정확히 전달되는지
    * 없는 유저이므로 포인트가 0으로 되는지
    * */
    @Test
    @DisplayName("포인트가 없는 유저를 조회하면 0 포인트를 반환한다")
    void getPoint_whenUserHasNoPoint_thenReturnZero() {
        // given
        long id = 1L;

        given(pointRepository.getUserPoint(id))
                .willReturn(UserPoint.empty(id));

        // when
        UserPoint result = pointService.getPoint(id);

        // then
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.point()).isEqualTo(0L);

        // 일부러 틀린 기대값 넣어보기 (RED)
         assertThat(result.point()).isEqualTo(999L);
    }

    /*
    * 내역조회
    * (1) 성공적인 조회
    * (2) 히스토리가 없는 경우 → 빈 리스트 반환
    * */
    // (1) happy case
    @Test
    @DisplayName("유저의 포인트 사용/충전 내역을 조회한다")
    void getHistories_whenUserExists_thenReturnHistoryList() {
        // given
        long id = 1L;

        PointHistory h1 = new PointHistory(1L, id, 100L, TransactionType.CHARGE, System.currentTimeMillis());
        PointHistory h2 = new PointHistory(2L, id, -50L, TransactionType.USE, System.currentTimeMillis());

        given(pointRepository.getHistories(id))
                .willReturn(List.of(h1, h2));

        // when
        List<PointHistory> result = pointService.getHistories(id);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).userId()).isEqualTo(id);
    }

    // 히스토리가 없는 경우 → 빈 리스트 반환
    // Repository가 정확히 한 번, 올바른 파라미터로 호출됐는지 검증
    @Test
    @DisplayName("거래 내역이 없는 유저를 조회하면 빈 리스트를 반환한다")
    void getHistories_whenUserHasNoHistory_thenReturnEmptyList() {
        // given
        long id = 2L;
        given(pointRepository.getHistories(id))
                .willReturn(List.of());

        // when
        List<PointHistory> result = pointService.getHistories(id);

        // then
        assertThat(result).isEmpty();
        then(pointRepository).should().getHistories(id);
    }

    @Test
    @DisplayName("유저가 포인트를 충전하면 누적된 포인트를 반환한다")
    void chargePoint_whenValidAmount_thenIncreasePoint() {
        long userId = 1L;

        // 충전할 포인트
        long amount = 100L;

        // 충전 전 기본 포인트 / 충전 후 기대 포인트
        UserPoint current = new UserPoint(userId, 200L, System.currentTimeMillis());
        UserPoint expected = new UserPoint(userId, 300L, System.currentTimeMillis());

        // 현재 포인트 조회
        given(pointRepository.getUserPoint(userId))
                .willReturn(current);

        log.info(" (1) 유저의 현재 포인트: {}", current);

        // 충전 내역 저장
        PointHistory savedHistory = new PointHistory(
                1L, userId, amount, TransactionType.CHARGE, System.currentTimeMillis()
        );
        given(pointRepository.saveHistory(any()))
                .willReturn(savedHistory);

        // 충전 후 사용자 포인트 저장
        given(pointRepository.saveUserPoint(any()))
                .willReturn(expected);

        log.info(" (2) 유저의 충천 후 포인트: {}", expected);

        // when
        UserPoint result = pointService.chargePoint(userId, amount);

        // then
        assertThat(result.point()).isEqualTo(300L);
        then(pointRepository).should().getUserPoint(userId);
        then(pointRepository).should().saveHistory(any());
        then(pointRepository).should().saveUserPoint(any());
    }

}
