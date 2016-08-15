package woo.ba.ben.core;


import java.lang.reflect.Field;
import java.nio.charset.CoderResult;

import static java.lang.Math.min;
import static java.nio.charset.CoderResult.OVERFLOW;
import static java.nio.charset.CoderResult.UNDERFLOW;
import static java.util.Arrays.fill;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public class UTF8Utils {
    public static final int INVALID_BLOCK_SIZE = -1;

    private static Field VALUE_FIELD = null;

    static {
        try {
            VALUE_FIELD = String.class.getDeclaredField("value");
            VALUE_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static char[] getCharArrayDirectly(final String str) {
        if (str == null) {
            return null;
        }

        try {
            return (char[]) VALUE_FIELD.get(str);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static int encodingDestBlockSize(final char[] source, final int srcStartOffset, final int srcLimit) {
        if (source == null || srcStartOffset < 0 || srcLimit < 0) {
            throw new IllegalArgumentException();
        }

        int sourceStartOffset = srcStartOffset;
        final int sourceLength = source.length - sourceStartOffset;
        int sourceRemaining = min(sourceLength, srcLimit);

        int size = 0;
        while (sourceStartOffset < sourceRemaining && source[sourceStartOffset] < 0x80) {
            sourceStartOffset++;
            size++;
        }

        int charValueInInt;
        for (int i = sourceStartOffset; i < sourceRemaining; i++) {
            charValueInInt = source[i];
            if (charValueInInt < 0x80) {
                size++;
            } else if (charValueInInt < 0x800) {
                size += 2;
            } else if (charValueInInt < 0x10000) {
                size += 3;
            } else if (charValueInInt < 0x200000) {
                size += 4;
            } else {
                return INVALID_BLOCK_SIZE;
            }
        }
        return size;
    }

    public static byte[] encode(final char[] source, final int srcStartOffset, final int srcLimit) {
        final int size = encodingDestBlockSize(source, srcStartOffset, srcLimit);
        if (size > 0) {
            final byte[] destination = new byte[size];
            encodeInternal(source, srcStartOffset, srcLimit, destination, 0, destination.length);
            return destination;
        }
        return null;
    }

    public static void encodeInternal(final char[] source, final int srcStartOffset, final int srcLimit,
                                      final byte[] destination, final int destStartOffset, final int destLimit) {

        int sourceStartOffset = srcStartOffset, destinationStartOffset = destStartOffset;
        final int sourceLength = source.length - sourceStartOffset;
        int sourceRemaining = min(sourceLength, srcLimit);
        while (sourceStartOffset < sourceRemaining && source[sourceStartOffset] < 0x80) {
            destination[destinationStartOffset++] = (byte) source[sourceStartOffset++];
        }

        int charValueInInt;
        for (int i = sourceStartOffset; i < sourceRemaining; i++) {
            charValueInInt = source[i];

            if (charValueInInt < 0x80) {
                destination[destinationStartOffset++] = (byte) charValueInInt;
            } else if (charValueInInt < 0x800) {
                // 2 bits
                destination[destinationStartOffset++] = (byte) (0xC0 | charValueInInt >> 6);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else if (charValueInInt < 0x10000) {
                // 3 bits
                destination[destinationStartOffset++] = (byte) (0xE0 | charValueInInt >> 12);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else {
                // 4 bits
                destination[destinationStartOffset++] = (byte) (0xF0 | charValueInInt >> 18);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt >> 12 & 0x3F);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            }
        }
    }

    public static CoderResult encode(final char[] source, final int srcStartOffset, final int srcLimit,
                                     final byte[] destination, final int destStartOffset, final int destLimit) {
        if (source == null || destination == null ||
                srcStartOffset < 0 || srcLimit < 0 || destStartOffset < 0 || destLimit < 0) {
            throw new IllegalArgumentException();
        }

        int sourceStartOffset = srcStartOffset, destinationStartOffset = destStartOffset;
        final int sourceLength = source.length - sourceStartOffset;
        int sourceCount = min(sourceLength, srcLimit);
        final int destinationLength = destination.length - destStartOffset;
        int destinationCount = min(destinationLength, destLimit);

        if (destinationCount < sourceCount) {
            return OVERFLOW;
        }

        final int smallerCount = min(sourceCount, destinationCount);
        while (destinationStartOffset < smallerCount && source[sourceStartOffset] < 0x80) {
            destination[destinationStartOffset++] = (byte) source[sourceStartOffset++];
        }

        int charValueInInt;
        for (int i = sourceStartOffset; i < sourceCount; i++) {
            charValueInInt = source[i];

            if (charValueInInt < 0x80) {
                //check remaining capacity
                if (destinationStartOffset + 1 > destinationCount) {
                    fill(destination, destStartOffset, destinationCount, (byte) 0);
                    return OVERFLOW;
                }

                // 1 bit
                destination[destinationStartOffset++] = (byte) charValueInInt;
            } else if (charValueInInt < 0x800) {
                //check remaining capacity
                if (destinationStartOffset + 2 > destinationCount) {
                    fill(destination, destStartOffset, destinationCount, (byte) 0);
                    return OVERFLOW;
                }

                // 2 bits
                destination[destinationStartOffset++] = (byte) (0xC0 | charValueInInt >> 6);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else if (charValueInInt < 0x10000) {
                //check remaining capacity
                if (destinationStartOffset + 3 > destinationCount) {
                    fill(destination, destStartOffset, destinationCount, (byte) 0);
                    return OVERFLOW;
                }

                // 3 bits
                destination[destinationStartOffset++] = (byte) (0xE0 | charValueInInt >> 12);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else if (charValueInInt < 0x200000) {
                //check remaining capacity
                if (destinationStartOffset + 4 > destinationCount) {
                    fill(destination, destStartOffset, destinationCount, (byte) 0);
                    return OVERFLOW;
                }

                // 4 bits
                destination[destinationStartOffset++] = (byte) (0xF0 | charValueInInt >> 18);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt >> 12 & 0x3F);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else {
                fill(destination, destStartOffset, destinationCount, (byte) 0);
                return OVERFLOW;
            }
        }
        return UNDERFLOW;
    }

    public static CoderResult encode(final char[] source, final int srcStartOffset, final int srcLimit,
                                     final long destAddress, final int destStartOffset, final int destLimit) {
        if (source == null || destAddress < 0 ||
                srcStartOffset < 0 || srcLimit < 0 || destStartOffset < 0 || destLimit < 0) {
            throw new IllegalArgumentException();
        }

        int sourceStartOffset = srcStartOffset, destinationStartOffset = destStartOffset;
        final int sourceLength = source.length - sourceStartOffset;
        int sourceCount = min(sourceLength, srcLimit);
        int destinationCount = destLimit - destinationStartOffset;

        if (destinationCount < sourceCount) {
            return OVERFLOW;
        }

        final int smallerCount = min(sourceCount, destinationCount);
        while (destinationStartOffset < smallerCount && source[sourceStartOffset] < 0x80) {
            putByte(destAddress, destinationStartOffset++, (byte) source[sourceStartOffset++]);
        }

        int charValueInInt;
        for (int i = sourceStartOffset; i < sourceCount; i++) {
            charValueInInt = source[i];

            if (charValueInInt < 0x80) {
                //check remaining capacity
                if (destinationStartOffset + 1 > destinationCount) {
                    cleanUpDestination(destAddress, destStartOffset, destinationCount);
                    return OVERFLOW;
                }

                // 1 bit
                putByte(destAddress, destinationStartOffset++, (byte) charValueInInt);
            } else if (charValueInInt < 0x800) {
                //check remaining capacity
                if (destinationStartOffset + 2 > destinationCount) {
                    cleanUpDestination(destAddress, destStartOffset, destinationCount);
                    return OVERFLOW;
                }

                // 2 bits
                putByte(destAddress, destinationStartOffset++, (byte) (0xC0 | charValueInInt >> 6));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | charValueInInt & 0x3F));
            } else if (charValueInInt < 0x10000) {
                //check remaining capacity
                if (destinationStartOffset + 3 > destinationCount) {
                    cleanUpDestination(destAddress, destStartOffset, destinationCount);
                    return OVERFLOW;
                }

                // 3 bits
                putByte(destAddress, destinationStartOffset++, (byte) (0xE0 | charValueInInt >> 12));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | charValueInInt >> 6 & 0x3F));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | charValueInInt & 0x3F));
            } else if (charValueInInt < 0x200000) {
                //check remaining capacity
                if (destinationStartOffset + 4 > destinationCount) {
                    cleanUpDestination(destAddress, destStartOffset, destinationCount);
                    return OVERFLOW;
                }

                // 4 bits
                putByte(destAddress, destinationStartOffset++, (byte) (0xF0 | charValueInInt >> 18));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | charValueInInt >> 12 & 0x3F));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | charValueInInt >> 6 & 0x3F));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | charValueInInt & 0x3F));
            } else {
                cleanUpDestination(destAddress, destStartOffset, destinationCount);
                return OVERFLOW;
            }
        }
        return UNDERFLOW;
    }

    private static void cleanUpDestination(final long destAddress, final int destStartOffset, final int count) {
        for (int idx = destStartOffset; idx < count; idx++) {
            UNSAFE.putByte(destAddress + idx, (byte) 0);
        }
    }

    private static void putByte(final long address, final int position, final byte value) {
        UNSAFE.putByte(address + position, value);
    }
}
