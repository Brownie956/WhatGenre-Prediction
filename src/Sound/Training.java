/*Author: Chris Brown
* Date: 17/03/2016
* Description: Class used for creating a training set*/
package Sound;

import jAudioFeatureExtractor.ACE.DataTypes.Batch;
import jAudioFeatureExtractor.DataModel;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Training {

    public Training(){}

    private enum Genre {
        ROCK, DANCE, CLASSICAL, REGGAE
    }

    private static HashMap<File, Genre> getRecordings(){
        HashMap<File, Genre> recordings = new HashMap<File, Genre>();
        final File folder = new File(Conf.TRAININGDATAPATH);
        if(folder.isDirectory()){
            for(File track : folder.listFiles()) {
                String fullName = track.getName();
                String fileGenre = fullName.substring(fullName.lastIndexOf("-") + 1, fullName.indexOf("."));
                try{
                    Genre genre = Genre.valueOf(fileGenre.toUpperCase());
                    recordings.put(track, genre);
                }
                catch (IllegalArgumentException e){
                    System.out.println("Unknown genre for " + fullName);
                }
            }
        }
        return recordings;
    }

    public static void createTrainingSet(){
        HashMap<File, Genre> recordings = getRecordings();

        try{
            File trainingData = new File(Conf.RESOURCESPATH, "trainingdatamfcc2.csv");
            PrintWriter writer = new PrintWriter(trainingData);

            int trainIndex = 0;
            for(File rec : recordings.keySet()){
                Batch batch = new Batch(Conf.featureFile, null);
                batch.setRecordings(new File[]{rec});
                batch.getAggregator();
                batch.setSettings(Conf.settingsmfccFile);

                DataModel dm = batch.getDataModel();
                OutputStream valsavepath = new FileOutputStream(Conf.FVOuputFile);
                OutputStream defsavepath = new FileOutputStream(Conf.FKOuputFile);
                dm.featureKey = defsavepath;
                dm.featureValue = valsavepath;
                batch.setDataModel(dm);

                batch.execute();

                double[][][] res = batch.getResults();
                //TODO remove this
                System.out.println("Recording " + (trainIndex + 1) + " of " + recordings.size());
/*
                for(int i = 0;  i < res.length; i++){
                    for(int j = 0;  j < res[i].length; j++){
                        for(int k = 0;  k < res[i][j].length; k++){
                            System.out.println("Result: " + res[i][j][k]);
                        }
                    }
                }
*/

/*                for(int i = 0;  i < res.length; i++){
                    if(trainIndex != 0) writer.print("\r\n");
                    for(int j = 0;  j < res[i].length; j++){
                        if(j != 0) writer.print(",");
                        for(int k = 0;  k < res[i][j].length; k++){
                            writer.print(res[i][j][k]);
                        }
                    }
                }*/

                for(int i = 0;  i < res.length; i++){
                    for(int j = 0;  j < res[i].length; j++){
                        for(int k = 0;  k < res[i][j].length; k++){
                            if(k % 4 != 0) writer.print(res[i][j][k] + ",");
                        }
                    }
                }

                //Append the genre
                Genre trackGenre = recordings.get(rec);
                switch (trackGenre){
                    case ROCK:
                        writer.print("1,0,0,0");
                        break;
                    case DANCE:
                        writer.print("0,1,0,0");
                        break;
                    case CLASSICAL:
                        writer.print("0,0,1,0");
                        break;
                    case REGGAE:
                        writer.print("0,0,0,1");
                        break;
                }
                writer.print("\r\n");
                trainIndex++;
            }
            writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
