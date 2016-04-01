package Sound;

import jAudioFeatureExtractor.ACE.DataTypes.Batch;
import jAudioFeatureExtractor.DataModel;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Analysis {

    public static void main(String[] args){
        /*args[0] = wav file
        * args[1] = features xml
        * args[2] = settings xml
        * args[3] = feature value xml
        * args[4] = feature def xml
        * args[5] = NN*/
        try {
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
        }

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
    }
}
