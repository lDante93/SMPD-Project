
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sokol
 */
public abstract class abstractClassifier {
   static double[][][] SplitData; //data splits into classes (first ind is class) class/faetures/samples
   static int[] ClassLabels;
   static final int TRAIN_SET=0, TEST_SET=1;
   static int[][] trainOrTestSet; //[class], [trainOrTest(0,1)]
    PR_GUI pr_gui = new PR_GUI();
   Random generator = new Random();  
   
   
/**
 * counts samples in class
 * @return 
 */
   public int getMaxSamplesInClass(){
       int maxSamples=0;
       for (int i=0;  i<pr_gui.ClassCount;i++){
           int currMax=0;
           for (int j=0;j< trainOrTestSet[i].length; j++){
               if(trainOrTestSet[i][j] == TEST_SET) currMax++;
           }
           if(currMax > maxSamples) maxSamples=currMax;
       }
       return maxSamples;
   } 
   
/**
 * Classifies the samples that are marked as a test set from all classes.
 * Input data must be parsed and trained.
 */   
    public void classifySamples(){
        pr_gui.clearClassifier();
        boolean [] classification = new boolean [getTrainCount()]; int n=0;
        for (int j=0;j<pr_gui.getClassCount();j++){ //check for this class
            for (int i=0; i < trainOrTestSet[j].length; i++){ //check of this sample,
                int flag=-1, classifiedClass=flag;
                classifiedClass = classifyTestSet(i,j, flag);
                if(classifiedClass!=flag){ //if is flag then ignore result
                classification[n++] = checkClassifiedSet(j,classifiedClass);
                }
            } 
       }
       printAccOfClassification(classification); //Acc accuracy
    }
       
       /**
        * checks if the class of the test sample is a class of the classification
        * @param currentClass true class of sample
        * @param classifiedClass class to which sample is classified
        * @return 
        */
     private boolean checkClassifiedSet(int currentClass, int classifiedClass){
        if(currentClass == classifiedClass){
            return true;
        } else return false;
    }
   /**
    * prints accuracy of classification to gui
    * @param classification set of good/bad guesses
    */
   private void printAccOfClassification (boolean[] classification){
        int goodGuess=0;
        for (int i=0; i<classification.length;i++){
            if(classification[i]==true) goodGuess++;
        }
        double y =100*(((double)goodGuess)/classification.length);
        pr_gui.setClassifierAcc(Integer.toString((int)y));
    }
    
    protected void printClassifiedClass(int classifiedClass,int sampleNo,int checkedClass){
        String nextClassifiedClass = classifiedClass+"("+sampleNo+","+checkedClass+"), ";
        pr_gui.addClassifyClasses(nextClassifiedClass);
        pr_gui.addClassifyLine(nextClassifiedClass);
    }
    
    protected int classifyTestSet(int sampleNo, int checkedClass, int flag){
        int Classification =flag; //class, 
        double classDist = Math.pow(100, 1000); //distance
        if (!isTrainSet(checkedClass,sampleNo)){ 
            for(int j=0; j < pr_gui.getClassCount();j++){   
                
                double currClassDist= getClassDistance(sampleNo, j, checkedClass); 
                if (currClassDist < classDist){
                    classDist = currClassDist;
                    Classification = j;
                }
                
            }
            printClassifiedClass(Classification, sampleNo,checkedClass);
        }
        return Classification;
    }
    
    protected double getClassDistance(int sampNo, int sampClass, int checkClass){
        double minDist = Math.pow(100, 100);
        for (int checkNo = 0; checkNo < SplitData[sampClass][0].length; checkNo++){ //count of samples in class
            if (isTrainSet(checkClass,checkNo)&& notCheckingSample(sampClass,checkClass,sampNo,checkNo)){
                double[] currFeatureSet = new double[SplitData[0].length];
                double[] checkPoint = new double[SplitData[0].length];
                for (int i = 0; i<SplitData[0].length; i++){ //to feature count
                    currFeatureSet[i] = SplitData[sampClass][i][checkNo]; //set of features for checked training sample
                    checkPoint[i] = SplitData[sampClass][i][sampNo]; //set of features for checked test sample
                }
                double dist = makeEuklides(currFeatureSet, checkPoint);
                if(dist < minDist) minDist = dist;
            }
        }    
        return minDist;
    }
    
    protected boolean notCheckingSample(int sampClass,int checkClass,int sampNo,int checkNo){
        if( sampClass==checkClass && sampNo==checkNo){
            return false;
        } else return true;
    }
   
    
    private int getTrainCount(){
        int count= 0;
        for (int j=0; j < pr_gui.getClassCount();j++){
            for (int i =0; i < trainOrTestSet[j].length; i++){
            if(trainOrTestSet[j][i] == 1) count++;
            }
        }
        return count;
    }
    
    protected double makeEuklides(double[] CurrFeatureSet, double[] checkPoint){
        double sumOfSquares =0;
        for (int feature=0;feature<CurrFeatureSet.length;feature++){
        double length = (CurrFeatureSet[feature]-checkPoint[feature]); //do kwadratu!!
        sumOfSquares+=length*length;
        }
        return Math.sqrt(sumOfSquares);
    }

    protected boolean isTrainSet(int checkedClass, int sample){
        if(trainOrTestSet[checkedClass][sample] < 1){ 
            return true;
        }else return false;
    }
    
    
 
}
