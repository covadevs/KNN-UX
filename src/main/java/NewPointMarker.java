import org.knowm.xchart.style.markers.Marker;

import java.awt.*;
import java.awt.geom.Path2D;

public class NewPointMarker extends Marker {
    final BasicStroke stroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

    @Override
    public void paint(Graphics2D g, double xOffset, double yOffset, int markerSize) {
        g.setStroke(stroke);

        double halfSize = (double) markerSize / 2;

        Path2D.Double path = new Path2D.Double();
        path.moveTo(xOffset - halfSize, yOffset - halfSize);
        path.lineTo(xOffset + halfSize, yOffset + halfSize);
        path.moveTo(xOffset - halfSize, yOffset + halfSize);
        path.lineTo(xOffset + halfSize, yOffset - halfSize);
        g.draw(path);
    }
}
