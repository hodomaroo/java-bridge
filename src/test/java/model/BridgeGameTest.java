package model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.Lists.newArrayList;

import bridge.BridgeMaker;
import bridge.BridgeNumberGenerator;
import bridge.model.BridgeGame;
import bridge.model.GameStatus;
import bridge.model.constant.MoveDirection;
import camp.nextstep.edu.missionutils.test.NsTest;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class BridgeGameTest extends NsTest {

    private final int SIZE = 3;
    private final String UP = MoveDirection.UP.getDirectionString();
    private final String DOWN = MoveDirection.DOWN.getDirectionString();

    private BridgeGame bridgeGame;

    @BeforeEach
    private void initialize() {
        BridgeNumberGenerator numberGenerator = new TestNumberGenerator(newArrayList(1, 0, 0));
        BridgeMaker bridgeMaker = new BridgeMaker(numberGenerator);
        bridgeGame = new BridgeGame(bridgeMaker.makeBridge(SIZE));
    }


    @Nested
    @DisplayName("get(Final)GameStatus 메서드는 GameStatus 객체를 반환하는데,")
    class describe_getGameStatus {

        @Test
        @DisplayName("getGameResult는 이동 기록과 마지막 이동의 성공여부만을 담아 반환한다.")
        void onlyContainsMoveResult() {
            GameStatus gameStatus = bridgeGame.getSimpleGameStatus();
            assertThatThrownBy(() -> gameStatus.tryCount()).isInstanceOf(NoSuchElementException.class);
            assertThat(gameStatus.fail()).isEqualTo(false);
            List<MoveDirection> dummy = new ArrayList<>();
            assertThat(gameStatus.getMoveChoices()).isEqualTo(dummy);
        }

        @Test
        @DisplayName("getFinalGameResult는 모든 정보를 담아 반환환다.")
        void containsAllData() {
            GameStatus gameStatus = bridgeGame.getGameStatus();
            assertThat(gameStatus.tryCount()).isEqualTo(1);
            assertThat(gameStatus.fail()).isEqualTo(false);
            List<MoveDirection> dummy = new ArrayList<>();
            assertThat(gameStatus.getMoveChoices()).isEqualTo(dummy);
        }
    }

    @Nested
    @DisplayName("retry 메서드는 게임 상태를 초기화하는데,")
    class describe_retry {

        @Test
        @DisplayName("시도 횟수는 1증가하고, 이동 기록은 초기화된다.")
        void increaseTryCountAndResetMoveHistory() {
            bridgeGame.move(MoveDirection.UP);
            bridgeGame.retry();
            GameStatus gameStatus = bridgeGame.getGameStatus();
            assertThat(gameStatus.tryCount()).isEqualTo(2);
            assertThat(bridgeGame.inProcess()).isEqualTo(true);

            assertThat(gameStatus.getMoveChoices()).isEmpty();

        }
    }

    static class TestNumberGenerator implements BridgeNumberGenerator {

        private final List<Integer> numbers;

        TestNumberGenerator(List<Integer> numbers) {
            this.numbers = numbers;
        }

        @Override
        public int generate() {
            return numbers.remove(0);
        }
    }

    @Override
    protected void runMain() {

    }
}
