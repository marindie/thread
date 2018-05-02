package com.thread.sleepisbad;

public class BadPoolManager {
    private static BadPoolManager manager = null;

    private int MAX = 20;
    private int count = 0;
    private java.util.Vector pool = new java.util.Vector();

    private BadPoolManager() {
        super();
        for(int i=0;i<MAX;i++){
            pool.addElement(new PoolObject(new Integer(i)));
        }
    }
    public static synchronized BadPoolManager getInstance() {
        if ( manager == null ) {
            manager = new BadPoolManager();
        }
        return manager;    
    }
    public synchronized PoolObject getPoolObject() {
        PoolObject obj = null;
        while( count >= MAX ) {
            try{
                Thread.sleep(1000);
            }catch(Exception e){}
            System.out.println("BadPoolManager : sleep... current count=" + count);
        }
        obj = (PoolObject)pool.elementAt(0);
        pool.removeElementAt(0);
        count++;
        return obj;
    }
    public synchronized void release(PoolObject obj) {
        count--;
        pool.addElement(obj);
        System.out.println("BadPoolManager: released ... current count=" + count);
    }
}
