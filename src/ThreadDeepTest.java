import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * Tests the stack limits of the VM.
 * 
 * Starts as many threads until memory is exhausted.
 * Each thread does a recursive method call until it runs out of stack.
 * The maximum number of threads and min/max stack depth is printed.
 * 
 * Interesting parameters to set:
 * ulimit -s
 * -Xss512k  ... -Xss128k
 * -XX:ThreadStackSize=512 ... -XX:ThreadStackSize=128
 * -server vs. -client
 * 
 * Set the other parameters to the ones you usually have:
 * -server -Xms1g -Xmx1g -XX:MaxPermSize=256m -XX:ReservedCodeCacheSize=64m
 * 
 * @author Ortwin Gl?k
 */
public class ThreadDeepTest implements Runnable {
    
    public static void main(String[] args) {
    	ThreadDeepTest app = new ThreadDeepTest();
        app.run();
    }
    
    int minRecursion = Integer.MAX_VALUE;
    int maxRecursion = 0;
    List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();

    public void run() {
        int i=0;
        long start = System.currentTimeMillis();
        try {
            for (;;i++) {
                Thread a = new AThread(String.valueOf(i));
                a.setDaemon(true);
                a.start();
                System.out.println(i+": GC "+ getGcSum());
            }
        } catch(OutOfMemoryError e) {
            // we create threads until we run out of memory
        }
        long duration = System.currentTimeMillis() - start;
        float tps = i / (duration / 1000.0f);
        System.out.println("Max. threads: "+ (i-1));
        System.out.println("Max. recursions: "+ maxRecursion);
        System.out.println("Min. recursions: "+ minRecursion);
        System.out.println("Threads per second: "+ tps);
    }
    
    private int getGcSum() {
        int sum = 0;
        for (GarbageCollectorMXBean mb : gcbeans) {
            sum += mb.getCollectionCount();
        }
        return sum;
    }
    
    public class AThread extends Thread {
        
        protected AThread(String name) {
            super(name);
        }

        public void run() {
            f(0);
        }
        
        private void f(int i) {
            try {
                i++;
                f(i);
            } catch(StackOverflowError e) {
                System.out.print("r-"+ this.getName()+": "+ i +"\n");
                synchronized (ThreadDeepTest.this) {
                    minRecursion = Math.min(minRecursion, i);
                    maxRecursion = Math.max(maxRecursion, i);
                }
                try {
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException e1) {
                    System.out.print("i");
                }
            }
        }
    }
}
