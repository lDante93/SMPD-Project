
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Dante
 */
public class Cross_Validation extends abstractClassifier {

    private double accuracyOfClassification;

    private Double[][] kSectionsArray;
    private Double[][][] SplitData;
    private int kSections;
    private int numberOfFeatures;
    private PR_GUI pr_gui = new PR_GUI();
    private Double[][] sectionTab;
    private Integer[][] numberOfSector;
    private Integer[][] numberOfSectorArray;
    private Integer[][] numberOfSectorArrayCopy;
    private Integer[][] numberOfClass;
    private Integer[][] numberOfClassArray;
    private Integer K_TrainOrTestSet[][][];

    Cross_Validation(PR_GUI gui, int i, int j) {
        pr_gui = gui;
        double[] resultTable = executeClassifier();
        double resultOfClassification = determineFinalResult(resultTable);
        printResult(resultOfClassification);
    }

    private void printResult(double result) {
        pr_gui.setClassifierAccur1(result);
        pr_gui.clearClassifierAccur();
    }

    private double determineFinalResult(double[] resultTable) {
        double temp = 0;
        for (int i = 0; i < resultTable.length; i++) {
            temp += resultTable[i];
        }
        return (double) temp / resultTable.length;
    }

    public Cross_Validation(PR_GUI gui, int kSections) {
        this.pr_gui = gui;
        this.kSections = kSections;

        makeSplitClassAndTrainingSets();
    }

    protected double[] executeClassifier() {
        double[] classificationResults = new double[pr_gui.getKSecs()];

        for (int i = 0; i < 1; i++) {
            super.trainOrTestSet = super.K_TrainOrTestSet[i];
            classifySample(pr_gui.getSelectionSelectClassMethAdv());
            classificationResults[i] = returnResult();
        }
        return classificationResults;
    }

    private double returnResult() {
        return pr_gui.getClassifierAccur();
    }

    private void classifySample(String selectionMessage) {
        if (selectionMessage.equals("Nearest neighbor (NN)")) {
            NNClassifier CNN = new NNClassifier(pr_gui);
            CNN.classifySamples();
        } else if (selectionMessage.equals("Nearest Mean (NM)")) {
            NMClassifier CNM = new NMClassifier(pr_gui);
            CNM.classifySamples();
        } else if (selectionMessage.equals("k-Nearest Neighbor (k-NN)")) {
            kNNClassifier CkNN = new kNNClassifier(pr_gui);
            CkNN.classifySamples();
        } else if (selectionMessage.equals("k-Nearest Mean (k-NM)")) {
            kNMClassifier CkNM = new kNMClassifier(pr_gui);
            CkNM.classifySamples();
        }
    }

    public void makeSplitClassAndTrainingSets() {
        int sectionLoopCount = 0;
        int sectionSamplesCount = 0;
        int amountOfSamples = pr_gui.getClassLabels().length;
        int[] countLoop = new int[pr_gui.FNew.length];
        int[] classLabelsPerArray = new int[pr_gui.getClassCount()];
        for (int i = 0; i < pr_gui.getSampleCount().length; i++) {
            classLabelsPerArray[i] = (pr_gui.getSampleCount()[i]) / (kSections);
        }
        numberOfFeatures = amountOfSamples / kSections;
        kSectionsArray = new Double[pr_gui.FNew.length][amountOfSamples];
        numberOfSector = new Integer[pr_gui.FNew.length][numberOfFeatures];
        numberOfSectorArray = new Integer[pr_gui.FNew.length][amountOfSamples];
        numberOfSectorArrayCopy = new Integer[pr_gui.FNew.length][amountOfSamples];
        numberOfClass = new Integer[pr_gui.FNew.length][numberOfFeatures];
        numberOfClassArray = new Integer[pr_gui.FNew.length][amountOfSamples];
        int samplesInSectorCount = 0;
        int classCounter = kSections - 1;
        while (sectionLoopCount < kSections) {

            sectionTab = new Double[pr_gui.FNew.length][numberOfFeatures];
            for (int i = 0; i < pr_gui.FNew.length; i++) {
                for (int j = sectionSamplesCount; j < sectionSamplesCount + numberOfFeatures; j++) {

                    if (samplesInSectorCount < (classLabelsPerArray[0] * (sectionLoopCount + 1))) {
                        sectionTab[i][j - sectionSamplesCount] = pr_gui.FNew[i][samplesInSectorCount];
                        numberOfSector[i][j - sectionSamplesCount] = sectionLoopCount;
                        numberOfClass[i][j - sectionSamplesCount] = pr_gui.getClassLabels()[samplesInSectorCount];
                        samplesInSectorCount++;
                    } else if (samplesInSectorCount >= (classLabelsPerArray[0] * (sectionLoopCount + 1))) {

                        sectionTab[i][(j - sectionSamplesCount)] = pr_gui.FNew[i][j + classLabelsPerArray[0] * (classCounter)];
                        numberOfSector[i][j - sectionSamplesCount] = sectionLoopCount;
                        numberOfClass[i][j - sectionSamplesCount] = pr_gui.getClassLabels()[j + classLabelsPerArray[0] * (classCounter)];
                    }

                }

            }
            for (int i = 0; i < pr_gui.FNew.length; i++) {

                for (int j = 0; j < sectionTab[0].length; j++) {
                    kSectionsArray[i][countLoop[i]] = sectionTab[i][j];
                    numberOfSectorArray[i][countLoop[i]] = numberOfSector[i][j];
                    numberOfClassArray[i][countLoop[i]] = numberOfClass[i][j];
                    countLoop[i]++;
                }

            }
            sectionSamplesCount = sectionSamplesCount + numberOfFeatures;
            sectionLoopCount++;
            classCounter--;
        }

        Double[][] kSectionsArray2 = new Double[kSectionsArray.length][amountOfSamples];

        kSectionsArray2 = kSectionsArray;
        for (int i = 0; i < kSectionsArray2.length; i++) {

            for (int j = 0; j < kSectionsArray2[i].length; j++) {
                if (kSectionsArray2[i][j] == null) {
                    kSectionsArray2[i][j] = -1.01;
                }
            }
        }
//        int c = 0;
//
//        for (int i = 0; i < kSectionsArray.length; i++) {
//            ArrayList<Object> list = new ArrayList<Object>();
//            for (int j = 0; j < kSectionsArray[i].length; j++) {
//                if (kSectionsArray[i][j] != null) {
//                    list.add(kSectionsArray[i][j]);
//                }
//            }
//            kSectionsArray2[c++] = list.toArray(new Double[list.size()]);
//        }

        int correctLength = (kSectionsArray2[0].length / pr_gui.getClassCount()) * pr_gui.getClassCount();
        int samplesInClass = kSectionsArray2[0].length / pr_gui.getClassCount();

        SplitData = new Double[pr_gui.getClassCount()][pr_gui.FNew.length][amountOfSamples];
        for (Double[][] a : SplitData) {
            for (Double[] b : a) {
                Arrays.fill(b, -1.01);
            }
        }

        for (int j = 0; j < pr_gui.FNew.length; j++) {
            for (int k = 0; k < correctLength; k++) {
                if (numberOfClassArray[0][k] == null) {
                    numberOfClassArray[0][k] = -1;
                }
                if (numberOfSectorArray[0][k] == null) {
                    numberOfSectorArray[0][k] = -1;
                }

                if (numberOfClassArray[0][k] == 0) {
                    SplitData[0][j][k] = kSectionsArray2[j][k];
                } else if (numberOfClassArray[0][k] == 1) {
                    SplitData[1][j][k] = kSectionsArray2[j][k];
                }
            }
        }

        super.SplitData = this.SplitData;
        K_TrainOrTestSet = new Integer[kSections][pr_gui.getClassCount()][correctLength];
        for (Integer[][] a : K_TrainOrTestSet) {
            for (Integer[] b : a) {
                Arrays.fill(b, -1);
            }
        }
        numberOfSectorArrayCopy = numberOfSectorArray;
        int lengthOfSectorArray = numberOfSectorArray[0].length / kSections;
        int numberOfLoopSections = 0;
        for (int x = 0; x < kSections; x++) {
            if (kSections <= 2) {
                int sector = lengthOfSectorArray / kSections;
                if (x == 0) {
                    for (int c = 0; c < numberOfSectorArray[0].length; c++) {
                        if (c < sector) {
                            numberOfSectorArray[0][c] = 1;
                        } else {
                            numberOfSectorArray[0][c] = 0;
                        }
                    }
                    numberOfLoopSections = 1;
                }
            }
            if (kSections > 2) {
                int sector = lengthOfSectorArray / kSections;
                if (x == 0) {
                    for (int c = 0; c < numberOfSectorArray[0].length; c++) {
                        if (c < sector) {
                            numberOfSectorArray[0][c] = 1;
                        } else {
                            numberOfSectorArray[0][c] = 0;
                        }
                    }
                    numberOfLoopSections = 1;
                }
                if (x == numberOfLoopSections) {
                    for (int c = 0; c < numberOfSectorArray[0].length; c++) {
                        if (c >= sector && c < sector * (numberOfLoopSections + 1)) {
                            numberOfSectorArray[0][c] = 1;
                        } else {
                            numberOfSectorArray[0][c] = 0;
                        }
                    }
                    numberOfLoopSections++;
                }

            }

            for (int j = 0; j < kSections; j++) {

                for (int k = 0; k < correctLength; k++) {

                    if (numberOfClassArray[0][k] == 0) {
                        if (numberOfSectorArray[0][k] == j) {
                            K_TrainOrTestSet[x][0][k] = j;

                        } else if (numberOfClassArray[0][k] == 1) {
                            if (numberOfSectorArray[0][k] == j) {
                                K_TrainOrTestSet[x][1][k] = j;

                            }
                        }
                    }
                }
            }
//            if (kSections == 2) {
//                for (int k = 0; k < correctLength; k++) {
//                    if (numberOfSectorArray[0][k] == 0) {
//                        numberOfSectorArray[0][k] = 1;
//                    } else if (numberOfSectorArray[0][k] == 1) {
//                        numberOfSectorArray[0][k] = 0;
//                    }
//
//                }
//            }

        }

        super.K_TrainOrTestSet = this.K_TrainOrTestSet;

        
    }

}
