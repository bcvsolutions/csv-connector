package eu.bcvsolutions.idm.connectors.csv;

import eu.bcvsolutions.idm.connectors.csv.options.CSVFilterTranslator;
import eu.bcvsolutions.idm.connectors.csv.options.CreateSchema;
import eu.bcvsolutions.idm.connectors.csv.options.ExecuteQuerry;
import eu.bcvsolutions.idm.connectors.csv.options.GetLatestSyncToken;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.Connector;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.operations.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * This sample connector provides (empty) implementations for all ConnId operations, but this is not mandatory: any
 * connector can choose which operations are actually to be implemented.
 *
 * @author Marek Klement
 */
@ConnectorClass(configurationClass = CSVConnConfiguration.class, displayNameKey = "CSV_CONNECTOR_DISPLAY")
public class CSVConnConnector implements Connector,
        CreateOp, UpdateOp, DeleteOp, SchemaOp, SyncOp, TestOp, SearchOp<CSVConnFilter> {

    private static final Log LOG = Log.getLog(CSVConnConnector.class);

    private CSVConnConfiguration configuration;

    @Override
    public CSVConnConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void init(final Configuration configuration) {
        this.configuration = (CSVConnConfiguration) configuration;
        LOG.ok("Connector {0} successfully inited", getClass().getName());
    }

    @Override
    public void dispose() {
        // dispose of any resources the this connector uses.
        LOG.info("Dispose");
    }

    @Override
    public Uid create(
            final ObjectClass objectClass,
            final Set<Attribute> createAttributes,
            final OperationOptions options) {
        throw new UnsupportedOperationException("Operation Create is not supported!");
//        LOG.info("Starting CREATE");
//        try {
//            return new CreateItem(configuration).createItem(objectClass,createAttributes,options);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        throw new ConnectorException("Found error in create item!");
    }

    @Override
    public Uid update(
            final ObjectClass objectClass,
            final Uid uid,
            final Set<Attribute> replaceAttributes,
            final OperationOptions options) {
        throw new UnsupportedOperationException("Operation Update is not supported!");
//        LOG.info("Starting UPDATE");
//        try {
//            return new UpdateItem(configuration).updateItem(objectClass,uid,replaceAttributes,options);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        throw new ConnectorException("Found error in update item!");
    }

    @Override
    public void delete(
            final ObjectClass objectClass,
            final Uid uid,
            final OperationOptions options) {
        throw new UnsupportedOperationException("Operation Delete is not supported!");
//        LOG.info("Starting DELETE");
//        try {
//            new DeleteItem(configuration).deleteItem(objectClass ,uid, options);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public Schema schema() {
        LOG.info("Starting SCHEMA");
        try {
            return new CreateSchema(configuration).generateSchema(this);
        } catch (IOException e) {
            throw new ConnectorException(e);
        }
    }

    @Override
    public void sync(
            final ObjectClass objectClass,
            final SyncToken token,
            final SyncResultsHandler handler,
            final OperationOptions options) {
        throw new UnsupportedOperationException("Operation Synchronization is not supported. Use reconscilation instead!");
//        LOG.info("Starting SYNC");
//        CSVConnFilter filter = createSyncFilter(token, configuration.getSyncTokenColumn());
//        ResultsHandler resultsHandler = connectorObject -> {
//
//            SyncToken newToken = new SyncToken(connectorObject.getAttributeByName(configuration.getSyncTokenColumn()).getValue().get(0));
//            SyncDeltaBuilder builder = new SyncDeltaBuilder();
//			builder.setObject(connectorObject)
//                    .setToken(newToken)
//                    //TODO operation DELETE
//                    .setDeltaType(SyncDeltaType.CREATE_OR_UPDATE)
//					.setObjectClass(objectClass);
//			return handler.handle(builder.build());
//		};
//        executeQuery(objectClass, filter, resultsHandler, options);
    }

    private CSVConnFilter createSyncFilter(SyncToken token, String syncTokenColumn) {
        if (syncTokenColumn == null) {
            throw new ConnectorException("SYNC - syncTokenColumn is not filled!");
        }
        if (token == null) {
            throw new ConnectorException("SYNC - token is NULL!");
        }
        final CSVConnFilter filter = new CSVConnFilter(CSVConnFilter.Operation.GT, syncTokenColumn, token.getValue());
        return filter;
    }

    @Override
    public SyncToken getLatestSyncToken(final ObjectClass objectClass) {
        LOG.info("Starting getLatestSyncToken");
        try {
            return new GetLatestSyncToken(configuration).getLatToken();
        } catch (IOException e) {
            throw new ConnectorException(e);
        }
    }

    @Override
    public void test() {
        LOG.info("Starting TEST");
        File fl = new File(configuration.getSourcePath());
        if (!(fl.canRead())) {
            throw new ConnectorException("Can't read given path. Check if can be read or if it is right!");
        }
        if (!(fl.canWrite())) {
            LOG.warn("Cannot write to source path!");
        }
    }

    @Override
    public FilterTranslator<CSVConnFilter> createFilterTranslator(
            final ObjectClass objectClass,
            final OperationOptions options) {
        LOG.info("Starting createFilterTranslator");
        return new CSVFilterTranslator();
    }

    @Override
    public void executeQuery(
            final ObjectClass objectClass,
            final CSVConnFilter query,
            final ResultsHandler handler,
            final OperationOptions options) {
        LOG.info("Starting executeQuery");
        List<ConnectorObject> result = null;
        try {
            result = new ExecuteQuerry(this.configuration, query).parse();
        } catch (IOException e) {
            throw new ConnectorException(e);
        }
        result.forEach(handler::handle);
    }
}
