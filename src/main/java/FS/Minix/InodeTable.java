package FS.Minix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InodeTable {
    protected Inode[] tables;
    protected int size;

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        return stream.toByteArray();
    }
}
