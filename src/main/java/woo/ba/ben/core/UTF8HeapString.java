package woo.ba.ben.core;


import java.nio.charset.CharacterCodingException;

import static woo.ba.ben.core.IHeapDataHandler.arrayEquals;
import static woo.ba.ben.core.UTF8Utils.encode;
import static woo.ba.ben.core.UTF8Utils.getCharArrayDirectly;
import static woo.ba.ben.util.MurmurHash3.hash32;

public class UTF8HeapString implements java.io.Serializable, Comparable<UTF8HeapString> {
    private static final long serialVersionUID = 6849454375434667710L;

    private byte[] content;
    private int hash;
    private boolean hashComputed;

    public UTF8HeapString(final String str) throws CharacterCodingException {
        checkNotNull(str);
        char[] chars = getCharArrayDirectly(str);
        initialise(chars);
    }

    public UTF8HeapString(final UTF8HeapString uStr) throws CharacterCodingException {
        checkNotNull(uStr);

        this.content = uStr.content;
        this.hash = uStr.hash;
        this.hashComputed = uStr.hashComputed;
    }

    public UTF8HeapString(final char[] chars) throws CharacterCodingException {
        initialise(chars);
    }

    private void initialise(char[] chars) throws CharacterCodingException {
        checkNotNull(chars);
        content = encode(chars);
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
        return arrayEquals(content, that.content);
    }

    @Override
    public int hashCode() {
        if (!hashComputed) {
            hash = hash32(0, content, 0, content.length);
            hashComputed = true;
        }
        return hash;
    }

    @Override
    public String toString() {
        //TODO: decode byte[]
        throw new RuntimeException("implement me");
    }

    private void checkNotNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }
}
