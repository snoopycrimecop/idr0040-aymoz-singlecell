package rendering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static annotations.CSVUtils.*;

/**
 * Creates a tab separated mapping between renderings settings files and the datasets they have
 * to be applied, by scanning through the filePaths.tsv file.
 * 
 * For example the filePaths.tsv contains lines like that for the images which get imported:
 * Dataset:name:YSP692_AGA1y_FIG1r_bar1D_DoseResponsePheromoneTreatment /uod/idr/filesets/idr0040-aymoz-singlecell/20180215/3105/Pos0
 * 
 * And a rendering file is called like: 
 * 3105_ome_rend.json
 * 
 * The link between dataset and rendering settings can be made via the path component '3105', i.e.
 * will result in an output line like:
 * 3105_ome_rend.json   YSP692_AGA1y_FIG1r_bar1D_DoseResponsePheromoneTreatment
 * 
 * The output file can then be used to apply the specific rendering settings to each image of 
 * a dataset.
 */
public class RenderingSettingsMapping {

    public static void main(String[] args) throws Exception {
        
        // BEGIN Parameters
        
        String filepathsFilename = "/Users/dlindner/Repositories/idr-metadata/idr0040-aymoz-singlecell/experimentA/idr0040-experimentA-filePaths.tsv";
        
        // Directory containing the rendering setting files
        String renderingDirname = "/Users/dlindner/Repositories/idr-metadata/idr0040-aymoz-singlecell/rendering_settings";
        
        String outputFilename = "/Users/dlindner/Repositories/idr-metadata/idr0040-aymoz-singlecell/rendering_settings/mapping.tsv";
        
        // END Parameters
        
        // Dataset -> PathId
        Map<String, String> mapping = new HashMap<String, String>();
        BufferedReader r = new BufferedReader(new FileReader(filepathsFilename));
        String line = null;
        while((line = r.readLine())!=null) {
            // Dataset:name:YSP692_AGA1y_FIG1r_bar1D_DoseResponsePheromoneTreatment /uod/idr/filesets/idr0040-aymoz-singlecell/20180215/3105/Pos0   Pos0
            
            String[] tmp = split(line, '\t');
            
            String[] col0 = split(tmp[0], ':');
            String ds = col0[col0.length-1];
            String[] col1 = split(tmp[1], '/');
            String pathId = col1[col1.length-2];
            
            if (!mapping.containsKey(ds))
                mapping.put(ds, pathId);
        }
        r.close();
        
        BufferedWriter w = new BufferedWriter(new FileWriter(outputFilename));
        
        for(Entry<String, String> e : mapping.entrySet()) {
            String ds = e.getKey();
            String pathId = e.getValue();
            
            // e. g. 2807_ome_rend.json
            String rendFileName = pathId+"_ome_rend.json";
            File rendFile = new File(renderingDirname, rendFileName);
            if (rendFile.exists())
                w.write(join(new String[]{ds, rendFileName}, '\t')+"\n");
        }
        w.close();
    }
}
