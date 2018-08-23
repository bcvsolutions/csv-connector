package eu.bcvsolutions.idm.connectors.csv.options;

import com.opencsv.*;
import eu.bcvsolutions.idm.connectors.csv.CSVConnConfiguration;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.Uid;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class for delleting items in CSV file
 *
 * @author Marek Klement
 */
public class DeleteItem extends Operations {

    private static final Log LOG = Log.getLog(CreateItem.class);

    public DeleteItem(CSVConnConfiguration conf) {
        super(conf);
    }

    /**
     * Checks if not uid not null, if we can get name and if user exists, then removes item
     * @param objectClass
     * @param uid
     * @param options
     * @return uid
     * @throws IOException
     */
    public Uid deleteItem(ObjectClass objectClass, Uid uid, OperationOptions options) throws IOException {
        if (uid == null) {
            throw new ConnectorException("DELETEITEM - Identifier must not be null!");
        }
        String name = uid.getUidValue();
        if (name == null || StringUtil.isBlank(name)) {
            throw new ConnectorException("DELETEITEM - Name was not provided!");
        }
        if (!userExists(name)) {
            throw new ConnectorException("DELETEITEM - Nothing to delete!");
        }
        removeItem(name);
        LOG.ok("ITEM WITH value [{}] was deleted successfully!", name);
        return new Uid(name);
    }

    /**
     * Removes actual item. Iterates over file and write down everything except the  item which should be deleted.
     * @param name
     * @throws IOException
     */
    private void removeItem(String name) throws IOException {
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(conf.getSeparator().charAt(0))
                .withIgnoreQuotations(true)
                .build();
        CSVReader reader = new CSVReaderBuilder(new FileReader(conf.getSourcePath()))
                .withCSVParser(parser)
                .build();
        Iterator<String[]> it = reader.iterator();
        List<String[]> buffer = new ArrayList<>();
        int uidNumber = getArrayNumber(headerFound, conf.getUid());
        while (it.hasNext()) {
            String[] line = it.next();
            if (!line[uidNumber].equals(name)) {
                buffer.add(line);
                System.out.println(line);
            }
        }
        reader.close();
        CSVWriter writer = new CSVWriter(new FileWriter(conf.getSourcePath()), conf.getSeparator().charAt(0), CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, "\n");
        writer.writeAll(buffer);
        writer.close();
    }
}
