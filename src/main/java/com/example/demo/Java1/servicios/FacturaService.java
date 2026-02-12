package com.example.demo.Java1.servicios;

import com.example.demo.Java1.Tablas.pedido;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.kernel.font.PdfFontFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

@Service
public class FacturaService {

    private static final String FACTURAS_DIR = "uploads/facturas";

    // Colores - Blanco y Gris
    private static final DeviceRgb COLOR_GRIS_OSCURO = new DeviceRgb(80, 80, 80);
    private static final DeviceRgb COLOR_GRIS_MEDIO = new DeviceRgb(120, 120, 120);
    private static final DeviceRgb COLOR_GRIS_CLARO = new DeviceRgb(220, 220, 220);

    public String generarFactura(pedido pedido) {
        try {
            // Crear directorio si no existe
            File dir = new File(FACTURAS_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Crear nombre del archivo
            String nombreArchivo = "FAC_" + pedido.getId_pedido() + ".pdf";
            String rutaCompleta = Paths.get(FACTURAS_DIR, nombreArchivo).toString();

            // Crear el PDF con márgenes ajustados para que entre en una página
            PdfWriter writer = new PdfWriter(rutaCompleta);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Márgenes reducidos para que entre en una página
            document.setMargins(25, 25, 25, 25);

            // ========== ENCABEZADO ==========
            agregarEncabezado(document, pedido);

            // ========== INFORMACIÓN DEL CLIENTE ==========
            document.add(new Paragraph("\n").setMarginBottom(5));
            agregarInformacionCliente(document, pedido);

            // ========== TABLA DE PRODUCTOS ==========
            document.add(new Paragraph("\n").setMarginBottom(5));
            agregarTablaProductos(document, pedido);

            // ========== RESUMEN FINANCIERO (CENTRADO) ==========
            document.add(new Paragraph("\n").setMarginBottom(5));
            agregarResumenCentrado(document, pedido);

            // ========== PIE DE PÁGINA ==========
            document.add(new Paragraph("\n").setMarginBottom(3));
            agregarPieDePageina(document);

            // Cerrar documento
            document.close();

            System.out.println("✅ PDF generado exitosamente: " + rutaCompleta);
            return rutaCompleta;

        } catch (Exception e) {
            System.err.println("❌ Error al generar PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void agregarEncabezado(Document document, pedido pedido) throws Exception {
        // Logo y nombre de empresa
        Paragraph logo = new Paragraph("URBAN STREET")
                .setFont(PdfFontFactory.createFont())
                .setFontSize(28)
                .setFontColor(COLOR_GRIS_OSCURO)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(logo);

        Paragraph slogan = new Paragraph("Moda Urbana de Calidad")
                .setFontSize(9)
                .setFontColor(COLOR_GRIS_MEDIO)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(8);
        document.add(slogan);

        // Línea divisoria
        Table lineaDivisoria = new Table(1);
        lineaDivisoria.setWidth(500);
        Cell celdaLinea = new Cell();
        celdaLinea.setBorder(new SolidBorder(COLOR_GRIS_CLARO, 1.5f));
        celdaLinea.setPadding(0);
        celdaLinea.setHeight(0);
        lineaDivisoria.addCell(celdaLinea);
        document.add(lineaDivisoria);

        document.add(new Paragraph("\n").setMarginBottom(3));

        // Información de factura
        Table infoFactura = new Table(3).setWidth(500);

        // Columna 1: Tipo de documento
        Cell cell1 = new Cell()
                .add(new Paragraph("FACTURA")
                        .setFontSize(12)
                        .setFontColor(ColorConstants.WHITE))
                .setTextAlignment(TextAlignment.CENTER)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(COLOR_GRIS_OSCURO)
                .setPadding(8);

        // Columna 2: Número de factura
        Cell cell2 = new Cell()
                .add(new Paragraph(pedido.getNumero_factura())
                        .setFontSize(12)
                        .setFontColor(COLOR_GRIS_OSCURO))
                .setTextAlignment(TextAlignment.CENTER)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(COLOR_GRIS_CLARO)
                .setPadding(8);

        // Columna 3: Fecha
        Cell cell3 = new Cell()
                .add(new Paragraph("Fecha: " + pedido.getFecha_pedido())
                        .setFontSize(9)
                        .setFontColor(COLOR_GRIS_OSCURO))
                .setTextAlignment(TextAlignment.CENTER)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(COLOR_GRIS_CLARO)
                .setPadding(8);

        infoFactura.addCell(cell1);
        infoFactura.addCell(cell2);
        infoFactura.addCell(cell3);

        document.add(infoFactura);
    }

    private void agregarInformacionCliente(Document document, pedido pedido) throws Exception {
        Paragraph titulo = new Paragraph("INFORMACIÓN DEL CLIENTE")
                .setFontSize(10)
                .setFontColor(COLOR_GRIS_OSCURO)
                .setMarginBottom(5);
        document.add(titulo);

        Table infoCliente = new Table(2).setWidth(500);

        // ID Cliente
        agregarFilaInfo(infoCliente, "ID Cliente:", String.valueOf(pedido.getId_cliente()));

        // ID Pedido
        agregarFilaInfo(infoCliente, "ID Pedido:", String.valueOf(pedido.getId_pedido()));

        // Fecha de pedido
        agregarFilaInfo(infoCliente, "Fecha del Pedido:", pedido.getFecha_pedido());

        // Método de pago
        agregarFilaInfo(infoCliente, "Método de Pago:", formatearMetodoPago(pedido.getMetodo_pago()));

        document.add(infoCliente);
    }

    private void agregarFilaInfo(Table table, String etiqueta, String valor) {
        // Etiqueta
        Cell celdaEtiqueta = new Cell()
                .add(new Paragraph(etiqueta)
                        .setFontSize(9)
                        .setFontColor(COLOR_GRIS_OSCURO))
                .setBorder(new SolidBorder(COLOR_GRIS_CLARO, 0.5f))
                .setBackgroundColor(COLOR_GRIS_CLARO)
                .setPadding(6);

        // Valor
        Cell celdaValor = new Cell()
                .add(new Paragraph(valor)
                        .setFontSize(9)
                        .setFontColor(COLOR_GRIS_OSCURO))
                .setBorder(new SolidBorder(COLOR_GRIS_CLARO, 0.5f))
                .setPadding(6);

        table.addCell(celdaEtiqueta);
        table.addCell(celdaValor);
    }

    private void agregarTablaProductos(Document document, pedido pedido) throws Exception {
        Paragraph titulo = new Paragraph("DETALLE DE COMPRA")
                .setFontSize(10)
                .setFontColor(COLOR_GRIS_OSCURO)
                .setMarginBottom(5);
        document.add(titulo);

        // Tabla con detalles
        Table tablaProductos = new Table(3).setWidth(500);

        // Encabezados
        String[] encabezados = {"DESCRIPCIÓN", "CANTIDAD", "VALOR TOTAL"};
        for (String encabezado : encabezados) {
            Cell celdaEncabezado = new Cell()
                    .add(new Paragraph(encabezado)
                            .setFontSize(9)
                            .setFontColor(ColorConstants.WHITE))
                    .setBackgroundColor(COLOR_GRIS_OSCURO)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(7)
                    .setBorder(Border.NO_BORDER);
            tablaProductos.addCell(celdaEncabezado);
        }

        // Fila de producto
        Cell celdaDescripcion = new Cell()
                .add(new Paragraph("Productos Comprados")
                        .setFontSize(9)
                        .setFontColor(COLOR_GRIS_OSCURO))
                .setBorder(new SolidBorder(COLOR_GRIS_CLARO, 0.5f))
                .setPadding(7);

        Cell celdaCantidad = new Cell()
                .add(new Paragraph("1")
                        .setFontSize(9)
                        .setFontColor(COLOR_GRIS_OSCURO)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBorder(new SolidBorder(COLOR_GRIS_CLARO, 0.5f))
                .setPadding(7)
                .setTextAlignment(TextAlignment.CENTER);

        Cell celdaValor = new Cell()
                .add(new Paragraph("$" + String.format("%.2f", pedido.getTotal()))
                        .setFontSize(9)
                        .setFontColor(COLOR_GRIS_OSCURO)
                        .setTextAlignment(TextAlignment.RIGHT))
                .setBorder(new SolidBorder(COLOR_GRIS_CLARO, 0.5f))
                .setPadding(7)
                .setTextAlignment(TextAlignment.RIGHT);

        tablaProductos.addCell(celdaDescripcion);
        tablaProductos.addCell(celdaCantidad);
        tablaProductos.addCell(celdaValor);

        document.add(tablaProductos);
    }

    private void agregarResumenCentrado(Document document, pedido pedido) throws Exception {
        // Tabla de resumen CENTRADA
        Table tablaResumen = new Table(2).setWidth(280).setHorizontalAlignment(HorizontalAlignment.CENTER);

        double subtotal = pedido.getTotal();
        double iva = subtotal * 0.19; // 19% IVA Colombia
        double total = subtotal + iva;

        // Subtotal
        agregarFilaResumen(tablaResumen, "SUBTOTAL", String.format("$%.2f", subtotal), false);

        // IVA
        agregarFilaResumen(tablaResumen, "IVA (19%)", String.format("$%.2f", iva), false);

        // Envío
        agregarFilaResumen(tablaResumen, "ENVÍO", "GRATIS", false);

        // Total
        agregarFilaResumen(tablaResumen, "TOTAL", String.format("$%.2f", total), true);

        document.add(tablaResumen);
    }

    private void agregarFilaResumen(Table table, String etiqueta, String valor, boolean esTotal) {
        Cell celdaEtiqueta = new Cell()
                .add(new Paragraph(etiqueta)
                        .setFontSize(esTotal ? 10 : 8)
                        .setFontColor(esTotal ? ColorConstants.WHITE : COLOR_GRIS_OSCURO))
                .setBackgroundColor(esTotal ? COLOR_GRIS_OSCURO : COLOR_GRIS_CLARO)
                .setBorder(Border.NO_BORDER)
                .setPadding(6);

        Cell celdaValor = new Cell()
                .add(new Paragraph(valor)
                        .setFontSize(esTotal ? 10 : 8)
                        .setFontColor(esTotal ? ColorConstants.WHITE : COLOR_GRIS_OSCURO)
                        .setTextAlignment(TextAlignment.RIGHT))
                .setBackgroundColor(esTotal ? COLOR_GRIS_OSCURO : COLOR_GRIS_CLARO)
                .setBorder(Border.NO_BORDER)
                .setPadding(6);

        table.addCell(celdaEtiqueta);
        table.addCell(celdaValor);
    }

    private void agregarPieDePageina(Document document) throws Exception {
        // Línea divisoria
        Table lineaFinal = new Table(1).setWidth(500);
        Cell celdaLinea = new Cell();
        celdaLinea.setBorder(new SolidBorder(COLOR_GRIS_CLARO, 1));
        celdaLinea.setPadding(0);
        celdaLinea.setHeight(0);
        lineaFinal.addCell(celdaLinea);
        document.add(lineaFinal);

        document.add(new Paragraph("\n").setMarginBottom(2));

        // Información de contacto
        Paragraph contacto = new Paragraph()
                .add(new Paragraph("CONTACTO E INFORMACIÓN")
                        .setFontSize(9)
                        .setFontColor(COLOR_GRIS_OSCURO))
                .add("\n")
                .add(new Paragraph("📧 moralesstiven047@gmail.com / lassobaquero6@gmail.com")
                        .setFontSize(8)
                        .setFontColor(COLOR_GRIS_MEDIO))
                .add("\n")
                .add(new Paragraph("📱 3241704370 / 3164948993")
                        .setFontSize(8)
                        .setFontColor(COLOR_GRIS_MEDIO))
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);

        document.add(contacto);

        // Línea divisoria final
        Table lineaFinal2 = new Table(1).setWidth(500);
        Cell celdaLinea2 = new Cell();
        celdaLinea2.setBorder(new SolidBorder(COLOR_GRIS_CLARO, 1));
        celdaLinea2.setPadding(0);
        celdaLinea2.setHeight(0);
        lineaFinal2.addCell(celdaLinea2);
        document.add(lineaFinal2);

        document.add(new Paragraph("\n").setMarginBottom(2));

        // Agradecimiento final
        Paragraph agradecimiento = new Paragraph("¡Gracias por tu compra! Esperamos verte pronto.")
                .setFontSize(9)
                .setFontColor(COLOR_GRIS_MEDIO)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(agradecimiento);
    }

    private String formatearMetodoPago(String metodo) {
        if (metodo == null) return "No especificado";

        return switch (metodo.toLowerCase()) {
            case "nequi" -> "💳 Nequi";
            case "efectivo" -> "💵 Pago en Efectivo";
            case "tarjeta" -> "💳 Tarjeta de Crédito";
            default -> metodo;
        };
    }
}