package nextstep.ladder2;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import nextstep.ladder2.domain.NxLadder;
import nextstep.ladder2.domain.NxLadderGame;
import nextstep.ladder2.domain.NxLine;
import nextstep.ladder2.domain.NxPrizeMapper;
import nextstep.ladder2.domain.builder.RandomBuilder;
import nextstep.ladder2.domain.player.NxPlayers;
import nextstep.ladder2.domain.prize.NxPrizes;
import nextstep.ladder2.domain.result.NxResult;
import nextstep.ladder2.domain.result.NxResultBoard;
import nextstep.ladder2.view.InputView;
import nextstep.ladder2.view.ResultView;

public class Controller {
    private static final int INPUT_PLAYERS_NAME = 0;
    private static final int INPUT_PRIZES_NAME = 1;
    private static final int INPUT_LADDER_HEIGHT = 2;
    private static final String INPUT_RESULT_ALL = "all";

    public void run() {
        List<String> info = InputView.getInfoFromClient();

        NxPlayers players = new NxPlayers(Arrays.asList(info.get(INPUT_PLAYERS_NAME).split(",")));
        NxPrizes prizes = new NxPrizes(Arrays.asList(info.get(INPUT_PRIZES_NAME).split(",")));
        NxPrizeMapper prizeMapper = new NxPrizeMapper(players, prizes);
        NxLadder ladder = new NxLadder(Integer.parseInt(info.get(INPUT_LADDER_HEIGHT)),
                players.getPlayerCount(), new RandomBuilder());
        NxLadderGame game = new NxLadderGame(ladder, prizeMapper);

        ResultView.printObjectsName(players.getAllPlayerNames());
        ResultView.printLadderResult(convertToLadderViewDto(ladder.getLines()), players.getPlayerCount());

        ResultView.printObjectsName(prizes.getAllPrizesName());

        NxResultBoard resultBoard = game.start();

        String input = InputView.getPlayerResult();
        if (INPUT_RESULT_ALL.equals(input)) {
            ResultView.printResultList(convertToAllResultViewDto(resultBoard));
            return;
        }
        NxResult result = resultBoard.findResultByPlayer(players.findPlayerByName(input));
        ResultView.printResultList(convertToResultViewDto(result));
    }

    public List<String> convertToLadderViewDto(List<NxLine> lines) {
        return lines.stream()
                .map(NxLine::getBridgeValues)
                .map(this::convertToLineViewDto)
                .collect(Collectors.toList());
    }

    private String convertToLineViewDto(List<Boolean> line) {
        StringJoiner stringJoiner = new StringJoiner(",");
        for(int i = 0; i < line.size(); i++) {
            convertToStringIfTrue(line.get(i), i, stringJoiner);
        }
        return stringJoiner.toString();
    }

    private void convertToStringIfTrue(boolean bool, int index, StringJoiner stringJoiner) {
        if (bool) {
            stringJoiner.add(String.valueOf(index));
        }
    }

    private List<String> convertToResultViewDto(NxResult result) {
        return Arrays.asList(result.getPrize().getPrizeName());
    }

    private List<String> convertToAllResultViewDto(NxResultBoard resultBoard) {
        return resultBoard.getResultBoard().stream()
                .map(result -> result.getPlayer().getName() + " : " + result.getPrize().getPrizeName())
                .collect(Collectors.toList());
    }
}
