/*Author: Chris Brown
* Date: 17/03/2016
* Description: Class used for creating a training set*/
package Sound;

import jAudioFeatureExtractor.ACE.DataTypes.Batch;
import jAudioFeatureExtractor.DataModel;

import java.io.*;
import java.util.HashMap;

public class Training {

    public Training(){}

    private enum Genre {
        ROCK, DANCE, CLASSICAL, REGGAE
    }

    private static HashMap<File, Genre> getRecordings(){
        HashMap<File, Genre> recordings = new HashMap<File, Genre>();
        final File folder = new File(Conf.TRAININGDATADIRPATH);
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
            File trainingData = new File(Conf.TRAININGDATAPATH);
            PrintWriter writer = new PrintWriter(trainingData);

            int trainIndex = 0;
            for(File rec : recordings.keySet()){
                System.out.println("Recording " + (trainIndex + 1) + " of " + recordings.size());

                Batch batch = new Batch(Conf.FEATURESPATH, null);
                batch.setRecordings(new File[]{rec});
                batch.setSettings(Conf.SETTINGSPATH);

                DataModel dm = batch.getDataModel();
                OutputStream defSavePath = new FileOutputStream(Conf.FKOUTPUTPATH);
                OutputStream valSavePath = new FileOutputStream(Conf.FVOUTPUTPATH);
                dm.featureKey = defSavePath;
                dm.featureValue = valSavePath;
                batch.setDataModel(dm);

                batch.execute();

                double[][][] res = batch.getResults();

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
