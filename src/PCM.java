import com.sun.media.sound.WaveFileReader;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by Jody on 9/28/2016.
 */
public class PCM {
    public static int getMaxValue(int[] array) {
        int maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
            }
        }
        return maxValue;
    }

    // getting the miniumum value
    public static int getMinValue(int[] array) {
        int minValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
            }
        }
        return minValue;
    }

    public static int[][] PCMtoArray(String path) {
        File file = new File(path);
        byte[] bytes;
        int[][] data;
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            bytes = new byte[(int) (ais.getFrameLength()) * (ais.getFormat().getFrameSize())];
            ais.read(bytes);

            int nbChannels = ais.getFormat().getChannels();
            int index = 0;

            data = new int[nbChannels][bytes.length / (2 * nbChannels)];

            for (int audioByte = 0; audioByte < bytes.length;)
            {
                for (int channel = 0; channel < nbChannels; channel++)
                {
                    // Do the byte to sample conversion.
                    int low = (int) bytes[audioByte];
                    audioByte++;
                    int high = (int) bytes[audioByte];
                    audioByte++;
                    int sample = (high << 8) + (low & 0x00ff);

                    //System.out.println(sample);

                    data[channel][index] = sample;
                }
                index++;
            }

            return data;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
