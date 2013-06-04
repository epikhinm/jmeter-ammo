package me.schiz.jmeter.ammo;

import me.schiz.load.ammo.*;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class Cartridge extends ConfigTestElement
        implements TestStateListener, TestBean {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final ConcurrentHashMap<String, Ammo> cartridges = new ConcurrentHashMap<String, Ammo>();

    private String name;
    private String ammofile;
    private String chuckSize;
    private String storeInHeap;
    private String readAsByte;

    public String getTitle() {
        return this.getName();
    }

    public String getReadAsByte() {
        return this.readAsByte;
    }

    public void setReadAsByte(String  readAsByte) {
        this.readAsByte = readAsByte;
    }

    public String getChuckSize() {
        return this.chuckSize;
    }

    public void setChuckSize(String  chuckSize) {
        this.chuckSize = chuckSize;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmmofile() {
        return this.ammofile;
    }

    public void setAmmofile(String ammofile) {
        this.ammofile = ammofile;
    }

    public String getStoreInHeap() {
        return this.storeInHeap;
    }

    public void setStoreInHeap(String storeInHeap) {
        this.storeInHeap = storeInHeap;
    }

    public boolean useHeap() {
        if(this.storeInHeap.equalsIgnoreCase("true"))   return true;
        else return false;
    }

    public static Bullet takeBullet(String cartridgeName) {
        try {
            if(!cartridges.containsKey(cartridgeName)) {
                log.warn("Not found cartridge " + cartridgeName);
            } else return cartridges.get(cartridgeName).take();
        } catch (Exception e) {
            log.warn("Cartridge exception", e);
        }
        return null;
    }
    public static String take(String cartridgeName) {
        Bullet b = takeBullet(cartridgeName);
        if(b != null)   return String.valueOf(b.content);
        else return null;
    }

    @Override
    public void testStarted() {
        createCartridge(name, ammofile, Integer.parseInt(chuckSize), useHeap(), getReadAsByte().equalsIgnoreCase("True"));
    }

    public static void createCartridge(String name, String ammofile, int chuckSize, boolean storeInHeap, boolean readAsByte) {
        Ammo ammo;
        if(storeInHeap)   ammo = new HeapAmmo(ammofile, chuckSize, readAsByte);
        else {
            if(readAsByte) ammo = new UniversalAmmo(ammofile, chuckSize);
            else ammo = new SyncAmmo(ammofile, chuckSize);
        }
        cartridges.put(name, ammo);
    }

    @Override
    public void testStarted(String s) {
        testStarted();
    }

    @Override
    public void testEnded() {

    }

    @Override
    public void testEnded(String s) {
        testEnded();
    }
}
