package de.henrik.generator;

import de.henrik.data.RectangleData;

import java.util.Random;

public class RectangleDataGenerator extends AbstactGenerator<RectangleData> {
    public int REC_MIN_SIZE;
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
    public void generate() {
        Random random = new Random(seed);
        for (int i = 0; i < numberOfRectangles; i++) {
            int width = (maxSize - minSize == 0 ? 0 : random.nextInt(maxSize - minSize + 1)) + minSize;
            int height = (maxSize - minSize == 0 ? 0 : random.nextInt(maxSize - minSize + 1)) + minSize;
            if (width > height) {
                int temp = width;
                width = height;
                height = temp;
            }
            generatedData.add(new RectangleData(width, height));
        }

        generatedData.sort((o1, o2) -> {
            if (o1.getWidth() == o2.getWidth()) {
                return o1.getHeight() - o2.getHeight();
            } else {
                return o1.getWidth() - o2.getWidth();
            }
        });
    }
}
