package application;

public class Bus {
    private double value;
    private String tag;
    private boolean valueWritten = false;


    public Bus() {
        tag = "Balabeezo";
    }

    public void writeValue(double value,String tag) {
        this.tag = tag;
        this.value = value;
        this.valueWritten = true;
    }

    public boolean isValueWritten() {
        return valueWritten;
    }

    public double getValue() {
        return value;
    }
    public String getTag(){
        return tag;
    }
    public void setTag(String tag){
        this.tag=tag;
    }
    public void setContent(double content){
        this.value=content;
    }

    public void setIsValueWritten(boolean valueWritten){
        this.valueWritten = valueWritten;
    }

}
