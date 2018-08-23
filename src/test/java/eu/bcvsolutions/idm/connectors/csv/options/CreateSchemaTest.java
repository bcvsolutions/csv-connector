package eu.bcvsolutions.idm.connectors.csv.options;

import eu.bcvsolutions.idm.connectors.csv.CSVConnConfiguration;
import eu.bcvsolutions.idm.connectors.csv.CSVConnConnector;
import org.identityconnectors.framework.common.objects.Schema;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author marek
 * @date 12.1.18
 */
public class CreateSchemaTest {
    @Test
    public void generateSchemaTest() throws Exception {
        CSVConnConfiguration conf = new CSVConnConfiguration();
        conf.setIncludesHeader(true);
        conf.setSeparator(";");
        conf.setUid("uid");
        String currentDirectory = System.getProperty("user.dir");
        conf.setSourcePath(currentDirectory + "/src/test/resources/files/executeQuerryData.csv");
        // config test
        Exception ex = null;
        try {
            conf.validate();
        } catch (Exception e) {
            ex = e;
        }
        assertNull(ex);
        CSVConnConnector conn = new CSVConnConnector();
        conn.init(conf);
        Schema schema = new CreateSchema(conf).generateSchema(conn);
        System.out.println(schema.getObjectClassInfo());
        // have to be more, because __NAME__ is set, so items +1
        assertEquals("Wrong size of header!", 6, schema.getObjectClassInfo().iterator().next().getAttributeInfo().size());
    }

}