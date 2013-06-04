package me.schiz.jmeter.ammo;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.beans.PropertyDescriptor;


public class CartridgeBeanInfo
    extends BeanInfoSupport {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public CartridgeBeanInfo() {
        super(Cartridge.class);

        createPropertyGroup("cartridge", new String[] {
                "name",
                "ammofile",
                "chuckSize",
                "storeInHeap",
                "readAsByte"});


        PropertyDescriptor p = property("name");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        p = property("ammofile");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        p = property("chuckSize");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "4096");

        p = property("storeInHeap");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "False");

        p = property("readAsByte");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "False");

        if(log.isDebugEnabled()) {
            for (PropertyDescriptor pd : getPropertyDescriptors()) {
                log.debug(pd.getName());
                log.debug(pd.getDisplayName());
            }
        }
    }
}
