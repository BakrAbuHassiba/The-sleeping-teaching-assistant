
package TAs_Project;

import java.util.concurrent.Semaphore;


public class TeachingAssistant implements Runnable {

    private final Mutexlock wakeup;
    private final Semaphore chairs;
    private final Semaphore available;
    private final Thread t;
    private final int numberofTA;
    private final int numberofchairs;
    private final int helpTime=5000;


    public TeachingAssistant (Mutexlock wakeup, Semaphore chairs, Semaphore available, int numberofTA,int numberofchairs) {
        t = Thread.currentThread();
        this.wakeup = wakeup;
        wakeup = new Mutexlock(numberofTA);
        this.chairs = chairs;
        this.available = available;
        this.numberofTA = numberofTA;
        this.numberofchairs=numberofchairs;
    }

    @Override
    public void run() {
        while (true) {
            try {
                wakeup.release();
                t.sleep(helpTime);
                
                
                if (chairs.availablePermits() != numberofchairs) {
                    int releasedChairs = 0;
                    do {
                        t.sleep(helpTime);
                        chairs.release();
                        releasedChairs++;

                    } while (releasedChairs != numberofchairs);
                }
            } catch (InterruptedException e) {
                continue;
            }
        }
    }
}
