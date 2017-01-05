/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sokol
 */
public class NMClassifier extends abstractClassifier{
 
      NMClassifier(PR_GUI gui) {
        pr_gui = gui;
    }  
    
   /**
     * Determines the minimum distance of the specified class mean and specified test sample
     * @param sampNo number of sample
     * @param sampClass number of class
     * @param checkClass class to which distance is computed
     * @return minimum distance of the spec. sample to the spec. class
     */
      @Override
      protected double getClassDistance(int sampNo, int sampClass, int checkClass){

        double[] currFeatureSet = new double[pr_gui.getFeatureCount()]; // set of features for current sample
        double[] meanFeatureSet = new double[pr_gui.getFeatureCount()]; //mean feature set of sample
        double[] checkPoint = new double[pr_gui.getFeatureCount()];
        int currSampleCount=0;      
        // determine cumulative value of features for checked class and set of features for checked test sample
        for (int checkNo = 0; checkNo < SplitData[sampClass][0].length; checkNo++){ //count of samples in class
            if (isTrainSet(checkClass,checkNo)&& notCheckingSample(sampClass,checkClass,sampNo,checkNo)){ //if shuld be checked
                currSampleCount++; 
                for (int i = 0; i<SplitData[0].length; i++){ //to feature count
                    currFeatureSet[i] += SplitData[sampClass][i][checkNo]; //cumulative value of features for checked class
                    checkPoint[i] = SplitData[sampClass][i][sampNo]; //set of features for checked test sample
                }
            }
        }
        // computes the set of features of the class mean and a chenked test sample
        for (int i = 0; i<SplitData[0].length; i++){  //for all features
                    meanFeatureSet[i] = currFeatureSet[i]/currSampleCount; //generate the mean of features for the tested class
                    checkPoint[i] = SplitData[sampClass][i][sampNo]; //generate sample to be checked [features]
                }
        return makeEuklides(meanFeatureSet, checkPoint); //returns distance of sample to mean of tested calss
    }

    
}
