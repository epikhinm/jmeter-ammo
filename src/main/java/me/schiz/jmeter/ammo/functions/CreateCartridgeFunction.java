package me.schiz.jmeter.ammo.functions;

import me.schiz.jmeter.ammo.Cartridge;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CreateCartridgeFunction extends AbstractFunction {
    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__createCartridge";

    static {
        desc.add("Cartridge name");
        desc.add("Ammo file");
        desc.add("Chuck size (optional, default 4096)");
        desc.add("Store in heap (optional, default False)");
        desc.add("Read as byte (optional, default False)");
    }
    private Object[] values;

    public CreateCartridgeFunction() {
    }

    @Override
    public String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {
        JMeterVariables vars = getVariables();
        String cartridgeName=((CompoundVariable) values[0]).execute();
        String ammoFile=((CompoundVariable) values[1]).execute();
        int chuckSize = 4096;
        boolean storeInHeap = false;
        boolean readAsByte = false;
        //String result = Cartridge.take(cartridgeName);

        if (vars != null && values.length>2) {
            chuckSize = Integer.parseInt(((CompoundVariable) values[2]).execute());
        }
        if (vars != null && values.length>3) {
            storeInHeap = ((CompoundVariable) values[3]).execute().equalsIgnoreCase("true");
        }
        if (vars != null && values.length>4) {
            readAsByte = ((CompoundVariable) values[4]).execute().equalsIgnoreCase("true");
        }

        Cartridge.createCartridge(cartridgeName, ammoFile, chuckSize, storeInHeap, readAsByte);
        return cartridgeName + " OK";

    }

    @Override
    public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkMinParameterCount(parameters, 2);
        values = parameters.toArray();
    }

    @Override
    public String getReferenceKey() {
        return KEY;
    }

    public List<String> getArgumentDesc() {
        return desc;
    }
}