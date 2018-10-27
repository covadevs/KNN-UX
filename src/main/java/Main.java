import de.erichseifert.vectorgraphics2d.VectorGraphics2D;
import org.knowm.xchart.*;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import java.util.stream.IntStream;

public class Main {
    private int label0;
    private int label1;
    private int label2;

    public static void main(String[] args) {
        Main m = new Main();
        Flower[] flowers = m.readFlowers("treinamento.csv");
        // Create Chart
        final XYChart chart = new XYChartBuilder().width(1920).height(1080).title("Flower Chart").xAxisTitle("Petal Length").yAxisTitle("Pedal Width").theme(Styler.ChartTheme.XChart).build();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setToolTipsEnabled(true);
        chart.getStyler().setMarkerSize(10);

        // Series
        List<Double> xData0 = new LinkedList<Double>();
        List<Double> yData0 = new LinkedList<Double>();

        List<Double> xData1 = new LinkedList<Double>();
        List<Double> yData1 = new LinkedList<Double>();

        List<Double> xData2 = new LinkedList<Double>();
        List<Double> yData2 = new LinkedList<Double>();

        List<Double> newPointx = new LinkedList<>();
        List<Double> newPointy = new LinkedList<>();

        for (int i = 0; i < flowers.length; i++) {
            if(flowers[i].getLabel() == 0) {
                xData0.add(flowers[i].getPetalLength());
                yData0.add(flowers[i].getPetalWidth());
            }else if(flowers[i].getLabel() == 1) {
                xData1.add(flowers[i].getPetalLength());
                yData1.add(flowers[i].getPetalWidth());
            }if(flowers[i].getLabel() == 2) {
                xData2.add(flowers[i].getPetalLength());
                yData2.add(flowers[i].getPetalWidth());
            }
        }

        chart.addSeries("Flowers 0", xData0, yData0).setMarkerColor(new Color(0,0,255, 128));
        chart.addSeries("Flowers 1", xData1, yData1).setMarkerColor(new Color(255,255,0, 128));
        chart.addSeries("Flowers 2", xData2, yData2).setMarkerColor(new Color(128,0,128, 128));
//        series.setMarkerColor(Color.GREEN);


        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                // Create and set up the window.
                JFrame frame = new JFrame("Advanced Example");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // chart
                JPanel chartPanel = new XChartPanel<XYChart>(chart);
                frame.add(chartPanel);

                FocusListener focusListener = new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        JTextField txt = (JTextField)e.getComponent();
                        txt.selectAll();
                    }
                };
                // label
                JLabel pl = new JLabel("PL");
                JTextField plf = new JTextField("", 5);
                JLabel pw = new JLabel("PW");
                JTextField pwf = new JTextField("", 5);
                JLabel k = new JLabel("K");
                JTextField kf = new JTextField("", 5);
                plf.addFocusListener(focusListener);
                pwf.addFocusListener(focusListener);
                kf.addFocusListener(focusListener);

                JButton button = new JButton("Add Point");
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Double PETALLENGTH   = Double.parseDouble(plf.getText());
                        Double PETALWIDTH    = Double.parseDouble(pwf.getText());
                        Integer K             = Integer.parseInt(kf.getText());
                        Flower f = new Flower(PETALLENGTH,PETALWIDTH);
                        Integer label = m.classify(f, flowers, K, chart);

                        if(label == 0) {
                            xData0.add(Double.parseDouble(plf.getText()));
                            yData0.add(Double.parseDouble(pwf.getText()));
                        } else if(label == 1) {
                            xData1.add(Double.parseDouble(plf.getText()));
                            yData1.add(Double.parseDouble(pwf.getText()));
                        } else if(label == 2) {
                            xData2.add(Double.parseDouble(plf.getText()));
                            yData2.add(Double.parseDouble(pwf.getText()));
                        }

                        chart.removeSeries("Flowers 0");
                        chart.addSeries("Flowers 0", xData0, yData0).setMarker(SeriesMarkers.CIRCLE).setMarkerColor(new Color(0,0,255, 128));
                        chart.removeSeries("Flowers 1");
                        chart.addSeries("Flowers 1", xData1, yData1).setMarker(SeriesMarkers.DIAMOND).setMarkerColor(new Color(255,255,0, 128));;
                        chart.removeSeries("Flowers 2");
                        chart.addSeries("Flowers 2", xData2, yData2).setMarker(SeriesMarkers.SQUARE).setMarkerColor(new Color(128,0,128, 128));

                        chart.removeSeries("New Point");
                        newPointx.clear();
                        newPointy.clear();
                        newPointx.add(Double.parseDouble(plf.getText()));
                        newPointy.add(Double.parseDouble(pwf.getText()));
                        Series s = chart.addSeries("New Point", newPointx, newPointy).setMarker(new NewPointMarker()).setMarkerColor(Color.red);
                        plf.setText("");
                        pwf.setText("");
                        plf.requestFocus();
                        frame.repaint();
                    }
                });
                JPanel panel = new JPanel();
                panel.add(pl);
                panel.add(plf);
                panel.add(pw);
                panel.add(pwf);
                panel.add(k);
                panel.add(kf);
                panel.add(button);

                frame.add(panel, BorderLayout.SOUTH);

                // Display the window.
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    private Flower[] readFlowers(String filename) {
        List<Flower> flowers = new LinkedList<>();
        String line = "";
        String[] lineArray = null;

        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            line = bufferedReader.readLine();
            while(line != null) {
                lineArray = line.split(",");
                flowers.add(createFlower(lineArray));
                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Flower[] flowersToArray = new Flower[flowers.size()];
        return flowers.toArray(flowersToArray);
    }

    private Flower createFlower(String[] flowerAttrs) {
        final Integer PETALLENGTH   = 2;
        final Integer PETALWIDTH    = 3;
        final Integer LABEL         = 4;

        Flower flower = null;

        if(flowerAttrs.length == 5) {
            flower = new Flower(
                    Double.parseDouble(flowerAttrs[PETALLENGTH]),
                    Double.parseDouble(flowerAttrs[PETALWIDTH]),
                    Integer.parseInt(flowerAttrs[LABEL])
            );
        } else {
            flower = new Flower(
                    Double.parseDouble(flowerAttrs[PETALLENGTH]),
                    Double.parseDouble(flowerAttrs[PETALWIDTH])
            );
        }

        return flower;
    }

    private Integer classify(Flower flower, Flower[] flowers, int k, XYChart chart) {
        Distance[] dists = new Distance[flowers.length];
        IntStream.range(0, flowers.length)
                .forEach(index -> {
                    dists[index] = getDistance(flower, flowers[index]);
                });

        Arrays.sort(dists);
        List<Double> xNeighboors = new LinkedList<>();
        List<Double> yNeighboors = new LinkedList<>();
        IntStream.range(0, k)
                .forEach(index -> {
                    if(dists[index].getLabel() == 0) {
                        label0++;
                    } else if(dists[index].getLabel() == 1) {
                        label1++;
                    } else if(dists[index].getLabel() == 2) {
                        label2++;
                    }
                    xNeighboors.add(dists[index].getX());
                    yNeighboors.add(dists[index].getY());
                });
        chart.removeSeries("Neighboors");
        chart.addSeries("Neighboors", xNeighboors, yNeighboors).setMarker(new NeighborMarker(flower.getPetalLength(),flower.getPetalWidth(), xNeighboors.get(0), yNeighboors.get(0), chart)).setMarkerColor(Color.GREEN).setShowInLegend(false);
        return getLabel(label0, label1, label2);
    }

    private Distance getDistance(Flower flowerOne, Flower flowerTwo) {
        double soma = 0;
        soma += Math.pow((flowerOne.getPetalLength() - flowerTwo.getPetalLength()) ,2);
        soma += Math.pow((flowerOne.getPetalWidth() - flowerTwo.getPetalWidth()), 2);

        double dist = Math.sqrt(soma);
        return new Distance(dist, flowerTwo.getLabel(), flowerTwo.getPetalLength(), flowerTwo.getPetalWidth());
    }

    private Integer getLabel(int label0, int label1, int label2) {
        if(label0 > label1 && label0 > label2) {
            this.label0 = 0;
            this.label1 = 0;
            this.label2 = 0;
            return 0;
        } else if(label1 > label0 && label1 > label2) {
            this.label0 = 0;
            this.label1 = 0;
            this.label2 = 0;
            return 1;
        } else {
            this.label0 = 0;
            this.label1 = 0;
            this.label2 = 0;
            return 2;
        }
    }
}