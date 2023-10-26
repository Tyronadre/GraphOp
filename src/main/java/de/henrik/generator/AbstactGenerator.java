package de.henrik.generator;

public abstract class AbstactGenerator implements Generator {
    protected long seed;

    AbstactGenerator (long seed) {
        this.seed = seed;
    }

}
