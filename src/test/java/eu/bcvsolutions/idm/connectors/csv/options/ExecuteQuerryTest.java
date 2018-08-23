package eu.bcvsolutions.idm.connectors.csv.options;

import eu.bcvsolutions.idm.connectors.csv.CSVConnConfiguration;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test for creating Object into system from CSV
 *
 * @author marek
 */
public class ExecuteQuerryTest {

    @Test
    public void generateItemsFromConfigTest() throws IOException {
        //prepare configuration
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
        // parse csv file
        ExecuteQuerry parse = new ExecuteQuerry(conf, null);
        List<ConnectorObject> data = parse.parse();
        // check if header is set now
        assertNotNull(conf.getHeader());
        assertEquals("Wrong read of username! AT1", data.get(0).getAttributeByName("username").getValue().get(0), "karamel");
        assertEquals("Wrong read of firstname! AT1", data.get(0).getAttributeByName("firstname").getValue().get(0), "mel");
        assertEquals("Wrong read of lastname! AT1", data.get(0).getAttributeByName("lastname").getValue().get(0), "kara");
        assertEquals("Wrong read of desc! AT1", data.get(0).getAttributeByName("desc").getValue().get(0), "clovek 1");

        // atr 2
        assertEquals("Wrong read of username! AT2", data.get(1).getAttributeByName("username").getValue().get(0), "velbloud");
        assertEquals("Wrong read of firstname! AT2", data.get(1).getAttributeByName("firstname").getValue().get(0), "bloud");
        assertEquals("Wrong read of lastname! AT2", data.get(1).getAttributeByName("lastname").getValue().get(0), "vel");
        assertEquals("Wrong read of desc! AT2", data.get(1).getAttributeByName("desc").getValue().get(0), "clovek 2");
    }
}