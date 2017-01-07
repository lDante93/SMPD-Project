
import java.util.ArrayList;
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Dante
 */
public class Cross_Validation extends abstractClassifier{

    private double accuracyOfClassification;
    private double[][] F2;
    private ArrayList<Double[]> kSectionsArray;
    private Double[] kSectionsArray2;
    private Double[][][] SplitData;
    private int kSections;
    private int numberOfFeatures;
    private PR_GUI gui;
    private Double[] sectionTab;
    private int[] numberOfSector;

    public Cross_Validation(PR_GUI gui, int kSections) {
        this.gui = gui;
        this.kSections = kSections;

        metoda();
    }

    public void metoda() {
        int sectionLoopCount = 0;
        int sectionSamplesCount = 0;
        int amountOfSamples = gui.getClassLabels().length;
        F2 = new double[gui.FNew.length][amountOfSamples];
        int countLoop = 0;
        numberOfFeatures = amountOfSamples / kSections;

        for (int i = 0; i < F2.length; i++) {
            for (int j = 0; j < F2[i].length; j++) {
                F2[i][j] = gui.FNew[i][j];
            }
        }
        for (double[] x : F2) {
            System.out.println(Arrays.toString(x));
        }

        System.out.println(numberOfFeatures);
        kSectionsArray = new ArrayList<>();
        kSectionsArray2 = new Double[amountOfSamples];
        numberOfSector=new int[amountOfSamples];
        
        
        while (sectionLoopCount < kSections) {
            sectionTab = new Double[numberOfFeatures];
            for (int i = 0; i < 1; i++) {
                for (int j = sectionSamplesCount; j < sectionSamplesCount + numberOfFeatures; j++) {
                    sectionTab[j - sectionSamplesCount] = F2[i][j];
                    numberOfSector[j]=sectionLoopCount;
                }

            }

            for (int i = 0; i < sectionTab.length; i++) {
                kSectionsArray2[countLoop] = sectionTab[i];
                countLoop++;
            }

            sectionSamplesCount = sectionSamplesCount + numberOfFeatures;
            sectionLoopCount++;
        }


        SplitData = new Double[2][gui.FNew.length][kSectionsArray2.length];
        for (int i = 0; i < kSectionsArray2.length; i++) {
            if (i < gui.getSampleCount()[0]) {
                SplitData[0][0][i] = kSectionsArray2[i];
               
            } else if ((i < gui.getSampleCount()[1])) {
                SplitData[1][0][i] = kSectionsArray2[i];
            }
             if(SplitData[0][0][i]==null)
                {
                    SplitData[0][0][i]=-1.01;
                }
             if(SplitData[1][0][i]==null)
                {
                    SplitData[1][0][i]=-1.01;
                }
        }
//        SplitData=Arrays.stream(SplitData)
//                     .filter(s -> (s != null && SplitData.length > 0))
//                     .toArray(Double[][]::new);
   
super.SplitData=this.SplitData;
        System.out.println("y)");
    }
}


