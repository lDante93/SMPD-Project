
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Dante
 */
public class bootstrap extends abstractClassifier{
    
    PR_GUI pr_gui = new PR_GUI();
   Random generator = new Random();

        /**
     * Makes Training of the data for classifier
     * @param gui 
     */
    bootstrap (PR_GUI gui) {
        pr_gui = gui;
        if(pr_gui.getFNew()==null){     // if no reduced feature space have been derived
               super.Dataset = pr_gui.getF();
            }   else{
              super.Dataset = pr_gui.getFNew();
            }
        
        splitClases(super.Dataset);
        makeTrainAndTestSets();
    } 
    
    /**
     * executes the classifier and prints the results on the gui
     * @param gui
     * @param i 
     */
    bootstrap (PR_GUI gui, int i) {
        pr_gui = gui;
        double [] resultTable = executeClassifier();
        double resultOfClassification = determineFinalResult(resultTable);
        printResult(resultOfClassification);
    }
    
    /**
     * Calculates the final result of the classification
     * @param resultTable table with results of k classifications
     * @return final result of classification as double 0-1
     */
    private double determineFinalResult(double[] resultTable){
        double temp=0;
        for (int i=0; i<resultTable.length;i++){
            temp+=resultTable[i];
        }
        return  (double)temp/resultTable.length;
    }
    
    /**
     * Prints the result in pr_gui
     * @param result result of a classification
     */
    private void printResult(double result){
        pr_gui.setClassifierAccur1(result);
        pr_gui.clearClassifierAccur();
    }
    

    
    
      /**
  * splits Dataset into a new array used in all classifier [classes][features][samples]
  * @param Dataset input matrix of [features][samples] for all classes
  */
    private void splitClases(double[][] Dataset){
     super.SplitData = new Double[pr_gui.getClassCount()][Dataset.length][determineMaxSamples()]; //class/features/samples
     
     for (Double[][] a : super.SplitData) { //fills an entire array with Double -1.01
            for (Double[] b : a) {
                Arrays.fill(b, -1.01);
            }
     }
     
     int nLoop = 0;
     for (int i=0;i<pr_gui.getClassCount();i++){ //loop for class
         nLoop++; //current class
         for (int j=0;j<Dataset.length;j++){ //to feature count
             for (int k=0;k<pr_gui.SampleCount[i];k++){ //to sample count
                 super.SplitData[i][j][k] = Dataset[j][k+(nLoop-1)*pr_gui.SampleCount[0]]; // k+valid now only for two classes
             }
         }
     }
 } 
    /**
     * Determines maximum samples amount in F or FNew
     * @return 
     */
    private int determineMaxSamples(){
        int maksSamplesCount =0;
        for (int i=0; i<pr_gui.SampleCount.length; i++){
            if(maksSamplesCount < pr_gui.SampleCount[i]){
                maksSamplesCount = pr_gui.SampleCount[i];
            }
        }
        return maksSamplesCount;
    }
    /**
     * makes and fills the array K_TrainAndTestSets with flags for train or test sample
     */
    protected void makeTrainAndTestSets(){
        K_TrainOrTestSet = new Integer[pr_gui.getKLoops()][pr_gui.getClassCount()][SplitData[0][0].length];
        
        for (int i=0; i<pr_gui.getKLoops();i++){
            K_TrainOrTestSet[i] = updateSets();
        }
    }
    
    /**
     * makes and fills the trainOrTestSet for one classification
     * @return KTrainOrTestSet[i]
     */
    private Integer[][] updateSets(){
        Integer[][] updatedSets = new Integer[pr_gui.getClassCount()][SplitData[0][0].length];
        for (int i=0;i<pr_gui.getClassCount();i++){
            updatedSets[i] = updateClass(i);
            while (setHasNoTestSamples(updatedSets[i]) ){ //set must have at least one test sample for big sample amounts && updatedSets[i].length>50
               updatedSets[i] = updateClass(i); 
            }
        }
        
        return updatedSets;
    }
    
    /**
     * Determines, whether set has at least one test sample
     * @param setToCheck input set of flags
     * @return true if no test samples; false if at least one test sample
     */
    private boolean setHasNoTestSamples(Integer[] setToCheck){
        for(int i=0; i<setToCheck.length; i++){
            if (setToCheck[i] == TEST_SET){ //if there is one test sample set has test samples
                return false;
            }
        }
        return true;
    }
    
    /**
     * Updates the train or test set for one class
     * @return TrainOrTestSet for one class
     */
    private Integer[] updateClass(int classNumber){
        Integer[] randomNumb = createRandomNumberSet(classNumber);
        return setTrainOrTestSet(randomNumb);     
    }
    
    
    private Integer[] createRandomNumberSet(int classNumber){
        Integer[] randomNumb = new Integer[determineMaxSamples()];
        Arrays.fill(randomNumb, -1); //fills array with a flag
            
        for (int i=0; i<pr_gui.SampleCount[classNumber]; i++){
            randomNumb[i] = generator.nextInt(pr_gui.SampleCount[classNumber]);
        }
        return randomNumb;
    }
    /**
     * Makes and fills an array with flag of train or test set
     * @param randomNumb array with random numbers in range of 0 to samples.length
     * @return array with flag for train or test set
     */
    private Integer[] setTrainOrTestSet(Integer[] randomNumb){
        Integer[] TrainOrTestSet = new Integer[randomNumb.length];
        Arrays.fill(TrainOrTestSet, super.TEST_SET);
        for (int i=0; i< randomNumb.length; i++){
            if (!randomNumb[i].equals(-1)){ //if not flag
               for (int j=0; j<randomNumb.length; j++){
                if(i==randomNumb[j]) {//if there is such number in randomNumb - it is train set
                    TrainOrTestSet[i]=super.TRAIN_SET;
                    break; //if there is one, it is not necessary to check further
                    }
                } 
            }   else TrainOrTestSet[i]=-1;
        }
        return TrainOrTestSet;
    }
    
    
   
    /**
     * executes the proper classifier k times with an updated trainand test set
     * @return classification results [k] as double 0-1
     */
    protected double[] executeClassifier(){
            double[] classificationResults = new double[pr_gui.getKLoops()];
        for (int i=0; i<pr_gui.getKLoops(); i++){
            super.trainOrTestSet = this.K_TrainOrTestSet[i];
            classifySample(pr_gui.getSelectionSelectClassMethAdv());
             classificationResults[i] = returnResult();
        }
        return classificationResults;
    }
    
    /**
     * prints the result of one classification in pr_gui
     * @return double result 0-1
     */
    private double returnResult(){
        return pr_gui.getClassifierAccur();
    }
    
    /**
     * Classifies the samples with a proper cassifier
     * @param selectionMessage takes an Message, which classifier is chosen
     */
    private void classifySample(String selectionMessage){
        if ( selectionMessage.equals("Nearest neighbor (NN)")){
               NNClassifier CNN = new NNClassifier(pr_gui);
                CNN.classifySamples();
            } else if(selectionMessage.equals("Nearest Mean (NM)")){
                NMClassifier CNM = new NMClassifier(pr_gui);
                CNM.classifySamples();
            }else if(selectionMessage.equals("k-Nearest Neighbor (k-NN)")){
                kNNClassifier CkNN = new kNNClassifier(pr_gui);
                CkNN.classifySamples();
            }else if(selectionMessage.equals("k-Nearest Mean (k-NM)")){
                kNMClassifier CkNM = new kNMClassifier(pr_gui);
                CkNM.classifySamples();
            }
    }
  
}
