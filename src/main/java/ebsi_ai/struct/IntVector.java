package ebsi_ai.struct;

public class IntVector {
    private int vector;
    private int size;
    private final int dimension;

    public IntVector(int size, int... components) {
        if (size * components.length > 32) throw new IllegalArgumentException("Vector is too big");

        vector = 0;
        for (int component : components) {
            vector <<= size;
            vector |= component & (~0 >>> (Integer.SIZE - size));
        }

        this.size = size;
        dimension = components.length;
    }

    public int getValue(int index) {
        if (index >= dimension) throw new IndexOutOfBoundsException();
        index = dimension - index - 1;

        int mask = ~0 >>> (Integer.SIZE - size);
        return (vector & (mask << (size * index))) >>> (size * index);
    }

    public int getDimension() {
        return dimension;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("(");
        for (int i = 0; i < dimension; i++) {
            int value = getValue(i);
            if (i > 0) str.append(", ");
            str.append(value);
        }
        return str.append(")").toString();
    }
}
