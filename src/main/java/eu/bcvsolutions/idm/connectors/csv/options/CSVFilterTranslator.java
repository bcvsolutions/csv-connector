package eu.bcvsolutions.idm.connectors.csv.options;

import eu.bcvsolutions.idm.connectors.csv.CSVConnFilter;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.objects.filter.AbstractFilterTranslator;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanFilter;

/**
 * Filter translator - basic configuration
 *
 * @author Marek Klement
 */
public class CSVFilterTranslator extends AbstractFilterTranslator<CSVConnFilter> {

    private static final Log LOG = Log.getLog(CSVFilterTranslator.class);

    @Override
    protected CSVConnFilter createEqualsExpression(final EqualsFilter filter, final boolean not) {
        LOG.info("CSVFilterTranslator -- createEqualsExpression");
        return new CSVConnFilter(CSVConnFilter.Operation.EQ, filter.getAttribute().getName(), filter.getAttribute().getValue());
    }


    @Override
    protected CSVConnFilter createGreaterThanExpression(GreaterThanFilter filter, boolean not) {
        LOG.info("CSVFilterTranslator -- createGreaterThanExpression");
        return new CSVConnFilter(CSVConnFilter.Operation.GT, filter.getAttribute().getName(), filter.getAttribute().getValue());
    }
}
