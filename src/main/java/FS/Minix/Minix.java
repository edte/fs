package FS.Minix;

import Disk.Disk;
import Disk.SizeType;
import FS.FS;
import utils.Bitmap;
import utils.Number;

import java.io.*;
import java.util.Date;

/**
 * Minix 文件系统
 */
public class Minix implements FS {
    protected Boot boot;
    protected SuperBlock superBlock;
    protected InodeMap imap;
    protected BlockMap bmap;
    protected InodeTable inodeTable;

    // 硬盘
    protected Disk disk;


    public Minix() throws IOException {
        setDisk(new Disk("image", 5 * SizeType.G));
        setBoot(readBoot());
        setSuperBlock(readSuperBlock());
        setBmap(readBmap(9, 40));
        setImap(readImap(3, 6));
    }

    public void setBoot(Boot boot) {
        this.boot = boot;
    }

    public void setSuperBlock(SuperBlock superBlock) {
        this.superBlock = superBlock;
    }

    public void setBmap(BlockMap bmap) {
        this.bmap = bmap;
    }

    public void setImap(InodeMap imap) {
        this.imap = imap;
    }


    public void setDisk(Disk disk) {
        this.disk = disk;
    }

    protected void initBoot() {
        boot = new Boot();
    }

    protected void initSuperBlock() {
        superBlock = new SuperBlock();

        // 23563 表示 Minix 文件系统
        superBlock.setMagic(23563);
        // 每个块大小,单位是 Byte
        superBlock.setBlockSize(4 * SizeType.K);
        // inode 大小为 256B
        superBlock.setInodeSize(256);
        // 总块数，等于磁盘大小除以每块的大小
        superBlock.setBlocksCount((int) (disk.getSize() / superBlock.blockSize));
        // 空闲块数，todo
        superBlock.setFreeBlocksCount(superBlock.blocksCount);
        // inode 的数目，占用总块数的 15%
        superBlock.setInodesCount((int) (superBlock.blocksCount * 0.15));

        // imap 需要占据块数
        // inode 的数目即 imap 的 bit 数，如 inodeCount=199608
        // 则 imap 一共 199608 bit
        // 即 199608/8 Bytes
        // 一块假设 4096Bytes
        // 故一共需要 (199608/8)/4096=6.001=7 块
        superBlock.setImapCount(superBlock.inodesCount / (8 * superBlock.blockSize));
        // 如果存不下，需要再加一块
        if (superBlock.inodesCount % (8 * superBlock.blockSize) != 0) {
            superBlock.setImapCount(superBlock.imapCount++);
        }

        // 同 imap 的计算方法
        superBlock.setBmapCount(superBlock.blocksCount / (8 * superBlock.blockSize));
        if (superBlock.blocksCount % (8 * superBlock.blockSize) != 0) {
            superBlock.setBmapCount(superBlock.bmapCount++);
        }

        // inode map 的首指针
        // boot 区是第一块,superBlock 区是第二块
        // 故 inode map 的首指针是第三块
        superBlock.setImapPointer(3);

        // block map 的首指针
        superBlock.setBmapPointer(superBlock.imapPointer + superBlock.imapCount);

        // inode table 的首指针
        // 等于 bmap pointer + bmap 的长度
        superBlock.setInodeTablePointer(superBlock.bmapPointer + superBlock.bmapCount);

        // data blocks 的首指针
        // 等于 inodeTable pointer + inodeTable table 的长度
        // 一个块存 blockSize/inodeSize=16 个 inode
        // 则 inodeTable 的长度等于 inodeCount/16
        superBlock.setDataBlocksPointer(superBlock.inodeTablePointer + superBlock.inodesCount / (superBlock.blockSize / superBlock.inodeSize));
    }

    protected void initInodeMap() {
        imap = new InodeMap();
        imap.setSize(superBlock.imapCount * superBlock.blockSize);
    }

    protected void initBlockMap() {
        bmap = new BlockMap();
        bmap.setSize(superBlock.bmapCount * superBlock.blockSize);
        bmap.setUsedSize(1 + 1 + superBlock.imapCount + superBlock.bmapCount);
    }

    protected void initInodeTable() {
        inodeTable = new InodeTable();
    }

    protected byte[] writeStream() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // 写入 boot 区
        stream.write(boot.toBytes());

        // 写入超级块
        stream.write(superBlock.toBytes());

        // 写入 inode map
        stream.write(imap.toBytes());

        // 写入 block map
        stream.write(bmap.toBytes());

        // 写入 inode table,初始化不用写入 inode
//        stream.write(inodeTable.toBytes());

        return stream.toByteArray();
    }

    @Override
    public void mkfs() throws IOException {
        if (disk == null) {
            throw new IOException("磁盘未设置！");
        }
        initBoot();
        initSuperBlock();
        initInodeMap();
        initBlockMap();
        initInodeTable();
        disk.write(writeStream(), 0, writeStream().length);
    }


    // 读取 boot 区的数据
    Boot readBoot() throws IOException {
        Block block = readBlock(1);
        Boot boot = new Boot();
        boot.setData(new String(block.getBytes(), 0, Boot.toSizeLength));
        return boot;
    }

    // 获取引导区的数据
    SuperBlock readSuperBlock() throws IOException {
        return SuperBlock.parse(readBlock(2).bytes);
    }

    //  读取 imap
    InodeMap readImap(int imapPointer, int imapLength) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        InodeMap inodeMap = new InodeMap();

        for (int i = imapPointer; i < imapPointer + imapLength; i++) {
            stream.write(readBlock(i).getBytes());
        }

        inodeMap.setMaps(stream.toByteArray());
        return inodeMap;
    }

    public Boot getBoot() {
        return boot;
    }

    public SuperBlock getSuperBlock() {
        return superBlock;
    }

    public InodeMap getImap() {
        return imap;
    }

    public BlockMap getBmap() {
        return bmap;
    }

    public InodeTable getInodeTable() {
        return inodeTable;
    }

    public void setInodeTable(InodeTable inodeTable) {
        this.inodeTable = inodeTable;
    }

    void writeImap(int ino) throws IOException {
        writeBlock(imap.getMaps(), superBlock.imapPointer);
    }

    // 从 imap 中获取一个没有使用的 inode
    // 讲 byte array 转换为 bitmap，然后遍历找到一个为 false 的，即没有使用的
    int findFreeInode() throws IOException {
        return findFreeBit(imap.getMaps(), 1);
    }

    // 从一个 byte 数组中找到第一个为 0 的
    private int findFreeBit(byte[] maps, int type) {
        Bitmap bitmap = new Bitmap();
        bitmap.setBitmap(maps);
        for (int i = 0; i < bitmap.getLength(); i++) {
            if (!bitmap.get(i)) {
                bitmap.add(i);
                if (type == 1) {
                    imap.setMaps(bitmap.getBitmap());
                } else {
                    bmap.setMaps(bitmap.getBitmap());
                }
                return i;
            }
        }
        return -1;
    }

    // 读取 bmap
    BlockMap readBmap(int bmapPointer, int bmapLength) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BlockMap blockMap = new BlockMap();

        for (int i = bmapPointer; i < bmapPointer + bmapLength; i++) {
            stream.write(readBlock(i).getBytes());
        }

        blockMap.setMaps(stream.toByteArray());
        return blockMap;
    }

    // 从 bmap 中获取一个没有使用的 block
    // 讲 byte array 转换为 bitmap，然后遍历找到一个为 false 的，即没有使用的
    // 结果要加上 inode table length
    int findFreeBnode() throws IOException {
        return findFreeBit(bmap.getMaps(), 0) + superBlock.dataBlocksPointer;
    }

    // 根据 inodeNum 到 inode table 中获取对应的数据
    public Inode readInode(int inodeNum) throws IOException {
        Block block = readBlock(inoToBno(inodeNum));
        byte[] bytes = Number.subBytes(block.getBytes(), 256 * (inodeNum % 16), 256);
        return Inode.parse(bytes);
    }

    // 读取 inode 中的数据
    public byte[] readInodeData(int inodeNum) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        int[] list = readInode(inodeNum).getBlockList();

        for (int j : list) {
            if (j != 0) {
                stream.write(readBlock(j).getBytes());
            }
        }

        return stream.toByteArray();
    }

    //  读取某块的数据
    protected Block readBlock(int blockNo) throws IOException {
        Block block = new Block();
        block.setBytes(new byte[4096]);
        disk.read(block.bytes, (blockNo - 1) * 4096, block.bytes.length);
        return block;
    }

    // 写某个块的数据
    protected void writeBlock(byte[] bytes, int blockNo) throws IOException {
        disk.write(bytes, (blockNo - 1) * 4096, bytes.length);
    }

    // inode no 得到对应存储的该 inode 的块号
    // todo: 更改为通用的
    // 16 为一个块内存放的 inode 数
    // 49 是 inode table 的首地址
    // 至于为什么是这样，待解释
    int inoToBno(int ino) {
        return ino / 16 + 49;
    }

    // 新建立一个 inode，注意起始偏移量
    void createInode(int ino, byte[] bytes) throws IOException {
        disk.write(bytes, (inoToBno(ino) - 1) * 4096 + (ino % 16) * 256, bytes.length);
    }

    // 新建目录
    public int createDir(String name, int pino) throws IOException {
        // 获取空闲的  inode
        int ino = findFreeInode();
        // 获取空闲的  bnode,目录只用 1 块
        int bno = findFreeBnode();
        // 设置 bnode 索引
        int[] blockList = new int[8];
        blockList[0] = bno;
        // 建立 inode
        Inode inode = new Inode(1, ino, blockList.length, blockList, 1, 88, (int) new Date().getTime(), (int) new Date().getTime(), 1);
        // 建立 dentry
        Dentry dentry = new Dentry();
        dentry.setNameLength(name.getBytes().length);
        dentry.setName(name);
        dentry.setPino(pino);
        dentry.setIno(ino);
        dentry.setDirNum(2);
        dentry.setDirs(new Dir[]{new Dir(".", ".".getBytes().length, ino), new Dir("..", "..".getBytes().length, pino)});
        // 修改父 dentry 的 dirs
        if (!name.equals("/")) {
            modifyPDentityWhenAddEntity(name, pino, ino);
        }
        // 修改 imap
        writeBlock(imap.getMaps(), superBlock.imapPointer);
        // 修改 bmap
        writeBlock(bmap.getMaps(), superBlock.bmapPointer);
        // 修改 inode table
        createInode(ino, inode.toBytes());
        // 修改 data block
        writeBlock(dentry.toBytes(), bno);

        return ino;
    }

    // 读取 readDentry
    public Dentry readDentry(int ino) throws IOException {
        byte[] bytes = readInodeData(ino);
        return Dentry.parse(bytes);
    }

    public int createFile(String name, byte[] bytes, int pino) throws IOException {
        // 获取空闲的  inode
        int ino = findFreeInode();

        // 获取需要多少块才能存下文件数据
        int blockCount = bytes.length / superBlock.blockSize;
        if (bytes.length % superBlock.blockSize != 0) {
            blockCount++;
        }
        if (blockCount > 9) {
            throw new IOException("文件过大！");
        }

        // 获取空闲的 bnode 编号数组
        int[] bnos = new int[8];
        for (int i = 0; i < blockCount; i++) {
            bnos[i] = findFreeBnode();
        }

        // 建立 inode
        Inode inode = new Inode(0, ino, blockCount, bnos, bytes.length, 88, (int) new Date().getTime(), (int) new Date().getTime(), 1);

        // 修改父 dentry 的 dirs
        modifyPDentityWhenAddEntity(name, pino, ino);

        // 修改 imap
        writeBlock(imap.getMaps(), superBlock.imapPointer);
        // 修改 bmap
        writeBlock(bmap.getMaps(), superBlock.bmapPointer);

        // 修改 inode table
        createInode(ino, inode.toBytes());

        // 如果小于 4096，直接用一个块存即可
        if (bytes.length < superBlock.blockSize) {
            writeBlock(bytes, bnos[0]);
        } else {
            // 修改 data block
            for (int i = 0; i < blockCount; i++) {
                writeBlock(Number.subBytes(bytes, superBlock.blockSize * i, superBlock.blockSize * (i + 1)), bnos[i]);
            }
        }

        return ino;
    }

    // 读取文件内容
    public byte[] readFile(int ino) throws IOException {
        Inode inode = readInode(ino);
        int[] list = inode.getBlockList();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        for (int j : list) {
            if (j != 0) {
                stream.write(readBlock(j).getBytes());
            }
        }

        return Number.subBytes(stream.toByteArray(), 0, inode.fileSize);
    }

    // 当增加目录或文件时，增加父目录的 dirs 记录
    private void modifyPDentityWhenAddEntity(String name, int pino, int ino) throws IOException {
        Inode inode1 = readInode(pino);
        Dentry pdentry = readDentry(pino);
        Dir[] dirs = pdentry.getDirs();
        Dir[] newDir = new Dir[pdentry.dirNum + 1];
        System.arraycopy(dirs, 0, newDir, 0, dirs.length);
        newDir[pdentry.dirNum] = new Dir(name, name.length(), ino);
        pdentry.setDirs(newDir);
        pdentry.setDirNum(++pdentry.dirNum);
        writeBlock(pdentry.toBytes(), inode1.getBlockList()[0]);
    }

    // 挂载 rootfs，初始化目录结构
    public void mount(String name) throws IOException {
        if (name.equals("rootfs")) {
            createDir("/", 0);
            createDir("bin", 0);
            createDir("etc", 0);
            createDir("root", 0);
            createDir("sys", 0);
            createDir("usr", 0);
            createDir("dev", 0);
            createDir("home", 0);
            createDir("lib", 0);
            createDir("mnt", 0);
            createDir("proc", 0);
            createDir("run", 0);
            createDir("tmp", 0);
            createDir("var", 0);
        }
    }

    // 删除文件或目录
    public void Delete(int ino, int pino) throws IOException {
        // 修改 imap
        Bitmap bitmap = new Bitmap();
        bitmap.setBitmap(getImap().getMaps());
        bitmap.delete(ino);
        writeBlock(imap.getMaps(), superBlock.imapPointer);

        // 修改 bmap
        Bitmap bitmap1 = new Bitmap();
        bitmap1.setBitmap(getBmap().getMaps());
        Inode inode = readInode(ino);
        for (int i = 0; i < inode.blocksNumber; i++) {
            if (inode.getBlockList()[i] != 0) {
                bitmap1.delete(inode.getBlockList()[i] - superBlock.dataBlocksPointer);
            }
        }
        writeBlock(bmap.getMaps(), superBlock.bmapPointer);

        // inode table 清空
        createInode(ino, new byte[superBlock.inodeSize]);

        // 修改父目录中的 dirs
        Inode inode1 = readInode(pino);
        Dentry pdentry = readDentry(pino);
        Dir[] dirs = pdentry.getDirs();

        boolean flag = false;
        for (Dir dir : dirs) {
            if (dir.inodeNo == ino) {
                flag = true;
                break;
            }
        }

        Dir[] newDir;

        if (!flag) {
            return;
        }

        newDir = new Dir[pdentry.dirNum - 1];
        int j = 0;
        for (Dir dir : dirs) {
            if (dir.inodeNo != ino) {
                newDir[j] = new Dir(dir.filename, dir.nameLength, dir.inodeNo);
                j++;
            }
        }

        pdentry.setDirs(newDir);
        pdentry.setDirNum(--pdentry.dirNum);
        writeBlock(pdentry.toBytes(), inode1.getBlockList()[0]);
    }
}
