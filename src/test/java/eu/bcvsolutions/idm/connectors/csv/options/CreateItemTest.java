package eu.bcvsolutions.idm.connectors.csv.options;

import eu.bcvsolutions.idm.connectors.csv.CSVConnConfiguration;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.Uid;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Test for creating item in CSV file
 *
 * @author Marek Klement
 */
public class CreateItemTest {

    String path = "files" + File.separator + "createItemFile.csv";

    @Test
    @Ignore
    public void createNewItemInCSV() throws IOException {
        CSVConnConfiguration conf = new CSVConnConfiguration();
        String[] header = {"ID", "FirstName", "LastName", "Gender"};
        conf.setHeader(header);
        conf.setIncludesHeader(false);
        conf.setName("ID");
        conf.setUid("ID");
        conf.setSourcePath(path);
        conf.setSeparator(";");
        // prepare file
        File file = new File(path);
        if (file.canRead()) {
            file.delete();
        }
        file.createNewFile();

        // prepare attributes
        AttributeBuilder abID = new AttributeBuilder();
        AttributeBuilder abF = new AttributeBuilder();
        AttributeBuilder abL = new AttributeBuilder();
        AttributeBuilder abG = new AttributeBuilder();
        Attribute ID = abID
                .setName("ID")
                .addValue("1")
                .build();
        Attribute firstName = abF
                .setName("FirstName")
                .addValue("Marek")
                .build();
        Attribute lastName = abL
                .setName("LastName")
                .addValue("Klement")
                .build();
        Attribute gender = abG
                .setName("Gender")
                .addValue("Men")
                .build();
        Set<Attribute> attrs = new HashSet<>();
        attrs.add(ID);
        attrs.add(firstName);
        attrs.add(lastName);
        attrs.add(gender);
        //
        Uid back = new CreateItem(conf).createItem(null, attrs, null);
        assertEquals("Wrong expected Uid", "1", back.getUidValue());
        // test append
        //
        // prepare attributes
        AttributeBuilder abID2 = new AttributeBuilder();
        AttributeBuilder abF2 = new AttributeBuilder();
        AttributeBuilder abL2 = new AttributeBuilder();
        AttributeBuilder abG2 = new AttributeBuilder();
        Attribute ID2 = abID2
                .setName("ID")
                .addValue("2")
                .build();
        Attribute firstName2 = abF2
                .setName("FirstName")
                .addValue("Karel")
                .build();
        Attribute lastName2 = abL2
                .setName("LastName")
                .addValue("Nejedly")
                .build();
        Attribute gender2 = abG2
                .setName("Gender")
                .addValue("Men")
                .build();
        Set<Attribute> attrs2 = new HashSet<>();
        attrs2.add(ID2);
        attrs2.add(firstName2);
        attrs2.add(gender2);
        attrs2.add(lastName2);
        //
        Uid back2 = new CreateItem(conf).createItem(null, attrs2, null);
        assertEquals("Wrong expected Uid -- second line", "2", back2.getUidValue());
        // compare with sample file
        assertEquals("Sizes must be same!", file.length(), 40);
    }

}