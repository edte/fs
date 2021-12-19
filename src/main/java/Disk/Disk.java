package Disk;

import java.io.*;

public class Disk {
    private final String name;
    private final long size;
    private final File file;

    public Disk(String name, long size) throws IOException {
        this.name = name;
        this.size = size;

        this.file = new File(name);
        if (!this.file.exists()) {
            RandomAccessFile r = new RandomAccessFile(name, "rw");
            r.setLength(size);
            r.close();
        }
    }

    public Disk(String name) {
        this.file = new File(name);
        this.name = name;
        this.size = file.length();
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public void read(byte[] b, int off, int len) throws IOException {
        RandomAccessFile rf = new RandomAccessFile(file, "rw");
        rf.seek(off);
        rf.readFully(b, 0, len);
    }

    public void write(byte[] bytes, int off, int len) throws IOException {
        RandomAccessFile rf = new RandomAccessFile(file, "rw");
        rf.seek(off);
        rf.write(bytes, 0, len);
    }
}