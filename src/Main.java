import oracle.jrockit.jfr.JFR;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Jody on 9/27/2016.
 */
public class Main{
    static final int channel = 0;
    public static void main(String[] args){
        FFTMain fft = new FFTMain();

        JFrame frame = new JFrame();
        JPanel content = new JPanel(new BorderLayout());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(content);


        content.setVisible(true);

        frame.setSize(1000, 700);
        frame.setVisible(true);

        Mic mic = new Mic();
        //WAV wav = new WAV("C:\\bwv80.wav");

        //byte[] buffer = new byte[100000];

        int incrementer = 1;

        while(true){

            byte[] dataChunk = mic.getDataChunk();

            //System.arraycopy(dataChunk, 0, buffer, 0, dataChunk.length);

            JFreeChart chart = ChartFactory.createXYBarChart(
                    "FFT data", "Normalized Freq", false, "Magnitude", fft.calculateExisting(dataChunk), PlotOrientation.VERTICAL, true, true, false
            );

            NumberAxis yaxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
            NumberAxis xaxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
            yaxis.setRange(0, 50000);
            xaxis.setRange(4000, 5000);

            yaxis.setAutoRangeIncludesZero(false);

            ChartPanel panel = new ChartPanel(chart);

            panel.setDomainZoomable(false);
            panel.setRangeZoomable(false);


            content.removeAll();
            content.add(panel);

            panel.setPreferredSize(new Dimension(1000,700));
            panel.setVisible(true);

            panel.revalidate();

            panel = new ChartPanel(chart);
            panel.repaint();


            try{
                Thread.sleep(50);
            }catch(Exception e){
                e.printStackTrace();
            }


        }

    }
}

class FFTMain{
    FFT fft = new FFT();

    public XYSeriesCollection calculate(int channel) {

        XYSeriesCollection dataSetXY = new XYSeriesCollection();
        XYSeries series = new XYSeries("FFT");

        int[][] oldPCM = PCM.PCMtoArray("C:\\100hz.wav");
        int oldPCMLen = oldPCM[channel].length;

        double[] sampleData;

        double padLength = Math.pow(2, Math.ceil((int) Math.log((double) oldPCMLen) / Math.log(2)));
        padLength = padLength * 2;

        sampleData = new double[(int) padLength];
        // pad with 0's to get a multiple of 2 FFT data input

        //NORAMALIZATION
        int max = PCM.getMaxValue(oldPCM[channel]);

        for (int i = 0; i < oldPCMLen; i++) {
            //copy data over + normalization
            double x = oldPCM[channel][i];
            sampleData[i] = (double) x / (double) max;
            //System.out.println(sampleData[i]);
            //System.out.println(x);
        }


        //System.out.println(padLength + " : " + oldPCMLen);
        for (int i = (int) (padLength - oldPCMLen); i < padLength; i++) {
            //pad
            sampleData[i] = 0;
        }

        Complex[] data = new Complex[sampleData.length];


        //turn into complex number
        for (int i = 0; i < padLength; i++) {
            data[i] = new Complex(sampleData[i], 0);
            //System.out.println(pcmData[i]);
        }

        Complex[] res = fft.CooleyTukeyFFT(data);

        DecimalFormat df = new DecimalFormat("#.#");
        int p = 0;

        /*Arrays.sort(res, new Comparator<Complex>() {
            @Override
            public int compare(Complex o1, Complex o2) {
                //System.out.println(o1.imagniary + " " + o2.imagniary);
                return o1.compareTo(o2);
            }
        });*/


        //System.out.println("Padded Length: " + padLength);

        int avg = 2;
        for (int i = 0; i < padLength; i = i + avg) {
            int calc = 0;
            for (int f = 0; f < avg; f++) {
                if (i + f == padLength) {
                    break;
                }
                calc = (int) res[i + f].real;
            }
            calc = calc / avg;
            //System.out.println(a.real + " : " + a.imagniary);
            if (calc != 0) {
                series.add(i, calc);
            }
            p++;

        }

        dataSetXY.addSeries(series);

        return dataSetXY;
    }

    public XYSeriesCollection calculateExisting(byte[] micData) {

        XYSeriesCollection dataSetXY = new XYSeriesCollection();
        XYSeries series = new XYSeries("FFT");

        int[] newData = new int[micData.length];

        for(int i=0; i<micData.length; i++){
            newData[i] = micData[i];
        }

        int oldPCMLen = micData.length;

        double[] sampleData;

        double padLength = Math.pow(2, Math.ceil((int) Math.log((double) oldPCMLen) / Math.log(2)));
        padLength = padLength * 2;

        sampleData = new double[(int) padLength];
        // pad with 0's to get a multiple of 2 FFT data input

        //AUDIO FILTERING
        int max = PCM.getMaxValue(newData);


        for (int i = 0; i < oldPCMLen; i++) {
            //copy data over + normalization
            double x = newData[i];
            sampleData[i] = Math.pow(x, 2)/100;

        }


        System.out.println(padLength + " : " + oldPCMLen);
        for (int i = (int) (padLength - oldPCMLen); i < padLength; i++) {
            //pad
            sampleData[i] = 0;
        }

        Complex[] data = new Complex[sampleData.length];


        //turn into complex number
        for (int i = 0; i < padLength; i++) {
            data[i] = new Complex(sampleData[i], 0);
            //System.out.println(pcmData[i]);
        }



        Complex[] res = fft.CooleyTukeyFFT(data);

        DecimalFormat df = new DecimalFormat("#.#");
        int p = 0;

        /*Arrays.sort(res, new Comparator<Complex>() {
            @Override
            public int compare(Complex o1, Complex o2) {
                //System.out.println(o1.imagniary + " " + o2.imagniary);
                return o1.compareTo(o2);
            }
        });*/


        //System.out.println("Padded Length: " + padLength);

        int avg = 1;
        for (int i = 0; i < padLength; i = i + avg) {
            int calc = 0;
            for (int f = 0; f < avg; f++) {
                if (i + f == padLength) {
                    break;
                }
                calc = (int) res[i + f].real;
            }
            calc = calc / avg;
            //System.out.println(a.real + " : " + a.imagniary);
            if (calc > 0) {
                series.add(i/avg, calc);
            }
            p++;

        }



        //System.out.println(p);

        dataSetXY.addSeries(series);

        return dataSetXY;
    }
}
