
import java.util.ArrayList;
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
    double[][] Dataset;
    int [][][] K_TrainOrTestSet; // [K][class], [trainOrTest(0,1)] an array keeping all the k sets of flags with trainOrTestSet[][] of abstractClassifier
    
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
    
    private double determineFinalResult(double[] resultTable){
        double temp=0;
        for (int i=0; i<resultTable.length;i++){
            temp+=resultTable[i];
        }
        return  (double)temp/resultTable.length;
    }
    
    private void printResult(double result){
        pr_gui.setClassifierAccur1(result);
        pr_gui.clearClassifierAccur();
    }
    
    /**
     * Makes Training of the data for classifier
     * @param gui 
     */
    bootstrap (PR_GUI gui) {
        pr_gui = gui;
        if(pr_gui.getFNew()==null){     // if no reduced feature space have been derived
                Dataset = pr_gui.getFNew();
            }   else{
               Dataset = pr_gui.getF();
            }
        
        splitClases(Dataset);
        makeTrainAndTestSets();
    }
    
    
      /**
  * splits Dataset into a new array used in all classifier [classes][features][samples]
  * @param Dataset input matrix of [features][samples] for all classes
  */
    private void splitClases(double[][] Dataset){
     SplitData = new double[pr_gui.getClassCount()][Dataset.length][Dataset[0].length/pr_gui.getClassCount()]; //class/features/samples
     int nLoop = 0;
     for (int i=0;i<pr_gui.getClassCount();i++){ //loop for class
         nLoop++; //current class
         for (int j=0;j<Dataset.length;j++){ //to feature count
             for (int k=0;k<Dataset[0].length/pr_gui.getClassCount();k++){ //to sample count
                 SplitData[i][j][k] = Dataset[j][k+(nLoop-1)*Dataset[0].length/pr_gui.getClassCount()];
             }
         }
     }
 }   
    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    protected void makeTrainAndTestSets(){
        K_TrainOrTestSet = new int[pr_gui.getKLoops()][pr_gui.getClassCount()][SplitData[0][0].length];
        
        for (int i=0; i<pr_gui.getKLoops();i++){
            K_TrainOrTestSet[i] = updateSets();
        }
    }
    
    private int[][] updateSets(){
        int[][] updatedSets = new int[pr_gui.getClassCount()][SplitData[0][0].length];
        for (int i=0;i<pr_gui.getClassCount();i++){
            updatedSets[i] = updateClass();
        }
        return updatedSets;
    }
    
    private int[] updateClass(){
        for (int i=0;i<SplitData[0][0].length){
            generator.nextInt(SplitData[0][0].length)
        }
    }
   
    
    protected double[] executeClassifier(){
            double[] classificationResults = new double[pr_gui.getKLoops()];
        for (int i=0; i<pr_gui.getKLoops(); i++){
            classifySample(pr_gui.getSelectionSelectClassMeth());
             classificationResults[i] = returnResult();
        }
        return classificationResults;
    }
    
    private double returnResult(){
        return pr_gui.getClassifierAccur();
    }
    
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
    
    
    protected void generateTrainOrTestSet(double[][] Dataset, String TrainSetSize){
        
        
    for (int i = 0; i< Dataset[0].length; i++){
            
            int randomNumb = generateRandom(1, Dataset[0].length);
            excludeRows.add(randomNumb);
            if(((double) randomNumb)/Dataset[0].length < Th) {
                trainOrTestSet[classNo][getProperI(i, classNo)]=TRAIN_SET;
            }
            else {
                trainOrTestSet[classNo][getProperI(i, classNo)]=TEST_SET;
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
