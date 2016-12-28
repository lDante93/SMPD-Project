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
    
   
      @Override
      protected double getClassDistance(int sampNo, int sampClass, int checkClass){

        double[] currFeatureSet = new double[pr_gui.getFeatureCount()]; // set of features for current sample
        double[] meanFeatureSet = new double[pr_gui.getFeatureCount()]; //mean feature set of sample
        double[] checkPoint = new double[pr_gui.getFeatureCount()];
        int currSampleCount=0;       
        for (int checkNo = 0; checkNo < SplitData[sampClass][0].length; checkNo++){ //count of samples in class
            if (isTrainSet(checkClass,checkNo)&& notCheckingSample(sampClass,checkClass,sampNo,checkNo)){
                currSampleCount++; 
                for (int i = 0; i<SplitData[0].length; i++){ //to feature count
                    currFeatureSet[i] += SplitData[sampClass][i][checkNo];
                    checkPoint[i] = SplitData[sampClass][i][sampNo];
                }
            }
        }
        
        for (int i = 0; i<SplitData[0].length; i++){ 
                    meanFeatureSet[i] = currFeatureSet[i]/currSampleCount; //generate the mean of features for the class
                    checkPoint[i] = SplitData[sampClass][i][sampNo]; //generate sample to be checked [features]
                }
        return makeEuklides(meanFeatureSet, checkPoint); //returns distance of sample to mean of calss
    }

    
}
