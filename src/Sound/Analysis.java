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
            ClassifierMLP nnet = new ClassifierMLP((MultiLayerPerceptron) MultiLayerPerceptron.createFromFile(nn));
            System.out.print(nnet.classifyInstance(testRow));
        }
        catch (Exception e){
            e.printStackTrace();
        }*/

/*        Training.createTrainingSet();*/
        DataSet trainingSet = DataSet.createFromFile(Conf.TRAININGDATAPATH,Conf.NOINPUTS,Conf.NOOUTPUTS,",");
        ClassifierMLP nnet = new ClassifierMLP();
        nnet.train(trainingSet);
    }
}
