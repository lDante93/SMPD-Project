
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
   static Double[][][] SplitData; //data splits into classes (first ind is class) class/faetures/samples
   static int[] ClassLabels;
   static final int TRAIN_SET=0, TEST_SET=1;
   static int[][] trainOrTestSet; //[class], [trainOrTest(0,1)]
    PR_GUI pr_gui = new PR_GUI();
   Random generator = new Random();  
   //  needed only for bootstrap
    static double[][] Dataset;
    static int [][][] K_TrainOrTestSet; // [K][class], [trainOrTest(0,1)] an array keeping all the k sets of flags with trainOrTestSet[][] of abstractClassifier
    
   
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
 * Classifies all the samples that are marked as a test set from all classes.
 * Input data must be parsed and trained.
 */   
    public void classifySamples(){
        pr_gui.clearClassifier();
        setCenterPoints(); //ONLY USED IN KNM
        boolean [] classification = new boolean [getTrainCount()]; int n=0;
        for (int j=0;j<pr_gui.getClassCount();j++){ //check from this class
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
     * used only in kNM - makes possible to set the center points of clusters.
     */
    protected void setCenterPoints(){
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
    
   /**
    * Prints the classification of the current sample in pr_gui
    * @param classifiedClass class to which the sample is classified
    * @param sampleNo number of sample
    * @param checkedClass true class of sample
    */
    protected void printClassifiedClass(int classifiedClass,int sampleNo,int checkedClass){
        String nextClassifiedClass = classifiedClass+"("+sampleNo+","+checkedClass+"), ";
        pr_gui.addClassifyClasses(nextClassifiedClass);
        pr_gui.addClassifyLine(nextClassifiedClass);
    }
   
    
    /**
     * Classifies one specified sample to the class to which distance is the shortest.
     * Prints the classification on the console in pr_gui.
     * @param sampleNo number of sample to which distance is classified
     * @param checkedClass class to which belongs the test sample
     * @param flag flag if no classification took place
     * @return classification of the sample to the class
     */
    protected int classifyTestSet(int sampleNo, int checkedClass, int flag){
        int Classification =flag; //class, 
        double classDist = Math.pow(100, 1000); //distance
        if (!isTrainSet(checkedClass,sampleNo)){ 
            for(int j=0; j < pr_gui.getClassCount();j++){   //for classes
                
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
    
    /**
     * Determines the minimum distance of the specified class and specified test sample
     * @param sampNo number of sample
     * @param sampClass number of class
     * @param checkClass class to which distance is computed
     * @return minimum distance of the spec. sample to the spec. class
     */
    protected double getClassDistance(int sampNo, int checkClass, int sampClass){
        double minDist = Math.pow(100, 100);
        for (int checkNo = 0; checkNo < SplitData[sampClass][0].length; checkNo++){ //count of samples in class
            if (isTrainSet(checkClass,checkNo)&& notCheckingSample(sampClass,checkClass,sampNo,checkNo)){
                double[] currFeatureSet = new double[SplitData[0].length];
                double[] checkPoint = new double[SplitData[0].length];
                for (int i = 0; i<SplitData[0].length; i++){ //to feature count
                    currFeatureSet[i] = SplitData[checkClass][i][checkNo]; //set of features for checked training sample
                    checkPoint[i] = SplitData[sampClass][i][sampNo]; //set of features for checked test sample
                }
                double dist = makeEuklides(currFeatureSet, checkPoint);
                if(dist < minDist) minDist = dist;
            }
        }    
        return minDist;
    }
    
    /**
     * Checks if the currently checked training sample is not in fact this sample
     * @param sampClass true class of samle
     * @param checkClass class that is now checked
     * @param sampNo true number of sample
     * @param checkNo number of sample now checked
     * @return 
     */
    protected boolean notCheckingSample(int sampClass,int checkClass,int sampNo,int checkNo){
        if( sampClass==checkClass && sampNo==checkNo){
            return false;
        } else return true;
    }
   
    /**
     * Computes the training count
     * @return the amount of training samples
     */
    private int getTrainCount(){
        int count= 0;
        for (int j=0; j < pr_gui.getClassCount();j++){
            for (int i =0; i < trainOrTestSet[j].length; i++){
            if(trainOrTestSet[j][i] == 1) count++;
            }
        }
        return count;
    }
    /**
     * Computes an Euklides between the training and test sample in specified class
     * @param CurrFeatureSet feature set of training sample
     * @param checkPoint feature set of test sample
     * @return distance between the two points (test and training)
     */
    protected double makeEuklides(double[] CurrFeatureSet, double[] checkPoint){
        double sumOfSquares =0;
        for (int feature=0;feature<CurrFeatureSet.length;feature++){
        double length = (CurrFeatureSet[feature]-checkPoint[feature]); //do kwadratu!!
        sumOfSquares+=length*length;
        }
        return Math.sqrt(sumOfSquares);
    }
/**
 * checks if the sample checked is in training set
 * @param checkedClass class of checked sample
 * @param sample number of checked sample
 * @return 
 */
    protected boolean isTrainSet(int checkedClass, int sample){
        if(trainOrTestSet[checkedClass][sample] < 1){ 
            return true;
        }else return false;
    }
    
    
 
}
