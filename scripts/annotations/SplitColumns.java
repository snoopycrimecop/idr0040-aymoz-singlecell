package annotations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static annotations.CSVUtils.*;

/**
 * Splits a column with multiple entries into multiple columns (one for each entry).
 * 
 * E.g. If you split a column 'Comment [Gene Identifier]' which contains multiple entries
 * in some rows separated by semicolon, e. g. 'GENE1;GENE2', in the output file the 
 * 'Comment [Gene Identifier]' column will be removed, and instead there will be two new
 * columns 'Comment [Gene Identifier] 1' (for 'GENE1') and 'Comment [Gene Identifier] 2' 
 * (for 'GENE2'). 
 */
public class SplitColumns {

    public static void main(String[] args) throws Exception {
        
        // BEGIN Parameters
        
        String in = "/Users/dlindner/Repositories/idr-metadata/idr0040-aymoz-singlecell/experimentA/idr0040-experimentA-annotation_2.csv";
        String out = "/Users/dlindner/Repositories/idr-metadata/idr0040-aymoz-singlecell/experimentA/idr0040-experimentA-annotation_3.csv";

        // The column to split
        String columnToSplit = "Comment [Gene Identifier]";
        
        // END Parameters
        
        BufferedReader r = new BufferedReader(new FileReader(in));
        BufferedWriter w = new BufferedWriter(new FileWriter(out));

        String line = null;
        List<String> header = new ArrayList<String>();
        int n = 0;
        int columnIndex = -1;
        while((line = r.readLine())!=null) {
            String[] parts = split(line);
            if (header.isEmpty()) {
                for (int i=0; i<parts.length; i++) {
                    if (!parts[i].equals(columnToSplit)) {
                       header.add(parts[i]);
                    }
                    else {
                        columnIndex = i;
                    }
                }
                continue;
            }
            else {
                String[] tmp = split(parts[columnIndex], ';');
                if (tmp.length > n) {
                    n = tmp.length;
                }
            }
        }
        r.close();
        
        for (int i=0; i<n; i++) {
            header.add(columnToSplit+" "+(i+1));
        }
        
        String[] tmp = new String[header.size()];
        tmp = header.toArray(tmp);
        w.write(join(tmp)+"\n");
        
        r = new BufferedReader(new FileReader(in));
        r.readLine(); // skip header
        while((line = r.readLine())!=null) {
            String[] outline = new String[header.size()];
            
            String[] parts = split(line);
            int outIndex = 0;
            for (int i=0; i<parts.length; i++) {
                if (i != columnIndex) {
                    outline[outIndex] = parts[i];
                    outIndex++;
                }
            }
            
            String[] parts2 = split(parts[columnIndex], ';');
            for (int i=0; i<parts2.length; i++) {
                outline[outIndex+i] = parts2[i];
            }
                    
            w.write(join(outline)+"\n");
        }

        r.close();
        w.close();
    }
}
