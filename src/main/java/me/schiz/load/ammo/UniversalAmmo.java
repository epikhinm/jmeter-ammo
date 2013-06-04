package me.schiz.load.ammo;

import java.io.*;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;


import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class UniversalAmmo extends Ammo {
    private static final Logger log = LoggingManager.getLoggerForClass();

    protected int reader_cache = 4096;
    //protected BufferedReader reader;
    protected DataInputStream dis;
    protected String ammofile;
    protected long ammosize;
    protected volatile boolean first_loop;
    protected boolean gzip;

    public UniversalAmmo(String file, int buffer) {
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

    protected DataInputStream init() throws IOException {
        if(gzip) {
            //reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(ammofile))), reader_cache);
            dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(ammofile), reader_cache), reader_cache));
        }
        else {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(ammofile), reader_cache));
        }
        return dis;
    }

    public long getAmmosize() {
        if(first_loop) return -1;
        else return ammosize;
    }

    public static String readLine(InputStream in) throws IOException {
        byte[] buf = new byte[128];
        int pos = 0;
        for (;;) {
            int ch = in.read();
            if (ch == '\n' || ch < 0) break;
            buf[pos++] = (byte) ch;
            if (pos == buf.length) buf = Arrays.copyOf(buf, pos + 128);
        }
        return new String(Arrays.copyOf(buf, pos), "UTF-8");
    }

    public synchronized Bullet take() throws InterruptedException, IOException {
        Bullet b = new Bullet();
        String s = readLine(dis);

        if(s.isEmpty()) {
            first_loop = false;
            this.close();
            init();
            return this.take();
        }

        if(b.parseTitle(s) == false) {
            first_loop = false;
            this.close();
            init();
            return this.take();
        }
        b.content = new char[(int) b.length];
        byte[] tmp = new byte[(int)b.length];
        dis.read(tmp, 0, b.length);
        b.content = (new String(tmp)).toCharArray();
        //dis.read(b.content, 0, b.length);
        if(first_loop) ammosize++;
        return b;
    }

    public void close() {
        try {
            this.dis.close();
        } catch (IOException e) {
            log.error("Cannot close ammo", e);
        }
    }

    public static void main(String[] args) {
        int count = 100;
        System.out.println("Hello, world! I am Jumper");
        UniversalAmmo ammo = new UniversalAmmo("/home/schizophrenia/mulca4.ammo.small",  65536);
        //ammo.start();
        //for(int j= 100;j-->0;) {
        long start = System.currentTimeMillis();
        for (int i = 0; i<count;i++) {
            try {
                Bullet b = null;
                try {
                    b = ammo.take();

                    System.out.println(String.valueOf(b.content));
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
