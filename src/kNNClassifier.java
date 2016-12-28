
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *NOT SUPPORTED YET!!
 * currently if k>than possible k is just ignored (lower taken)
 * @author Sokol
 */
public class kNNClassifier extends abstractClassifier{
    
       kNNClassifier(PR_GUI gui) {
        pr_gui = gui;
    } 
       
       private double flag = 1000000000; //flag to fill minDist and serializedDistance with large number
    
       
           @Override    
    protected int classifyTestSet(int sampleNo, int checkedClass, int flag){
        int Classification =flag; //class, 
        if (!isTrainSet(checkedClass,sampleNo)){ 
        double[][] classDist=  getKClassDist(checkedClass,sampleNo); //set of distances for each class class/dist.
            
            Classification = classifyForKSamples(classDist); // if -1 than no more action taken - classification not possible
            
            printClassifiedClass(Classification, sampleNo,checkedClass);
        }
        return Classification;
    } 
    
   
    
    
     /**
     * 
     * @param checkedClass class to which distance is checked
     * @param sampleNo number of sample (from total samples) that is now tested
     * @return array of k distances for class samples in increasing manner
     */
    private double[][] getKClassDist(int checkedClass, int sampleNo){
    double[][] classDist = new double[pr_gui.getClassCount()][pr_gui.getKSamplesCount()];
    
        if (!isTrainSet(checkedClass,sampleNo)){  //if sample in test set ...
            for(int j=0; j < pr_gui.getClassCount();j++){           //for all classes      
                classDist[j]= getClassDistanceKNN(sampleNo, j, checkedClass);    //determine k nearest distances          
            }
        }
    return classDist;
    }
    
    
    /**
     * Classifies the sample takin into account nearest k distances and their classes
     * @param classDist array of k distances for class samples in increasing manner
     * @return classification of the current sample
     */
     private int classifyForKSamples(double[][] classDist){
        double[][] classAndDistance = serializeOutput(classDist); 
        return determineClass(classAndDistance);
}
    
        protected double[] getClassDistanceKNN(int sampNo, int sampClass, int checkClass){
        
            double minDist[] = new double[pr_gui.getKSamplesCount()]; //make array of mindist with length of k
        Arrays.fill(minDist, flag);
        
        //Math.pow(100, 100);
        for (int checkNo = 0; checkNo < SplitData[checkClass][0].length; checkNo++){ //count of samples in class to be checked
            if (isTrainSet(checkClass,checkNo)&& notCheckingSample(sampClass,checkClass,sampNo,checkNo)){ //if eligible sample
                double[] currFeatureSet = new double[SplitData[0].length]; //set of features for current. checked training sample
                double[] checkPoint = new double[SplitData[0].length]; //set of features for checked test sample
                for (int i = 0; i<SplitData[0].length; i++){ //fill arrays with current data
                    currFeatureSet[i] = SplitData[sampClass][i][checkNo];
                    checkPoint[i] = SplitData[sampClass][i][sampNo];
                }
                double dist = makeEuklides(currFeatureSet, checkPoint);
                for (int k=minDist.length-1; k >= 0; k--){ //for row k replaces value if the current dist is lower and moves value in k to the next (higher) row
                   if(dist < minDist[k]){
                       if(minDist.length >= k+2) minDist[k+1] = minDist[k];
                       minDist[k] = dist;
                   } 
                }   
            }
        }    
        return minDist;
    }
 
   

 
   
    
    



private double[][] serializeOutput(double[][] classDist){
    double[][] minDistClass = changeOrderIncr(classDist);    //k minimum distances and respective class
    return minDistClass;
}
/**
 * Modifies the minimum distances for each class to set of minimum disances globally and their corresponding classes
 * @param classDist array with smallest distances for each class [class][distance]
 * @return array of smallest distances globally [0][i] distance [1][i] class
 */
private double[][] changeOrderIncr(double[][] classDist){
    double[][] serializedDistance = new double[2][classDist[0].length];
    Arrays.fill(serializedDistance[0], flag); Arrays.fill(serializedDistance[1], 0);
    
    for (int i=0; i<classDist.length; i++){ //ustawić najmniejszy największy z etykietą klasy i sprawdzić ignorując zera
        for (int j=0; j<classDist[i].length; j++){ //for number of distances
           for (int k=serializedDistance[0].length-1; k >= 0; k--){ //for row k replaces value if the current dist is lower and moves value in k to the next (higher) row
                   if(classDist[i][j] < serializedDistance[0][k]){
                       if(serializedDistance[0].length >= k+2)  {
                           serializedDistance[0][k+1] = serializedDistance[0][k]; //move up curr value in array if it has higher index
                           serializedDistance[1][k+1] = serializedDistance[1][k]; //move up class of this value
                       } 
                       serializedDistance[0][k] = classDist[i][j]; //replace curr value in array
                       serializedDistance[1][k] = i; //replace class
                   } else break; // if dist not lower than highest won't replace any lower index
                       
            }
        }
    }
    return serializedDistance;
}

protected int determineClass(double[][] classAndDistance){
    int Classifier =0;
    int[] classCount = setClassCount(classAndDistance);
     
    return classifyForK(classCount);
}

private int[] setClassCount(double[][] classAndDistance){
    int[] classCount = new int[pr_gui.getClassCount()];
    Arrays.fill(classCount, 0);
    for (int i =0; i<classAndDistance[0].length; i++){
        classCount[(int)classAndDistance[1][i]]++; //possible lossy conversion here!
    }
    return classCount;
}

private int classifyForK(int[] classCount){
    int classifiedClass=0;
    int maxVal=0;
    for (int i=0; i<classCount.length; i++){
        if (classCount[i] > maxVal ) {
            maxVal=classCount[i];
            classifiedClass=i;
        } else if (maxVal !=0 && classCount[i] == maxVal){
            classifiedClass=-1; //two or more classes have the same (highest) number of shortest distances in k, determination of class not possible
        }
    } 
    return classifiedClass;
}

}
