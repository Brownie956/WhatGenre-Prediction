/*Author: Chris Brown
* Date: 16/04/2016
* Description: Genre predictor class*/
package Sound;

public class GenrePredictor {
    //TODO move genre enum here

    public GenrePredictor(){

    }

    public Conf.Genre getTrackGenre(String fileName){
        String genreFromName = fileName.substring(fileName.lastIndexOf("-") + 1, fileName.indexOf("."));
        Conf.Genre fileGenre = Conf.Genre.UNKNOWN;
        try {
            fileGenre = Conf.Genre.valueOf(genreFromName.toUpperCase());
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        return fileGenre;
    }
}
