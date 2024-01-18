package application;

import java.lang.reflect.Array;

public class RegisterFile {
    private Register[] registers;

    public RegisterFile() {
        registers = new Register[32];
        for (int i = 0; i < 32; i++) {
            registers[i] = new Register("F" + i);
        }
    }

    public Register[] getRegisters() {
        return registers;
    }

    public double getRegisterContent(String registerName) {
        for (int i = 0; i < 32; i++) {
            if (registers[i].getRegisterName().equals(registerName)) {
                return registers[i].getContent();
            }
        }
        return -1;
    }

    public boolean isReady(String registerName) {
        for (Register register:registers) {
            if (register.getRegisterName().equals(registerName)) {
                if (register.getQi() == "")
                    return true;
            }
        }
        return false;
    }

    public String getRegisterQi(String registerName) {
        for (Register register:registers) {
            if (register.getRegisterName().equals(registerName)) {
                return register.getQi();
            }
        }
        return "";
    }

    public void setRegisterContent(String registerName, double value) {
        for (Register register:registers) {
            if (register.getRegisterName().equals(registerName)) {
                register.setContent(value);
            }
        }
    }

    public void setRegisterQi(String registerName, String qi) {
        for (Register register:registers) {
            if (register.getRegisterName().equals(registerName)) {
                register.setQi(qi);
            }
        }
    }

}


