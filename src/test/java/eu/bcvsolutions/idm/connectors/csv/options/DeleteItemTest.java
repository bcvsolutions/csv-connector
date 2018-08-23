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
public class DeleteItemTest {

    String path = "files" + File.separator + "deleteItemFile.csv";

    @Test
    @Ignore
    public void deleteItemTest() throws IOException {
        CSVConnConfiguration conf = new CSVConnConfiguration();
        conf.setIncludesHeader(true);
        conf.setName("uid");
        conf.setUid("uid");
        conf.setSourcePath(path);
        conf.setSeparator(";");

        // prepare attributes
        AttributeBuilder idAttributeBuilder = new AttributeBuilder();
        AttributeBuilder firstnameAttributeBuilder = new AttributeBuilder();
        AttributeBuilder lastnameAttributeBuilder = new AttributeBuilder();
        AttributeBuilder genderAttributeBuilder = new AttributeBuilder();
        AttributeBuilder descriptionAttributeBuilder = new AttributeBuilder();
        Attribute ID = idAttributeBuilder
                .setName("uid")
                .addValue("9")
                .build();
        Attribute firstName = firstnameAttributeBuilder
                .setName("username")
                .addValue("hodbod")
                .build();
        Attribute lastName = lastnameAttributeBuilder
                .setName("firstname")
                .addValue("bod")
                .build();
        Attribute gender = genderAttributeBuilder
                .setName("lastname")
                .addValue("hod")
                .build();
        Attribute desc = descriptionAttributeBuilder
                .setName("desc")
                .addValue("clovek 9")
                .build();
        Set<Attribute> attrs = new HashSet<>();
        attrs.add(ID);
        attrs.add(firstName);
        attrs.add(lastName);
        attrs.add(gender);
        attrs.add(desc);
        //
        Uid back = new DeleteItem(conf).deleteItem(null, new Uid("9"), null);
        assertEquals("Wrong expected Uid", "9", back.getUidValue());
        // put it back for next test
        Uid create = new CreateItem(conf).createItem(null, attrs, null);
        assertTrue(back.getUidValue().equals(create.getUidValue()));
    }

}