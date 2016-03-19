/*Author: Chris Brown
* Date: 17/03/2016
* Description: A class to store configuration settings*/
package Sound;

public class Conf {

    private static Conf sessionInstance = new Conf();

    public static final String RESOURCESPATH = "res/";
    public static final String TRAININGDATAPATH = RESOURCESPATH + "trainingmusic/";

    public static final String featureFile = RESOURCESPATH + "features.xml";
    public static final String settingsFile = RESOURCESPATH + "settings.xml";
    public static final String settings2File = RESOURCESPATH + "settings2.xml";
    public static final String settingsmfccFile = RESOURCESPATH + "settingsmfcc.xml";
    public static final String FKOuputFile = RESOURCESPATH + "fk.xml";
    public static final String FVOuputFile = RESOURCESPATH + "fv.xml";


    public static Conf getConfig() {
        return sessionInstance;
    }

    private Conf() {
    }
}
