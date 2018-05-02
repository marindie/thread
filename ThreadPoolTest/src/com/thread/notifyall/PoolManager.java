package com.thread.notifyall;

public class PoolManager {
	private static PoolManager manager = null;

    private int MAX = 20;
    private int count = 0;
    private java.util.Vector pool = new java.util.Vector();

    private PoolManager() {
        for(int i=0;i<MAX;i++){
            pool.addElement( new PoolObject(new Integer(i)) );
        }
    }
    public static synchronized PoolManager getInstance() {
        if ( manager == null ) {
            manager = new PoolManager();
        }
        return manager;    
    }
    /**
     * PS: getPoolObject() 에서 파라메터로 "int from" 을 실제로는 받을 필요가
     *     없으나 어떤 Client Thread 에서 요청이 온지를 디버깅 하기 위해 넣어둔 것
     *     뿐임.
     */
     
    public synchronized PoolObject getPoolObject() {
        while( count >= MAX ) {
            try{
                System.out.println("getPoolObject( active count before =" + count);
                
                // Max값을 초과한 나머지는 모두 아래 wait(timeout) 에서 대기함.
                // 아래에서 1초를 wait 하게 하지만, 실제로는 그 이전에 
                // notify() 에 의해 깨어나게 됨.
                wait(1*1000); 
                //System.out.println("awaiked. ...");

            }catch(Exception e){
                System.out.println("getPoolObject: awaiked " + e.toString());
            }
        }
        PoolObject obj = (PoolObject)pool.elementAt(0);
        pool.removeElementAt(0);
        count++;
        System.out.println("getPoolObject( active count after =" + count);
        return obj;
    }
    public synchronized void release(PoolObject obj) {
        count--;
        pool.addElement(obj);

        //notify();
        notifyAll(); 

        // 만약 notifyAll() 을 하면 getPoolObject()에서 wait()에서 걸린
        // 모든 Thread 가 일시에 일어나게 되어 쓸데없이 전부 while 문을 동시에
        // 확인하게되고, 이는 CPU 부하를 야기할 것으로 생각되나,
        // 일부 책에서는 notify() 대신 notifyAll() 을 권장하기도 함.
        // 두가지를 모두 테스트해 보면 아주 재밌는 현상을 만날 수 있을 것임.

        System.out.println("Thread-" + obj.getIndex() + ": released :active count=" + count);
    }
}
