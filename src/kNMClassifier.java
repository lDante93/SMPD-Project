
import java.util.ArrayList;
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * TUTAJ JESZCZE BRAK SUPPORTU DO NIEROWNOMIERNEJ ILOSCI SAMPLES!!!!!!!!!!!!!!!!!!!!!!
 *source for k-means algorithm onmyphd.com/?p=k-means.clustering
 * @author Sokol
 */
public class kNMClassifier extends abstractClassifier{
    
    kNMClassifier(PR_GUI gui) {
        pr_gui = gui;
    } 
    
    double[][][] centerPoints; //[class][k-center number][feature value]
    int[][] clusterClassification; //[class][classification] classification of respective points in class to clusters
   boolean isChangeInPointsClassification=true;
  
   /**
    * Sets all center points of clusters to their final, optimized positions.
    * 
    */
   @Override
  protected void setCenterPoints(){
    initializeCenterPoints();
    while (isChangeInPointsClassification){ //stops when no more point changes its flag to cluster
        isChangeInPointsClassification=false;
        getClosestPoints(); //updates isChangeInPointsClassification
        setCenterPosition();
    }

   } 
  /**
   * Determine and flag closest points to each cluster center.
   * changes clusterClassification(flag) so that it is valid for current Center position of Clusters.
   */
  private void getClosestPoints(){
       for (int i=0; i<pr_gui.getClassCount(); i++){ //for each class
           for (int j=0; j<pr_gui.SampleCount[i] /*SplitData[i][0].length*/; j++){ //for each point of this class         
                    double currDist = 1000000000; //distance from point to current cluster centre
                    int classification=-1;
                   double[] CurrFeatureSet = initializeCurrFeatureSet(i,j); //make set of features of the point
                   for (int k=0; k<pr_gui.getKSamplesCount(); k++){ //for each cluster centre
                        double checkPoint[] = initializeCheckPoint(i,k); //make set of features for one cluster centre
                        if (currDist > makeEuklides(CurrFeatureSet, checkPoint)){ //change classification
                            currDist = makeEuklides(CurrFeatureSet, checkPoint);
                            classification=k;
                        }
                   }
                   if (clusterClassification[i][j] != classification){
                       clusterClassification[i][j] = classification;
                       isChangeInPointsClassification=true;
                   }      
           }
       }
  }
  
  /**
   * Makes feateres set of the point in test set
   * @param currClass class of this test point
   * @param currPoint number of curr point
   * @return  array with all features of current point
   */
   private double[] initializeCurrFeatureSet(int currClass, int currPoint ) {
       double[] CurrFeatureSet = new double[SplitData[currClass].length];
        for (int k=0; k<SplitData[currClass].length; k++){ //for each feature
            CurrFeatureSet[k]=SplitData[currClass][k][currPoint];
        }
        return CurrFeatureSet;
    }
   
   /**
    * Makes features set of the current center point of cluster
    * @param currClass class in which the curr center point is
    * @param currClassCentre number of the current center point (current cluster)
    * @return array with all features of current center point of cluster
    */
   private double[] initializeCheckPoint(int currClass, int currClassCentre ) {
       double[] CurrFeatureSet = new double[SplitData[currClass].length];
        for (int k=0; k<SplitData[currClass].length; k++){ //for each feature of this class and class centre
            CurrFeatureSet[k]=centerPoints[currClass][currClassCentre][k];
        }
        return CurrFeatureSet;
    }
  
  /**
   * Initializes the center points of all clusters.
   */
  private void initializeCenterPoints(){
      
      makeRandomPartition(); //random partition used to initialize centers
      setCenterPosition(); //creates points for cluster centres
  }
  /**
   * Sets the center (mean) points of clusters taking the current clusterClassification of each sample to one of clusters.
   * clusterClassification cannot be empty to init method.
   * Updates centerPoints[][][] each time invoked.
   */
  private void setCenterPosition(){
      centerPoints = new double[pr_gui.getClassCount()][pr_gui.getKSamplesCount()][SplitData[0].length];
      for (int i=0; i<pr_gui.getClassCount(); i++){ //for each class
          for (int j=0; j<centerPoints[i].length; j++){ //for each center             
            for (int k=0; k<SplitData[i].length; k++){ //for each feature
                int clusterSize=0;
                double clusterCumulValue=0; //cumulative value of center point feature 
                for (int l=0; l<pr_gui.SampleCount[i]; l++){ //for each point TU SAMPLE COUNT OF CLASS
                    if (clusterClassification[i][l] == j){
                        clusterSize++;
                        clusterCumulValue+=SplitData[i][k][l];
                    }
                }
                if (clusterSize != 0){ // assign cluster position
                    centerPoints[i][j][k]=(double)clusterCumulValue/clusterSize;
                } else centerPoints[i][j][k]=Math.pow(100, 1000); //if nothing assigned, give a big value not to interfere with other computations
            }
          }
      }
  }
  /**
   * Makes partition of the samples randomly to one of the cluster centres.
   * Overrides the clusterClassification each time invoked.
   */
  private void makeRandomPartition(){
      clusterClassification = new int[pr_gui.getClassCount()][determineMaxSamples()];
      for (int o=0; o<pr_gui.getClassCount();o++){
          Arrays.fill(clusterClassification[o], -1); //gives flag for all samples, which stays for those out of sample count for this class
      }
      for (int i=0; i<pr_gui.getClassCount(); i++){ //for class number
          for (int j=0; j<pr_gui.SampleCount[i]; j++){ //for samples number in class
              clusterClassification[i][j] = generator.nextInt(pr_gui.getKSamplesCount()); //make random cluster classification
          }
      }
      
  }
  /**
   * Gets the minimum distance between the class center Points (centres of clusters) and test point
   * @param sampNo number of test point
   * @param sampClass class of test point
   * @param checkClass class of train point (center points of Class)
   * @return 
   */
  @Override
 protected double getClassDistance(int sampNo, int checkClass, int sampClass){
        double minDist = Math.pow(100, 100);
        for (int checkNo = 0; checkNo < centerPoints[sampClass].length; checkNo++){ //count of centres in class
                double[] currFeatureSet = new double[SplitData[0].length];
                double[] checkPoint = new double[SplitData[0].length];
                for (int i = 0; i<SplitData[0].length; i++){ //to feature count
                    currFeatureSet[i] = centerPoints[checkClass][checkNo][i]; //set of features for checked training sample
                    checkPoint[i] = SplitData[sampClass][i][sampNo]; //set of features for checked test sample
                }
                double dist = makeEuklides(currFeatureSet, checkPoint);
                if(dist < minDist) minDist = dist;
            
        }    
        return minDist;
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



}
