package utils;

import model.Event;
import model.Registration;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

/**
 * Utility class untuk export data ke PDF menggunakan iText
 */
public class PDFExporter {
    
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC);
    
    private static final BaseColor HEADER_BG = new BaseColor(41, 128, 185);
    private static final BaseColor ALT_ROW_BG = new BaseColor(236, 240, 241);
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
    
    /**
     * Export daftar events ke PDF
     */
    public static boolean exportEventsList(List<Event> events, String filePath) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            // Title
            Paragraph title = new Paragraph("DAFTAR EVENT VOLUNTEER", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            document.add(title);
            
            // Subtitle
            Paragraph subtitle = new Paragraph("VolSys - Volunteer Management System", SUBTITLE_FONT);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(20);
            document.add(subtitle);
            
            // Date generated
            Paragraph dateGen = new Paragraph("Tanggal: " + new SimpleDateFormat("dd MMMM yyyy").format(new java.util.Date()), NORMAL_FONT);
            dateGen.setSpacingAfter(20);
            document.add(dateGen);
            
            // Table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{0.5f, 2f, 2f, 1.5f, 1f});
            
            // Header
            addTableHeader(table, new String[]{"No", "Judul Event", "Lokasi", "Tanggal", "Status"});
            
            // Data rows
            int no = 1;
            boolean alternate = false;
            for (Event event : events) {
                BaseColor bgColor = alternate ? ALT_ROW_BG : BaseColor.WHITE;
                
                addTableCell(table, String.valueOf(no++), bgColor);
                addTableCell(table, event.getTitle(), bgColor);
                addTableCell(table, event.getLocation() != null ? event.getLocation() : "-", bgColor);
                addTableCell(table, event.getEventDate() != null ? dateFormat.format(event.getEventDate()) : "-", bgColor);
                addTableCell(table, event.getStatus() != null ? event.getStatus().toUpperCase() : "-", bgColor);
                
                alternate = !alternate;
            }
            
            document.add(table);
            
            // Footer
            Paragraph footer = new Paragraph("\nTotal Event: " + events.size(), NORMAL_FONT);
            footer.setSpacingBefore(20);
            document.add(footer);
            
            document.close();
            return true;
            
        } catch (Exception e) {
            System.err.println("Error exporting events to PDF: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Export daftar registrations user ke PDF
     */
    public static boolean exportUserRegistrations(List<Registration> registrations, String userName, String filePath) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            // Title
            Paragraph title = new Paragraph("DAFTAR EVENT YANG DIIKUTI", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            document.add(title);
            
            // Subtitle
            Paragraph subtitle = new Paragraph("VolSys - Volunteer Management System", SUBTITLE_FONT);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(20);
            document.add(subtitle);
            
            // User info
            Paragraph userInfo = new Paragraph("Volunteer: " + userName, NORMAL_FONT);
            userInfo.setSpacingAfter(5);
            document.add(userInfo);
            
            // Date generated
            Paragraph dateGen = new Paragraph("Tanggal: " + new SimpleDateFormat("dd MMMM yyyy").format(new java.util.Date()), NORMAL_FONT);
            dateGen.setSpacingAfter(20);
            document.add(dateGen);
            
            // Table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{0.5f, 2f, 1.5f, 1.5f, 1f});
            
            // Header
            addTableHeader(table, new String[]{"No", "Event", "Lokasi", "Tanggal Event", "Status"});
            
            // Data rows
            int no = 1;
            boolean alternate = false;
            for (Registration reg : registrations) {
                BaseColor bgColor = alternate ? ALT_ROW_BG : BaseColor.WHITE;
                
                addTableCell(table, String.valueOf(no++), bgColor);
                addTableCell(table, reg.getEventTitle() != null ? reg.getEventTitle() : "-", bgColor);
                addTableCell(table, reg.getEventLocation() != null ? reg.getEventLocation() : "-", bgColor);
                addTableCell(table, reg.getEventDate() != null ? dateFormat.format(reg.getEventDate()) : "-", bgColor);
                addTableCell(table, reg.getStatus() != null ? reg.getStatus().toUpperCase() : "-", bgColor);
                
                alternate = !alternate;
            }
            
            document.add(table);
            
            // Footer
            Paragraph footer = new Paragraph("\nTotal Partisipasi: " + registrations.size() + " event", NORMAL_FONT);
            footer.setSpacingBefore(20);
            document.add(footer);
            
            document.close();
            return true;
            
        } catch (Exception e) {
            System.err.println("Error exporting registrations to PDF: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Helper method untuk menambahkan header table
     */
    private static void addTableHeader(PdfPTable table, String[] headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setBackgroundColor(HEADER_BG);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            table.addCell(cell);
        }
    }
    
    /**
     * Helper method untuk menambahkan cell table
     */
    private static void addTableCell(PdfPTable table, String content, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(content, NORMAL_FONT));
        cell.setBackgroundColor(bgColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        table.addCell(cell);
    }
}
