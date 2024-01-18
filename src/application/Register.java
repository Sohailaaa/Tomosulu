package application;

public class Register {
    private String registerName;
    private String Qi;
    private double content;

    public Register(String registerName) {
        this.registerName = registerName;
        this.Qi = "";
        this.content = 0;
    }

    public void setQi(String Qi) {
        //this.content = 0;
        this.Qi = Qi;
    }

    public void setContent(double content) {
        this.Qi = "";
        this.content = content;
    }

    public String getQi() {
        return Qi;
    }

    public String toString() {
        return ("\n{Register: " + registerName + " Q: " + (Qi!=null?Qi:"") + " Content: " + content+"}");
    }

    public double getContent() {
        return content;
    }

    public String getRegisterName() {
        return registerName;
    }
}