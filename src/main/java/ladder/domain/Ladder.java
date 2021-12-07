package ladder.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Ladder {

    public static final String INVALID_WIDTH_HEIGHT_MESSAGE = "width와 height는 양수여야 합니다.";

    private static final int MIN_WIDTH = 1;
    private static final int MIN_HEIGHT = 1;
    private static final Function<Names, Integer> NAMES_TO_WIDTH = names -> names.count() * 2 - 1;

    private final List<Line> lines;

    public Ladder(Names names, int height) {
        this(NAMES_TO_WIDTH.apply(names), height);
    }

    public Ladder(int width, int height) {
        if (width < MIN_WIDTH || height < MIN_HEIGHT) {
            throw new IllegalArgumentException(INVALID_WIDTH_HEIGHT_MESSAGE);
        }

        this.lines = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            lines.add(new Line(width));
        }
    }

    public int height() {
        return lines.size();
    }

    public int width() {
        return lines.get(0).width();
    }

    public List<Line> getLines() {
        return Collections.unmodifiableList(lines);
    }

}
