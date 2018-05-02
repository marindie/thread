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
                    // �������� ��� �Ʒ� wait(timeout) ���� �����.
                    // �Ʒ����� 1 �ʸ� wait �ϰ� ������, �����δ� �� ������
                    // notify() �� ���� ����� ��.
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
            // ���� notifyAll() �� �ϸ� getPoolObject()���� wait()���� �ɸ�
            // ��� Thread �� �Ͻÿ� �Ͼ�� �Ǿ� �������� ���� while ���� ���ÿ�
            // Ȯ���ϰԵǰ�, �̴� CPU ���ϸ� �߱��� ������ �����ǳ�,
            // �Ϻ� å������ notify() ��� notifyAll() �� �����ϱ⵵ ��.
            // �ΰ����� ��� �׽�Ʈ�� ���� ���� ��մ� ������ ���� �� ���� ����.
        }
        //System.out.println("Object-" + obj.getIndex() + ": released.");
    }
}
