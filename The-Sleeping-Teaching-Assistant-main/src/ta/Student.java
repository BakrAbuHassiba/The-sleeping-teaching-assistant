
package ta;

import java.util.concurrent.Semaphore;


public class Student implements Runnable {

    private final int programTime;
    private final int count = 0;
    private final int studentNum;
    private final Mutexlock wakeup;
    private final Semaphore chairs;
    private final Semaphore available;
    private final int numberofchairs;
    private final int numberofTA;
    private final int helpTime=5000;
    private final Thread t;

    public Student(int programTime, Mutexlock wakeup, Semaphore chairs, Semaphore available, int studentNum,int numberofchairs, int numberofTA) {
        this.programTime = programTime;
        wakeup = new Mutexlock(numberofTA);
        this.wakeup = wakeup;
        this.chairs = chairs;
        this.available = available;
        this.studentNum = studentNum;
        t = Thread.currentThread();
        this.numberofchairs = numberofchairs;
        this.numberofTA = numberofTA;
    }

    @Override
    public void run() {
        while (true) {
            try {
                t.sleep(programTime * 1000);
                if (available.tryAcquire()) {
                    try {
                        wakeup.take();
                        t.sleep(helpTime);
                    } catch (InterruptedException e) {
                        continue;
                    } finally {
                        available.release();
                    }
                } else {

                    if (chairs.tryAcquire()) {
                        try {

                            available.acquire();
                            t.sleep(helpTime);
//                            available.release();
                        } catch (InterruptedException e) {
                            continue;
                        }
                        finally {
                            available.release();
                        }
                    } else {
                        t.sleep(helpTime);
                    }
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
