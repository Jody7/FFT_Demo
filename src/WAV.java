import sun.audio.AudioStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;

/**
 * Created by Jody on 9/29/2016.
 */
public class WAV {
    File file;
    AudioInputStream audioInputStream;
    int bytesPerFrame;

    int numBytes;
    byte[] audioBytes;

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    public WAV(String fileName){
        file = new File(fileName);
        try {
            audioInputStream = AudioSystem.getAudioInputStream(file);
            bytesPerFrame = audioInputStream.getFormat().getFrameSize();

            numBytes = 1024 * bytesPerFrame;
            audioBytes = new byte[numBytes];

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public byte[] getDataChunk(int byteSecond) {
        byte[] temp = new byte[2048];
        try {
            if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
                bytesPerFrame = 1;
            }

            //audioInputStream.read(audioBytes);
            audioInputStream.read(audioBytes, 0, byteSecond);

            System.arraycopy(audioBytes, 0, temp, 0, byteSecond);

            System.out.println("------ = = =" + audioBytes.length);


            /*
            while (((numBytesRead = audioInputStream.read(audioBytes)) != -1) && (numBytesRead >= byteSecond)) {
                System.out.println(numBytesRead);
                temp = audioBytes;
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
}
