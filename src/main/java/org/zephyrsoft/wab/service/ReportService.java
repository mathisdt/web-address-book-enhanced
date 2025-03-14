package org.zephyrsoft.wab.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.zephyrsoft.wab.Constants;
import org.zephyrsoft.wab.model.Family;
import org.zephyrsoft.wab.model.Person;
import org.zephyrsoft.wab.report.ReportLoader;
import org.zephyrsoft.wab.report.SimpleDataSource;
import org.zephyrsoft.wab.repository.FamilyRepository;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final FamilyRepository familyRepository;

    public byte[] exportAsPdf() {
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String printableDate = date.format(LocalDate.now());

        // load all families and build the pdf
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream(
                Constants.REPORT_TEMPLATE));

            // add parameters "logo" and "date"
            Map<String, Object> parameters = new HashMap<>();
            parameters.put(Constants.LOGO, getClass().getResourceAsStream(Constants.LOGO_IMAGE));
            parameters.put(Constants.DATE, "Stand: " + printableDate);
            parameters.put(Constants.PERSON_SUBREPORT, ReportLoader.loadLayout(Constants.PERSON));

            // the sorting of the families is done inside buildDataSource()
            JRDataSource dataSource = buildDataSource();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            JRPdfExporter exporter = new JRPdfExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);

            exporter.exportReport();
            return outStream.toByteArray();
        } catch (JRException e) {
            throw new IllegalStateException("could not export as PDF", e);
        }
    }

    private JRDataSource buildDataSource() {
        SimpleDataSource ret = new SimpleDataSource();

        List<Family> families = familyRepository.findAll();
        Collections.sort(families);
        for (Family f : families) {
            // add one line in the DS for every family
            ret.beginNewRow();
            ret.put(Constants.ATTRIBUTE_LAST_NAME, f.getLastName());
            ret.put(Constants.ATTRIBUTE_STREET, f.getStreet());
            ret.put(Constants.ATTRIBUTE_POSTAL_CODE, f.getPostalCode());
            ret.put(Constants.ATTRIBUTE_CITY, f.getCity());
            ret.put(Constants.ATTRIBUTE_CONTACT1, f.getContact1());
            ret.put(Constants.ATTRIBUTE_CONTACT2, f.getContact2());
            ret.put(Constants.ATTRIBUTE_CONTACT3, f.getContact3());
            SimpleDataSource members = new SimpleDataSource(Constants.ATTRIBUTE_MEMBERS);
            List<Person> persons = new ArrayList<>(f.getMembers());
            Collections.sort(persons);
            for (Person p : persons) {
                // add one line in the DS for every member of the family
                members.beginNewRow();
                // the "first name" field contains also the person's last name if filled and different from family last
                // name
                String firstName = p.getFirstName();
                if (p.getLastName() != null && !p.getLastName().trim().isEmpty()
                    && !p.getLastName().equalsIgnoreCase(f.getLastName())) {
                    firstName += " " + p.getLastName();
                }
                members.put(Constants.ATTRIBUTE_FIRST_NAME, firstName);
                members.put(Constants.ATTRIBUTE_BIRTHDAY, p.getBirthday());
                members.put(Constants.ATTRIBUTE_CONTACT1, p.getContact1());
                members.put(Constants.ATTRIBUTE_CONTACT2, p.getContact2());
                members.put(Constants.ATTRIBUTE_CONTACT3, p.getContact3());
            }
            ret.put(members);
        }

        return ret;
    }
}
