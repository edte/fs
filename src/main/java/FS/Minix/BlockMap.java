package FS.Minix;

import utils.Bitmap;

import java.io.IOException;

/**
 * 块位图，记录块的使用情况
 * key: 块号
 * value: 0 表示未使用，1 表示已经使用
 */
public class BlockMap {
    protected byte[] maps;
    // bmap 占据的块数
    protected int size;
    // 已经使用的块数
    protected int usedSize;

    public void setSize(int size) {
        this.size = size;
        maps = new byte[size];
    }

    public void setUsedSize(int usedSize) {
        this.usedSize = usedSize;
    }

    public void setMaps(byte[] maps) {
        this.maps = maps;
    }

    public byte[] getMaps() {
        return maps;
    }

    public byte[] toBytes() throws IOException {
        utils.Bitmap bitmap = new Bitmap(size);
        for (int i = 0; i < usedSize; i++) {
            bitmap.add(i);
        }
        setMaps(bitmap.getBitmap());
        return bitmap.getBitmap();
    }
}
