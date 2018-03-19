package annotations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import static annotations.CSVUtils.*;

/**
 * If a cell contains whitespaces at the start or end they will be
 * trimmed. Also trims whitespaces if a cell contains multiple values
 * separated by ';
 */
public class RemoveWhitespaces {

    public static void main(String[] args) throws Exception {

        // BEGIN Parameters

        String in = "/Users/dlindner/Repositories/idr-metadata/idr0040-aymoz-singlecell/experimentA/idr0040-experimentA-annotation.csv";
        String out = "/Users/dlindner/Repositories/idr-metadata/idr0040-aymoz-singlecell/experimentA/idr0040-experimentA-annotation_2.csv";

        // END Parameters
        
        BufferedReader r = new BufferedReader(new FileReader(in));
        BufferedWriter w = new BufferedWriter(new FileWriter(out));

        String line = null;
        while ((line = r.readLine()) != null) {
            String[] parts = split(line);
            String[] outparts = new String[parts.length];

            for (int i = 0; i < parts.length; i++) {
                if (parts[i].indexOf(';') > 0) {
                    String[] tmp = split(parts[i], ';');
                    for (int j = 0; j < tmp.length; j++)
                        tmp[j] = tmp[j].trim();
                    outparts[i] = join(tmp, ';');
                } else {
                    outparts[i] = parts[i].trim();
                }
            }

            w.write(join(outparts)+"\n");
        }
        r.close();
        w.close();
    }

}
