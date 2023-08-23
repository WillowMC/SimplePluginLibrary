package net.willowmc.spl.tool;

/**
 * Contains a tool data item
 *
 * @param key  data key
 * @param type data type
 * @param data data
 * @param <T>  data type
 */
public record ToolData<T>(String key, ToolDataType<T> type, T data) {
    /**
     * Returns the data, encoded to a string.
     *
     * @return encoded data
     */
    public String encode() {
        return type.toData(data);
    }
}
