package com.thread.timeout;

public class PoolObject {
	    private static java.util.Random random = new java.util.Random();
	    private Integer index = null;
	
	public PoolObject(Integer i) {
	    this.index = i;
	}
	public void execute() {
	    System.out.println("Thread-" + index + ": started.");
	    int second = 0;
	    try{
	        // 5 - 15 seconds random sleep.
	        second = 5 + (int)(random.nextDouble()*10);
	        Thread.sleep(second*1000);
	    }catch(Exception e){}
	    System.out.println("Thread-" + index + ": elapsed=" + second);
	}
	public Integer getIndex() {
	    return index;
	}
}

