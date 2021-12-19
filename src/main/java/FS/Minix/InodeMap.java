package FS.Minix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * inode 位图，记录 inode 的使用情况
 * key: 块号
 * value: 0 表示未使用，1 表示已经使用
 */
public class InodeMap {
    protected byte[] maps;
    protected int size;

    public void setSize(int size) {
        this.size = size;
        maps = new byte[size];
    }

    public byte[] getMaps() {
        return maps;
    }

    public void setMaps(byte[] maps) {
        this.maps = maps;
    }

    // 全是 0，一共 inode map 需要的块数 * 4096 个
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(maps);
        setMaps(stream.toByteArray());
        return stream.toByteArray();
    }
}