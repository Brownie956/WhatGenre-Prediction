/*Author: Chris Brown
* Date: 17/03/2016
* Description: A class to store configuration settings*/
package Sound;

public class Conf {

    private static Conf sessionInstance = new Conf();

    public static final String RESOURCESPATH = "res/";
    public static final String TRAININGDATADIRPATH = RESOURCESPATH + "trainingmusic-cut/";
    public static final String TRAININGDATAPATH = RESOURCESPATH + "trainingData.csv";
    public static final String TESTROCKDATADIRPATH = RESOURCESPATH + "testMusic-rock/";
    public static final String TESTDANCEDATADIRPATH = RESOURCESPATH + "testMusic-dance/";
    public static final String TESTCLASSICALDATADIRPATH = RESOURCESPATH + "testMusic-classical/";
    public static final String TESTREGGAEDATADIRPATH = RESOURCESPATH + "testMusic-reggae/";
    public static final String TESTROCKDATAPATH = RESOURCESPATH + "testData-rock.csv";
    public static final String TESTDANCEDATAPATH = RESOURCESPATH + "testData-dance.csv";
    public static final String TESTCLASSICALDATAPATH = RESOURCESPATH + "testData-classical.csv";
    public static final String TESTREGGAEDATAPATH = RESOURCESPATH + "testData-reggae.csv";
    public static final String FOLDLOGPATH = RESOURCESPATH + "fold-log.txt";

    public static final String FEATURESPATH = RESOURCESPATH + "features.xml";
    public static final String SETTINGSPATH = RESOURCESPATH + "settings.xml";
    public static final String FKOUTPUTPATH = RESOURCESPATH + "fk.xml";
    public static final String FVOUTPUTPATH = RESOURCESPATH + "fv.xml";

    public static final String NNOUTPUTPATH = RESOURCESPATH + "genreNet.nnet";

    public static final int NOINPUTS = 54;
    public static final int NOOUTPUTS = 4;
    public static final int NOOFFOLDS = 10;

    public static Conf getConfig() {
        return sessionInstance;
    }

    private Conf() {
    }
}
