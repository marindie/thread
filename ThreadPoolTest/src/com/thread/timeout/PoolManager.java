package com.thread.timeout;

public class PoolManager {
    private static PoolManager manager = null;

    private int MAX_RESOURCE = 20;
    private long TIME_WAIT = 2 * 1000;
    private java.util.Hashtable pool = new java.util.Hashtable();
    private java.util.Stack freeStack = new java.util.Stack();

    private PoolManager() {
        for(int i=0;i<MAX_RESOURCE;i++){
            Integer index = new Integer(i);
            pool.put( index,  new PoolObject(index) );
            freeStack.push(index);
        }
    }
    public static synchronized PoolManager getInstance() {
        if ( manager == null ) {
            manager = new PoolManager();
        }
        return manager;    
    }
    public  PoolObject getPoolObject() throws Exception {
        Integer index = null;
        long start = System.currentTimeMillis();
        synchronized(this){
            while( freeStack.empty()  ) {
                try{
                    // 나머지는 모두 아래 wait(timeout) 에서 대기함.
                    // 아래에서 1 초를 wait 하게 하지만, 실제로는 그 이전에
                    // notify() 에 의해 깨어나게 됨.
                    wait(1 * 1000);
                    
                }catch(Exception e){
                    System.err.println("getPoolObject: awaiked " + e.toString());
                }
                long end = System.currentTimeMillis();
                if ( freeStack.empty() && (end - start) >= TIME_WAIT ) {
                    throw new Exception("getPoolObject : timeout(" + TIME_WAIT + ") exceed");
                }
            }
            index = (Integer)freeStack.pop();
        }
        PoolObject obj = (PoolObject)pool.get(index);
        return obj;
    }
    public void release(PoolObject obj) {
        synchronized ( this ){
            freeStack.push(obj.getIndex());
            
            //notify();
            notifyAll(); 
            // 만약 notifyAll() 을 하면 getPoolObject()에서 wait()에서 걸린
            // 모든 Thread 가 일시에 일어나게 되어 쓸데없이 전부 while 문을 동시에
            // 확인하게되고, 이는 CPU 부하를 야기할 것으로 생각되나,
            // 일부 책에서는 notify() 대신 notifyAll() 을 권장하기도 함.
            // 두가지를 모두 테스트해 보면 아주 재밌는 현상을 만날 수 있을 것임.
        }
        //System.out.println("Object-" + obj.getIndex() + ": released.");
    }
}
