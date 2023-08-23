package net.willowmc.spl.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BitwiseHelper {
    /**
     * Test if a bit is set
     *
     * @param n number
     * @param i index
     * @return is bit in n with index i set
     */
    public boolean testBit(long n, int i) {
        return n << ~i < 0;
    }
}
