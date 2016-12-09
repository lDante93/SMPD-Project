
import Jama.Matrix;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sokol
 */
public class FeatureExtraction {
    
    FeatureExtraction(PR_GUI gui){
        double TotEnergy=Double.parseDouble(gui.gettf_PCA_Energy())/100.0;
            // Target dimension (if k>0) or flag for energy-based dimension (k=0)
            int k=0;
//            double[][] FF = { {1,1}, {1,2}};
//            double[][] FF = { {-2,0,2}, {-1,0,1}};
            // F is an array of initial features, FNew is the resulting array
            double[][] FFNorm = centerAroundMean(gui.F); 
            Matrix Cov = computeCovarianceMatrix(FFNorm);
            Matrix TransformMat = extractFeatures(Cov,TotEnergy, k);     
            gui.FNew = projectSamples(new Matrix(FFNorm),TransformMat);
            // FNew is a matrix with samples projected to a new feature space
            gui.setFeatureCount(gui.FNew.length/gui.getClassCount());

            gui.setl_NewDim(gui.getFNew().length+"");
    }
    
    
    /**
     * 
     * @param M
     * @return 
     */
    private double[][] centerAroundMean(double[][] M) {
        
        double[] mean = new double[M.length];
        for(int i=0; i<M.length; i++)
            for(int j=0; j<M[0].length; j++)
                mean[i]+=M[i][j];
        for(int  i=0; i<M.length; i++) mean[i]/=M[0].length;
        for(int i=0; i<M.length; i++)
            for(int j=0; j<M[0].length; j++)
                M[i][j]-=mean[i];
        return M;
    }

    private double[][] projectSamples(Matrix FOld, Matrix TransformMat) {
        
        return (FOld.transpose().times(TransformMat)).transpose().getArrayCopy();
    }

    
    /**
     * 
     * @param m
     * @return 
     */
    private Matrix computeCovarianceMatrix(double[][] m) {
//        double[][] C = new double[M.length][M.length];
        
        Matrix M = new Matrix(m);
        Matrix MT = M.transpose();       
        Matrix C = M.times(MT);
        return C;
    }

       
/**
 * 
 * @param C
 * @param Ek
 * @param k
 * @return 
 */    
    private Matrix extractFeatures(Matrix C, double Ek, int k) {               
        
        Matrix evecs, evals;
        // compute eigenvalues and eigenvectors
        evecs = C.eig().getV();
        evals = C.eig().getD();
        
        // PM: projection matrix that will hold a set dominant eigenvectors
        Matrix PM;
        if(k>0) {
            // preset dimension of new feature space
//            PM = new double[evecs.getRowDimension()][k];
            PM = evecs.getMatrix(0, evecs.getRowDimension()-1, 
                    evecs.getColumnDimension()-k, evecs.getColumnDimension()-1);
        }
        else {
            // dimension will be determined based on scatter energy
            double TotEVal = evals.trace(); // total energy
            double EAccum=0;
            int m=evals.getColumnDimension()-1;
            while(EAccum<Ek*TotEVal){
                EAccum += evals.get(m, m);
                m--;
            }
            PM = evecs.getMatrix(0, evecs.getRowDimension()-1,m+1,evecs.getColumnDimension()-1);
        }

/*            System.out.println("Eigenvectors");                
            for(int i=0; i<r; i++){
                for(int j=0; j<c; j++){
                    System.out.print(evecs[i][j]+" ");
                }
                System.out.println();                
            }
            System.out.println("Eigenvalues");                
            for(int i=0; i<r; i++){
                for(int j=0; j<c; j++){
                    System.out.print(evals[i][j]+" ");
                }
                System.out.println();                
            }
*/
        
        return PM;
    }

    
}
