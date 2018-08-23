package eu.bcvsolutions.idm.connectors.csv.options;

import com.opencsv.*;
import eu.bcvsolutions.idm.connectors.csv.CSVConnConfiguration;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.Uid;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Class for update record in CSV file
 *
 * @author Marek Klement
 */
public class UpdateItem extends Operations {

    private static final Log LOG = Log.getLog(CreateItem.class);

    public UpdateItem(CSVConnConfiguration conf) {
        super(conf);
    }

    /**
     * Checks for if user exist and if uid is valid. Then updates current line
     * @param objectClass
     * @param uid
     * @param updateAttributes
     * @param options
     * @return
     * @throws IOException
     */
    public Uid updateItem(ObjectClass objectClass, Uid uid, Set<Attribute> updateAttributes, OperationOptions options) throws IOException {
        if (uid == null) {
            throw new ConnectorException("UPDATEITEM - Identifier must not be null!");
        }
        String uidValue = uid.getUidValue();
        if (uidValue == null || StringUtil.isBlank(uidValue)) {
            throw new ConnectorException("UPDATEITEM - Name was not provided!");
        }
        if (!userExists(uidValue)) {
            throw new ConnectorException("UPDATEITEM - User doesn't exists!");
        }
        if (updateAttributes == null) {
            throw new ConnectorException("UPDATEITEM - UpdateAttributes were not provided!");
        }
        update(updateAttributes, uidValue);
        LOG.ok("ITEM WITH value [{}] was updated successfully!", uidValue);
        return new Uid(uidValue);
    }

    /**
     * Rewrite whole file and updates line with needed uid
     * @param updateAttributes
     * @param name
     * @throws IOException
     */
    private void update(Set<Attribute> updateAttributes, String name) throws IOException {
        int uidNumber = getArrayNumber(headerFound, conf.getUid());
        boolean changed = false;
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(conf.getSeparator().charAt(0))
                .withIgnoreQuotations(true)
                .build();
        CSVReader reader = new CSVReaderBuilder(new FileReader(conf.getSourcePath()))
                .withCSVParser(parser)
                .build();
        Iterator<String[]> it = reader.iterator();
        List<String[]> buffer = new ArrayList<>();
        while (it.hasNext()) {
            String[] line = it.next();
            if (!line[uidNumber].equals(name)) {
                buffer.add(line);
            } else {
                line = createUpdatedLine(updateAttributes, line);
                buffer.add(line);
                changed = true;
            }
        }
        if (!changed) {
            throw new ConnectorException("Nothing was changed at all! check name setting!");
        }
        reader.close();
        CSVWriter writer = new CSVWriter(new FileWriter(conf.getSourcePath()), conf.getSeparator().charAt(0), CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, "\n");
        writer.writeAll(buffer);
        writer.close();
    }
}
