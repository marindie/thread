package com.wide.util;

public class PoolManager {
    private static PoolManager manager = null;

    private int MAX = 20;
    private int count = 0;
    private java.util.Vector pool = new java.util.Vector();

    private PoolManager() {
        super();
        for(int i=0;i<MAX;i++){
            pool.addElement(new PoolObject(new Integer(i)));
        }
    }
    public static synchronized PoolManager getInstance() {
        if ( manager == null ) {
            manager = new PoolManager();
        }
        return manager;    
    }
    public synchronized PoolObject getPoolObject() {
        PoolObject obj = null;
        while( count >= MAX ) {
            try{
                Thread.sleep(1000);
            }catch(Exception e){}
            System.out.println("PoolManager : sleep... current count=" + count);
        }
        obj = (PoolObject)pool.elementAt(0);
        pool.removeElementAt(0);
        count++;
        return obj;
    }
    public synchronized void release(PoolObject obj) {
        count--;
        pool.addElement(obj);
        System.out.println("PoolManager: released ... current count=" + count);
    }
}
