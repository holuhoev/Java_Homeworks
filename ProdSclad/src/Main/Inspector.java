package Main;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Inspector implements Runnable {
    private boolean working = true;
    private LinkedBlockingQueue<AtomicLong> goods;
    private int p; // persentage of positive goods;
    private float timeFarmer;//time to check one good from Farmer
    private float timeSeller;//time to check one good
    private Object waiter = new Object();

    private LinkedBlockingQueue<Farmer> farmers;
    private LinkedBlockingQueue<Seller> sellers;

    public Inspector(float timeFarmer, float timeSeller, int p, LinkedBlockingQueue<Farmer> farmerProducts, LinkedBlockingQueue<Seller> sellerProducts) {
        this.p = p;
        this.timeFarmer = timeFarmer;
        this.timeSeller = timeSeller;

        farmers = farmerProducts;
        sellers = sellerProducts;
    }

    private void checkAndAdd(Farmer farmer) {
        try {
            int products = farmer.getProducts();


            AtomicLong value = goods.peek();
            int n = getNumberOfPositiveGoods(products);
            if (((value.longValue() + n) < 0)) {
                goods.add(new AtomicLong(n));
            } else {
                goods.peek().set(value.longValue() + n);
            }


            Thread.sleep((int) (timeFarmer * 1000));
            farmer.notify();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void farmerCame(Farmer farmer){
        if(farmers.size() == 0 && sellers.size() == 0) {
            waiter.notify();
        }
        farmers.add(farmer);

    }

    public void sellerCame(Seller seller){
        if(farmers.size() == 0 && sellers.size() == 0) {
            waiter.notify();
        }
        sellers.add(seller);
    }

    private  int getNumberOfPositiveGoods(int n) {
        return (100 - p) * n / 100;
    }

    public void giveGoodsToSellers(Seller seller) {
        System.out.println("На складе: " + goods.peek().longValue() + " товаров");

        try {
            if (seller.getProducts() > goods.peek().longValue()) {
                System.out.println("Нехватка продуктов на складе!");
            } else {
                goods.peek().set(goods.peek().longValue() - seller.getProducts());
                sellers.remove(seller);

                System.out.println("Со склада забрали: " + seller.getProducts());

                Thread.sleep((int) (timeSeller * 1000));
                seller.notify();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        System.out.println("Inspector " + Thread.currentThread().getName() + " start working!");
        while(isWorking()){
            while(farmers.size() != 0){
                try {
                    checkAndAdd(farmers.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (sellers.size() != 0){
                sellers.forEach(this::giveGoodsToSellers);
            }
            synchronized (waiter) {
                try {
                    waiter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean isWorking() {
        return working;
    }
}
