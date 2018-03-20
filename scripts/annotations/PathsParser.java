package annotations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import static annotations.CSVUtils.*;

/**
 * For Micromanager images the assembled image will have a random name
 * of one of the images it was assembled from. It makes more sense to assign
 * a custom image name, for example the directory name the single images were part of.
 * 
 * This script does that by adding another column for the image name in the filepaths.tsv .
 * 
 * In detail:
 * It takes the original filePaths.tsv containing lines like:
 * Dataset:name:YSP692_AGA1y_FIG1r_bar1D_DoseResponsePheromoneTreatment /uod/idr/filesets/idr0040-aymoz-singlecell/20180215/3105/Pos0 
 * 
 * ...and writes another filePath.tsv with an additional column for the image name:
 * Dataset:name:YSP692_AGA1y_FIG1r_bar1D_DoseResponsePheromoneTreatment /uod/idr/filesets/idr0040-aymoz-singlecell/20180215/3105/Pos0   Pos0
 * 
 * ... which is simply the last part of the file path 'PosX'.
 */
public class PathsParser {

    public static void main(String[] args) throws Exception {
        
        // BEGIN Parameters
        
        String in = "idr0040-experimentA-filePaths.tsv";
        String out = "idr0040-experimentA-filePaths_2.tsv";
        
        // END Parameters
        
        BufferedReader r = new BufferedReader(new FileReader(in));
        BufferedWriter w = new BufferedWriter(new FileWriter(out));
        
        String line = null;
        while((line = r.readLine())!=null) {
            String[] tmp = split(line, '\t');
            String[] tmp2 = tmp[1].split("/");
            
            String[] outline = new String[tmp.length+1];
            for(int i=0; i<tmp.length; i++)
                outline[i] = tmp[i];
            outline[tmp.length] = tmp2[tmp2.length-1];
            w.write(join(outline, '\t')+"\n");
        }
        r.close();
        w.close();
    }

}
