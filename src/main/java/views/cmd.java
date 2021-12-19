package views;

import Disk.Disk;
import Disk.SizeType;
import VFS.Dentry;
import VFS.SuperBlock;
import runtime.runtime;

import java.io.IOException;

public enum cmd {
    help {
        public void calc(String[] args) {
            System.out.println("help: 查看帮助");
            System.out.println("exit: 退出程序");
            System.out.println("clear: 清空程序");
            System.out.println("createDisk: 创建磁盘");
            System.out.println("mkfs: 格式化文件系统");
            System.out.println("pwd: 查看当前路径");
            System.out.println("mount: 挂载文件系统");
            System.out.println("mkdir: 创建目录");
            System.out.println("rm: 删除文件或目录");
            System.out.println("touch: 创建文件");
            System.out.println("ls: 查看当前目录下的文件");
            System.out.println("ll: 查看文件详细信息");
            System.out.println("cat：查看文件内容");
            System.out.println("imap: 查看 imap");
            System.out.println("bmap: 查看 bmap");
            System.out.println("superblock： 查看超级块信息");
            System.out.println("flush： 刷新内存中加载的信息");
        }
    },
    exit {
        public void calc(String[] args) {
            System.exit(-1);
        }
    },
    clear {
        public void calc(String[] args) {
//            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            for (int i = 0; i < 50; ++i) System.out.println();
        }
    },
    createDisk {
        public void calc(String[] args) {
            try {
                Disk disk = new Disk(args[1], 5 * SizeType.G);
                runtime.setDisk(disk);
                System.out.println("成功创建磁盘！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    },
    mkfs {
        public void calc(String[] args) {
            VFS.SuperBlock superBlock = new SuperBlock();
            try {
                superBlock.mkfs(args[1]);
                runtime.setSuperBlock(superBlock);
                System.out.println("格式化成功!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    },
    rm {
        public void calc(String[] args) {
            try {
                runtime.getDentry().Delete(args[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    },
    mount {
        public void calc(String[] args) {
            try {
                runtime.getSuperBlock().mount(args[1]);
                System.out.println("挂载成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    },
    mkdir {
        public void calc(String[] args) {
            try {
                runtime.getDentry().mkdir(args[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    },
    touch {
        public void calc(String[] args) {
            try {
                runtime.getDentry().touch(args[1], args[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    },
    ls {
        public void calc(String[] args) {
            try {
                runtime.getDentry().Ls();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    },
    ll {
        public void calc(String[] args) {
            try {
                runtime.getDentry().LL();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    },
    pwd {
        public void calc(String[] args) {
            try {
                runtime.getDentry().pwdAction();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    },
    cd {
        public void calc(String[] args) throws IOException {
            runtime.getDentry().cd(args[1]);
        }
    },
    cat {
        public void calc(String[] args) {
            try {
                runtime.getDentry().cat(args[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    },

    imap {
        public void calc(String[] args) {
            runtime.getSuperBlock().imap();
        }
    },

    bmap {
        public void calc(String[] args) {
            runtime.getSuperBlock().bmap();
        }
    },

    superblock {
        public void calc(String[] args) {
            runtime.getSuperBlock().superblock();
        }
    },

    flush {
        public void calc(String[] args) {
            try {
                runtime.init();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public abstract void calc(String[] args) throws IOException, InterruptedException;
}
