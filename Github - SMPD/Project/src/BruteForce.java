/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sokol
 */

import Jama.Matrix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Sokol
 */
public class BruteForce {

    ArrayList<int[]> combspairs = new ArrayList<int[]>(); //pairs of combinations
    double[][] filteredF; //transformed feature matrix
    LinkedHashMap<Integer, Double> filteredFeatures = new LinkedHashMap<Integer, Double>();
    int[] SampleCount;
    PR_GUI pr_gui = new PR_GUI();
    
    
    public BruteForce(PR_GUI gui, int[] flags, int d, int FeatureCount, int[] samplecount) {
        pr_gui = gui;
        SampleCount = Arrays.copyOf(samplecount, samplecount.length);
        selectFeatures(flags, d, FeatureCount);
    }

    /**
     * Selects features
     *
     * @param flags vector... array of index.length =featureCount
     * @param d FS dimension
     *
     */
    private void selectFeatures(int[] flags, int d, int FeatureCount) {
        // for now: check all individual features using 1D, 2-class Fisher criterion

        if (d == 1) {
            double FLD = 0, tmp;
            int max_ind = -1;

            for (int i = 0; i < FeatureCount; i++) {
              
                if ((tmp = computeFisherLD(pr_gui.getF()[i])) > FLD) {
                    FLD = tmp;
                    max_ind = i;
                }
            }
            pr_gui.l_FLD_winner.setText(max_ind + "");
            pr_gui.l_FLD_val.setText(FLD + "");
            // to do: compute for higher dimensional spaces, use e.g. SFS for candidate selection

        } else if (d > 1) {  //higher dimensional spaces

            filterFeatures(64, FeatureCount);
            combspairs.clear();         //usuwa poprzednie pary
            int[] combinations = new int[d];

            //System.out.println("combinations:  " + Arrays.toString(combinations));
            getCombs(FeatureCount, d, combinations, d);   //return all pairs to combspairs array
            for (int[] x : combspairs) // 2D ArrayList of Int[]
            {
                //System.out.println("Pary: " + Arrays.toString(x));
            }

            double maxFisher = 0, temp;
            int[] winnerPair = new int[d];
            for (int[] pair : combspairs) {
                ArrayList<double[]> vecs = new ArrayList<double[]>();
                for (int i = 0; i < pair.length; i++) {
                    vecs.add(filteredF[pair[i] - 1]);
                }
                for (double[] x : vecs) {
                    //System.out.println("Vecs: " + Arrays.toString(x));
                }

                if ((temp = computeFisherLD2D(vecs)) > maxFisher) {
                    maxFisher = temp;
                    winnerPair = pair;
                }
                //System.out.println("pair: " + Arrays.toString(pair));
            }
            Object[] originalFeatures = filteredFeatures.keySet().toArray();

            //System.out.println("originalFeatures: " + Arrays.toString(originalFeatures));

            String winners = "";
            for (int pair : winnerPair) {
                winners += originalFeatures[pair - 1] + " (" + pair + "), ";
            }
            //l_FLD_winner.setText(originalFeatures[winnerPair[0]-1] +" (" + winnerPair[0] + 
            //        ") and "+originalFeatures[winnerPair[1]-1] + " (" + winnerPair[1] + ")");
            pr_gui.l_FLD_winner.setText(winners);
            pr_gui.l_FLD_val.setText(maxFisher + "");
        }
    }

    /**
     *
     * @param vecs
     * @return
     */
    private double computeFisherLD2D(ArrayList<double[]> vecs) {
        double detA = 0, detB = 0, diff = 0;
        //System.out.println("Vec size: " + vecs.size());
        double[] mA = new double[vecs.size()];
        double[] mB = new double[vecs.size()];

        for (int i = 0; i < mA.length; i++) {
            mA[i] = mB[i] = 0;
        }
        double[][] xa = new double[vecs.size()][SampleCount[0]];
        double[][] xb = new double[vecs.size()][SampleCount[1]];
        //System.out.println();

        for (int i = 0, j = 0, k = 0; i < vecs.get(0).length; i++) {
            if (pr_gui.getClassLabels()[i] == 0) {
                for (int a = 0; a < vecs.size(); a++) {
                    mA[a] += vecs.get(a)[i];
                    xa[a][j] = vecs.get(a)[i];
                }

                j++;
            } else {
                for (int a = 0; a < vecs.size(); a++) {
                    mB[a] += vecs.get(a)[i];
                    xb[a][k] = vecs.get(a)[i];
                }

                k++;
            }
        }
        for (int i = 0; i < xa.length; i++) {
            for (int j = 0; j < xa[i].length; j++) {
                //System.out.print(xa[i][j] + " ");
            }
            //System.out.println();
        }
        for (int i = 0; i < xb.length; i++) {
            for (int j = 0; j < xb[i].length; j++) {
                //System.out.print(xb[i][j] + " ");
            }
            //System.out.println();
        }

        for (int i = 0; i < mA.length; i++) {
            mA[i] /= SampleCount[0];
            mB[i] /= SampleCount[1];
        }

        //System.out.println("mA: " + Arrays.toString(mA));
        //System.out.println("mB: " + Arrays.toString(mB));

        double[][] avgA = new double[vecs.size()][SampleCount[0]];
        double[][] avgB = new double[vecs.size()][SampleCount[1]];
        for (int i = 0; i < SampleCount[0]; i++) {
            for (int j = 0; j < mA.length; j++) {
                avgA[j][i] = mA[j];
            }
        }
        for (int i = 0; i < SampleCount[1]; i++) {
            for (int j = 0; j < mB.length; j++) {
                avgB[j][i] = mB[j];
            }
        }
        Matrix AvgA = new Matrix(avgA);
        Matrix AvgB = new Matrix(avgB);
        Matrix Xa = new Matrix(xa);
        Matrix Xb = new Matrix(xb);
        Xa = Xa.minus(AvgA);
        Matrix XaT = Xa.transpose();
        Matrix Sa = Xa.times(XaT);
        Matrix Sb = Xb.minus(AvgB).times((Xb.minus(AvgB)).transpose());
        detA = Sa.det();
        detB = Sb.det();
        double sum = 0;
        for (int i = 0; i < mA.length; i++) {
            sum += (mA[i] - mB[i]) * (mA[i] - mB[i]);
        }
        diff = Math.sqrt(sum);
        return diff / (detA + detB);
    }

    /**
     *
     * @param n feature count
     * @param rest FS dimensions
     * @param pairs array of FS dimension with combinations
     * @param k FS dimensions
     */
    private void getCombs(int n, int rest, int[] pairs, int k) {
        for (int i = n; i >= rest; i--) {
            pairs[rest - 1] = i; //?????????
            if (rest > 1) {
                getCombs(i - 1, rest - 1, pairs, k);
            } else {
                int[] temp = new int[pairs.length];
                for (int j = 0; j < pairs.length; j++) {
                    temp[j] = pairs[j]; //temp z parami
                }
                combspairs.add(temp);    //dodaje int[] do combspairs       
            }
        }
    }

    /**
     *
     * @param vec computes Fisher for FS=1
     * @return Fisher discriminant
     */
    private double computeFisherLD(double[] vec) {
        // 1D, 2-classes
        double mA = 0, mB = 0, sA = 0, sB = 0;
        for (int i = 0; i < vec.length; i++) {
            if (pr_gui.getClassLabels()[i] == 0) {
                mA += vec[i]; //meanA
                sA += vec[i] * vec[i]; // deltaA^2
                //System.out.println(mA + sA);

            } else {
                mB += vec[i]; // meanB
                sB += vec[i] * vec[i]; //deltaB^2
                //System.out.println(mA + sA);
            }
        }
        mA /= SampleCount[0];
        mB /= SampleCount[1];
        sA = sA / SampleCount[0] - mA * mA;
        sB = sB / SampleCount[1] - mB * mB;
        //System.out.println(mA + mB + sA + sB + (Math.abs(mA - mB) / (Math.sqrt(sA) + Math.sqrt(sB))));
        return Math.abs(mA - mB) / (Math.sqrt(sA) + Math.sqrt(sB));
    }

    /**
     * Obrócenie tablicy
     *
     * @param d
     */
    private void filterFeatures(int d, int FeatureCount) {

        filteredFeatures.clear();
        filteredF = new double[d][];

        for (int i = 0; i < FeatureCount; i++) { //FeatureCount - liczba cech - (liczba w wierszu w notatniku)
            filteredFeatures.put(i, computeFisherLD(pr_gui.getF()[i])); //dodaje do linkedHashMap filteredFeatures obliczonego fishera 1 wymiarowego pod dany int
        }

        for (int i = 0; i < FeatureCount - d; i++) {
            Integer minKey = filteredFeatures.entrySet().iterator().next().getKey(); //najmniejszy klucz
            double minVal = filteredFeatures.get(minKey);//najmniejsza wartosc

            for (Integer key : filteredFeatures.keySet()) {  //szuka najmniejszej wartosci dla danego klucza sposrod filtered features
                if (filteredFeatures.get(key) < minVal) {
                    minKey = key;
                    minVal = filteredFeatures.get(key);
                }
            }
            filteredFeatures.remove(minKey);
        }
        int i = 0;
        for (Integer key : filteredFeatures.keySet()) {
            filteredF[i] = pr_gui.getF()[key];
            System.out.println("Filtered F[" + i + "]: " + Arrays.toString(filteredF[i]));
            i++;
        }
    }


}

