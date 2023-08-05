package com.jike.exercise.deadlock;

public class SychronizedAccount {

    private int balance;

    public SychronizedAccount(int balance) {
        this.balance = balance;
    }

    public void transfer(SychronizedAccount target, int amt) {
        synchronized (this) {
            synchronized (target) {
                if (balance > amt) {
                    this.balance -= amt;
                    target.balance += amt;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final SychronizedAccount a = new SychronizedAccount(100);
        final SychronizedAccount b = new SychronizedAccount(100);

        Thread th1 = new Thread(() ->{
            int count = 0;
            while (count++ < 1000) {
                a.transfer(b, 5);
            }
        }, "t1");

        Thread th2 = new Thread(() ->{
            int count = 0;
            while (count++ < 1000) {
                b.transfer(a, 5);
            }
        }, "t2");

        th1.start();
        th2.start();

        th1.join();
        th2.join();

        System.out.println("end");
    }
}
