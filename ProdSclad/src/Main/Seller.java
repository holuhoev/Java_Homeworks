package Main;

import java.util.Random;

public class Seller implements Runnable {
    private int products;
    private Inspector inspector;
    final private float timeOfSleep;
    private boolean worked = true;

    public int getProducts(){
        return products;
    }

    public Seller(Inspector inspector,float timeOfSleep){
        products = getRandomInt(100);
        this.inspector = inspector;
        this.timeOfSleep = timeOfSleep;
    }

    private int getRandomInt(int bounds){
        Random ran = new Random();
        int n;
        n = ran.nextInt(bounds);
        return n;
    }

    @Override
    public void run() {

        while (isWorked()) {
            try {
                inspector.sellerCame(this);
                synchronized (this){
                    this.wait();
                }
                Thread.currentThread().sleep((long)(timeOfSleep*1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean isWorked() {
        return worked;
    }

    public void setWorked(boolean worked) {
        this.worked = worked;
    }
}
