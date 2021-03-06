package annotations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

import static annotations.CSVUtils.*;

/**
 * Merges several CSV files into a single CSV file. First line of the CSV files
 * must be a header line! Ignores entries starting with a '#' (also whole
 * columns if their header starts with a '#'!)
 */
public class CSVMerger {

    // BEGIN Parameters
    
    // The Excel files /uod/idr/filesets/idr0040-aymoz-singlecell/20180215/idr0000-experimentB-assays_*.xls
    // saved as *.csv
    static final String[] files = {
            "idr0000-experimentB-assays_DoseResponsesSupFigs.csv",
            "idr0000-experimentB-assays_MatingFig.csv",
            "idr0000-experimentB-assays_PheromoneTmtMainFig.csv",
            "idr0000-experimentB-assays_PheromoneTmtSupFig.csv"};

    static final String outFile = "idr0040-experimentA-annotation.csv";
    
    // END Parameters
    
    public static void main(String[] args) throws Exception {
        BufferedWriter out = new BufferedWriter(new FileWriter(outFile));

        // Parse first line of each file to get the headers
        HashSet<String> tmp = new LinkedHashSet<String>();
        for (String file : files) {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = in.readLine();
            String[] split = split(line);
            for (String s : split) {
                if (s.length() > 0 && !s.startsWith("#"))
                    tmp.add(s);
            }
            in.close();
        }

        // The headers
        String[] headers = new String[tmp.size()];
        // The headers mapped to their position (column index) in the output file
        HashMap<String, Integer> pos = new HashMap<String, Integer>();
        int i = 0;
        for (String header : tmp) {
            headers[i] = header;
            pos.put(header, new Integer(i));
            i++;
        }

        // write header line
        out.write(join(headers) + "\n");

        // merge the input csv files together
        for (String file : files) {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = in.readLine();
            
            // the headers of this particular input csv
            String[] thisHeaders = split(line);

            while ((line = in.readLine()) != null) {
                String[] parts = split(line);
                
                // the assembled output line
                String[] outline = new String[headers.length];
                
                // iterate over each column
                for (int col = 0; col < parts.length; col++) {
                    if (parts[col].startsWith("#"))
                        continue;
                    
                    // get the correct column index for the output
                    Integer outIndex = pos.get(thisHeaders[col]);
                    if (outIndex != null)
                        outline[outIndex.intValue()] = parts[col];
                }
                out.write(join(outline) + "\n");
            }
            in.close();
        }

        out.close();
    }

}
