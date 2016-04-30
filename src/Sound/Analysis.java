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
        try {
            File track = new File(args[0]);

            Batch batch = new Batch(args[1], null);
            batch.setRecordings(new File[]{track});
            batch.getAggregator();
            batch.setSettings(args[2]);

            DataModel dm = batch.getDataModel();
            OutputStream valsavepath = new FileOutputStream(args[3]);
            OutputStream defsavepath = new FileOutputStream(args[4]);
            dm.featureKey = defsavepath;
            dm.featureValue = valsavepath;
            batch.setDataModel(dm);

            String nn = args[5];
            ClassifierMLP nnetClassifier = new ClassifierMLP((MultiLayerPerceptron) MultiLayerPerceptron.createFromFile(nn));
            nnetClassifier.classifyInstance(track, batch, args[3]);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        /*--------------------------------
        * SINGLE/MULTI INSTANCE TEST
        *
        * INPUT: WAV FILE
        * OUTPUT: GENRE
        * -------------------------------*/

/*        ClassifierMLP mlp = new ClassifierMLP((MultiLayerPerceptron) MultiLayerPerceptron.createFromFile(Conf.NNOUTPUTPATH));
        File[] files = DataSetCreator.getFilesInDir(Conf.RESOURCESPATH + "temp-dir/wav");
        mlp.classifyInstance(files[0]);*/

        /*--------------------------------
        * TEST NETWORK
        *
        * INPUT: CSV FILES
        * OUTPUT: NUMBER OF CORRECT PREDICTIONS
        * -------------------------------*/

/*        File[] rockFiles = DataSetCreator.getFilesInDir(Conf.getTECsvDir(Conf.Genre.ROCK));
        int rockRes = mlp.testNetwork(rockFiles);
        System.out.println(rockRes);*/
        //run all tests before storing the confusion matrix
        //...
        //mlp.storecMatrix(Conf.CMATRIXPATH);
    }
}
