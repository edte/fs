package FS.Minix;

import utils.Number;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

// 表示文件或目录
public class Dir {
    // 共 263
    // 255 byte
    int nameLength;
    protected String filename;
    protected int inodeNo;

    public Dir() {
    }

    public Dir(String filename, int nameLength, int inodeNo) {
        this.nameLength = nameLength;
        this.filename = filename;
        this.inodeNo = inodeNo;
    }


    public int getNameLength() {
        return nameLength;
    }

    public void setNameLength(int nameLength) {
        this.nameLength = nameLength;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getInodeNo() {
        return inodeNo;
    }

    public void setInodeNo(int inodeNo) {
        this.inodeNo = inodeNo;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(Number.toHH(nameLength));
        byte[] d = new byte[255];
        System.arraycopy(filename.getBytes(), 0, d, 0, filename.length());
        stream.write(d);
        stream.write(Number.toHH(inodeNo));
        return stream.toByteArray();
    }

    public static Dir parse(byte[] bytes) {
        Dir dir = new Dir();
        dir.setNameLength(Number.toHI(utils.Number.subBytes(bytes, 0, 4)));
        dir.setFilename(new String(bytes, 4, dir.nameLength));
        dir.setInodeNo(Number.toHI(utils.Number.subBytes(bytes, 4 + 255, 4)));
        return dir;
    }
}

