package annotations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import static annotations.CSVUtils.*;

/**
 * Removes non-ASCII gibberish
 */
public class RemoveNonASCII {

    public static void main(String[] args) throws Exception {
        
        // BEGIN Parameters
        
        String in = "idr0040-experimentA-annotation.csv";
        String out = "idr0040-experimentA-annotation_2.csv";
        
        char separator = ',';
        
        // END Parameters
        
        BufferedReader r = new BufferedReader(new FileReader(in));
        BufferedWriter w = new BufferedWriter(new FileWriter(out));
        
        String line = null;
        while((line = r.readLine())!=null) {
            String[] tmp = split(line, separator);
            for (int i=0; i<tmp.length; i++) {
                // non-ascii regex is [^\\p{ASCII}] 
                // but some fields in idr0040 contain non-ascii followed by underscore
                // (remove underscore as well)
                tmp[i] = tmp[i].replaceAll("[^\\p{ASCII}]+_?", "");
            }
            w.write(join(tmp, separator)+"\n");
        }
        r.close();
        w.close();
    }

}
