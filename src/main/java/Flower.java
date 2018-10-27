import java.math.BigInteger;

public class Flower {
    private double petalLength;
    private double petalWidth;

    private int label;

    public Flower(double petalLength, double petalWidth, int label) {
        this.petalLength = petalLength;
        this.petalWidth = petalWidth;
        this.label = label;
    }

    public Flower(double petalLength, double petalWidth) {
        this.petalLength = petalLength;
        this.petalWidth = petalWidth;
    }

    public double getPetalLength() {
        return petalLength;
    }

    public void setPetalLength(double petalLength) {
        this.petalLength = petalLength;
    }

    public double getPetalWidth() {
        return petalWidth;
    }

    public void setPetalWidth(double petalWidth) {
        this.petalWidth = petalWidth;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Flower{" +
                ", petalLength=" + petalLength +
                ", petalWidth=" + petalWidth +
                ", label=" + label +
                "}\n";
    }
}