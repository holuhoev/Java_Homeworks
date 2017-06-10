package Main;


import java.util.Random;

public class Farmer implements Runnable {
    private int products;
    final private float timeOfSleep;
    private Inspector inspector;
    private boolean worked = true;

    public int getProducts(){
        return products;
    }

    public Farmer(Inspector inspector, float timeOfSleep){
        products =  getRandomInt(100);

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
        while(isWorked()) {
            try {

                inspector.farmerCame(this);
                synchronized (this) {
                    this.wait();
                }
                Thread.currentThread().sleep((long)(timeOfSleep * 1000));
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
