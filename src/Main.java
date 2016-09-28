/**
 * Created by Jody on 9/27/2016.
 */
public class Main {
    public static void main(String[] args){
        FFT fft = new FFT();

        Complex[] data = new Complex[8];

        data[0] = new Complex(1,0);
        data[1] = new Complex(1,0);
        data[2] = new Complex(1,0);
        data[3] = new Complex(1,0);
        data[4] = new Complex(0,0);
        data[5] = new Complex(0,0);
        data[6] = new Complex(0,0);
        data[7] = new Complex(0,0);



        Complex[] res = fft.CooleyTukeyFFT(data);

        for(Complex a : res){
            System.out.println(a.real + " : " + a.imagniary);
        }

    }
}
