


import java.util.ArrayList;
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sokol
 */

   

public class dataTrainer extends abstractClassifier{

     dataTrainer (PR_GUI gui, int i) {
        pr_gui = gui;
    }
    
    /**
 * Divides examples to training and test part
 * @param Dataset FNew from gui
 * @param TrainSetSize tf_TrainSetSize.getText() from gui
 */
    void generateTraining_and_Test_Flags(double[][] Dataset, String TrainSetSize){
        
        splitClases(Dataset);
        
        trainOrTestSet = new Integer[pr_gui.getClassCount()][determineMaxSamples()];
        for(int i=0; i<trainOrTestSet.length; i++){ //filling array with a flag
           Arrays.fill(trainOrTestSet[i], -1); 
        }
        
        double Th = Double.parseDouble(TrainSetSize)/100.0;
        int classNo=0;
        ArrayList<Integer> excludeRows = new ArrayList<Integer>();
        for (int i = 0; i< Dataset[0].length; i++){
            classNo = getProperClassNo(i);
            int randomNumb = generateRandom(1, Dataset[0].length, excludeRows);
            excludeRows.add(randomNumb);
            if(((double) randomNumb)/Dataset[0].length < Th) {
                trainOrTestSet[classNo][getProperI(i, classNo)]=TRAIN_SET;
            }
            else {
                trainOrTestSet[classNo][getProperI(i, classNo)]=TEST_SET;
            }                   
        }
    }
    /**
     * Returns the class, to which belongs the sample
     * @param i sample
     * @return 
     */
    private int getProperClassNo(int i){
        int samplesCount=0; //cumulative count of samples for classes
        for (int j=0; j<pr_gui.getClassCount(); j++){
            if(i < samplesCount+pr_gui.SampleCount[j]){
            return j;
            }
            samplesCount+=pr_gui.SampleCount[j];
        }
        return -1; //makes error
    }
    
    /**
     * Gives the number of sample of the current class
     * @param currI current sample of Dataset
     * @param classNo current class in which is the sample
     * @return the number of the sample in the current class
     */
    private int getProperI(int currI, int classNo){
        int integerToSubtract=0;
        for(int i=0; i<classNo; i++){
            integerToSubtract+=pr_gui.SampleCount[i];
        }
        return currI-integerToSubtract;
    }
 /**
  * splits Dataset into a new array used in all classifier [classes][features][samples]
  *ILOSC PROBEK W KLASACH NIE JEST TAKA SAMA - DODAC TEN PROBLEM tutaj flaga -1.01
  * 
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
   
    public int generateRandom(int start, int end, ArrayList<Integer> excludeRows) {
    int range = end - start +1;
    int random = generator.nextInt(range) + 1;

    while(excludeRows.contains(random)){
        random = generator.nextInt(range)+1;
    }

    return random;
    }
}
