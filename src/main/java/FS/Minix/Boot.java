package FS.Minix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 引导块，暂时保留
 * 占用一块，即 4 k
 */
public class Boot {
    // 保留
    private String data = "正在启动磁盘。。";
    // 填充使引导块占据 4k
    protected byte[] padding = new byte[4072];

    // 序列化后的数据长度
    public static int toSizeLength = new Boot().data.getBytes().length;

    public void setData(String data) {
        this.data = data;
    }

    public byte[] getPadding() {
        return padding;
    }

    public String getData() {
        return data;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(data.getBytes());
        stream.write(padding);
        return stream.toByteArray();
    }
}