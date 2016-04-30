/*Author: Chris Brown
* Date: 17/03/2016
* Description: A class to store configuration settings*/
package Sound;

public class Conf {

    private static Conf sessionInstance = new Conf();
    private GenrePredictor genrePredictor;

    public enum Genre {
        ROCK, DANCE, CLASSICAL, REGGAE, UNKNOWN
    }

    public static final double GENRETHRESHOLD = 60;

    public static final String RESOURCESPATH = "res/";

    /*TRAINING MUSIC FILE DIRECTORIES*/
    public static final String TR_MUSIC_BASE_DIR = RESOURCESPATH + "trainingMusic";
    private static String getTRGenreDir(Genre genre){
        return TR_MUSIC_BASE_DIR + "/" + genre.toString().toLowerCase() + "/";
    }
    public static String getTRWavDir(Genre genre){
        return getTRGenreDir(genre) + "wav/";
    }
    public static String getTRCsvDir(Genre genre){
        return getTRGenreDir(genre) + "csv/"; //TODO change me
    }
    public static String getTROCsvDir(Genre genre){
        return getTRGenreDir(genre) + "csv-sh-overall/"; //TODO change me
    }
    public static String getTRTrackCsvPath(String trackName, Genre genre){
        return getTRCsvDir(genre) + trackName + ".csv";
    }
    public static String getTROTrackCsvPath(String trackName, Genre genre){
        return getTROCsvDir(genre) + trackName + ".csv";
    }

    /*TEST MUSIC FILE DIRECTORIES*/
    public static final String TE_MUSIC_BASE_DIR = RESOURCESPATH + "testMusic";
    private static String getTEGenreDir(Genre genre){
        return TE_MUSIC_BASE_DIR + "/" + genre.toString().toLowerCase() + "/";
    }
    public static String getTEWavDir(Genre genre){
        return getTEGenreDir(genre) + "wav/";
    }
    public static String getTECsvDir(Genre genre){
        return getTEGenreDir(genre) + "csv/"; //TODO change me
    }
    public static String getTETrackCsvPath(String trackName, Genre genre){
        return getTECsvDir(genre) + trackName + ".csv";
    }

    public static final String TRAININGDATAOUTPUTDIRPATH = RESOURCESPATH + "trainingmusic-cut-csv/300/";
    public static final String TEMPTRAININGCOMBOSPATH = RESOURCESPATH + "temp-trainingCombos-500/";

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
    public static final String SETTINGS_MFCC_WIN_PATH = RESOURCESPATH + "mfcc-settings.xml";
    public static final String SETTINGS_MFCC_OV_PATH = RESOURCESPATH + "mfcc-overall-settings.xml";
    public static final String SETTINGS_MFCC_SF_WIN_PATH = RESOURCESPATH + "mfcc-sf-settings.xml";
    public static final String SETTINGS_MFCC_SF_OV_PATH = RESOURCESPATH + "mfcc-sf-overall-settings.xml";
    public static final String SETTINGS_SLEDGEHAMMER_WIN_PATH = RESOURCESPATH + "sledgehammer-settings.xml";
    public static final String SETTINGS_SLEDGEHAMMER_OV_PATH = RESOURCESPATH + "sledgehammer-overall-settings.xml";
    public static final String FKOUTPUTPATH = RESOURCESPATH + "fk.xml";
    public static final String FVOUTPUTPATH = RESOURCESPATH + "fv.xml";

//    public static final String NNOUTPUTPATH = RESOURCESPATH + "genreNet.nnet";
//    public static final String NNOUTPUTPATH = RESOURCESPATH + "genreNet-temp.nnet";
//    public static final String NNOUTPUTPATH = RESOURCESPATH + "mfcc-genreNet.nnet";
//    public static final String NNOUTPUTPATH = RESOURCESPATH + "mfcc-sf-genreNet.nnet";
    public static final String NNOUTPUTPATH = RESOURCESPATH + "sledgehammer-genreNet.nnet";

    public static final String CMATRIXPATH = RESOURCESPATH + "confusion-matrix.csv";

    public static final int NOINPUTS = 30;
    public static final int NOOFHIDDENNEURONS = 40;
    public static final int NOOUTPUTS = 4;
    public static final int NOOFFOLDS = 10;

    public static final int NOOFWINDOWSTOAVERAGE = 300;
    //Assume 1 window length
    private static final double SINGLEWINDOWTIME = 0.0093;
    public static final double MULTIWINDOWTIME = NOOFWINDOWSTOAVERAGE * SINGLEWINDOWTIME;

    public static Conf getConfig() {
        return sessionInstance;
    }
    public GenrePredictor getGenrePredictor(){
        return genrePredictor;
    }

    private Conf() {
        this.genrePredictor = new GenrePredictor();
    }
}
