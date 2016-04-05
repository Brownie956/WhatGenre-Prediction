/*Author: Chris Brown
* Date: 17/03/2016
* Description: A class to store configuration settings*/
package Sound;

public class Conf {

    private static Conf sessionInstance = new Conf();

    public enum Genre {
        ROCK, DANCE, CLASSICAL, REGGAE, UNKNOWN
    }

    public static final double GENRETHRESHOLD = 60;

    public static final String RESOURCESPATH = "res/";
    public static final String TRAININGDATADIRPATH = RESOURCESPATH + "trainingmusic-cut/";
    public static final String TRAININGTEMPDIRPATH = RESOURCESPATH + "temp-trainingMusic/";
//    public static final String TRAININGDATAOUTPUTDIRPATH = RESOURCESPATH + "trainingmusic-cut-csv/50/";
//    public static final String TRAININGDATAOUTPUTDIRPATH = RESOURCESPATH + "trainingmusic-cut-csv/100/";
//    public static final String TRAININGDATAOUTPUTDIRPATH = RESOURCESPATH + "trainingmusic-cut-csv/150/";
//    public static final String TRAININGDATAOUTPUTDIRPATH = RESOURCESPATH + "trainingmusic-cut-csv/200/";
//    public static final String TRAININGDATAOUTPUTDIRPATH = RESOURCESPATH + "trainingmusic-cut-csv/250/";
    public static final String TRAININGDATAOUTPUTDIRPATH = RESOURCESPATH + "trainingmusic-cut-csv/300/";
//    public static final String TRAININGDATAOUTPUTDIRPATH = RESOURCESPATH + "trainingmusic-cut-csv/350/";
//    public static final String TRAININGDATAOUTPUTDIRPATH = RESOURCESPATH + "trainingmusic-cut-csv/400/";
//    public static final String TRAININGDATAOUTPUTDIRPATH = RESOURCESPATH + "trainingmusic-cut-csv/500/";
//    public static final String TRAININGDATAOUTPUTDIRPATH = RESOURCESPATH + "trainingmusic-cut-csv/700/";
//    public static final String TRAININGDATAOUTPUTDIRPATH = RESOURCESPATH + "trainingmusic-cut-csv/1000/";
//    public static final String TEMPTRAININGCOMBOSPATH = RESOURCESPATH + "temp-trainingCombos-100/";
//    public static final String TEMPTRAININGCOMBOSPATH = RESOURCESPATH + "temp-trainingCombos-200/";
//    public static final String TEMPTRAININGCOMBOSPATH = RESOURCESPATH + "temp-trainingCombos-300/";
//    public static final String TEMPTRAININGCOMBOSPATH = RESOURCESPATH + "temp-trainingCombos-400/";
    public static final String TEMPTRAININGCOMBOSPATH = RESOURCESPATH + "temp-trainingCombos-500/";
//    public static final String TEMPTRAININGCOMBOSPATH = RESOURCESPATH + "temp-trainingCombos-600/";
//    public static final String TEMPTRAININGCOMBOSPATH = RESOURCESPATH + "temp-trainingCombos-700/";
//    public static final String TEMPTRAININGCOMBOSPATH = RESOURCESPATH + "temp-trainingCombos-800/";
//    public static final String TEMPTRAININGCOMBOSPATH = RESOURCESPATH + "temp-trainingCombos-900/";
//    public static final String TEMPTRAININGCOMBOSPATH = RESOURCESPATH + "temp-trainingCombos-1000/";
//    public static final String TEMPTRAININGCOMBOSPATH = RESOURCESPATH + "temp-trainingCombos/";

    public static final String TRAININGDATAPATH = RESOURCESPATH + "trainingData.csv";
    public static final String TRAININGDATATEMPPATH = RESOURCESPATH + "tempTrainingData.csv";
    public static String getTrainingDataFoldPath(int foldNumber){
        return TEMPTRAININGCOMBOSPATH + "trainingDataFold" + foldNumber + ".csv";
    }
    public static String getTestDataFoldPath(int foldNumber){
        return TEMPTRAININGCOMBOSPATH + "testDataFold" + foldNumber + ".csv";
    }

    public static String getTrackCSVPath(String trackName){
        return TRAININGDATAOUTPUTDIRPATH + trackName + ".csv";
//        return TESTREGGAEDATACSVDIRPATH + trackName + ".csv";
    }

    public static final String TESTROCKDATADIRPATH = RESOURCESPATH + "testMusic-rock/";
    public static final String TESTDANCEDATADIRPATH = RESOURCESPATH + "testMusic-dance/";
    public static final String TESTCLASSICALDATADIRPATH = RESOURCESPATH + "testMusic-classical/";
    public static final String TESTREGGAEDATADIRPATH = RESOURCESPATH + "testMusic-reggae/";

    public static final String TESTROCKDATACSVDIRPATH = RESOURCESPATH + "testMusic-rock-csv/";
    public static final String TESTDANCEDATACSVDIRPATH = RESOURCESPATH + "testMusic-dance-csv/";
    public static final String TESTCLASSICALDATACSVDIRPATH = RESOURCESPATH + "testMusic-classical-csv/";
    public static final String TESTREGGAEDATACSVDIRPATH = RESOURCESPATH + "testMusic-reggae-csv/";

    public static final String TESTROCKDATAPATH = RESOURCESPATH + "testData-rock.csv";
    public static final String TESTDANCEDATAPATH = RESOURCESPATH + "testData-dance.csv";
    public static final String TESTCLASSICALDATAPATH = RESOURCESPATH + "testData-classical.csv";
    public static final String TESTREGGAEDATAPATH = RESOURCESPATH + "testData-reggae.csv";

    public static final String FOLDLOGPATH = RESOURCESPATH + "fold-log-temp.txt";

    public static final String FEATURESPATH = RESOURCESPATH + "features.xml";
    public static final String SETTINGSPATH = RESOURCESPATH + "settings.xml";
    public static final String SETTINGSWINPATH = RESOURCESPATH + "settings-win.xml";
    public static final String FKOUTPUTPATH = RESOURCESPATH + "fk.xml";
    public static final String FVOUTPUTPATH = RESOURCESPATH + "fv.xml";

//    public static final String NNOUTPUTPATH = RESOURCESPATH + "genreNet.nnet";
    public static final String NNOUTPUTPATH = RESOURCESPATH + "genreNet-temp.nnet";

//    public static final int NOINPUTS = 54;
    public static final int NOINPUTS = 16;
//    public static final int NOOFHIDDENNEURONS = 16;
    public static final int NOOFHIDDENNEURONS = 200;
    public static final int NOOUTPUTS = 4;
    public static final int NOOFFOLDS = 10;

    public static final int NOOFWINDOWSTOAVERAGE = 50;

    public static Conf getConfig() {
        return sessionInstance;
    }

    private Conf() {
    }
}
