package eu.bcvsolutions.idm.connectors.csv;

import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ConnectorObject;

/**
 * This implementation depends on the logic that this connector follows.
 *
 * @author Marek Klement
 */
public class CSVConnFilter {

    private static final Log LOG = Log.getLog(CSVConnFilter.class);

    public enum Operation {
        EQ, GT
    }

    private final Operation operation;
    private final String attributeName;
    private final Object attributeValue;

    public CSVConnFilter(Operation operation, String attributeName, Object attributeValue) {
        this.operation = operation;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }


    public boolean evaluate(ConnectorObject obj) {
        final Attribute attribute = obj.getAttributeByName(attributeName);
        boolean toReturn;
        LOG.info("ATTRIBUTE [{}] WITH value [{}] compare to ATTRIBUTE [{}]", attribute.getName(), attribute.getValue(), attributeValue);
        if (attribute == null) {
            return false;
        }
        if (attributeName == null) {
            throw new ConnectorException("FILTER - No token attribute given!");
        }
        if (attributeValue == null) {
            throw new ConnectorException("FILTER - No token value given!");
        }
        switch (operation) {
            case EQ:
                //TODO: multivalued
                toReturn = attributeValue.equals(attribute.getValue());
                return toReturn;
            case GT:
                String secondValue = attribute.getValue().get(0).toString();
                int firstValue = attributeValue.toString().compareTo(secondValue);
                toReturn = firstValue < 0;
                return toReturn;
            default:
                return true;
        }

    }

    public Operation getOperation() {
        return operation;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Object getAttributeValue() {
        return attributeValue;
    }

}
