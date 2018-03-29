package annotations;

import static annotations.CSVUtils.split;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

/**
 * Simple checks if there are images which would be part
 * of multiple datasets (and prints them on stdout if any found).
 * That's usually an indicator that something's wrong.
 */
public class CheckMultipleDatasets {

    public static void main(String[] args) throws IOException {
        // BEGIN Parameters

        String in = "idr0040-experimentA-filePaths.tsv";

        int datasetColumn = 0;
        int imageColumn = 1;
        char separator = '\t';

        // END Parameters

        BufferedReader r = new BufferedReader(new FileReader(in));

        HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();

        String line = null;
        while ((line = r.readLine()) != null) {
            String[] tmp = split(line, separator);
            String img = tmp[imageColumn];
            String ds = tmp[datasetColumn];

            if (!map.containsKey(img))
                map.put(img, new HashSet<String>());

            map.get(img).add(ds);
        }
        r.close();

        for (Entry<String, HashSet<String>> e : map.entrySet()) {
            if (e.getValue().size() > 1) {
                System.out.println("Image: " + e.getKey()
                        + " part of datasets:");
                for (String d : e.getValue())
                    System.out.println(d);
                System.out.println();
            }
        }
    }

}
