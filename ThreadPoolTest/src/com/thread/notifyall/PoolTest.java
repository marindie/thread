package com.thread.notifyall;

public class PoolTest extends Thread{
    private int client = 0;
    public PoolTest(int n){
        this.client = n;
    }
    public static void main(java.lang.String[] args) {
        // PoolObject �� �ִ밪�� 20 ���� ���� ��û�� ������� �׽�Ʈ�� ��.
        int max = 40;
        System.out.println("Calling " + max + " threads");

        Thread[] threads = new PoolTest[max];
        for(int i=0;i<max;i++){
            threads[i] = new PoolTest(i);
            threads[i].start();
        }
    }
    public void run(){
        PoolManager manager = null;
        PoolObject obj = null;
        try {
            manager = PoolManager.getInstance();
            obj = manager.getPoolObject();
            obj.execute();
        }
        finally{
            if ( obj != null ) manager.release(obj);
        }
    }
}
