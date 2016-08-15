package woo.ba.ben.core;


import java.nio.charset.CharacterCodingException;
import java.nio.charset.CoderResult;

import static java.nio.charset.CoderResult.OVERFLOW;
import static woo.ba.ben.core.DataHandlerFactory.nativeOrderHeapDataHandler;
import static woo.ba.ben.core.UTF8Utils.*;
import static woo.ba.ben.util.MurmurHash3.murmurhash3_x86_32;

public class UTF8HeapString implements java.io.Serializable, Comparable<UTF8HeapString> {
    private static final long serialVersionUID = 6849454375434667710L;

    private byte[] content;
    private int hash;
    private boolean hashComputed;

    public UTF8HeapString(final String str) throws CharacterCodingException {
        char[] chars = getCharArrayDirectly(str);
        initialise(chars);
    }

    public UTF8HeapString(final UTF8HeapString uStr) throws CharacterCodingException {
        if (uStr == null) {
            throw new IllegalArgumentException();
        }

        this.content = uStr.content;
        this.hash = uStr.hash;
        this.hashComputed = uStr.hashComputed;
    }

    public UTF8HeapString(final char[] chars) throws CharacterCodingException {
        initialise(chars);
    }

    private void initialise(char[] chars) throws CharacterCodingException {
        if (chars == null) {
            throw new IllegalArgumentException();
        }
        final int size = encodingDestBlockSize(chars, 0, chars.length);
        byte[] content = new byte[size];
        final CoderResult result = encode(chars, 0, chars.length, content, 0, content.length);
        if (result == OVERFLOW) {
            throw new CharacterCodingException();
        }
    }

    @Override
    public int compareTo(final UTF8HeapString o) {
        //TODO:
        throw new RuntimeException("implement me");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UTF8HeapString that = (UTF8HeapString) o;
        return nativeOrderHeapDataHandler().arrayEquals(content, that.content);
    }

    @Override
    public int hashCode() {
        if (!hashComputed) {
            hash = murmurhash3_x86_32(content, 0, content.length, content.length);
            hashComputed = true;
        }
        return hash;
    }

    @Override
    public String toString() {
        //TODO: decode byte[]
        throw new RuntimeException("implement me");
    }
}
