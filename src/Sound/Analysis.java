package Sound;

import jAudioFeatureExtractor.ACE.DataTypes.Batch;
import jAudioFeatureExtractor.DataModel;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

public class Analysis {

    public static void main(String[] args){
        /*args[0] = wav file
        * args[1] = features xml
        * args[2] = settings xml
        * args[3] = feature value xml
        * args[4] = feature def xml
        * args[5] = NN*/
/*        try {
            Batch batch = new Batch(args[1], null);
            batch.setRecordings(new File[]{new File(args[0])});
            batch.getAggregator();
            batch.setSettings(args[2]);

            DataModel dm = batch.getDataModel();
            OutputStream valsavepath = new FileOutputStream(args[3]);
            OutputStream defsavepath = new FileOutputStream(args[4]);
            dm.featureKey = defsavepath;
            dm.featureValue = valsavepath;
            batch.setDataModel(dm);

            batch.execute();

            double[][][] res = batch.getResults();
            ArrayList<Double> vals = new ArrayList<Double>();
            for(int i = 0;  i < res.length; i++){
                for(int j = 0;  j < res[i].length; j++){
                    for(int k = 0;  k < res[i][j].length; k++){
                        if(k % 4 != 0) vals.add(res[i][j][k]);
                    }
                }
            }

            String nn = args[5];
            DataSetRow testRow = new DataSetRow(vals);
            ClassifierMLP nnetClassifier = new ClassifierMLP((MultiLayerPerceptron) MultiLayerPerceptron.createFromFile(nn));
            System.out.print(nnetClassifier.outputToString(nnetClassifier.classifyInstance(testRow)));
        }
        catch (Exception e){
            e.printStackTrace();
        }*/

/*        DataSetCreator.createDataSet(Conf.TRAININGDATADIRPATH, Conf.TRAININGDATAPATH);*/
/*        DataSet trainingSet = DataSet.createFromFile(Conf.TRAININGDATAPATH,Conf.NOINPUTS,Conf.NOOUTPUTS,",");
        ClassifierMLP nnet = new ClassifierMLP();
        nnet.train(trainingSet);*/
//        DataSetCreator.createDataSet(Conf.TESTROCKDATADIRPATH, Conf.TESTROCKDATAPATH);
//        DataSetCreator.createDataSet(Conf.TESTDANCEDATADIRPATH, Conf.TESTDANCEDATAPATH);
//        DataSetCreator.createDataSet(Conf.TESTCLASSICALDATADIRPATH, Conf.TESTCLASSICALDATAPATH);
//        DataSetCreator.createDataSet(Conf.TESTREGGAEDATADIRPATH, Conf.TESTREGGAEDATAPATH);

//        DataSet rockTestSet = DataSet.createFromFile(Conf.TESTROCKDATAPATH,Conf.NOINPUTS,Conf.NOOUTPUTS,",");
//        DataSet danceTestSet = DataSet.createFromFile(Conf.TESTDANCEDATAPATH,Conf.NOINPUTS,Conf.NOOUTPUTS,",");
//        DataSet classicalTestSet = DataSet.createFromFile(Conf.TESTCLASSICALDATAPATH,Conf.NOINPUTS,Conf.NOOUTPUTS,",");
//        DataSet reggaeTestSet = DataSet.createFromFile(Conf.TESTREGGAEDATAPATH,Conf.NOINPUTS,Conf.NOOUTPUTS,",");
//        ClassifierMLP mlp = new ClassifierMLP((MultiLayerPerceptron) MultiLayerPerceptron.createFromFile(Conf.NNOUTPUTPATH));
//
//        double rockTestError = mlp.testNetwork(rockTestSet);
//        double danceTestError = mlp.testNetwork(danceTestSet);
//        double classicalTestError = mlp.testNetwork(classicalTestSet);
//        double reggaeTestError = mlp.testNetwork(reggaeTestSet);
//        double overallTestError = Utils.mean(rockTestError, danceTestError, classicalTestError, reggaeTestError);
//
//        System.out.println("Rock test error: " + rockTestError);
//        System.out.println("Dance test error: " + danceTestError);
//        System.out.println("Classical test error: " + classicalTestError);
//        System.out.println("Reggae test error: " + reggaeTestError);
//        System.out.println("Overall test error: " + overallTestError);

//        int rockTestCorrect = mlp.attemptClassify(rockTestSet);
//        int danceTestCorrect = mlp.attemptClassify(danceTestSet);
//        int classicalTestCorrect = mlp.attemptClassify(classicalTestSet);
//        int reggaeTestCorrect = mlp.attemptClassify(reggaeTestSet);
//        double overallCorrect = Utils.mean(rockTestCorrect, danceTestCorrect, classicalTestCorrect, reggaeTestCorrect);
//
//        System.out.println("Rock test correct: " + rockTestCorrect);
//        System.out.println("Dance test correct: " + danceTestCorrect);
//        System.out.println("Classical test correct: " + classicalTestCorrect);
//        System.out.println("Reggae test correct: " + reggaeTestCorrect);
//        System.out.println("Overall correct: " + overallCorrect);

//        DataSetCreator.createDataSetPerWindow(Conf.TRAININGDATADIRPATH);
//        DataSet trainingSet = DataSet.createFromFile(Conf.TRAININGDATATEMPPATH,Conf.NOINPUTS,Conf.NOOUTPUTS,",");
//        ClassifierMLP nnet = new ClassifierMLP((MultiLayerPerceptron) MultiLayerPerceptron.createFromFile(Conf.NNOUTPUTPATH));
//        nnet.train();

//        File file = new File("res/testmusic/Avicii-HeyBrother.wav");
//        nnet.classifyInstance(file);

//        DataSetCreator.createTrainingMusicCSVFiles(Conf.TESTREGGAEDATADIRPATH);
/*        File[] csvFiles = DataSetCreator.getFilesInDir(Conf.TRAININGDATAOUTPUTDIRPATH);
        HashMap<File[], File[]> trainTestMap = DataSetCreator.getTrainTestCombos(csvFiles);
        ClassifierMLP mlp = new ClassifierMLP();
        mlp.train(trainTestMap);*/
//        ClassifierMLP mlp = new ClassifierMLP((MultiLayerPerceptron) MultiLayerPerceptron.createFromFile(Conf.NNOUTPUTPATH));
//        File[] rockFiles = DataSetCreator.getFilesInDir(Conf.TESTROCKDATACSVDIRPATH);
//        File[] danceFiles = DataSetCreator.getFilesInDir(Conf.TESTDANCEDATACSVDIRPATH);
//        File[] classicalFiles = DataSetCreator.getFilesInDir(Conf.TESTCLASSICALDATACSVDIRPATH);
//        File[] reggaeFiles = DataSetCreator.getFilesInDir(Conf.TESTREGGAEDATACSVDIRPATH);
//        File[] bohemian = DataSetCreator.getFilesInDir(Conf.RESOURCESPATH + "temp-dir/");
//        int beheClassified = mlp.testNetwork(bohemian);
//        int rockClassified = mlp.testNetwork(rockFiles);
//        int danceClassified = mlp.testNetwork(danceFiles);
//        int classicalClassified = mlp.testNetwork(classicalFiles);
//        int reggaeClassified = mlp.testNetwork(reggaeFiles);
//        int totalClassified = rockClassified + danceClassified + classicalClassified + reggaeClassified;
//
//        System.out.println("Total test rock correct: " + rockClassified);
//        System.out.println("Total test dance correct: " + danceClassified);
//        System.out.println("Total test classical correct: " + classicalClassified);
//        System.out.println("Total test reggae correct: " + reggaeClassified);
//        System.out.println("Total correct: " + totalClassified);
//        DataSetCreator.createTrainingMusicCSVFiles(Conf.getTRWavDir(Conf.Genre.ROCK));
//        DataSetCreator.createTrainingMusicCSVFiles(Conf.getTRWavDir(Conf.Genre.DANCE));
//        DataSetCreator.createTrainingMusicCSVFiles(Conf.getTRWavDir(Conf.Genre.CLASSICAL));
//        DataSetCreator.createTrainingMusicCSVFiles(Conf.getTRWavDir(Conf.Genre.REGGAE));

//        DataSetCreator.createTrainingMusicCSVFiles(Conf.getTEWavDir(Conf.Genre.ROCK));
//        DataSetCreator.createTrainingMusicCSVFiles(Conf.getTEWavDir(Conf.Genre.DANCE));
//        DataSetCreator.createTrainingMusicCSVFiles(Conf.getTEWavDir(Conf.Genre.CLASSICAL));
//        DataSetCreator.createTrainingMusicCSVFiles(Conf.getTEWavDir(Conf.Genre.REGGAE));

/*        ArrayList<String> rockFileNames = DataSetCreator.getFileNamesInDir(Conf.getTROCsvDir(Conf.Genre.ROCK), true);
        ArrayList<String> danceFileNames = DataSetCreator.getFileNamesInDir(Conf.getTROCsvDir(Conf.Genre.DANCE), true);
        ArrayList<String> classicalFileNames = DataSetCreator.getFileNamesInDir(Conf.getTROCsvDir(Conf.Genre.CLASSICAL), true);
        ArrayList<String> reggaeFileNames = DataSetCreator.getFileNamesInDir(Conf.getTROCsvDir(Conf.Genre.REGGAE), true);
        ArrayList<String> allFileNames = new ArrayList<String>();
        allFileNames.addAll(rockFileNames);
        allFileNames.addAll(danceFileNames);
        allFileNames.addAll(classicalFileNames);
        allFileNames.addAll(reggaeFileNames);

        Collections.shuffle(allFileNames);

        HashMap<ArrayList<String>, ArrayList<String>> trainTestCombos = DataSetCreator.getTrainTestCombos(allFileNames);
        ClassifierMLP mlp = new ClassifierMLP();
        mlp.trainByStrings(trainTestCombos);*/

/*        File[] rockFiles = DataSetCreator.getFilesInDir(Conf.getTRCsvDir(Conf.Genre.ROCK));
        File[] danceFiles = DataSetCreator.getFilesInDir(Conf.getTRCsvDir(Conf.Genre.DANCE));
        File[] classicalFiles = DataSetCreator.getFilesInDir(Conf.getTRCsvDir(Conf.Genre.CLASSICAL));
        File[] reggaeFiles = DataSetCreator.getFilesInDir(Conf.getTRCsvDir(Conf.Genre.REGGAE));
        File[] allFiles = new File[rockFiles.length + danceFiles.length + classicalFiles.length + reggaeFiles.length];

        int allFilesIndex = 0;
        for(File file : rockFiles){
            allFiles[allFilesIndex] = file;
            allFilesIndex++;
        }
        for(File file : danceFiles){
            allFiles[allFilesIndex] = file;
            allFilesIndex++;
        }
        for(File file : classicalFiles){
            allFiles[allFilesIndex] = file;
            allFilesIndex++;
        }
        for(File file : reggaeFiles){
            allFiles[allFilesIndex] = file;
            allFilesIndex++;
        }

        List filesList = Arrays.asList(allFiles);
        Collections.shuffle(filesList);
        allFiles = (File[]) filesList.toArray(new File[filesList.size()]);

        HashMap<File[], File[]> trainTest = DataSetCreator.getTrainTestCombos(allFiles);
        ClassifierMLP mlp = new ClassifierMLP();
        mlp.train(trainTest);*/

        File[] rockFiles = DataSetCreator.getFilesInDir(Conf.getTECsvDir(Conf.Genre.ROCK));
        File[] danceFiles = DataSetCreator.getFilesInDir(Conf.getTECsvDir(Conf.Genre.DANCE));
        File[] classicalFiles = DataSetCreator.getFilesInDir(Conf.getTECsvDir(Conf.Genre.CLASSICAL));
        File[] reggaeFiles = DataSetCreator.getFilesInDir(Conf.getTECsvDir(Conf.Genre.REGGAE));

        ClassifierMLP mlp = new ClassifierMLP((MultiLayerPerceptron) MultiLayerPerceptron.createFromFile(Conf.NNOUTPUTPATH));
        int rockRes = mlp.testNetwork(rockFiles);
        int danceRes = mlp.testNetwork(danceFiles);
        int classicalRes = mlp.testNetwork(classicalFiles);
        int reggaeRes = mlp.testNetwork(reggaeFiles);
        System.out.println(rockRes);
        System.out.println(danceRes);
        System.out.println(classicalRes);
        System.out.println(reggaeRes);
        mlp.storecMatrix(Conf.CMATRIXPATH);
    }
}
