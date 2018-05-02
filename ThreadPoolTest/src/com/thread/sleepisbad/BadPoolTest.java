package com.thread.sleepisbad;

public class BadPoolTest extends Thread{
    private int client = 0;
    public BadPoolTest(int n){
        this.client = n;
    }
    public static void main(java.lang.String[] args) {
        // PoolObject �� �ִ밪�� 20 ���� ���� ��û�� ������� �׽�Ʈ�� ��.
        int max = 40;
        System.out.println("Calling " + max + " threads");

        Thread[] threads = new BadPoolTest[max];
        for(int i=0;i<max;i++){
            threads[i] = new BadPoolTest(i);
            threads[i].start();
        }
    }
    public void run(){
        BadPoolManager manager = null;
        PoolObject obj = null;
        try {
            manager = BadPoolManager.getInstance();
            obj = manager.getPoolObject();
            obj.execute();
        }
        finally{
            if ( obj != null ) manager.release(obj);
        }
    }
}
