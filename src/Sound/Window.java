/*Author: Chris Brown
* Date: 1/04/2016
* Description: A class to represent a window of audio*/
package Sound;

public class Window {
    private double[][] featureValues;

    public Window(double[][] featureValues){
        this.featureValues = featureValues;
    }

    public double[][] getFeatureValues() {
        return featureValues;
    }
}
