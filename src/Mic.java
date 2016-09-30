import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;

/**
 * Created by Jody on 9/29/2016.
 */
public class Mic {
    int numBytesRead;
    int CHUNK_SIZE = 1024;
    byte[] data;
    DataLine.Info dataLineInfo;


    TargetDataLine microphone;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    public Mic(){
        createMic();
    }

    public byte[] getDataChunk(){
        microphone.start();
        getData(out);
        byte[] temp = out.toByteArray();
        out.reset();
        return temp;
    }

    public void createMic(){
        AudioFormat format = new AudioFormat(6 * 8000.0f, 16, 1, true, true);
        try {
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);

            System.out.println(microphone.getLineInfo());

            data = new byte[microphone.getBufferSize() / 5];

            dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void getData(ByteArrayOutputStream out){
        int bytesRead = 0;
        while (bytesRead < 3000) {
            numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
            bytesRead += numBytesRead;
            // write the mic data to a stream for use later
            out.write(data, 0, numBytesRead);
        }
    }
}
