package me.schiz.load.ammo;

import java.io.*;
import java.util.ArrayList;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class HeapAmmo extends Ammo {
    protected Ammo ammoReader;
    protected volatile boolean isLoaded;
    protected AtomicCyclicCounter counter;
    protected ArrayList<Bullet> ammo;

    public HeapAmmo(String file, int buffer) {
        isLoaded = false;
        ammoReader = new SyncAmmo(file, buffer);
        ammo = new ArrayList<Bullet>();
    }

    public HeapAmmo(String file, int buffer, boolean readAsByte) {
        isLoaded = false;
        if(readAsByte)  ammoReader = new UniversalAmmo(file, buffer);
        else ammoReader = new SyncAmmo(file, buffer);
        ammo = new ArrayList<Bullet>();
    }

    public long getAmmosize() {
        if(first_loop) return -1;
        else return ammosize;
    }

    public synchronized Bullet take() throws InterruptedException, IOException {
        Bullet b;

        if(!isLoaded) {
            synchronized (ammoReader) {
                b = ammoReader.take();
                if(ammoReader.getAmmosize() != -1) {
                    isLoaded = true;
                    counter = new AtomicCyclicCounter(ammo.size() + 1);
                    ammosize = ammoReader.getAmmosize();
                }
                ammo.add(b);
                if(isLoaded)    ammo.trimToSize();
            }
        } else {
            b = ammo.get(counter.cyclicallyIncrementAndGet() % (int)ammosize); // Fucking slow code
        }

        return b;
    }

    public void close() {
        ammoReader.close();
        ammo.clear();
        ammo = null;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hello, world! I am HeapAmmo");

        HeapAmmo ha = new HeapAmmo("/home/schizophrenia/2.ammo.gz", 4096, false);

        long start_load = System.currentTimeMillis();

        while(ha.getAmmosize() < 0) {
            ha.take();
        }

        long end_load = System.currentTimeMillis();

        System.out.println("ammosize " + ha.getAmmosize());

        if(end_load > start_load)   System.out.println("load speed: " + ha.getAmmosize() * 1000 / (end_load -start_load) + " rps");

        long count = 100000000;

        for(int i=10;i-->0;) {
            long start_take = System.currentTimeMillis();
            for(long j=count;j-->0;) {
                ha.take();
            }
            long end_take = System.currentTimeMillis();
            if(end_take > start_take)   System.out.println("take speed: " + count * 1000 / (end_take - start_take) + " rps");
        }
    }
}
