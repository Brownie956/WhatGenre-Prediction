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
import org.neuroph.util.data.sample.Sampling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ClassifierMLP {

    private MultiLayerPerceptron mlp;

    public ClassifierMLP(){
        this.mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,Conf.NOINPUTS,15,Conf.NOOUTPUTS);
    }

    public ClassifierMLP(MultiLayerPerceptron nnet){
        int numOfInputs = nnet.getInputsCount();
        int numOfOutputs = nnet.getOutputsCount();
        if(numOfOutputs != Conf.NOOUTPUTS){
            System.out.println("NN has " + numOfOutputs + " outputs");
            System.out.println("Required number of outputs: " + Conf.NOOUTPUTS);
        }
        else if(numOfInputs != Conf.NOINPUTS){
            System.out.println("NN has " + numOfInputs + " inputs");
            System.out.println("Required number of inputs: " + Conf.NOINPUTS);
        }
        else{
            this.mlp = nnet;
        }
    }

    public void train(DataSet trainingSet){

        //Select 90:10 (Training:Test)
        int totalNoOfRows = trainingSet.getRows().size();
        int noOfTestRows = totalNoOfRows / Conf.NOOFFOLDS;

        //Calculate combinations
        HashMap<DataSet, DataSet> trainTestCombos = new HashMap<DataSet, DataSet>();

        //For every fold, get the training:test combos
        for(int fold = 0; fold < Conf.NOOFFOLDS; fold++){
            //Get the starting index of test rows
            int startPoint = fold * noOfTestRows;
            DataSet trainSet = new DataSet(Conf.NOINPUTS, Conf.NOOUTPUTS);
            DataSet testSet = new DataSet(Conf.NOINPUTS, Conf.NOOUTPUTS);

            //Extract the test rows
            for(int i = 0; i < totalNoOfRows; i++){
                DataSetRow tempRow = trainingSet.getRowAt(i);
                if(i >= startPoint && i < startPoint + noOfTestRows){
                    //Test Row
                    testSet.addRow(tempRow);
                }
                else {
                    //Train Row
                    trainSet.addRow(tempRow);
                }
            }
            //Store combo
            trainTestCombos.put(trainSet, testSet);
        }

        //For every combo, train and get error
        try {
            File trainingData = new File(Conf.FOLDLOGPATH);
            PrintWriter logWriter = new PrintWriter(trainingData);
            double[] foldErrors = new double[trainTestCombos.size()];

            int i = 1;
            for(Map.Entry combo : trainTestCombos.entrySet()){
                mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,Conf.NOINPUTS,16,Conf.NOOUTPUTS);
                mlp.getLearningRule().addListener(new LearningListener());
                mlp.getLearningRule().setLearningRate(0.18);
                mlp.getLearningRule().setMaxIterations(20000);
                mlp.getLearningRule().setMaxError(0.001);
                mlp.learn((DataSet) combo.getKey());

                double error = testNetwork((DataSet) combo.getValue());
                foldErrors[i - 1] = error;
                logWriter.println("Fold " + i + " error: " + error);
                i++;
            }

            //Find average error of the folds
            double sum = 0;
            for(int fold = 0; fold < foldErrors.length; fold++){
                sum += foldErrors[fold];
            }
            double averageError = sum / foldErrors.length;
            logWriter.println("Overall error: " + averageError);
            logWriter.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        mlp.save(Conf.NNOUTPUTPATH);
        System.out.println("Done training.");
    }

    public double testNetwork(DataSet testSet){
        int noOfRows = testSet.getRows().size();
        double[] errorVals = new double[noOfRows];

        for(int rowIndex = 0; rowIndex < noOfRows; rowIndex++){
            //Calculate output
            DataSetRow testRow = testSet.getRowAt(rowIndex);
            mlp.setInput(testRow.getInput());
            mlp.calculate();

            //Calculate average output error
            double[] output = mlp.getOutput();
            double[] desiredOutput = testRow.getDesiredOutput();

            //Check for invalid output
            if(output.length != desiredOutput.length){
                System.out.println("Output number mismatch");
                System.out.println("Actual number: " + output.length);
                System.out.println("Required number: " + desiredOutput.length);
                return -1;
            }

            //Individual genre value errors
            double[] genreErrorVals = new double[output.length];
            for(int i = 0; i < output.length; i++){
                genreErrorVals[i] = Math.abs((output[i] - desiredOutput[i]) * 100);
            }

            //Calculate average
            double errorSum = 0;
            for(int i = 0; i < genreErrorVals.length; i++){
                errorSum += genreErrorVals[i];
            }
            errorVals[rowIndex] = errorSum / genreErrorVals.length;
        }

        //Calculate overall classification error
        double sum = 0;
        for(int i = 0; i < errorVals.length; i++){
            sum += errorVals[i];
        }
        return sum / errorVals.length;
    }

    public double[] classifyInstance(DataSetRow instance){
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

        return outputPercentages;
    }

    static class LearningListener implements LearningEventListener {

        public void handleLearningEvent(LearningEvent event) {
            BackPropagation bp = (BackPropagation) event.getSource();
            System.out.println("Current iteration: " + bp.getCurrentIteration());
            System.out.println("Error: " + bp.getTotalNetworkError());
        }

    }

    public String outputToString(double[] outputs){
        //Create string in the format: val1,val2,val3,...,valN
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < outputs.length; i++){
            sb.append(outputs[i]);
            if(i < outputs.length - 1){
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
