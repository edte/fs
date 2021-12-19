package views;

import java.io.IOException;

// 策略模式，自动选择对应的 action
public class Action {
    static void listen(String[] arg) throws IOException, InterruptedException {
        switch (arg[0]) {
            case "help" -> cmd.help.calc(arg);
            case "exit" -> cmd.exit.calc(arg);
            case "clear" -> cmd.clear.calc(arg);
            case "createDisk" -> cmd.createDisk.calc(arg);
            case "mkfs" -> cmd.mkfs.calc(arg);
            case "mkdir" -> cmd.mkdir.calc(arg);
            case "mount" -> cmd.mount.calc(arg);
            case "touch" -> cmd.touch.calc(arg);
            case "ls" -> cmd.ls.calc(arg);
            case "ll" -> cmd.ll.calc(arg);
            case "pwd" -> cmd.pwd.calc(arg);
            case "cd" -> cmd.cd.calc(arg);
            case "rm" -> cmd.rm.calc(arg);
            case "cat" -> cmd.cat.calc(arg);
            case "imap" -> cmd.imap.calc(arg);
            case "bmap" -> cmd.bmap.calc(arg);
            case "superblock" -> cmd.superblock.calc(arg);
            case "flush" -> cmd.flush.calc(arg);
            default -> System.out.printf("bash: command not found: %s", arg[0]);
        }
    }
}
