package me.schiz.load.ammo;

import java.io.*;
import java.util.zip.GZIPInputStream;


import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class SyncAmmo extends Ammo {
    private static final Logger log = LoggingManager.getLoggerForClass();

    protected int reader_cache = 4096;
    protected BufferedReader reader;
    protected String ammofile;
    protected long ammosize;
    protected volatile boolean first_loop;
    protected boolean gzip;

    public SyncAmmo(String file, int buffer) {
        reader_cache = buffer;
        ammofile = new String(file);
        if(ammofile.endsWith(".gz") || ammofile.endsWith(".gzip")) gzip = true;
        ammosize = 0;
        first_loop = true;
        try {
            init();
        } catch (IOException e) {
           log.error("Cannot init ammo", e);
        }
    }

    protected BufferedReader init() throws IOException {

        if(gzip) {
            reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(ammofile))), reader_cache);
        }
        else {
            reader = new BufferedReader(new FileReader(ammofile), reader_cache);
        }
        return reader;
    }

    public long getAmmosize() {
        if(first_loop) return -1;
        else return ammosize;
    }

    public synchronized Bullet take() throws InterruptedException, IOException {
        Bullet b = new Bullet();
        String s = reader.readLine();
        System.out.println(s);

        if(b.parseTitle(s) == false) {
            first_loop = false;
            this.close();
            init();
            return this.take();
        }
        b.content = new char[(int) b.length];
        reader.read(b.content, 0, b.length);
        if(first_loop) ammosize++;
        return b;
    }

    public void close() {
        try {
            this.reader.close();
        } catch (IOException e) {
            log.error("Cannot close ammo", e);
        }
    }

    public static void main(String[] args) {
        int count = 100;
        System.out.println("Hello, world! I am Jumper");
        SyncAmmo ammo = new SyncAmmo("/home/schizophrenia/mulca4.ammo.small",  65536);
        //ammo.start();
        //for(int j= 100;j-->0;) {
            long start = System.currentTimeMillis();
            for (int i = 0; i<count;i++) {
                try {
                    Bullet b = null;
                    try {
                        b = ammo.take();
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    //System.out.println(b.name);
                    if(b.content.length == 0)    System.err.println("error");
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            System.out.println("speed " + count  * 1000  / (System.currentTimeMillis() - start));
        //}
        //ammo.interrupt();
        //ammo.stop();

    }
}
