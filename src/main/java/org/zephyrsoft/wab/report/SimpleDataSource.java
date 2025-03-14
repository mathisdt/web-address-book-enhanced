package org.zephyrsoft.wab.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

/**
 * simple data source which holds all data in maps with strings as keys
 */
public final class SimpleDataSource implements JRRewindableDataSource {

	private final List<Map<String, Object>> data;
	private Iterator<Map<String, Object>> iterator;
	@Getter
	@Setter
	private String keyName;
	@Getter
	private Map<String, Object> currentReadEntry;
	private Map<String, Object> currentWriteEntry;

	public SimpleDataSource() {
		this(null);
	}

	public SimpleDataSource(String keyName) {
		this.data = new ArrayList<>();
		this.keyName = keyName;
	}

	public int getRecordCount() {
		return data.size();
	}

	public void beginNewRow() {
		currentWriteEntry = new HashMap<>();
		data.add(currentWriteEntry);
	}

	public void put(String key, Object value) {
		if (currentWriteEntry == null) {
			beginNewRow();
		}
		currentWriteEntry.put(key, value);
	}

	public void put(SimpleDataSource subDataSource) {
		if (subDataSource != null) {
			String subDataSourceKeyName = subDataSource.getKeyName();
			if (subDataSourceKeyName == null) {
				throw new IllegalArgumentException("subDataSource needs a keyName");
			} else {
				put(subDataSourceKeyName, subDataSource);
			}
		} else {
			throw new IllegalArgumentException("subDataSource was null");
		}
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		Object obj = currentReadEntry.get(jrField.getName());
		// fail fast: provoke ClassCastException if object cannot be cast to target class
		return jrField.getValueClass().cast(obj);
	}

	@Override
	public boolean next() throws JRException {
		if (iterator == null)
			iterator = data.iterator();
		boolean ret = iterator.hasNext();
		if (ret) {
			currentReadEntry = iterator.next();
		}
		return ret;
	}

	@Override
	public void moveFirst() {
		iterator = null;
		currentReadEntry = null;
	}
}
