package eu.bcvsolutions.idm.connectors.csv.options;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import eu.bcvsolutions.idm.connectors.csv.CSVConnConfiguration;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * This is abstract class including common operations for delete and create, update
 *
 * @author Marek Klement
 */
abstract class Operations {

    protected final CSVConnConfiguration conf;
    protected String[] headerFound;

    Operations(CSVConnConfiguration conf) {
        this.conf = conf;
        if (conf.isIncludesHeader()) {
            headerFound = readHeader();
        } else {
            headerFound = conf.getHeader();
        }
    }

    /**
     * check if user already exist in csv file
     *
     * @param user
     * @return
     */
    boolean userExists(String user) throws IOException {
        String uid;
        String name;
        if (conf.getName() != null) {
            name = conf.getName();
        } else {
            name = conf.getUid();
        }
        uid = conf.getUid();
        int i = 0;
        int uidNum = -1;
        int nameNum = -1;
        for (String head : headerFound) {
            if (head.equals(name)) {
                nameNum = i;
            }
            if (head.equals(uid)) {
                uidNum = i;
            }
            ++i;
        }
        if (uidNum == -1 || nameNum == -1) {
            throw new ConnectorException("Name number not found!");
        }
        return userContains(nameNum, uidNum, user);
    }

    /**
     * if header is included, this function finds the header
     *
     * @return
     */
    String[] readHeader() {
        String[] header = null;
        final CSVReader reader;
        final CSVParser parser;
        try {
            parser = new CSVParserBuilder()
                    .withSeparator(conf.getSeparator().charAt(0))
                    .withIgnoreQuotations(true)
                    .build();
            reader = new CSVReaderBuilder(new FileReader(conf.getSourcePath()))
                    .withCSVParser(parser)
                    .build();
            Iterator<String[]> it = reader.iterator();
            if (it.hasNext()) {
                header = it.next();
            } else {
                throw new ConnectorException("File is blank, please check file!");
            }
            reader.close();
        } catch (IOException e) {
            throw new ConnectorException(e);
        }
        return header;
    }

    /**
     * Finds the __NAME__ attribute
     *
     * @param createAttributes
     * @return
     */
    String findName(Set<Attribute> createAttributes) {
        String name = "__NAME__";
        for (Attribute at : createAttributes) {
            if (at.getName().equals(conf.getName()) || at.getName().equals(name)) {
                return at.getValue().get(0).toString();
            }
        }
        return null;
    }

    /**
     * Finds array pointer number for __NAME__
     *
     * @param header
     * @return
     */
    int getArrayNumber(String[] header, String att) {
        int i = 0;
        for (String s : header) {
            if (s.equals(att)) {
                return i;
            }
            ++i;
        }
        throw new ConnectorException("Name is not present in header!");
    }

    /**
     * creates updated line of attributes
     *
     * @return
     * @params updateAttributes, line
     */
    public String[] createUpdatedLine(Set<Attribute> updateAttributes, String[] line) {
        for (Attribute att : updateAttributes) {
            int num = getArrayNumber(headerFound, att.getName());
            line[num] = att.getValue().get(0).toString();
        }
        return line;
    }

    private boolean userContains(int nameNum, int uidNum, String user) throws IOException {
        final CSVReader reader;
        final CSVParser parser;
        int skip = 0;
        if (conf.isIncludesHeader()) {
            skip = 1;
        }
        parser = new CSVParserBuilder()
                .withSeparator(conf.getSeparator().charAt(0))
                .withIgnoreQuotations(true)
                .build();
        reader = new CSVReaderBuilder(new FileReader(conf.getSourcePath()))
                .withCSVParser(parser)
                .withSkipLines(skip)
                .build();
        Iterator<String[]> it = reader.iterator();
        while (it.hasNext()) {
            String[] line = it.next();
            if (line[uidNum].equals(user) || line[nameNum].equals(user)) {
                reader.close();
                return true;
            }
        }
        return false;
    }
}
