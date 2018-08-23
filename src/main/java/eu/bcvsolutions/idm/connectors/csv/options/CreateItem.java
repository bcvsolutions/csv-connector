package eu.bcvsolutions.idm.connectors.csv.options;

import com.opencsv.CSVWriter;
import eu.bcvsolutions.idm.connectors.csv.CSVConnConfiguration;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.Uid;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

/**
 * Class for creating items in CSV file from system
 *
 * @author Marek Klement
 */
public class CreateItem extends Operations {

    private static final Log LOG = Log.getLog(CreateItem.class);

    public CreateItem(CSVConnConfiguration conf) {
        super(conf);
    }

    /**
     * Checks if name is not null and if user exists and then creates new item
     * @param objectClass
     * @param createAttributes
     * @param options
     * @return Uid
     * @throws IOException
     */
    public Uid createItem(ObjectClass objectClass, Set<Attribute> createAttributes, OperationOptions options) throws IOException {
        String name = findName(createAttributes);
        if (name == null || StringUtil.isBlank(name)) {
            throw new ConnectorException("CREATEITEM - Name was not provided!");
        }
        if (userExists(name)) {
            throw new ConnectorException("CREATEITEM - User already exists!");
        }
        addItem(createAttributes);
        LOG.ok("ITEM WITH value [{}] was created successfully!", name);
        return new Uid(name);
    }

    /**
     * Writes new item to the file. Actually it writes all file again plus new item
     * @param createAttributes
     * @throws IOException
     */
    private void addItem(Set<Attribute> createAttributes) throws IOException {
        final CSVWriter writer = new CSVWriter(new FileWriter(conf.getSourcePath(), true), conf.getSeparator().charAt(0), CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, "\n");
        String[] blankLine = new String[headerFound.length];
        String[] nextLine = createUpdatedLine(createAttributes, blankLine);
        writer.writeNext(nextLine);
        writer.close();
    }
}