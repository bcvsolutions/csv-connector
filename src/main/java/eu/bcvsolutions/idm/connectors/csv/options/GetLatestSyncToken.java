package eu.bcvsolutions.idm.connectors.csv.options;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import eu.bcvsolutions.idm.connectors.csv.CSVConnConfiguration;
import org.identityconnectors.framework.common.objects.SyncToken;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

/**
 * When we need latest token this class will figure out which one is lowest
 *
 * @author Marek Klement
 */

public class GetLatestSyncToken extends Operations {

    public GetLatestSyncToken(CSVConnConfiguration conf) {
        super(conf);
    }

    /**
     * Finds the lowest token by iterating over in file
     * @return lowest token
     * @throws IOException
     */
    public SyncToken getLatToken() throws IOException {
        String latest = null;
        final CSVReader reader;
        final CSVParser parser;
        parser = new CSVParserBuilder()
                .withSeparator(conf.getSeparator().charAt(0))
                .withIgnoreQuotations(true)
                .build();
        reader = new CSVReaderBuilder(new FileReader(conf.getSourcePath()))
                .withCSVParser(parser)
                .build();
        Iterator<String[]> it = reader.iterator();
        if (conf.isIncludesHeader()) {
            conf.setHeader(it.next());
        }
        int headerNum = getArrayNumber(conf.getHeader(), conf.getSyncTokenColumn());
        while (it.hasNext()) {
            String[] line = it.next();
            String token = line[headerNum];
            if (latest == null) {
                latest = token;
            } else {
                if (latest.compareTo(token) < 0) {
                    latest = token;
                }
            }
        }
        reader.close();
        return new SyncToken(latest);
    }
}
