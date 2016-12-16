
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
    
    
        protected double[] getClassDistanceKNN(int sampNo, int sampClass, int checkClass){
        double flag = 1000000000; //flag to fill minDist with large number
            double minDist[] = new double[pr_gui.getKSamplesCount()];
        Arrays.fill(minDist, flag);
        
        Math.pow(100, 100);
        for (int checkNo = 0; checkNo < SplitData[sampClass][0].length; checkNo++){ //count of samples in class
            if (isTrainSet(checkClass,checkNo)&& notCheckingSample(sampClass,checkClass,sampNo,checkNo)){
                double[] currFeatureSet = new double[pr_gui.getFeatureCount()];
                double[] checkPoint = new double[pr_gui.getFeatureCount()];
                for (int i = 0; i<pr_gui.getFeatureCount(); i++){
                    currFeatureSet[i] = SplitData[sampClass][i][checkNo];
                    checkPoint[i] = SplitData[sampClass][i][sampNo];
                }
                double dist = makeEuklides(currFeatureSet, checkPoint);
                for (int k=0; k < minDist.length; k++){ //for row k replaces value if the current is lower and moves value in k to the next row
                   if(dist < minDist[k]){
                       if(minDist.length >= k+1) minDist[k+1] = minDist[k];
                       minDist[k] = dist;
                   } 
                }   
            }
        }    
        return minDist;
    }
 
   
    @Override    
    protected int classifyTestSet(int sampleNo, int checkedClass, int flag){
        int Classification =flag; //class, 
        double[][] classDist=  getKClassDist(checkedClass,sampleNo); //set of distances for each class class/dist.
            
            Classification = classifyForKSamples(classDist); //mamy klasę i dystanse od najmniejszego do największego w ilości k
            //może być dist = 0 - wtedy ignorować
            
            printClassifiedClass(Classification, sampleNo,checkedClass);
        
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
    
        if (!isTrainSet(checkedClass,sampleNo)){ 
            for(int j=0; j < pr_gui.getClassCount();j++){                 
                classDist[j]= getClassDistanceKNN(sampleNo, j, checkedClass);             
            }
        }
    return classDist;
    }
    
    
private int classifyForKSamples(double[][] classDist){
    int Classifier = 0;
    double[][] classAndDistance = serializeOutput(classDist); 
    Classifier = determineClass(classAndDistance);
    
    for (int i=0; i<classDist[0].length; i++){ //ustawić najmniejszy największy z etykietą klasy i sprawdzić ignorując zera
        
    } 
    return Classifier;
}


private double[][] serializeOutput(double[][] classDist){
    double[][] serializedDistance = changeOrder(classDist);    
    serializedDistance = sortDistance(serializedDistance);  
    return serializedDistance;
}

private double[][] changeOrder(double[][] classDist){
    double[][] serializedDistance = new double[2][classDist[0].length];
    for (int i=0; i<classDist.length; i++){ //ustawić najmniejszy największy z etykietą klasy i sprawdzić ignorując zera
       int treshold=0;
        for (int j=0; j<classDist[i].length; j++){
           serializedDistance[1][treshold] = classDist[i][j];
           serializedDistance[2][treshold] = i;              
            treshold++;
        }
        treshold+=classDist[i].length;
    }
    return serializedDistance;
}



}
