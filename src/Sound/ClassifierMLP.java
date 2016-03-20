/*Author: Chris Brown
* Date: 17/03/2016
* Description: Multi Layer Perceptron classifier class. Creates NN, trains and classifies instances*/
package Sound;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.ConvolutionalBackpropagation;
import org.neuroph.nnet.learning.DynamicBackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

public class ClassifierMLP {

    private MultiLayerPerceptron mlp;
    private int numOfInputs = 39;
    private int numOfOutputs = 4;

    public ClassifierMLP(){
        this.mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,numOfInputs,20,10,numOfOutputs);
    }

    public ClassifierMLP(MultiLayerPerceptron nnet){
        int numOfInputs = nnet.getInputsCount();
        int numOfOutputs = nnet.getOutputsCount();
        if(numOfOutputs != this.numOfOutputs){
            System.out.println("NN has " + numOfOutputs + " outputs");
            System.out.println("Required number of outputs: " + this.numOfOutputs);
        }
        else if(numOfInputs != this.numOfInputs){
            System.out.println("NN has " + numOfInputs + " inputs");
            System.out.println("Required number of inputs: " + this.numOfInputs);
        }
        else{
            this.mlp = nnet;
        }
    }

    public void train(DataSet trainingSet){

        mlp.getLearningRule().addListener(new LearningListener());
        mlp.getLearningRule().setLearningRate(0.01);
        mlp.getLearningRule().setMaxIterations(20000);
        mlp.getLearningRule().setMaxError(0.0001);
        mlp.learn(trainingSet);
        mlp.save(Conf.RESOURCESPATH + "genremfccNet.nnet");
        System.out.println("Done training.");
        System.out.println("Testing network...");
    }

    public String classifyInstance(DataSetRow instance){
        mlp.setInput(instance.getInput());
        mlp.calculate();
        double[] output = mlp.getOutput();

        //Get total output
        double totalOutput = 0;
        for(int i = 0; i < output.length; i++){
            totalOutput += output[i];
        }

        //Calculate percentages
        double[] outputPercentages = new double[output.length];
        for(int i = 0; i < outputPercentages.length; i++){
            outputPercentages[i] = Utils.round(output[i] / totalOutput * 100, 2);
        }

        return outputToString(outputPercentages);
    }

    static class LearningListener implements LearningEventListener {

        public void handleLearningEvent(LearningEvent event) {
            BackPropagation bp = (BackPropagation) event.getSource();
            System.out.println("Current iteration: " + bp.getCurrentIteration());
            System.out.println("Error: " + bp.getTotalNetworkError());
        }

    }

    private String outputToString(double[] outputs){
        //Create string in the format: val1,val2,val3,...,valN
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < outputs.length; i++){
            sb.append(outputs[i]);
            if(i < outputs.length - 1){
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
