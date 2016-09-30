import org.jfree.data.category.DefaultCategoryDataset;

import java.text.DecimalFormat;

/**
 * Created by Jody on 9/27/2016.
 */
public class FFT {

    public FFT(){

    }

    public Complex polar(double rho, double theta){
        return new Complex(rho * Math.cos(theta), rho * Math.sin(theta));
    }

    public Complex[] CooleyTukeyFFT(Complex[] dataInput){ // Divide & Conquer method
        int N = dataInput.length;
        if (N<=1) return dataInput; // Best Case

        Complex[] odd = new Complex[(N/2) + (N%2)];
        Complex[] even = new Complex[(N/2)];

        int oddCount = 0;
        int evenCount = 0;
        //Divide

        for(int i=0; i<N; i++){
            if(i%2==0) {
                //even
                even[evenCount] = dataInput[i];
                evenCount++;
            }else{
                //odd
                odd[oddCount] = dataInput[i];
                oddCount++;
            }
        }

        // Conquer
        CooleyTukeyFFT(even);
        CooleyTukeyFFT(odd);



        // Combine
        for(int k = 0; k < N/2; ++k){
            Complex t = polar(1.0, -2 * Math.PI * k / N);
            //System.out.println(odd[k]);
            t = t.prod(odd[k]);
            dataInput[k] = even[k].sum(t);
            dataInput[k+N/2] = even[k].dif(t);
        }


        return dataInput;
    }

}

class Complex {
    public double real;
    public double imagniary;

    public Complex(double _real, double _imaginary) {
        real = _real;
        imagniary = _imaginary;
    }

    public Complex sum(Complex b){
        return new Complex(this.real + b.real, this.imagniary + b.imagniary);
    }
    public Complex dif(Complex b){
        return new Complex(this.real - b.real, this.imagniary - b.imagniary);
    }
    public Complex prod(Complex b) {
        return new Complex(this.real * b.real - this.imagniary * b.imagniary, this.real * b.imagniary + this.imagniary * b.real);
    }
    public static Complex expi(double i) {
        return new Complex(Math.cos(i),Math.sin(i));
    }
    public int compareTo(Complex b){
        if(this.imagniary > b.imagniary) {
            return 1;
        }
        else if(this.imagniary < b.imagniary){
            return -1;
        }
        return 0;
    }
}