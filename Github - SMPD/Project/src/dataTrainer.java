

import java.util.ArrayList;

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
        
        trainOrTestSet = new int[pr_gui.getClassCount()][pr_gui.getSamplesCount()/pr_gui.getClassCount()]; //no support for diff sample size
        double Th = Double.parseDouble(TrainSetSize)/100.0;
        int classNo=0;
        ArrayList<Integer> excludeRows = new ArrayList<Integer>();
        for (int i = 0; i< Dataset[0].length; i++){
            classNo = i / (pr_gui.getSamplesCount()/pr_gui.getClassCount());
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
     * Gives the number of sample of the current class
     * @param currI current sample of Dataset
     * @param classNo current class in which is the sample
     * @return the number of the sample in the current class
     */
    private int getProperI(int currI, int classNo){
        return currI-classNo*((pr_gui.getSamplesCount()/pr_gui.getClassCount()));
    }
 /**
  * splits Dataset into a new array used in all classifier [classes][features][samples]
  * @param Dataset input matrix of [features][samples] for all classes
  */
    private void splitClases(double[][] Dataset){
     SplitData = new double[pr_gui.getClassCount()][pr_gui.getFeatureCount()][Dataset[0].length/pr_gui.getClassCount()]; //class/features/samples
     int nLoop = 0;
     for (int i=0;i<pr_gui.getClassCount();i++){
         nLoop++;
         for (int j=0;j<pr_gui.getFeatureCount();j++){
             for (int k=0;k<Dataset[0].length/pr_gui.getClassCount();k++){
                 SplitData[i][j][k] = Dataset[j][k+(nLoop-1)*pr_gui.getFeatureCount()];
             }
         }
     }
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
