/*Author: Chris Brown
* Date: 17/03/2016
* Description: Class used for creating a training set*/
package Sound;

import jAudioFeatureExtractor.ACE.DataTypes.Batch;
import jAudioFeatureExtractor.ACE.DataTypes.DataSet;
import jAudioFeatureExtractor.ACE.XMLParsers.XMLDocumentParser;
import jAudioFeatureExtractor.DataModel;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Sound.GenrePredictor.Genre;

public class DataSetCreator {

    public DataSetCreator(){}

    private static HashMap<File, Genre> getRecordings(String recordingsDir){
        HashMap<File, Genre> recordings = new HashMap<File, Genre>();
        final File folder = new File(recordingsDir);
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

    public static File[] getFilesInDir(String directory){
        final File folder = new File(directory);
        if(folder.isDirectory()){
            return folder.listFiles();
        }
        else {
            System.out.println("File path is not a directory");
            System.out.println(directory);
            return new File[0];
        }
    }

    public static ArrayList<String> getFileNamesInDir(String directory, boolean includeExt){
        ArrayList<String> fileNames = new ArrayList<String>();
        final File folder = new File(directory);
        //check directory param is actually a directory
        if(folder.isDirectory()){
            File[] files = folder.listFiles();
            for(File file : files){
                //add if not a directory
                if(!file.isDirectory()){
                    //do we need to remove the extension?
                    if(includeExt){
                        fileNames.add(file.getName());
                    }
                    else {
                        String fileName = file.getName();
                        fileNames.add(fileName.substring(0,fileName.indexOf(".")));
                    }
                }
            }
        }
        else {
            System.out.println("File path is not a directory");
            System.out.println(directory);
        }
        return fileNames;
    }

    public static void createDataSetOverall(String recordingsDir, String outputPath){
        HashMap<File, Genre> recordings = getRecordings(recordingsDir);

        try{
            File trainingData = new File(outputPath);
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

    public static void createDataSetPerWindow(String recordingsDir){
        HashMap<File, Genre> recordings = getRecordings(recordingsDir);
        File[] files = new File[recordings.size()];
        int fileIndex = 0;
        for(File rec : recordings.keySet()){
            files[fileIndex] = rec;
            fileIndex++;
        }

        Object[] trainingDataResults = extract(files);
        HashMap<Object[], Object[]> trainTestCombos = getTrainTestCombos(trainingDataResults);

        int fold = 1;
        for(HashMap.Entry combo : trainTestCombos.entrySet()){
            //Extract features for training set
            Object[] trainRes = (Object[]) combo.getKey();
            //Store
            processToFile(trainRes, Conf.getTrainingDataFoldPath(fold));
            //Extract features for test set
            Object[] testRes = (Object[]) combo.getValue();
            //Store
            processToFile(testRes, Conf.getTestDataFoldPath(fold));
            fold++;
        }
    }

    public static void createTrainingMusicCSVFiles(String recordingsDir){
        HashMap<File, Genre> recordings = getRecordings(recordingsDir);

        int fileNumber = 1;
        for(Map.Entry recording : recordings.entrySet()){
            System.out.println("Extracting track number: " + fileNumber);
            Object[] extractedValues = extract(new File[]{(File) recording.getKey()});
            //For every extracted dataSet - Should be 1
            for(Object trackObj : extractedValues){
                DataSet track = (DataSet) trackObj;
                String id = track.identifier;
                String audioTrackName = id.substring(id.lastIndexOf("/") + 1, id.indexOf("."));
//                processToFile(extractedValues, Conf.getTrackCSVPath(audioTrackName));
//                processOverallToFile(extractedValues, Conf.getTROTrackCsvPath(audioTrackName, (Genre) recording.getValue()));
                processToFile(extractedValues, Conf.RESOURCESPATH + "temp-dir/csv/" + audioTrackName + ".csv");
            }
            fileNumber++;
        }
    }

    public static ArrayList<ArrayList<ArrayList<Double>>> processToArrayList(Object[] results, boolean includeGenre){
        ArrayList<ArrayList<ArrayList<Double>>> dataSet = new ArrayList<ArrayList<ArrayList<Double>>>();
        try{
            //For every track...
            for(int i = 0; i < results.length; i++){
                ArrayList<ArrayList<Double>> trackDataSet = new ArrayList<ArrayList<Double>>();
                DataSet track = (DataSet) results[i];

                //Exclude first window (Doesn't contain features that rely on previous window)
                //Average windows and add to ArrayList
                int startIndex = 1;
                while(startIndex < track.sub_sets.length) {
                    ArrayList<Double> meanWindowValsList = new ArrayList<Double>();
                    //Get windows to average
                    Window[] windowsToAverage = getWindowsToAverage(track, startIndex);
                    //Update the start index
                    startIndex += windowsToAverage.length;
                    //Average the windows
                    double[] meanWindowVals = calculateMeanWindowValues(windowsToAverage);
                    //Filter values
                    meanWindowVals = filterDataRow(meanWindowVals);
                    //Write values to file
                    for(double val : meanWindowVals){
                        meanWindowValsList.add(val);
                    }

                    if(includeGenre){
                        String id = track.identifier;
                        String audioTrackName = id.substring(id.lastIndexOf("/") + 1, id.indexOf("."));

                        //Get genre values to append
                        String trackNameGenre = audioTrackName.substring(audioTrackName.lastIndexOf("-") + 1, audioTrackName.length());
                        String genreValues = getTrackExpectedOutput(trackNameGenre);

                        //Append genre
                        String[] genreVals = genreValues.split(",");
                        for(String val : genreVals){
                            try{
                                meanWindowValsList.add(Double.valueOf(val));
                            }
                            catch(NumberFormatException e){
                                e.printStackTrace();
                            }
                        }
                    }
                    trackDataSet.add(meanWindowValsList);
                }
                dataSet.add(trackDataSet);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return dataSet;
    }

    private static double[] calculateMeanWindowValues(Window[] windows){
        int noOfValues = 0;

        //Empty check
        if(windows.length == 0) {
            return new double[0];
        }

        //How many values in a window
        for(double[] vals : windows[0].getFeatureValues()){
            noOfValues += vals.length;
        }

        double[][] collectedVals = new double[windows.length][noOfValues];

        //For every window
        for(int i = 0; i < windows.length; i++){
            int valIndex = 0;
            //Get the values
            //For every feature
            for(double[] featVals : windows[i].getFeatureValues()){
                //for every val for that feature
                for(double featVal : featVals){
                    //Store val
                    collectedVals[i][valIndex] = featVal;
                    valIndex++;
                }
            }
        }

        //Calculate mean values
        double[] meanVals = new double[noOfValues];
        //For every value
        for(int i = 0; i < noOfValues; i++){
            double[] tempArray = new double[windows.length];
            //Get the value for each window and store it
            for(int j = 0; j < tempArray.length; j++){
                tempArray[j] = collectedVals[j][i];
            }

            //Calculate mean of array
            meanVals[i] = Utils.mean(tempArray);
        }

        return meanVals;
    }

    public static Object[] extract(File[] audioTracks){
        Object[] res = new Object[0];
        try {
            Batch batch = new Batch(Conf.FEATURESPATH, null);
            batch.setRecordings(audioTracks);
//            batch.setSettings(Conf.SETTINGSWINPATH);
            //TODO change me
            batch.setSettings(Conf.SETTINGS_SLEDGEHAMMER_WIN_PATH);

            DataModel dm = batch.getDataModel();
            OutputStream defSavePath = new FileOutputStream(Conf.FKOUTPUTPATH);
            OutputStream valSavePath = new FileOutputStream(Conf.FVOUTPUTPATH);
            dm.featureKey = defSavePath;
            dm.featureValue = valSavePath;
            batch.setDataModel(dm);

            batch.execute();

            res = (Object[]) XMLDocumentParser.parseXMLDocument(Conf.FVOUTPUTPATH, "feature_vector_file");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public static Object[] extract(File[] audioTracks, Batch batch, String outputPath){
        Object[] res = new Object[0];
        try{
            batch.execute();
            res = (Object[]) XMLDocumentParser.parseXMLDocument(outputPath, "feature_vector_file");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    private static void processOverallToFile(Object[] results, String outputPath){
        try{
            File outputFile = new File(outputPath);
            PrintWriter writer = new PrintWriter(outputFile);

            //For every track...
            for(int i = 0; i < results.length; i++){
                ArrayList<Double> extractedValues = new ArrayList<Double>();

                DataSet track = (DataSet) results[i];
                String id = track.identifier;
                String audioTrackName = id.substring(id.lastIndexOf("/") + 1, id.indexOf("."));

                //Get genre values to append
                String trackNameGenre = audioTrackName.substring(audioTrackName.lastIndexOf("-") + 1, audioTrackName.length());
                String genreValues = getTrackExpectedOutput(trackNameGenre);

                //Get all the values
                for(int j = 0; j < track.feature_values.length; j++){
                    for(int k = 0; k < track.feature_values[j].length; k++){
                        if(k != 0){
                            extractedValues.add(track.feature_values[j][k]);
                        }
                    }
                }

                //Convert to array and filter
                double[] vals = Utils.convertToPrimative(extractedValues);
                //TODO change me
                vals = filterDataRow(vals);

                //Save values to file
                for(double val : vals){
                    writer.print(val + ",");
                }
                //Append genre
                writer.print(genreValues);
                writer.print("\r\n");
            }
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void processToFile(Object[] results, String outputPath){
        try{
            File outputFile = new File(outputPath);
            PrintWriter writer = new PrintWriter(outputFile);

            //For every track...
            for(int i = 0; i < results.length; i++){
                DataSet track = (DataSet) results[i];
                String id = track.identifier;
                String audioTrackName = id.substring(id.lastIndexOf("/") + 1, id.indexOf("."));

                //Get genre values to append
                String trackNameGenre = audioTrackName.substring(audioTrackName.lastIndexOf("-") + 1, audioTrackName.length());
                String genreValues = getTrackExpectedOutput(trackNameGenre);

                //Exclude first window (Doesn't contain features that rely on previous window)
                //Average windows and write to file
                int startIndex = 1;
                while(startIndex < track.sub_sets.length) {
                    //Get windows to average
                    Window[] windowsToAverage = getWindowsToAverage(track, startIndex);
                    //Update the start index
                    startIndex += windowsToAverage.length;
                    //Average the windows
                    double[] meanWindowVals = calculateMeanWindowValues(windowsToAverage);
                    //Filter values
                    meanWindowVals = filterDataRow(meanWindowVals);
                    //Write values to file
                    for(double val : meanWindowVals){
                        writer.print(val + ",");
                    }

                    //Append genre
                    writer.print(genreValues);
                    writer.print("\r\n");
                }
            }
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private static Window[] getWindowsToAverage(DataSet track, int startIndex){
        Window[] windowsToAverage;

        //Calculate the number of windows to average
        if(startIndex + Conf.NOOFWINDOWSTOAVERAGE - 1 >= track.sub_sets.length){
            windowsToAverage = new Window[track.sub_sets.length - startIndex];
        }
        else {
            windowsToAverage = new Window[Conf.NOOFWINDOWSTOAVERAGE];
        }

        //Loop over windows and get the window to average
        int winIndex = 0;
        while(winIndex < Conf.NOOFWINDOWSTOAVERAGE && startIndex < track.sub_sets.length){
            Window window = new Window(track.sub_sets[startIndex].feature_values);
            windowsToAverage[winIndex] = window;
            winIndex++;
            startIndex++;
        }

        return windowsToAverage;
    }

    private static double[] filterDataRow(double[] rowData){
        ArrayList<Double> filteredValues = new ArrayList<Double>();

        for(int j = 0; j < rowData.length; j++){
            double val = rowData[j];
            //Scale Spectral Centroid
            if(j == 0){
                val = val / 10;
            }
            //Scale Compactness
            else if(j == 3){
                val = val / 1000;
            }
            //Scale Zero-Crossings
            else if(j == 6){
                val = val / 10;
            }
            //Scale Strongest Frequency via Zero-Crossings
            else if(j == 7){
                val = val / 1000;
            }

            //Add to list if value if not the first MFCC value
            if(j != 8){
                filteredValues.add(val);
            }
        }

        //Convert ArrayList<Double> to double[]
        return Utils.convertToPrimative(filteredValues);
    }

    private static String getTrackExpectedOutput(String trackNameGenre){
        String genreValues = "";
        try {
            Genre trackGenre = Genre.valueOf(trackNameGenre.toUpperCase());
            switch (trackGenre){
                case ROCK:
                    genreValues = "1,0,0,0";
                    break;
                case DANCE:
                    genreValues = "0,1,0,0";
                    break;
                case CLASSICAL:
                    genreValues = "0,0,1,0";
                    break;
                case REGGAE:
                    genreValues = "0,0,0,1";
                    break;
            }
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        return genreValues;
    }

    public static HashMap<File[], File[]> getTrainTestCombos(File[] files){
        int noOfTestFiles = files.length / Conf.NOOFFOLDS;

        HashMap<File[], File[]> trainTestCombos = new HashMap<File[], File[]>();

        //For every fold, get the training:test combos
        for(int fold = 0; fold < Conf.NOOFFOLDS; fold++){
            //Get the starting index of test rows
            int startPoint = fold * noOfTestFiles;
            ArrayList<File> trainingFiles = new ArrayList<File>();
            ArrayList<File> testFiles = new ArrayList<File>();

            //Extract the test files
            for(int i = 0; i < files.length; i++){
                File tempFile = files[i];
                if(i >= startPoint && i < startPoint + noOfTestFiles){
                    //Test File
                    testFiles.add(tempFile);
                }
                else {
                    //Train Row
                    trainingFiles.add(tempFile);
                }
            }
            //Store combo
            File[] trainingFilesArray = trainingFiles.toArray(new File[trainingFiles.size()]);
            File[] testFilesArray = testFiles.toArray(new File[testFiles.size()]);
            trainTestCombos.put(trainingFilesArray, testFilesArray);
        }
        return trainTestCombos;
    }

    private static HashMap<Object[], Object[]> getTrainTestCombos(Object[] extractedResults){
        int noOfTestFiles = extractedResults.length / Conf.NOOFFOLDS;

        HashMap<Object[], Object[]> trainTestCombos = new HashMap<Object[], Object[]>();

        //For every fold, get the training:test combos
        for(int fold = 0; fold < Conf.NOOFFOLDS; fold++){
            //Get the starting index of test rows
            int startPoint = fold * noOfTestFiles;
            ArrayList<Object> trainingFiles = new ArrayList<Object>();
            ArrayList<Object> testFiles = new ArrayList<Object>();

            //Extract the test results
            for(int i = 0; i < extractedResults.length; i++){
                Object tempFile = extractedResults[i];
                if(i >= startPoint && i < startPoint + noOfTestFiles){
                    //Test File
                    testFiles.add(tempFile);
                }
                else {
                    //Train Row
                    trainingFiles.add(tempFile);
                }
            }
            //Store combo
            Object[] trainingFilesArray = trainingFiles.toArray(new Object[trainingFiles.size()]);
            Object[] testFilesArray = testFiles.toArray(new Object[testFiles.size()]);
            trainTestCombos.put(trainingFilesArray, testFilesArray);
        }
        return trainTestCombos;
    }

    public static HashMap<ArrayList<String>, ArrayList<String>> getTrainTestCombos(ArrayList<String> files){
        int noOfTestFiles = files.size() / Conf.NOOFFOLDS;

        HashMap<ArrayList<String>, ArrayList<String>> trainTestCombos = new HashMap<ArrayList<String>, ArrayList<String>>();

        //For every fold, get the training:test combos
        for(int fold = 0; fold < Conf.NOOFFOLDS; fold++){
            //Get the starting index of test rows
            int startPoint = fold * noOfTestFiles;
            ArrayList<String> trainingFiles = new ArrayList<String>();
            ArrayList<String> testFiles = new ArrayList<String>();

            //Extract the test files
            for(int i = 0; i < files.size(); i++){
                String tempFile = files.get(i);
                if(i >= startPoint && i < startPoint + noOfTestFiles){
                    //Test File
                    testFiles.add(tempFile);
                }
                else {
                    //Train Row
                    trainingFiles.add(tempFile);
                }
            }
            //Store combo
            trainTestCombos.put(trainingFiles, testFiles);
        }
        return trainTestCombos;
    }

    private static double[][] getMeanWindowVals(DataSet track){
        //Exclude first window (Doesn't contain features that rely on previous window)
        //Get windows to average
        int numOfResults;
        if(track.sub_sets.length % Conf.NOOFWINDOWSTOAVERAGE == 0){
            numOfResults = track.sub_sets.length / Conf.NOOFWINDOWSTOAVERAGE;
        }
        else {
            numOfResults = (track.sub_sets.length / Conf.NOOFWINDOWSTOAVERAGE) + 1;
        }

        double[][] windowAverages = new double[numOfResults][Conf.NOINPUTS];

        int startIndex = 1;
        int resultIndex = 0;
        while(startIndex < track.sub_sets.length) {
            Window[] windowsToAverage;
            //How many windows are we going to average?
            if (startIndex + Conf.NOOFWINDOWSTOAVERAGE - 1 >= track.sub_sets.length) {
                windowsToAverage = new Window[track.sub_sets.length - startIndex];
            } else {
                windowsToAverage = new Window[Conf.NOOFWINDOWSTOAVERAGE];
            }

            //Collect the windows to average
            int winIndex = 0;
            while (winIndex < Conf.NOOFWINDOWSTOAVERAGE && startIndex < track.sub_sets.length) {
                Window window = new Window(track.sub_sets[startIndex].feature_values);
                windowsToAverage[winIndex] = window;
                winIndex++;
                startIndex++;
            }
            windowAverages[resultIndex] = calculateMeanWindowValues(windowsToAverage);
            resultIndex++;
        }

        return windowAverages;
    }
}
