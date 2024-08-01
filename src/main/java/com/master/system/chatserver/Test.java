package com.master.system.chatserver;

public class Test {
    public static void main(String[] args) {
        int x = 2;
        while ((x = getNum()) != 5) {
            System.out.println(x);
        }
    }

    private static int getNum() {
        return 5;
    }
}
