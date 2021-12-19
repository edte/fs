package views;

import VFS.Dentry;
import runtime.runtime;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class view {
    public static void main(String[] args) throws IOException, InterruptedException, CloneNotSupportedException {
        runtime.init();
        Scanner scanner = new Scanner(System.in);
        String path = "/";
        while (true) {
            Dentry dentry = runtime.getDentry();
            if (dentry != null) {
                path = dentry.pwd();
            }
            System.out.printf("# root @ ENIAC in %s [%s]\n", path, new SimpleDateFormat("HH:mm:ss").format(new Date()));
            String s = scanner.nextLine();
            Action.listen(s.split("\\s+"));
            System.out.println();
            System.out.println();
        }
    }
}
