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
     * PS: getPoolObject() ���� �Ķ���ͷ� "int from" �� �����δ� ���� �ʿ䰡
     *     ������ � Client Thread ���� ��û�� ������ ����� �ϱ� ���� �־�� ��
     *     ����.
     */
     
    public synchronized PoolObject getPoolObject() {
        while( count >= MAX ) {
            try{
                System.out.println("getPoolObject( active count before =" + count);
                
                // Max���� �ʰ��� �������� ��� �Ʒ� wait(timeout) ���� �����.
                // �Ʒ����� 1�ʸ� wait �ϰ� ������, �����δ� �� ������ 
                // notify() �� ���� ����� ��.
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

        // ���� notifyAll() �� �ϸ� getPoolObject()���� wait()���� �ɸ�
        // ��� Thread �� �Ͻÿ� �Ͼ�� �Ǿ� �������� ���� while ���� ���ÿ�
        // Ȯ���ϰԵǰ�, �̴� CPU ���ϸ� �߱��� ������ �����ǳ�,
        // �Ϻ� å������ notify() ��� notifyAll() �� �����ϱ⵵ ��.
        // �ΰ����� ��� �׽�Ʈ�� ���� ���� ��մ� ������ ���� �� ���� ����.

        System.out.println("Thread-" + obj.getIndex() + ": released :active count=" + count);
    }
}
