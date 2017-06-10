package Main;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class StoreHouse {
    boolean isInitialize;

    private short n;
    private byte m;
    private int M;
    private int N;
    private float t1;
    private float t2;
    private float T1;
    private float T2;
    private int p;

    public StoreHouse(int M, int N, float t1, float t2, float T1, float T2, int p) throws Exception {
        setM(M);
        setN(N);
        set_t1(t1);
        set_t2(t2);
        setT1(T1);
        setT2(T2);
        if (p < 0 || p > 100)
            throw new Exception();
        else setP(p);
        isInitialize = true;
    }

   public  void start(){
        if(isInitialize) {

            LinkedBlockingQueue<Farmer> farmerProducts = new LinkedBlockingQueue<Farmer>(M);
            LinkedBlockingQueue<Seller> sellerProducts = new LinkedBlockingQueue<Seller>(N);

            Inspector inspector = new Inspector(t1, t2, p,farmerProducts,sellerProducts);
            Thread inspThread = new Thread(inspector);
            inspThread.setName("Inspector");
            inspThread.start();

            for (int i=0;i<N;i++){
                Thread temp = new Thread(new Farmer(inspector,T1));
                temp.setName("F_"+(i+1));
                temp.start();
            }
            for (int i=0;i<M;i++){
                Thread temp = new Thread(new Seller(inspector,T2));
                temp.setName("S_"+(i+1));
                temp.start();
            }
        }
   }


    public int getM() {
        return M;
    }

    public void setM(int m) {
        M = m;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public float get_t1() {
        return t1;
    }

    public void set_t1(float t1) {
        this.t1 = t1;
    }

    public float get_t2() {
        return t2;
    }

    public void set_t2(float t2) {
        this.t2 = t2;
    }

    public float getT1() {
        return T1;
    }

    public void setT1(float t1) {
        T1 = t1;
    }

    public float getT2() {
        return T2;
    }

    public void setT2(float t2) {
        T2 = t2;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }



}
