/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 * 
 * @author Sokol
 */
class Classifier {
    
    double[][] TrainingSet, TestSet;
    int[] ClassLabels;
    final int TRAIN_SET=0, TEST_SET=1;
    PR_GUI pr_gui = new PR_GUI();
 
    
    public void Classifier(PR_GUI gui){
        pr_gui = gui;
    }
/**
 * 
 * @param Dataset
 * @param TrainSetSize 
 */
    void generateTraining_and_Test_Sets(double[][] Dataset, String TrainSetSize){

        int[] Index = new int[Dataset[0].length];
        double Th = Double.parseDouble(TrainSetSize)/100.0;
        int TrainCount=0, TestCount=0;
        
        for(int i=0; i<Dataset[0].length; i++) 
            if(Math.random()<=Th) {
                Index[i]=TRAIN_SET;
                TrainCount++;
            }
            else {
                Index[i]=TEST_SET;
                TestCount++;
            }   
        TrainingSet = new double[Dataset.length][TrainCount];
        TestSet = new double[Dataset.length][TestCount];
        TrainCount=0;
        TestCount=0;
        // label vectors for training/test sets
        for(int i=0; i<Index.length; i++){
            if(Index[i]==TRAIN_SET){
                System.arraycopy(Dataset[i], 0, TrainingSet[TrainCount++], 0, Dataset[0].length);
            }
            else
                System.arraycopy(Dataset[i], 0, TestSet[TestCount++], 0, Dataset[0].length);                
        }
    }
    
    protected void trainClissifier(double[][] TrainSet){
        
    }
    
}
/* class NNClassifier extends Classifier {
    
    
    
    @Override
    protected void trainClissifier(double[][] TrainSet){
    
    }
}
*/