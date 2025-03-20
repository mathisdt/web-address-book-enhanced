package org.zephyrsoft.wab.service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.zephyrsoft.wab.configuration.ReportConfiguration;
import org.zephyrsoft.wab.model.Family;
import org.zephyrsoft.wab.model.Person;
import org.zephyrsoft.wab.report.FamilyReportField;
import org.zephyrsoft.wab.report.HeaderReportField;
import org.zephyrsoft.wab.report.PersonReportField;
import org.zephyrsoft.wab.repository.FamilyRepository;

import lombok.RequiredArgsConstructor;

import static java.util.stream.Collectors.joining;

@Service
@RequiredArgsConstructor
public class ReportService {
    private static final Pattern PLACEHOLDER = Pattern.compile("\\{([^}]+)}");

    private final ReportConfiguration reportConfiguration;
    private final FamilyRepository familyRepository;

    public byte[] exportAsPdf() {
        String html = createHtml();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(toXHTML(html));
        renderer.layout();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        renderer.createPDF(outStream);
        return outStream.toByteArray();
    }

    String createHtml() {
        return "<html><head><title>" + reportConfiguration.getTitle() + "</title><style>\n" +
            reportConfiguration.getCss() +
            "\n</style></head><body>\n" +
            getHeader() +
            familyRepository.findAll().stream()
                .sorted()
                .map(this::getFamily)
                .collect(joining("\n", "\n", "\n")) +
            "</body></html>";
    }

    private static String toXHTML(String html) {
        Document document = Jsoup.parse(html);
        document.outputSettings().charset(StandardCharsets.UTF_8);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        return document.html();
    }

    private String getHeader() {
        String template = reportConfiguration.getHeader();
        Matcher matcher = PLACEHOLDER.matcher(template);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String placeholderName = matcher.group(1);
            HeaderReportField reportField = HeaderReportField.ofName(placeholderName);
            matcher.appendReplacement(sb, getHeaderField(reportField));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String getHeaderField(HeaderReportField field) {
        return StringUtils.defaultString(switch (field) {
            case DATE -> {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(reportConfiguration.getDateFormat());
                yield dateFormatter.format(LocalDate.now());
            }
        });
    }

    private String getFamily(Family family) {
        String template = reportConfiguration.getFamily();
        Matcher matcher = PLACEHOLDER.matcher(template);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String placeholderName = matcher.group(1);
            FamilyReportField reportField = FamilyReportField.ofName(placeholderName);
            matcher.appendReplacement(sb, getFamilyField(family, reportField));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String getFamilyField(Family family, FamilyReportField field) {
        return StringUtils.defaultString(switch (field) {
            case LAST_NAME -> family.getLastName();
            case ADDRESS -> family.getAddress();
            case CONTACTS -> family.getContacts();
            case PERSONS -> family.getMembers().stream()
                .sorted()
                .map(this::getPerson)
                .collect(joining("\n", "\n", "\n"));
        });
    }

    private String getPerson(Person person) {
        String template = reportConfiguration.getPerson();
        Matcher matcher = PLACEHOLDER.matcher(template);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String placeholderName = matcher.group(1);
            PersonReportField reportField = PersonReportField.ofName(placeholderName);
            matcher.appendReplacement(sb, getPersonField(person, reportField));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String getPersonField(Person person, PersonReportField field) {
        return StringUtils.defaultString(switch (field) {
            case NAME -> Stream.of(person.getFirstName(), person.getLastName())
                .filter(StringUtils::isNotEmpty)
                .collect(joining(" "));
            case BIRTHDAY -> person.getBirthday();
            case CONTACTS -> person.getContacts();
        });
    }
}
