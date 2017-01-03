
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
public class Cross_Validation {

    
    private double accuracyOfClassification;
    private double[][] F2;
    private ArrayList<Double[]> kSectionsArray;
    private int kSections;
    private int numberOfFeatures;
    private PR_GUI gui;
    Double[] sectionTab;

    public Cross_Validation(PR_GUI gui, int kSections) {
        this.gui = gui;
        this.kSections = kSections;

        metoda();
    }

    public void metoda() {
        int sectionLoopCount = 0;
        int sectionSamplesCount = 0;
        F2 = new double[gui.FNew.length][10];
        numberOfFeatures = 10 / kSections;

        for (int i = 0; i < F2.length; i++) {
            for (int j = 0; j < F2[i].length; j++) {
                F2[i][j] = gui.FNew[i][j];
            }
        }
//        for (int i = 0; i < 10; i++) {
//            F2[0][i] = i + 1;
//        }
        for (double[] x : F2) {
            System.out.println(Arrays.toString(x));
        }

        System.out.println(numberOfFeatures);
        kSectionsArray = new ArrayList<>();

        while (sectionLoopCount < kSections) {
            sectionTab = new Double[numberOfFeatures];
            for (int i = 0; i < 1; i++) {
                for (int j = sectionSamplesCount; j < sectionSamplesCount + numberOfFeatures; j++) {
                    sectionTab[j-sectionSamplesCount] = F2[i][j];
                }
                kSectionsArray.add(sectionTab);
            }

//            for (int i = 0; i < gui.FNew.length; i++) {
//                
//                for (int j = sectionSamplesCount; j < (sectionSamplesCount + gui.FNew.length); j++) {
//                    
//                    sectionTab[i][j - sectionSamplesCount] = F2[i][j];
//                }
            // System.out.println(Arrays.toString(sectionTab));
        
        kSectionsArray.add(sectionTab);
//                 for(Double[] x:kSectionsArray)
//        System.out.println(Arrays.toString(x));
//                    System.out.println();
//            }

        sectionSamplesCount = sectionSamplesCount + numberOfFeatures;
        sectionLoopCount++;
        }
        for (Double[] x : kSectionsArray) {

            System.out.println(Arrays.toString(x));
        }
        System.out.println("y)");
    }
}

// fr (int i = 0; i < 1; i++) {
//                sectionTab = new Double[numberOfFeatures][];
//                for (int j = sectionSamplesCount; j < (sectionSamplesCount + numberOfFeatures); j++) {
//                    
//                    sectionTab[j - sectionSamplesCount] = F2[i][j];
//                }
//               // System.out.println(Arrays.toString(sectionTab));
//                 kSectionsArray.add(sectionTab);
////                 for(Double[] x:kSectionsArray)
////        System.out.println(Arrays.toString(x));
////                    System.out.println();
////            }
//
//            sectionSamplesCount = sectionSamplesCount + numberOfFeatures;
//            sectionLoopCount++;
//
//        }    

