package org.zephyrsoft.wab.report;

import java.io.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.*;

/**
 * loads precompiled report designs (*.jasper)
 */
public class ReportLoader {
	
	private ReportLoader() {
		// only static usage
	}
	
	private static String getResourceName(String name) {
		return "/org/zephyrsoft/wab/report/" + name + ".jasper";
	}
	
	public static JasperReport loadLayout(String name) {
		
		if (name == null) {
			throw new IllegalArgumentException("report name is null");
		}
		
		String resource = getResourceName(name);
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
		
		if (input == null) {
			// try without leading slash
			input = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource.substring(1));
			if (input == null) {
				throw new IllegalArgumentException("could not load report '" + resource + "'");
			}
		}
		
		JasperReport jasperReport = null;
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(input);
		} catch (JRException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
		
		return jasperReport;
	}
}
