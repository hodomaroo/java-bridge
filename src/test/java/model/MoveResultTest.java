package model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MoveResultTest {
    private static Boolean succeed = true;
    private static Boolean fail = false;
    private static Boolean undefined = null;

    @Nested
    @DisplayName("getMatchResult 메서드는 숫자 값을 입력받아")
    class describe_getMoving {

        @Test
        @DisplayName("알맞은 MoveResult 객체를 반환한다.")
        void returnMovingTest(){
            assertThat(MoveResult.getMatchResult(succeed)).isEqualTo(MoveResult.SUCCESS);
            assertThat(MoveResult.getMatchResult(fail)).isEqualTo(MoveResult.FAIL);
            assertThat(MoveResult.getMatchResult(undefined)).isEqualTo(MoveResult.NULL);
        }
    }
}
