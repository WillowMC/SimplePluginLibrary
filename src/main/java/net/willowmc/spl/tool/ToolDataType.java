package net.willowmc.spl.tool;

import java.util.function.Function;

/**
 * Wrapper for data types that can be stored in tools.
 *
 * @param <T> underlying data type
 */
public class ToolDataType<T> {
    public static ToolDataType<Double> DOUBLE = new ToolDataType<>(Object::toString, Double::parseDouble);
    public static ToolDataType<Float> FLOAT = new ToolDataType<>(Object::toString, Float::parseFloat);
    public static ToolDataType<Long> LONG = new ToolDataType<>(Object::toString, Long::parseLong);
    public static ToolDataType<Integer> INTEGER = new ToolDataType<>(Object::toString, Integer::parseInt);
    public static ToolDataType<String> STRING = new ToolDataType<>(s -> s, s -> s);

    private final Function<T, String> toData;
    private final Function<String, T> fromData;

    /**
     * Register a type.
     *
     * @param toData   encode function
     * @param fromData decode function
     */
    public ToolDataType(Function<T, String> toData, Function<String, T> fromData) {
        this.toData = toData;
        this.fromData = fromData;
    }

    /**
     * Encode given data for this type.
     *
     * @param data data
     * @return encoded data
     */
    public String toData(T data) {
        return toData.apply(data);
    }

    /**
     * Decode given data for this type.
     *
     * @param data data
     * @return decoded data
     */
    public T fromData(String data) {
        return fromData.apply(data);
    }
}
