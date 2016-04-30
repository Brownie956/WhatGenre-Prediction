/*Author: Chris Brown
* Date: 16/04/2016
* Description: Genre predictor class*/
package Sound;

public class GenrePredictor {
    public enum Genre {
        ROCK, DANCE, CLASSICAL, REGGAE, UNKNOWN
    }

    public GenrePredictor(){

    }

    public Genre getTrackGenre(String fileName){
        String genreFromName = fileName.substring(fileName.lastIndexOf("-") + 1, fileName.indexOf("."));
        Genre fileGenre = Genre.UNKNOWN;
        try {
            fileGenre = Genre.valueOf(genreFromName.toUpperCase());
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        return fileGenre;
    }
}
