package de.igorlueckel.magicmirror;

import java.io.IOException;

/**
 * Created by Igor on 27.10.2015.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        //new ProcessBuilder().command("raspi-monitor", "off").start();
        Runtime.getRuntime().exec("/usr/bin/startChromium", new String[]{"/usr/bin"});
    }
}
