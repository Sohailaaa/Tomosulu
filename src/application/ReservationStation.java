package application;

import java.util.concurrent.atomic.AtomicInteger;

public class ReservationStation {
    private FunctionalUnit[] functionalUnits;

    public ReservationStation(int length, Bus bus, int subLatency,int addLatency, int divideLatency, int mulLatency, InstructionType type) {
        functionalUnits = new FunctionalUnit[length];
        for (int i = 0; i < length; i++) {
            String tag = "";
            if (type.equals(InstructionType.MULTIPLY)) {
                tag += "M" + (i + 1);
            } else {
                if (type.equals(InstructionType.ADD)) {
                    tag += "A" + (i + 1);
                }
            }
            functionalUnits[i] = new FunctionalUnit(subLatency, addLatency, divideLatency, mulLatency, tag, bus);
        }

    }

    public FunctionalUnit[] getStations() {
        return functionalUnits;
    }

    public FunctionalUnit issueFunctionalUnit() {
        for (FunctionalUnit functionalUnit : functionalUnits) {
            if (!functionalUnit.isBusy()) {
                return functionalUnit;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        for (FunctionalUnit functionalUnit : functionalUnits) {
            if (functionalUnit.isBusy()) {
                return false;
            }
        }
        return true;
    }



}
