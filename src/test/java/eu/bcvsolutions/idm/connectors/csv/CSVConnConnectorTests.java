package eu.bcvsolutions.idm.connectors.csv;

import eu.bcvsolutions.idm.connectors.csv.CSVConnConfiguration;
import eu.bcvsolutions.idm.connectors.csv.CSVConnConnector;
import org.identityconnectors.framework.api.APIConfiguration;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.api.ConnectorFacadeFactory;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.test.common.TestHelpers;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;

public class CSVConnConnectorTests {

    protected CSVConnConfiguration newConfiguration() {
        return new CSVConnConfiguration();
    }

    protected ConnectorFacade newFacade() {
        ConnectorFacadeFactory factory = ConnectorFacadeFactory.getInstance();
        APIConfiguration impl = TestHelpers.createTestConfiguration(CSVConnConnector.class, newConfiguration());
        impl.getResultsHandlerConfiguration().setFilteredResultsHandlerInValidationMode(true);
        return factory.newInstance(impl);
    }

    @Test
    @Ignore
    public void basic() {
        Uid created = newFacade().create(
                ObjectClass.ACCOUNT,
                Collections.<Attribute>emptySet(),
                new OperationOptionsBuilder().build());
        assertNotNull(created);
    }
}
