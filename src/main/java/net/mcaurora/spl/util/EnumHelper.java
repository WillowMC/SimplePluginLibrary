package net.mcaurora.spl.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EnumHelper {
    /**
     * Check if string is a valid value of enum.
     *
     * @param value     value
     * @param enumClass enum class
     */
    public <E extends Enum<E>> boolean isValid(String value, Class<E> enumClass) {
        if (value == null) return false;
        try {
            Enum.valueOf(enumClass, value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
