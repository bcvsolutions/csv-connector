package eu.bcvsolutions.idm.connectors.csv.options;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import eu.bcvsolutions.idm.connectors.csv.CSVConnConfiguration;
import eu.bcvsolutions.idm.connectors.csv.CSVConnFilter;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for holding functions to parse CSV into object
 *
 * @author Marek Klement
 */
public class ExecuteQuerry {

    private final CSVConnConfiguration conf;
    private final CSVConnFilter filter;
    private static final Log LOG = Log.getLog(ExecuteQuerry.class);

    public ExecuteQuerry(CSVConnConfiguration conf, CSVConnFilter filter) {
        this.conf = conf;
        this.filter = filter;
    }

    public List<ConnectorObject> parse() throws IOException {
        return readIt();
    }

    /**
     * Check config, parse header and then iterates over each line and makes new objects
     * @return List of objects for system
     * @throws IOException
     */
    private List<ConnectorObject> readIt() throws IOException {
        List<ConnectorObject> items = null;
        CSVReader reader = null;
        final CSVParser parser;
        parser = new CSVParserBuilder()
                .withSeparator(conf.getSeparator().charAt(0))
                .withIgnoreQuotations(true)
                .build();
        reader = new CSVReaderBuilder(new FileReader(conf.getSourcePath()))
                .withCSVParser(parser)
                .build();
        Iterator<String[]> it = reader.iterator();
        headerSetter(it);
        if (conf.getName() == null) {
            conf.setName(conf.getUid());
        }
        checkNameAndUidPresence(conf.getName(), conf.getUid());
        LOG.info("STARTED to read CSV");
        items = itterate(it);
        LOG.ok("All objects were created successfully!");
        reader.close();
        return items;
    }

    /**
     * Iterates whole file and creating from each line new object
     * @param it
     * @return objects list
     */
    private List<ConnectorObject> itterate(Iterator<String[]> it) {
        List<ConnectorObject> items = new LinkedList<>();
        while (it.hasNext()) {
            String[] item = it.next();
            final ConnectorObject obj = transform(conf.getHeader(), item, conf.getName(), conf.getUid());
            if (filter != null) {
                if (filter.evaluate(obj)) {
                    items.add(obj);
                    LOG.ok("OBJECT WITH value [{}] was added successfully!", obj.getName().getNameValue());
                }
            } else {
                items.add(obj);
            }
        }
        return items;
    }

    /**
     * Sets header
     * @param it
     * @throws IOException
     */
    private void headerSetter(Iterator<String[]> it) throws IOException {
        if (conf.isIncludesHeader()) {
            conf.setHeader(it.next());
        }
    }

    /**
     * Transform each line into new object to system
     * @param header
     * @param line
     * @param name
     * @param uid
     * @return connector object
     */
    private ConnectorObject transform(String[] header, String[] line, String name, String uid) {
        ConnectorObjectBuilder builder = new ConnectorObjectBuilder();
        for (int i = 0; i < header.length; i++) {
            String head = header[i];
            String lin = line[i];
            if (head.equals(name)) {
                builder.setName(lin);
            }
            if (head.equals(uid)) {
                builder.setUid(lin);
            }
            builder.addAttribute(head, lin);
        }
        return builder.build();
    }

    private void checkNameAndUidPresence(String name, String uid) {
        String[] header = conf.getHeader();
        boolean isName = false;
        boolean isUid = false;
        for (String item : header) {
            if (item.equals(name)) {
                isName = true;
            }
            if (item.equals(uid)) {
                isUid = true;
            }
        }
        if (!isName) {
            throw new ConnectorException("Wrong declaration of __NAME__ in configuration class: " + name);
        }
        if (!isUid) {
            throw new ConnectorException("Wrong declaration of __UID__ in configuration class: " + uid);
        }
    }

}
