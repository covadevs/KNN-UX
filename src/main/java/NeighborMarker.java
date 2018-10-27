
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.Marker;

import java.awt.*;
import java.awt.geom.Line2D;

public class NeighborMarker extends Marker {
    final BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
    private double toX;
    private double toY;
    private double x;
    private double y;
    private Styler xyStyler;
    private Chart chart;

    public NeighborMarker(double toX, double toY, double x, double y, Chart chart) {
        this.toX = toX;
        this.toY = toY;
        this.x = x;
        this.y = y;
        this.xyStyler = chart.getStyler();
        this.chart = chart;
    }

    @Override
    public void paint(Graphics2D g, double xOffset, double yOffset, int markerSize) {
        g.setStroke(stroke);

        double xTickSpace = xyStyler.getPlotContentSize() * g.getClipBounds().getWidth();
        double xLeftMargin = Utils.getTickStartOffset((int) g.getClipBounds().getWidth(), xTickSpace);

        double yTickSpace = xyStyler.getPlotContentSize() * g.getClipBounds().getHeight();
        double yTopMargin = Utils.getTickStartOffset((int) g.getClipBounds().getHeight(), yTickSpace);

        XYSeries[] series = (XYSeries[]) chart.getSeriesMap().values().toArray(new XYSeries[chart.getSeriesMap().size()]);
        double xMin = series[0].getXMin();
        double xMax = series[0].getXMax();

        double yMin = series[0].getYMin();
        double yMax = series[0].getYMax();

        for(XYSeries s : series) {
            if (s.getXMin() < xMin) {
                xMin = s.getXMin();
            }

            if(s.getXMax() > xMax) {
                xMax = s.getXMax();
            }

            if (s.getYMin() < yMin) {
                yMin = s.getYMin();
            }

            if(s.getYMax() > yMax) {
                yMax = s.getYMax();
            }
        }

        //PlotContent_XY
        double xTransform = xLeftMargin + ((toX - xMin) / (xMax - xMin) * xTickSpace);
        double yTransform = g.getClipBounds().getHeight() - (yTopMargin + (toY - yMin) / (yMax - yMin) * yTickSpace);

        double toXoffset = g.getClipBounds().getX() + xTransform;
        double toYoffset = g.getClipBounds().getY() + yTransform;

        Line2D line = new Line2D.Double();
        ((Line2D.Double) line).x1 = xOffset;
        ((Line2D.Double) line).y1 = yOffset;
        ((Line2D.Double) line).x2 = toXoffset;
        ((Line2D.Double) line).y2 = toYoffset;
        g.draw(line);
    }

}
