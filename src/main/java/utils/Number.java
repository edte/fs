package utils;

import java.math.BigInteger;

// 一些对数字操作的方法
public class Number {
    // int arrays to byte arrays
    public static byte[] itob(int[] intArr) {
        int byteLength = intArr.length * 4;//长度
        byte[] bt = new byte[byteLength];//开辟数组
        int curint = 0;
        for (int j = 0, k = 0; j < intArr.length; j++, k += 4) {
            curint = intArr[j];
            bt[k] = (byte) ((curint >> 24) & 0b1111_1111);//右移4位，与1作与运算
            bt[k + 1] = (byte) ((curint >> 16) & 0b1111_1111);
            bt[k + 2] = (byte) ((curint >> 8) & 0b1111_1111);
            bt[k + 3] = (byte) ((curint) & 0b1111_1111);
        }
        return bt;
    }

    // byte 数组转 int 数组
    public static int[] btoi(byte[] btarr) {
        if (btarr.length % 4 != 0) {
            return null;
        }
        int[] intarr = new int[btarr.length / 4];

        int i1, i2, i3, i4;
        for (int j = 0, k = 0; j < intarr.length; j++, k += 4)//j循环int		k循环byte数组
        {
            i1 = btarr[k];
            i2 = btarr[k + 1];
            i3 = btarr[k + 2];
            i4 = btarr[k + 3];

            if (i1 < 0) {
                i1 += 256;
            }
            if (i2 < 0) {
                i2 += 256;
            }
            if (i3 < 0) {
                i3 += 256;
            }
            if (i4 < 0) {
                i4 += 256;
            }
            intarr[j] = (i1 << 24) + (i2 << 16) + (i3 << 8) + (i4 << 0);//保存Int数据类型转换

        }
        return intarr;
    }


    // int 转 byte []   低字节在前（低字节序）
    public static byte[] toLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    // int 转 byte []   高字节在前（高字节序）
    public static byte[] toHH(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    // byte [] 转 int 低字节在前（低字节序）
    public static int toLI(byte[] b) {
        int res = 0;
        for (int i = 0; i < b.length; i++) {
            res += (b[i] & 0xff) << (i * 8);
        }
        return res;
    }

    // byte [] 转 int 高字节在前（高字节序）
    public static int toHI(byte[] b) {
        int res = 0;
        for (int i = 0; i < b.length; i++) {
            res += (b[i] & 0xff) << ((3 - i) * 8);
        }
        return res;
    }

    /**
     * 将byte[]转为各种进制的字符串
     *
     * @param bytes byte[]
     * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String baseConversion(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix); // 这里的1代表正数
    }

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }
}