package com.gdb.util;

import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    private InputUtil() {}  

    public static Scanner getScanner() {
        return scanner;
    }
}