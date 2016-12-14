
/*
must implement create FNew!!!!!!!
*/

import Jama.Matrix;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Fisher {

    private PR_GUI gui;
    private ArrayList<int[]> combspairs = new ArrayList<int[]>();

    public Fisher(PR_GUI gui) {
        this.gui = gui;
        selectFeatures(Integer.parseInt((String) gui.getSelbox_nfeat1().getSelectedItem())); //calling a selectFeatures method  with a selected fisher dimension as a parameter
    }

    public void selectFeatures(int d) { //argument d as a selected by user dimension

        if (d == 1) { //computing 1 dimension fisher
            Fisher1D();
        } else if (d > 1) {
            if (gui.getFSalgorithm().getSelectedItem().toString().equals("BruteForce")) {
                BruteForce(d);
            } else {
                SFS(d);
            }
        }
    }

    private void Fisher1D() {
        double FLD = 0, tmp; //fisher linear discriminant
        int max_ind = -1;
        for (int i = 0; i < gui.getFeatureCount(); i++) { //FeatureCount is number of 64 features (for maple oak)
            if ((tmp = computeFisher1D(gui.getF()[i])) > FLD) //F[i] is an array that have values of one of the feature (perpendicularly column in the notepad)
            { //if computed fisher1d for a f[i]  is greater than FLD, then is set as tmp (for example for maple oak it will be the fifth feature that is greater than first feature)

                FLD = tmp;
                max_ind = i;
            }
        }
        gui.getjTextArea1().setText(max_ind + ""); //it shows the index of the best fisher (but it counts from 0 , so the last feature of the 64elements could be 63)
        gui.getl_FLD_val().setText(FLD + ""); //it shows the best computed fisher (the highest)
    }

    private double computeFisher1D(double[] vec) {

        double mA = 0, mB = 0, sA = 0, sB = 0; //mean and deviation

        for (int i = 0; i < vec.length; i++) { //vec is a perpendicular column of F array

            if (gui.getClassLabels()[i] == 0) {//classLabel is a group of objects; 0 is for example an A class, and 1 is B class          
                mA += vec[i];
                sA += vec[i] * vec[i];
            } else {
                mB += vec[i];
                sB += vec[i] * vec[i];
            }
        }
        mA /= gui.getSampleCount()[0];
        mB /= gui.getSampleCount()[1];
        sA = sA / gui.getSampleCount()[0] - mA * mA;
        sB = sB / gui.getSampleCount()[1] - mB * mB; //the number of samples (the horizontally rows in a txt)

        return Math.abs(mA - mB) / (Math.sqrt(sA) + Math.sqrt(sB)); //it is the other way to calculate fisher (not like on laboratories)
    }

    private void BruteForce(int d) {
        combspairs.clear();         //delete previous pairs

        int[] combinations = new int[d];//declares an combination array, for example if d==2, then its [0,0]
    
        getCombs(gui.getFeatureCount(), d, combinations, d);   //return all pairs to combspairs array

        double maxFisher = 0, temp;
        int[] winnerPair = new int[d]; //declares a winner array, for example if d==2, then its [0,0]
        for (int[] pair : combspairs) {//the purpose of this loop is to make from for example array[5,6] an array of [objects of 5th feature, objects of 6th feature]; 
            ArrayList<double[]> vecs = new ArrayList<double[]>();
            for (int i = 0; i < pair.length; i++) {
                vecs.add(gui.F[pair[i] - 1]);//vec is an array for example[x,y], where x and y is an another array of an objects of selected feature
            }
            if ((temp = computeFisherBruteForceND(vecs)) > maxFisher) {//it will give the higheest computed fisher and a pair
                maxFisher = temp;
                winnerPair = pair; //winner pair is the for example 2 dimension selected: a pair [1,3]
            }
        }

        String winners = "";
        for (int pair : winnerPair) {
            winners += " (" + pair + "), \n";
        }
        gui.getjTextArea1().setText(winners);
        gui.getl_FLD_val().setText(maxFisher + "");
    }

    private double computeFisherBruteForceND(ArrayList<double[]> vecs) {
        double detA = 0, detB = 0, diff = 0;

        double[] mA = new double[vecs.size()]; //declares an array of vecs.size so of the selected dimension size
        double[] mB = new double[vecs.size()];

        for (int i = 0; i < mA.length; i++) {//we 0 all of the components
            mA[i] = mB[i] = 0;

        }
        double[][] xa = new double[vecs.size()][gui.getSampleCount()[0]];//declares an array of dimension as a rows, and sample count as a columns(for example 4 for an A class)
        double[][] xb = new double[vecs.size()][gui.getSampleCount()[1]];

        for (int i = 0, j = 0, k = 0; i < vecs.get(0).length; i++) {
            if (gui.getClassLabels()[i] == 0) {//for class A

                for (int a = 0; a < vecs.size(); a++) {
                    mA[a] += vecs.get(a)[i];//a sum for all of  the vec objects
                    xa[a][j] = vecs.get(a)[i];//a it only gives the numbers to the arrays of multi-dimensional array

                }
                j++;

            } else {//for class B 
                for (int a = 0; a < vecs.size(); a++) {
                    mB[a] += vecs.get(a)[i];
                    xb[a][k] = vecs.get(a)[i];
                }
                k++;
            }
        }

        for (int i = 0; i < mA.length; i++) {//computing mean
            mA[i] /= gui.getSampleCount()[0];
            mB[i] /= gui.getSampleCount()[1];
        }

        double[][] avgA = new double[vecs.size()][gui.getSampleCount()[0]];//declares an arrays of rows as vec size, and columns as numbers of sample(for example 2x4)
        double[][] avgB = new double[vecs.size()][gui.getSampleCount()[1]];
        for (int i = 0; i < gui.getSampleCount()[0]; i++) {//into columns are put a value of mean(for example for 2 classes avg[0][i] have 1.25, and avg[1][i] have 1,5
            for (int j = 0; j < mA.length; j++) {
                avgA[j][i] = mA[j];
            }
        }
        for (int i = 0; i < gui.getSampleCount()[1]; i++) {
            for (int j = 0; j < mB.length; j++) {
                avgB[j][i] = mB[j];
            }
        }
        Matrix AvgA = new Matrix(avgA);//converts a two-dimensionarrays into a matrix classes
        Matrix AvgB = new Matrix(avgB);
        Matrix Xa = new Matrix(xa);
        Matrix Xb = new Matrix(xb);
        Xa = Xa.minus(AvgA);
        Matrix XaT = Xa.transpose();
        Matrix Sa = Xa.times(XaT); //Sa = (Xa - AvgA)(Xa - AvgA)T
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

    private void SFS(int d) {
        combspairs.clear();         //delete previous pairs
        double[] set = new double[d];//declares an array of the given dimension
        int[] winner = null;//declares an array of the winner set
        for (int step = 0; step < d; step++) {
            winner = computeFisherSFSND(step + 1, winner);//
        }
    }

    private int[] computeFisherSFSND(int d, int[] previous) {
        double maxFisher = 0, temp;
        int[] winnerSet = new int[d];
        int[] combinations = new int[d];
        if (previous == null) {//if there were not computed SFS-fisher before
            getCombs(gui.getFeatureCount(), d, combinations, d);   //return all pairs to combspairs array, 
        } else {
            for (int i = 1; i <= gui.getFeatureCount(); i++) {//number of features (columns in .txt)
                if (!isAlready(i, previous)) {
                    int[] set = new int[d];
                    System.arraycopy(previous, 0, set, 0, previous.length);
                    set[d - 1] = i;
                    combspairs.add(set);
                }
            }
        }
        for (int[] pair : combspairs) {
            ArrayList<double[]> vecs = new ArrayList<double[]>();
            for (int i = 0; i < pair.length; i++) {
                vecs.add(gui.getF()[pair[i] - 1]);
            }

            if ((temp = computeFisherBruteForceND(vecs)) > maxFisher) {
                maxFisher = temp;
                winnerSet = pair;
            }
        }
      
        String winners = "";
        for (int winner : winnerSet) {
            winners += " (" + winner + "), \n";
        }
        gui.getjTextArea1().setText(winners);
        gui.getl_FLD_val().setText(maxFisher + "");
        return winnerSet;
    }

    private boolean isAlready(int i, int[] array) {
        for (int element : array) {
            if (element == i) {
                return true;
            }
        }
        return false;
    }

    private void getCombs(int n, int rest, int[] pairs, int k) { //it makes an array of cobinations, for example if dimension=rest=2 it makes first[5,6] array, then [4,6] etc.. then [4,5], [3,5]

        for (int i = n; i >= rest; i--) {

            pairs[rest - 1] = i;

            if (rest > 1) {
                getCombs(i - 1, rest - 1, pairs, k);
            } else {
                int[] temp = new int[pairs.length];

                for (int j = 0; j < pairs.length; j++) {

                    temp[j] = pairs[j];

                }
                combspairs.add(temp); //after this loop we have all of the combspairs
            }

        }
    }
}
