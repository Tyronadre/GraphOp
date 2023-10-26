package de.henrik.generator;

import de.henrik.data.RectangleData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RectangleDataGenerator extends AbstactGenerator {
    private final int minSize;
    private final int maxSize;
    private final int numberOfRectangles;

    public RectangleDataGenerator(long seed, int minSize, int maxSize, int numberOfRectangles) {
        super(seed);
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.numberOfRectangles = numberOfRectangles;
    }

    @Override
    public List<RectangleData> generate() {
        Random random = new Random(seed);

        var rectangles = new ArrayList<RectangleData>();

        for (int i = 0; i < numberOfRectangles; i++) {
            int width = random.nextInt(maxSize - minSize) + minSize;
            int height = random.nextInt(maxSize - minSize) + minSize;
            rectangles.add(new RectangleData(width, height));
        }

        rectangles.sort((o1, o2) -> {
            if (o1.width() == o2.width()) {
                return o1.height() - o2.height();
            } else {
                return o1.width() - o2.width();
            }

        });
        return rectangles;
    }
}
