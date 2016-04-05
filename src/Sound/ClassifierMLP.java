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
import java.util.*;

public class ClassifierMLP {

    private MultiLayerPerceptron mlp;
    private static int learningFold = 1;

    public ClassifierMLP(){
        this.mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,Conf.NOINPUTS,Conf.NOOFHIDDENNEURONS,Conf.NOOUTPUTS);
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

    public void train(){
        try{
            File foldLog = new File(Conf.FOLDLOGPATH);
            PrintWriter logWriter = new PrintWriter(foldLog);
            double[] foldErrors = new double[Conf.NOOFFOLDS];
            //Get error values of each fold
            for(int i = 1; i <= Conf.NOOFFOLDS; i++){
                String trainingDataPath = Conf.getTrainingDataFoldPath(i);
                String testDataPath = Conf.getTestDataFoldPath(i);

                double foldError = train(trainingDataPath, testDataPath);
                foldErrors[i - 1] = foldError;
                logWriter.println("Fold " + i + " error: " + foldError);
                learningFold++;
            }

            //Get average error
            double averageError = Utils.mean(foldErrors);
            logWriter.println("Overall error: " + averageError);
            logWriter.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        mlp.save(Conf.NNOUTPUTPATH);
        System.out.println("Done training.");
    }

    public void train(HashMap<File[], File[]> trainTestCombos){
        try {
            File foldLog = new File(Conf.FOLDLOGPATH);
            PrintWriter logWriter = new PrintWriter(foldLog);

            //For every combo
            int totalCorrect = 0;
            int totalTested = 0;
            for (HashMap.Entry<File[], File[]> combo : trainTestCombos.entrySet()) {
                DataSet trainingDataSet = new DataSet(Conf.NOINPUTS, Conf.NOOUTPUTS);
                //Combine the training sets into one trainingDataSet
                for (File track : combo.getKey()) {
                    DataSet trackDataSet = DataSet.createFromFile(track.getAbsolutePath(), Conf.NOINPUTS, Conf.NOOUTPUTS, ",");
                    for (DataSetRow row : trackDataSet.getRows()) {
                        trainingDataSet.addRow(row);
                    }
                }

                //Train network
                mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, Conf.NOINPUTS, Conf.NOOFHIDDENNEURONS, Conf.NOOUTPUTS);
                mlp.getLearningRule().addListener(new LearningListener());
                mlp.getLearningRule().setLearningRate(0.14);
                mlp.getLearningRule().setMaxIterations(100);
                mlp.getLearningRule().setMaxError(0.004);
                mlp.learn(trainingDataSet);

                //Test network
                int correctlyPredicted = testNetwork(combo.getValue());
                totalCorrect += correctlyPredicted;
                logWriter.print("Fold " + learningFold + " correctly predicted: " + correctlyPredicted);
                logWriter.println(" / " + combo.getValue().length);
                learningFold++;
                totalTested += combo.getValue().length;
            }
            logWriter.println("Total correctly predicted: " + totalCorrect);
            logWriter.close();

            if(totalCorrect < 28){
                //Try again
                train(trainTestCombos);
            }
            else {
                mlp.save(Conf.NNOUTPUTPATH);
                System.out.println("Done training.");
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private double train(String trainingDataPath, String testDataPath){
        DataSet trainingData = DataSet.createFromFile(trainingDataPath, Conf.NOINPUTS, Conf.NOOUTPUTS, ",");
        DataSet testData = DataSet.createFromFile(testDataPath, Conf.NOINPUTS, Conf.NOOUTPUTS, ",");

        mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,Conf.NOINPUTS,Conf.NOOFHIDDENNEURONS,Conf.NOOUTPUTS);
        mlp.getLearningRule().addListener(new LearningListener());
        mlp.getLearningRule().setLearningRate(0.1);
        mlp.getLearningRule().setMaxIterations(250);
        mlp.getLearningRule().setMaxError(0.01);
        mlp.learn(trainingData);

        return testNetwork(testData);
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
                mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,Conf.NOINPUTS,Conf.NOOFHIDDENNEURONS,Conf.NOOUTPUTS);
                mlp.getLearningRule().addListener(new LearningListener());
                mlp.getLearningRule().setLearningRate(0.1);
                mlp.getLearningRule().setMaxIterations(200);
                mlp.getLearningRule().setMaxError(0.001);
                mlp.learn((DataSet) combo.getKey());

                learningFold++;

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

    public int testNetwork(File[] testFiles){
        int noOfCorrectPredictions = 0;
        for(File track : testFiles){
            ArrayList<Conf.Genre> predictions = new ArrayList<Conf.Genre>();
            String fileName = track.getName();
            String genreFromFileName = fileName.substring(fileName.lastIndexOf("-") + 1, fileName.indexOf("."));
            Conf.Genre fileGenre = Conf.Genre.UNKNOWN;
            try{
                fileGenre = Conf.Genre.valueOf(genreFromFileName.toUpperCase());
            }
            catch(IllegalArgumentException e){
                e.printStackTrace();
            }


            DataSet trackDataSet = DataSet.createFromFile(track.getAbsolutePath(), Conf.NOINPUTS, Conf.NOOUTPUTS, ",");
            for(DataSetRow row : trackDataSet.getRows()){
                double[] rowResult = classifyInstance(row);

                //Print prediction
                Conf.Genre prediction = makePrediction(rowResult);
                predictions.add(prediction);
//                System.out.println("Row prediction: " + prediction);
            }

            //Find the majority of predictions
            Conf.Genre audioTrackPrediction = findGenreMajority(predictions);
            if(audioTrackPrediction.equals(fileGenre)){
                //Correct
                noOfCorrectPredictions++;
            }
            System.out.println("Predicted Genre: " + audioTrackPrediction);
            System.out.println("Actual Genre: " + fileGenre);
        }
        return noOfCorrectPredictions;
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

    public Conf.Genre classifyInstance(File audioTrack){
        ArrayList<Conf.Genre> predictions = new ArrayList<Conf.Genre>();

        //Extract the values
        Object[] featVals = DataSetCreator.extract(new File[]{audioTrack});
        ArrayList<ArrayList<ArrayList<Double>>> dataSet = DataSetCreator.processToArrayList(featVals, false);

        //Should only be one track in the returned values
        if(dataSet.size() > 1){
            System.out.println("Error: More than 1 audio track data returned");
            return Conf.Genre.UNKNOWN;
        }

        ArrayList<ArrayList<Double>> trackDataSet = dataSet.get(0);

        //For every row in data set, get output
        for(ArrayList<Double> row : trackDataSet){
            DataSetRow dataSetRow = new DataSetRow(row);
            double[] rowResult = classifyInstance(dataSetRow);

            //Print prediction
            Conf.Genre prediction = makePrediction(rowResult);
            predictions.add(prediction);
            System.out.println("Row prediction: " + prediction);
        }

        //Find the majority of predictions
        Conf.Genre audioTrackPrediction = findGenreMajority(predictions);
        System.out.println(audioTrackPrediction);
        return audioTrackPrediction;
    }

    private Conf.Genre findGenreMajority(ArrayList<Conf.Genre> predictions){
        int[] genreTotals = new int[Conf.Genre.values().length];
        HashMap<Conf.Genre, Integer> genreCounts = new HashMap<Conf.Genre, Integer>(Conf.Genre.values().length);
        genreCounts.put(Conf.Genre.ROCK, 0);
        genreCounts.put(Conf.Genre.DANCE, 0);
        genreCounts.put(Conf.Genre.CLASSICAL, 0);
        genreCounts.put(Conf.Genre.REGGAE, 0);
        genreCounts.put(Conf.Genre.UNKNOWN, 0);


        //Tally up
        for(Conf.Genre genre : predictions){
            if(genre == Conf.Genre.ROCK){
                genreCounts.put(Conf.Genre.ROCK, genreCounts.get(Conf.Genre.ROCK) + 1);
            }
            else if(genre == Conf.Genre.DANCE){
                genreCounts.put(Conf.Genre.DANCE, genreCounts.get(Conf.Genre.DANCE) + 1);
            }
            else if(genre == Conf.Genre.CLASSICAL){
                genreCounts.put(Conf.Genre.CLASSICAL, genreCounts.get(Conf.Genre.CLASSICAL) + 1);
            }
            else if(genre == Conf.Genre.REGGAE){
                genreCounts.put(Conf.Genre.REGGAE, genreCounts.get(Conf.Genre.REGGAE) + 1);
            }
            else genreCounts.put(Conf.Genre.UNKNOWN, genreCounts.get(Conf.Genre.UNKNOWN) + 1);
        }

        //Is there a genre majority
        Conf.Genre genrePrediction = Conf.Genre.UNKNOWN;
        for(HashMap.Entry<Conf.Genre, Integer> entry : genreCounts.entrySet()){
            if(entry.getValue().equals(genreCounts.get(genrePrediction))){
                genrePrediction = Conf.Genre.UNKNOWN;
            }
            else if(entry.getValue() > genreCounts.get(genrePrediction)){
                genrePrediction = entry.getKey();
            }
        }

        return genrePrediction;
    }

    private Conf.Genre makePrediction(double[] results){
        Conf.Genre predictedGenre = Conf.Genre.UNKNOWN;
        for(int i = 0; i < results.length; i++){
            if(results[i] > Conf.GENRETHRESHOLD){
                //Predicted a genre
                switch (i){
                    case 0:
                        predictedGenre = Conf.Genre.ROCK;
                        break;
                    case 1:
                        predictedGenre = Conf.Genre.DANCE;
                        break;
                    case 2:
                        predictedGenre = Conf.Genre.CLASSICAL;
                        break;
                    case 3:
                        predictedGenre = Conf.Genre.REGGAE;
                        break;
                    default:
                        predictedGenre = Conf.Genre.UNKNOWN;
                }
                break;
            }
        }
        return predictedGenre;
    }

    public int attemptClassify(DataSet testSet){
        //Returns number of correctly classified
        int numCorrect = 0;

        for(int i = 0; i < testSet.size(); i++){
            double[] rowRes = classifyInstance(testSet.getRowAt(i));
            double[] expected = testSet.getRowAt(i).getDesiredOutput();

            //Find index of predicted and correct genre
            int predictedGenreIndex = findIndexOfLargest(rowRes);
            int correctGenreIndex = findIndexOfLargest(expected);

            if(predictedGenreIndex == correctGenreIndex) numCorrect++;
        }

        return numCorrect;
    }

    private int findIndexOfLargest(double[] results){
        int indexOfLargest = 0;
        try{
            for(int i = 0; i < results.length; i++){
                if(results[i] > results[indexOfLargest]){
                    indexOfLargest = i;
                }
            }
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("Empty results array");
            return -1;
        }
        return indexOfLargest;
    }

    static class LearningListener implements LearningEventListener {

        public void handleLearningEvent(LearningEvent event) {
            BackPropagation bp = (BackPropagation) event.getSource();
            System.out.println("Current iteration: " + bp.getCurrentIteration() + " Fold: " + learningFold);
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
