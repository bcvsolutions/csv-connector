package eu.bcvsolutions.idm.connectors.csv.options;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import eu.bcvsolutions.idm.connectors.csv.CSVConnConfiguration;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.spi.Connector;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class creates schema from given CSV file (Configuration path).
 *
 * @author Marek Klement
 */
public class CreateSchema {

    private final CSVConnConfiguration conf;
    private static final Log LOG = Log.getLog(CreateSchema.class);

    public CreateSchema(CSVConnConfiguration conf) {
        this.conf = conf;
    }

    /**
     * Takes first line and make it into header - crates schema. Also checks for length of each line by iterator.
     * @param connector
     * @return schema
     * @throws IOException
     */
    public Schema generateSchema(Connector connector) throws IOException {
        String[] header = null;
        final CSVParser parser = new CSVParserBuilder()
                .withSeparator(conf.getSeparator().charAt(0))
                .withIgnoreQuotations(true)
                .build();
        CSVReader reader = new CSVReaderBuilder(new FileReader(conf.getSourcePath()))
                .withCSVParser(parser)
                .build();
        Iterator<String[]> it = reader.iterator();
        if (conf.isIncludesHeader()) {
            header = findHeader(it);
        } else if (!conf.isIncludesHeader() && conf.getHeader() != null) {
            checkLength(it, conf.getHeader().length, 1);
            header = conf.getHeader();
        } else {
            throw new ConnectorException("CREATESCHEMA - Wrong header found!");
        }
        reader.close();
        return createSchema(connector, header);
    }

    /**
     * Finds and parses header
     * @param it
     * @return header
     */
    private String[] findHeader(Iterator<String[]> it) {
        String[] header;
        if (it.hasNext()) {
            header = it.next();
            checkLength(it, header.length, 2);
        } else {
            throw new ConnectorException("CREATESCHEMA - CSV file is blank, cannot resolve header!");
        }
        LOG.ok("Header found successfully!");
        return header;
    }

    /**
     * Checks if length of header is same as each line
     * @param it
     * @param length
     * @param i
     */
    private void checkLength(Iterator<String[]> it, int length, int i) {
        while (it.hasNext()) {
            String[] next = it.next();
            if (next.length != length) {
                throw new ConnectorException("CREATESCHEMA - At " + i + ". line is " + next.length + " items, but expect " + length);
            }
            ++i;
        }
        LOG.ok("All lines have same amount of items!");
    }

    /**
     * Creates new schema by schema and attribute info builder
     * @param connector
     * @param header
     * @return schema by builder
     */
    private Schema createSchema(Connector connector, String[] header) {
        SchemaBuilder sb = new SchemaBuilder(connector.getClass());
        final Set<AttributeInfo> attributeInfos = new HashSet<>();
        if (conf.getName() == null) {
            conf.setName(conf.getUid());
        }
        for (String column : header) {
            final AttributeInfoBuilder attributeInfoBuilder = new AttributeInfoBuilder();
            if (column.equals(conf.getUid()) || column.equals(conf.getName())) {
                attributeInfoBuilder.setRequired(true);

            }
            attributeInfoBuilder.setName(column);
            attributeInfoBuilder.setCreateable(true);
            attributeInfoBuilder.setUpdateable(true);
            attributeInfos.add(attributeInfoBuilder.build());
        }
        sb.defineObjectClass(ObjectClass.ACCOUNT_NAME, attributeInfos);
        LOG.ok("Schema was created successfully!");
        return sb.build();
    }
}
