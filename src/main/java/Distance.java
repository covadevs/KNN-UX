public class Distance implements Comparable<Distance> {
    private double distance;
    private int label;
    private double x;
    private double y;

    public Distance(double distance, int label, double x, double y) {
        this.distance = distance;
        this.label = label;
        this.x = x;
        this.y = y;
    }


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Distance{" +
                "distance=" + distance +
                ", label=" + label +
                '}';
    }

    @Override
    public int compareTo(Distance o) {
        if(this.distance >= o.getDistance()) {
            return 1;
        } else if (this.distance <= o.getDistance()) {
            return -1;
        }
        return 0;
    }
}