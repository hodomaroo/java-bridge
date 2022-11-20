package model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.Lists.newArrayList;

import bridge.BridgeMaker;
import bridge.BridgeNumberGenerator;
import camp.nextstep.edu.missionutils.test.NsTest;
import dto.GameResult;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import model.enums.GameStatus;
import model.enums.MoveChoice;
import model.enums.MoveResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class BridgeGameTest extends NsTest {

    private final int SIZE = 3;
    private final String UP = MoveChoice.UP.moving;
    private final String DOWN = MoveChoice.DOWN.moving;

    private BridgeGame bridgeGame;

    @BeforeEach
    private void initialize() {
        BridgeNumberGenerator numberGenerator = new TestNumberGenerator(newArrayList(1, 0, 0));
        BridgeMaker bridgeMaker = new BridgeMaker(numberGenerator);
        bridgeGame = new BridgeGame(new Bridge(bridgeMaker.makeBridge(SIZE)));
    }


    @Nested
    @DisplayName("getGameStatus 메서드는 현재 게임 상태를 반환하는데,")
    class describe_getMatchStatus {

        @Test
        @DisplayName("게임이 진행중인 경우 IN_PROCESS 객체를 반환한다.")
        void returnInProcess() {
            assertThat(bridgeGame.getGameStatus()).isEqualTo(GameStatus.IN_PROCESS);
        }

        @Test
        @DisplayName("게임을 실패한 경우 OVER 객체를 반환한다.")
        void returnOVER() {
            bridgeGame.move(DOWN);
            assertThat(bridgeGame.getGameStatus()).isEqualTo(GameStatus.OVER);
        }

        @Test
        @DisplayName("게임을 클리어한 경우 CLEARED 객체를 반환한다.")
        void returnCleared() {
            bridgeGame.move(UP);
            bridgeGame.move(DOWN);
            bridgeGame.move(DOWN);
            assertThat(bridgeGame.getGameStatus()).isEqualTo(GameStatus.CLEARED);
        }
    }

    @Nested
    @DisplayName("get(Final)GameResult 메서드는 GameResult 객체를 반환하는데,")
    class describe_getGameResult {

        @Test
        @DisplayName("getGameResult는 이동 기록만을 담아 반환한다.")
        void onlyContainsMoveResult() {
            GameResult gameResult = bridgeGame.getGameResult();
            assertThatThrownBy(() -> gameResult.getTryCount()).isInstanceOf(NoSuchElementException.class);
            assertThatThrownBy(() -> gameResult.getStatus()).isInstanceOf(NoSuchElementException.class);
            List<List<MoveResult>> dummy = List.of(new ArrayList<>(), new ArrayList<>());
            assertThat(gameResult.getMoveResult()).isEqualTo(dummy);
        }

        @Test
        @DisplayName("getFinalGameResult는 모든 정보를 담아 반환환다.")
        void containsAllData() {
            GameResult gameResult = bridgeGame.getFinalGameResult();
            assertThat(gameResult.getTryCount()).isEqualTo(1);
            assertThat(gameResult.getStatus()).isEqualTo(GameStatus.IN_PROCESS);
            List<List<MoveResult>> dummy = List.of(new ArrayList<>(), new ArrayList<>());
            assertThat(gameResult.getMoveResult()).isEqualTo(dummy);
        }
    }

    @Nested
    @DisplayName("retry 메서드는 게임 상태를 초기화하는데,")
    class describe_retry {

        @Test
        @DisplayName("시도 횟수는 1증가하고, 이동 기록은 초기화된다.")
        void increaseTryCountAndResetMoveHistory() {
            bridgeGame.move(UP);
            bridgeGame.retry();
            GameResult gameResult = bridgeGame.getFinalGameResult();
            assertThat(gameResult.getTryCount()).isEqualTo(2);
            assertThat(gameResult.getStatus()).isEqualTo(GameStatus.IN_PROCESS);

            for (List<MoveResult> rowMoveResult : gameResult.getMoveResult()) {
                assertThat(rowMoveResult).isEmpty();
            }
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
