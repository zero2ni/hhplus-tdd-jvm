package io.hhplus.tdd;

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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

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

}
