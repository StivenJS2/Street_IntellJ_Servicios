package com.example.demo.Java1.servicios;

import com.example.demo.Java1.Tablas.pedido;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.font.PdfFontFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
public class FacturaService {

    private static final String FACTURAS_DIR = "uploads/facturas";
    private static final String STORAGE_PATH = "C:/xampp/htdocs/Street_Laravel/public/storage/";

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

            // Crear el PDF
            PdfWriter writer = new PdfWriter(rutaCompleta);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Márgenes
            document.setMargins(25, 25, 25, 25);

            // ========== ENCABEZADO ==========
            agregarEncabezado(document, pedido);

            // ========== INFORMACIÓN DEL CLIENTE ==========
            document.add(new Paragraph("\n").setMarginBottom(5));
            agregarInformacionCliente(document, pedido);

            // ========== TABLA DE PRODUCTOS CON IMÁGENES ==========
            document.add(new Paragraph("\n").setMarginBottom(5));
            agregarTablaProductosConImagenes(document, pedido);

            // ========== RESUMEN FINANCIERO (SIN IVA) ==========
            document.add(new Paragraph("\n").setMarginBottom(5));
            agregarResumenSinIVA(document, pedido);

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
        lineaDivisoria.setWidth(UnitValue.createPercentValue(100));
        Cell celdaLinea = new Cell();
        celdaLinea.setBorder(new SolidBorder(COLOR_GRIS_CLARO, 1.5f));
        celdaLinea.setPadding(0);
        celdaLinea.setHeight(0);
        lineaDivisoria.addCell(celdaLinea);
        document.add(lineaDivisoria);

        document.add(new Paragraph("\n").setMarginBottom(3));

        // Información de factura
        Table infoFactura = new Table(3).setWidth(UnitValue.createPercentValue(100));

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

        Table infoCliente = new Table(2).setWidth(UnitValue.createPercentValue(100));

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

    /**
     * 🆕 NUEVA FUNCIÓN: Tabla de productos con imágenes y detalles completos
     */
    private void agregarTablaProductosConImagenes(Document document, pedido pedido) throws Exception {
        Paragraph titulo = new Paragraph("DETALLE DE COMPRA")
                .setFontSize(10)
                .setFontColor(COLOR_GRIS_OSCURO)
                .setMarginBottom(5);
        document.add(titulo);

        // Verificar si hay items
        List<Map<String, Object>> items = pedido.getItems();

        if (items == null || items.isEmpty()) {
            // Fallback: mostrar tabla simple si no hay items
            agregarTablaProductosSimple(document, pedido);
            return;
        }

        // Tabla: Imagen | Descripción | Cantidad | Precio Unit. | Subtotal
        float[] columnWidths = {60, 180, 60, 80, 80}; // Anchos en puntos
        Table tablaProductos = new Table(columnWidths).setWidth(UnitValue.createPercentValue(100));

        // Encabezados
        String[] encabezados = {"IMAGEN", "DESCRIPCIÓN", "CANT.", "PRECIO UNIT.", "SUBTOTAL"};
        for (String encabezado : encabezados) {
            Cell celdaEncabezado = new Cell()
                    .add(new Paragraph(encabezado)
                            .setFontSize(8)
                            .setFontColor(ColorConstants.WHITE))
                    .setBackgroundColor(COLOR_GRIS_OSCURO)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(5)
                    .setBorder(Border.NO_BORDER);
            tablaProductos.addCell(celdaEncabezado);
        }

        // Filas de productos
        for (Map<String, Object> item : items) {
            agregarFilaProducto(tablaProductos, item);
        }

        document.add(tablaProductos);
    }

    /**
     * Agregar una fila de producto con imagen
     */
    private void agregarFilaProducto(Table table, Map<String, Object> item) {
        try {
            // Extraer datos del item
            String nombre = (String) item.getOrDefault("nombre", "Producto");
            int cantidad = ((Number) item.getOrDefault("cantidad", 1)).intValue();
            double precioUnitario = ((Number) item.getOrDefault("precio_unitario", 0.0)).doubleValue();
            double subtotal = ((Number) item.getOrDefault("subtotal", 0.0)).doubleValue();
            String talla = (String) item.getOrDefault("talla", "N/A");
            String color = (String) item.getOrDefault("color", "N/A");
            String rutaImagen = (String) item.getOrDefault("imagen", "");

            // 1. CELDA DE IMAGEN
            Cell celdaImagen = new Cell();
            celdaImagen.setBorder(new SolidBorder(COLOR_GRIS_CLARO, 0.5f));
            celdaImagen.setPadding(5);
            celdaImagen.setTextAlignment(TextAlignment.CENTER);

            try {
                Image imagen = obtenerImagenProducto(rutaImagen);
                if (imagen != null) {
                    imagen.setWidth(50);
                    imagen.setHeight(50);
                    imagen.setHorizontalAlignment(HorizontalAlignment.CENTER);
                    celdaImagen.add(imagen);
                } else {
                    celdaImagen.add(new Paragraph("Sin\nImagen")
                            .setFontSize(7)
                            .setFontColor(COLOR_GRIS_MEDIO)
                            .setTextAlignment(TextAlignment.CENTER));
                }
            } catch (Exception e) {
                celdaImagen.add(new Paragraph("❌")
                        .setFontSize(20)
                        .setTextAlignment(TextAlignment.CENTER));
            }

            // 2. CELDA DE DESCRIPCIÓN
            Cell celdaDescripcion = new Cell();
            celdaDescripcion.add(new Paragraph(nombre)
                    .setFontSize(9)
                    .setFontColor(COLOR_GRIS_OSCURO));
            celdaDescripcion.add(new Paragraph("Talla: " + talla + " | Color: " + color)
                    .setFontSize(7)
                    .setFontColor(COLOR_GRIS_MEDIO));
            celdaDescripcion.setBorder(new SolidBorder(COLOR_GRIS_CLARO, 0.5f));
            celdaDescripcion.setPadding(7);

            // 3. CELDA DE CANTIDAD
            Cell celdaCantidad = new Cell()
                    .add(new Paragraph(String.valueOf(cantidad))
                            .setFontSize(9)
                            .setFontColor(COLOR_GRIS_OSCURO))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBorder(new SolidBorder(COLOR_GRIS_CLARO, 0.5f))
                    .setPadding(7);

            // 4. CELDA DE PRECIO UNITARIO
            Cell celdaPrecioUnit = new Cell()
                    .add(new Paragraph("$" + String.format("%,.2f", precioUnitario))
                            .setFontSize(9)
                            .setFontColor(COLOR_GRIS_OSCURO))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBorder(new SolidBorder(COLOR_GRIS_CLARO, 0.5f))
                    .setPadding(7);

            // 5. CELDA DE SUBTOTAL
            Cell celdaSubtotal = new Cell()
                    .add(new Paragraph("$" + String.format("%,.2f", subtotal))
                            .setFontSize(9)
                            .setFontColor(COLOR_GRIS_OSCURO))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBorder(new SolidBorder(COLOR_GRIS_CLARO, 0.5f))
                    .setPadding(7);

            // Agregar celdas a la tabla
            table.addCell(celdaImagen);
            table.addCell(celdaDescripcion);
            table.addCell(celdaCantidad);
            table.addCell(celdaPrecioUnit);
            table.addCell(celdaSubtotal);

        } catch (Exception e) {
            System.err.println("❌ Error al agregar fila de producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtener imagen del producto desde la ruta de storage
     */
    private Image obtenerImagenProducto(String rutaImagen) {
        if (rutaImagen == null || rutaImagen.isEmpty()) {
            return null;
        }

        try {
            // Construir ruta completa
            String rutaCompleta;

            // Si la ruta ya incluye "uploads/"
            if (rutaImagen.startsWith("uploads/")) {
                rutaCompleta = rutaImagen;
            }
            // Si la ruta empieza con "storage/"
            else if (rutaImagen.startsWith("storage/")) {
                rutaCompleta = STORAGE_PATH + rutaImagen.substring(8); // Remover "storage/"
            }
            // Si es solo el nombre del archivo
            else {
                rutaCompleta = STORAGE_PATH + rutaImagen;
            }

            File archivoImagen = new File(rutaCompleta);

            if (!archivoImagen.exists()) {
                System.err.println("⚠️ Imagen no encontrada: " + rutaCompleta);
                return null;
            }

            return new Image(ImageDataFactory.create(rutaCompleta));

        } catch (MalformedURLException e) {
            System.err.println("❌ Error al cargar imagen: " + e.getMessage());
            return null;
        }
    }

    /**
     * Tabla simple de productos (fallback si no hay items)
     */
    private void agregarTablaProductosSimple(Document document, pedido pedido) throws Exception {
        Table tablaProductos = new Table(3).setWidth(UnitValue.createPercentValue(100));

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
                        .setFontColor(COLOR_GRIS_OSCURO))
                .setBorder(new SolidBorder(COLOR_GRIS_CLARO, 0.5f))
                .setPadding(7)
                .setTextAlignment(TextAlignment.CENTER);

        Cell celdaValor = new Cell()
                .add(new Paragraph("$" + String.format("%,.2f", pedido.getTotal()))
                        .setFontSize(9)
                        .setFontColor(COLOR_GRIS_OSCURO))
                .setBorder(new SolidBorder(COLOR_GRIS_CLARO, 0.5f))
                .setPadding(7)
                .setTextAlignment(TextAlignment.RIGHT);

        tablaProductos.addCell(celdaDescripcion);
        tablaProductos.addCell(celdaCantidad);
        tablaProductos.addCell(celdaValor);

        document.add(tablaProductos);
    }

    /**
     * 🆕 RESUMEN SIN IVA
     */
    private void agregarResumenSinIVA(Document document, pedido pedido) throws Exception {
        Table tablaResumen = new Table(2).setWidth(280).setHorizontalAlignment(HorizontalAlignment.CENTER);

        double subtotal = pedido.getTotal();
        double envio = 0.0; // Envío gratis
        double total = subtotal + envio;

        // Subtotal
        agregarFilaResumen(tablaResumen, "SUBTOTAL", String.format("$%,.2f", subtotal), false);

        // Envío
        agregarFilaResumen(tablaResumen, "ENVÍO", "GRATIS", false);

        // Total (sin IVA)
        agregarFilaResumen(tablaResumen, "TOTAL", String.format("$%,.2f", total), true);

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
        Table lineaFinal = new Table(1).setWidth(UnitValue.createPercentValue(100));
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
        Table lineaFinal2 = new Table(1).setWidth(UnitValue.createPercentValue(100));
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