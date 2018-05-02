package com.thread.sleepisbad;

public class PoolObject {
    private static java.util.Random random = new java.util.Random();
    private Integer index = null;

public PoolObject(Integer i) {
    this.index = i;
}
public void execute() {
    // 어떤 비즈니스 로직....
    System.out.println("Thread-" + index + ": started.");
    int second = 0;
    try{
        // 5 - 15 seconds random sleep.
        second = 5 + (int)(random.nextDouble()*10);
        Thread.sleep(second*1000);
    }catch(Exception e){}
    System.out.println("Thread-" + index + ": elapsed=" + second);
}
}

