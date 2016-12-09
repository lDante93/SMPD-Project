
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
public class SequentialForwardSelection {
     ArrayList<int[]> combspairs = new ArrayList<int[]>(); //pairs of combinations
    double[][] filteredF; //transformed feature matrix
    LinkedHashMap<Integer, Double> filteredFeatures = new LinkedHashMap<Integer, Double>();
    int[] SampleCount;
    PR_GUI pr_gui = new PR_GUI();
    
   public SequentialForwardSelection(PR_GUI gui) {
        pr_gui = gui;
        
        //selectFeatures(flags, d, FeatureCount);
    }
    
  
    
}
