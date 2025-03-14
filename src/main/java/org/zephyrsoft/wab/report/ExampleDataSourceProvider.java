package org.zephyrsoft.wab.report;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataSourceProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperReport;

import org.zephyrsoft.wab.Constants;

/**
 * DataSourceProvider for designing the reports with iReport
 */
public class ExampleDataSourceProvider implements JRDataSourceProvider {

	public ExampleDataSourceProvider() {
		// nothing to do
	}

	@Override
	public JRDataSource create(JasperReport report) throws JRException {
		SimpleDataSource families = new SimpleDataSource();
		families.beginNewRow();
		families.put(Constants.ATTRIBUTE_LAST_NAME, "Example Family One");
		families.put(Constants.ATTRIBUTE_STREET, "Example Street One");
		families.put(Constants.ATTRIBUTE_POSTAL_CODE, "12345");
		families.put(Constants.ATTRIBUTE_CITY, "Example City One");
		SimpleDataSource person1 = new SimpleDataSource(Constants.ATTRIBUTE_MEMBERS);
		person1.beginNewRow();
		person1.put(Constants.ATTRIBUTE_FIRST_NAME, "Example Person One");
		person1.beginNewRow();
		person1.put(Constants.ATTRIBUTE_FIRST_NAME, "Example Person Two");
		families.put(person1);
		return families;
	}

	@Override
	public void dispose(JRDataSource dataSource) throws JRException {
		// nothing to do
	}

	@Override
	public boolean supportsGetFieldsOperation() {
		return false;
	}

	@Override
	public JRField[] getFields(JasperReport report) throws JRException {
		throw new UnsupportedOperationException();
	}

}
