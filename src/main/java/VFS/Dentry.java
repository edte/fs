package VFS;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import FS.Minix.Dir;
import runtime.runtime;
import utils.Strings;

public class Dentry extends File implements Cloneable {
    // 目录名
    String name;
    // 父目录
    Dentry parent;
    // inode no
    int ino;
    // 子目录
    List<Dentry> child;

    public Dentry(String name, Dentry parent, int ino) {
        this.name = name;
        this.parent = parent;
        this.ino = ino;
    }

    public Dentry() {
    }

    public void Ls() throws IOException {
        dealNullChild();
        for (Dentry dentry : child) {
            System.out.print(Strings.flushLeft(' ', 6, dentry.getName()) + "  ");
        }
    }

    public void mkdir(String name) throws IOException {
        int dir = runtime.getSuperBlock().createDir(name, this.ino);
        child.add(new Dentry(name, this, dir));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dentry getParent() {
        return parent;
    }

    public void setParent(Dentry parent) {
        this.parent = parent;
    }

    public int getIno() {
        return ino;
    }

    public void setIno(int ino) {
        this.ino = ino;
    }

    public List<Dentry> getChild() {
        return child;
    }

    public void setChild(List<Dentry> child) {
        this.child = child;
    }

    public void cd(String name) throws IOException {
        dealNullChild();

        if (name.equals("/")) {
            runtime.setDentry(runtime.getSuperBlock().root);
            return;
        }
        if (name.equals(".")) {
            return;
        }
        if (name.equals("..") && !this.getName().equals("/")) {
            runtime.setDentry(this.parent);
            return;
        }
        for (Dentry dentry : this.child) {
            if (name.equals(dentry.getName())) {
                if (runtime.getInode().getInode(dentry.ino).getMode() == 1) {
                    runtime.setDentry(dentry);
                } else {
                    System.out.printf("cd: not a directory: %s\n", name);
                }
                return;
            }
        }

        System.out.println("cd: no such file or directory:" + " " + name);
    }

    private void dealNullChild() throws IOException {
        if (child == null) {
            child = new ArrayList<>();
            FS.Minix.Dentry dentry = runtime.getSuperBlock().readDentry(ino);
            Dir[] dirs = dentry.getDirs();
            for (Dir dir : dirs) {
                child.add(new Dentry(dir.getFilename(), this, dir.getInodeNo()));
            }
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Dentry dentry = null;
        try {
            dentry = (Dentry) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return dentry;
    }


    public String pwd() throws CloneNotSupportedException {
        StringBuilder buffer = new StringBuilder();

        Dentry dentry = (Dentry) this.clone();

        if (this.parent == null) {
            buffer.append("/");
        }

        ArrayList<String> arrayList = new ArrayList<>();
        while (dentry.parent != null) {
            arrayList.add(dentry.name);
            arrayList.add("/");
            dentry = dentry.parent;
        }

        for (int i = arrayList.size() - 1; i >= 0; i--) {
            buffer.append(arrayList.get(i));
        }

        return buffer.toString();
    }

    public void pwdAction() throws CloneNotSupportedException {
        System.out.println(pwd());
    }

    public void touch(String name, String data) throws IOException {
        runtime.getSuperBlock().createFile(name, data, this.ino);
        child.add(new Dentry(name, this, this.ino));
    }

    public void cat(String arg) throws IOException {
        for (Dentry dentry : this.child) {
            if (dentry.getName().equals(arg)) {
                byte[] bytes = runtime.getSuperBlock().readInodeData(dentry.getIno());
                Inode inode = runtime.getInode().getInode(dentry.ino);
                if (inode.getMode() == 1) {
                    System.out.println("cat: read error: Is a directory");
                } else {
                    System.out.println(new String(bytes));
                }
                return;
            }
        }
        System.out.printf("cat: can't open '%s': No such file or directory\n", arg);
    }

    public void LL() throws IOException {
        dealNullChild();
        for (Dentry dentry : child) {
            Inode inode = runtime.getInode().getInode(dentry.getIno());

            System.out.println(inode.fileSize + " " + inode.getMode() + " " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(String.valueOf(inode.getCreateTime())))) + " " + inode.getUid() + " " + dentry.getIno() + " " + dentry.getName());
        }
    }

    public void Delete(String name) throws IOException {
        if (name.equals(".") || name.equals("..")) {
            System.out.println("rm: can't remove '.' or '..'");
            return;
        }

        for (Dentry dentry : this.child) {
            if (dentry.getName().equals(name)) {
                runtime.getSuperBlock().Delete(dentry.ino, this.ino);
                this.child.remove(dentry);
                return;
            }
        }
    }
}

