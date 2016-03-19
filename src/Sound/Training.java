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

    public static void createTrainingSet(){
        String trainMusic1 = Conf.TRAININGDATAPATH + "ACDC-HighwayToHell.wav";
        String trainMusic2 = Conf.TRAININGDATAPATH + "Avicii-Levels.wav";
        String trainMusic3 = Conf.TRAININGDATAPATH + "Beethoven-FurElise.wav";
        String trainMusic4 = Conf.TRAININGDATAPATH + "Beethoven-MoonlightPianoSonata.wav";
        String trainMusic5 = Conf.TRAININGDATAPATH + "BobMarley-ThreeLittleBirds.wav";
        String trainMusic6 = Conf.TRAININGDATAPATH + "Darude-Sandstorm.wav";
        String trainMusic7 = Conf.TRAININGDATAPATH + "DennisBrown-Revolution.wav";
        String trainMusic8 = Conf.TRAININGDATAPATH + "DonCarlos-MrSun.wav";
        String trainMusic9 = Conf.TRAININGDATAPATH + "FooFighters-ThePretender.wav";
        String trainMusic10 = Conf.TRAININGDATAPATH + "GunsNRoses-WelcomeToTheJungle.wav";
        String trainMusic11 = Conf.TRAININGDATAPATH + "JohannSebastianBach-Air.wav";
        String trainMusic12 = Conf.TRAININGDATAPATH + "JohnHolt-PoliceInHelicopter.wav";
        String trainMusic13 = Conf.TRAININGDATAPATH + "Mozart-StringSerenade.wav";
        String trainMusic14 = Conf.TRAININGDATAPATH + "MrProbz-Waves.wav";
        String trainMusic15 = Conf.TRAININGDATAPATH + "PhilipWesley-TheApproachingNight.wav";
        String trainMusic16 = Conf.TRAININGDATAPATH + "Sigala-EasyLove.wav";
        String trainMusic17 = Conf.TRAININGDATAPATH + "SylfordWalker-BurnBabylon.wav";
        String trainMusic18 = Conf.TRAININGDATAPATH + "Tiesto-AdagioForStrings.wav";
        String trainMusic19 = Conf.TRAININGDATAPATH + "Wolfmother-Woman.wav";
        String trainMusic20 = Conf.TRAININGDATAPATH + "Zedd-Clarity.wav";
        String trainMusic21 = Conf.TRAININGDATAPATH + "ZZTop-LaGrange.wav";

        LinkedHashMap<File, Genre> recordingz = new LinkedHashMap<File, Genre>();
        recordingz.put(new File(trainMusic1), Genre.ROCK);
        recordingz.put(new File(trainMusic2), Genre.DANCE);
        recordingz.put(new File(trainMusic3), Genre.CLASSICAL);
        recordingz.put(new File(trainMusic4), Genre.CLASSICAL);
        recordingz.put(new File(trainMusic5), Genre.REGGAE);
        recordingz.put(new File(trainMusic6), Genre.DANCE);
        recordingz.put(new File(trainMusic7), Genre.REGGAE);
        recordingz.put(new File(trainMusic8), Genre.REGGAE);
        recordingz.put(new File(trainMusic9), Genre.ROCK);
        recordingz.put(new File(trainMusic10), Genre.ROCK);
        recordingz.put(new File(trainMusic11), Genre.CLASSICAL);
        recordingz.put(new File(trainMusic12), Genre.REGGAE);
        recordingz.put(new File(trainMusic13), Genre.CLASSICAL);
        recordingz.put(new File(trainMusic14), Genre.DANCE);
        recordingz.put(new File(trainMusic15), Genre.CLASSICAL);
        recordingz.put(new File(trainMusic16), Genre.DANCE);
        recordingz.put(new File(trainMusic17), Genre.REGGAE);
        recordingz.put(new File(trainMusic18), Genre.DANCE);
        recordingz.put(new File(trainMusic19), Genre.ROCK);
        recordingz.put(new File(trainMusic20), Genre.DANCE);
        recordingz.put(new File(trainMusic21), Genre.ROCK);

        try{
            File trainingData = new File(Conf.RESOURCESPATH, "trainingdatamfcc.csv");
            PrintWriter writer = new PrintWriter(trainingData);

            int trainIndex = 0;
            for(File rec : recordingz.keySet()){
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
                for(int i = 0;  i < res.length; i++){
                    for(int j = 0;  j < res[i].length; j++){
                        for(int k = 0;  k < res[i][j].length; k++){
                            System.out.println("Result: " + res[i][j][k]);
                        }
                    }
                }

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
                    if(trainIndex != 0) writer.print("\r\n");
                    for(int j = 0;  j < res[i].length; j++){
                        for(int k = 0;  k < res[i][j].length; k++){
                            if(k % 4 != 0) writer.print(res[i][j][k] + ",");
                        }
                    }
                }

                //Append the genre
                Genre trackGenre = recordingz.get(rec);
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

                trainIndex++;
            }
            writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
