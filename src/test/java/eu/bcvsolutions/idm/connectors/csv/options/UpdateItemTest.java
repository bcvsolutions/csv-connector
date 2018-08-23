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
import static org.junit.Assert.assertTrue;

/**
 * TODO describtion
 *
 * @author Marek Klement
 */
public class UpdateItemTest {

    String path = "files" + File.separator + "updateItemFile.csv";

    @Test
    @Ignore
    public void updateTest() throws IOException {
        CSVConnConfiguration conf = new CSVConnConfiguration();
        conf.setIncludesHeader(true);
        conf.setName("uid");
        conf.setUid("uid");
        conf.setSourcePath(path);
        conf.setSeparator(";");

        // prepare attributes
        AttributeBuilder abID = new AttributeBuilder();
        AttributeBuilder abF = new AttributeBuilder();
        AttributeBuilder abL = new AttributeBuilder();
        AttributeBuilder abG = new AttributeBuilder();
        AttributeBuilder abGg = new AttributeBuilder();
        Attribute ID = abID
                .setName("uid")
                .addValue("5")
                .build();
        Attribute firstName = abF
                .setName("username")
                .addValue("bocahontas")
                .build();
        Attribute lastName = abL
                .setName("firstname")
                .addValue("boca")
                .build();
        Attribute gender = abG
                .setName("lastname")
                .addValue("hontas")
                .build();
        Attribute desc = abGg
                .setName("desc")
                .addValue("nadclovek 5")
                .build();
        Set<Attribute> attrs = new HashSet<>();
        attrs.add(ID);
        attrs.add(firstName);
        attrs.add(lastName);
        attrs.add(gender);
        attrs.add(desc);
        //
        Uid back = new UpdateItem(conf).updateItem(null, new Uid("5"), attrs, null);
        assertEquals("Uid back is wrong!", "5", back.getUidValue());
        // prepare attributes
        AttributeBuilder abID2 = new AttributeBuilder();
        AttributeBuilder abF2 = new AttributeBuilder();
        AttributeBuilder abL2 = new AttributeBuilder();
        AttributeBuilder abG2 = new AttributeBuilder();
        AttributeBuilder abGg2 = new AttributeBuilder();
        Attribute ID2 = abID2
                .setName("uid")
                .addValue("5")
                .build();
        Attribute firstName2 = abF2
                .setName("username")
                .addValue("apache")
                .build();
        Attribute lastName2 = abL2
                .setName("firstname")
                .addValue("che")
                .build();
        Attribute gender2 = abG2
                .setName("lastname")
                .addValue("apa")
                .build();
        Attribute desc2 = abGg2
                .setName("desc")
                .addValue("clovek 5")
                .build();
        Set<Attribute> attrs2 = new HashSet<>();
        attrs2.add(ID2);
        attrs2.add(firstName2);
        attrs2.add(lastName2);
        attrs2.add(gender2);
        attrs2.add(desc2);
        //
        Uid back2 = new UpdateItem(conf).updateItem(null, new Uid("5"), attrs2, null);
        assertEquals("Uid back is wrong!", "5", back2.getUidValue());
        assertTrue(back.getUidValue().equals(back2.getUidValue()));
    }

}