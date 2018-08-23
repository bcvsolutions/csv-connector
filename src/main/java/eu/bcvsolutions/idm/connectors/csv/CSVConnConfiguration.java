package eu.bcvsolutions.idm.connectors.csv;

import org.identityconnectors.common.StringUtil;
import org.identityconnectors.framework.common.exceptions.ConfigurationException;
import org.identityconnectors.framework.spi.AbstractConfiguration;
import org.identityconnectors.framework.spi.ConfigurationProperty;

import java.io.File;

/**
 * Main configuration class. Here are set all attributes which are needed for connection
 *
 * @author Marek Klement
 */

public class CSVConnConfiguration extends AbstractConfiguration {

    /**
     * Separator of CSV file for each column
     */
    private String separator = ";";

    /**
     * path to CSV file
     */
    private String sourcePath;

    /**
     * boolean if CSV file includes header or not
     */
    private boolean includesHeader = false;

    /**
     * if file doesn't includes header, it has to be set before parse
     */
    private String[] header;

    /**
     * Have to be set identifier = __UID__
     */
    private String uid;

    /**
     * Have to be set __NAME__
     */
    private String name;
    /**
     * Shows sync token for synchronization
     */
    private String syncTokenColumn;

    @ConfigurationProperty(displayMessageKey = "SEPARATOR_DISPLAY",
            helpMessageKey = "SEPARATOR_HELP", required = true, order = 1)
    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @ConfigurationProperty(displayMessageKey = "includesHeader.display",
            helpMessageKey = "includesHeader.help", order = 2)
    public boolean isIncludesHeader() {
        return includesHeader;
    }

    public void setIncludesHeader(boolean includesHeader) {
        this.includesHeader = includesHeader;
    }

    @ConfigurationProperty(displayMessageKey = "sourcePath.display",
            helpMessageKey = "sourcePath.help", required = true, order = 3)
    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    @ConfigurationProperty(displayMessageKey = "header.display",
            helpMessageKey = "header.help", order = 4)
    public String[] getHeader() {
        return header;
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    @ConfigurationProperty(displayMessageKey = "uid.display",
            helpMessageKey = "uid.help", required = true, order = 5)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @ConfigurationProperty(displayMessageKey = "name.display",
            helpMessageKey = "name.help", order = 6)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ConfigurationProperty(displayMessageKey = "syncToken.display",
            helpMessageKey = "syncToken.help", order = 7)
    public String getSyncTokenColumn() {
        return syncTokenColumn;
    }

    public void setSyncTokenColumn(String syncTokenColumn) {
        this.syncTokenColumn = syncTokenColumn;
    }

    @Override
    public void validate() {
        if (StringUtil.isBlank(separator) || separator == null) {
            throw new ConfigurationException("separator must not be blank!");
        }

        if (StringUtil.isBlank(sourcePath) || sourcePath == null) {
            throw new ConfigurationException("sourcePath must not be blank!");
        }

        if (!(new File(sourcePath).canRead())) {
            throw new ConfigurationException("Can't read given path. Check if can be read or if it is right!");
        }

        if (!includesHeader && header == null) {
            throw new ClassCastException("File doesn't include header, but header is not set!");
        }

        if (StringUtil.isBlank(uid) || uid == null) {
            throw new ConfigurationException("uid must not be blank!");
        }
    }


}
