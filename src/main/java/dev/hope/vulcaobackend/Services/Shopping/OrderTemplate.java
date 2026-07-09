package dev.hope.vulcaobackend.Services.Shopping;


import dev.hope.vulcaobackend.DTOs.UserDTO;
import dev.hope.vulcaobackend.Models.Business.BusinessModel;
import dev.hope.vulcaobackend.Models.Shopping.OrderItem;
import dev.hope.vulcaobackend.Models.Shopping.OrderModel;
import dev.hope.vulcaobackend.Properties.OrderStatus;
import dev.hope.vulcaobackend.Services.GlobalServices;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public class OrderTemplate {

    // ── Brand palette (mirrors the site stylesheet) ───────────────────────────
    private static final String INK = "#0f0e0e";
    private static final String CREAM = "#f7f4ef";
    private static final String ACCENT = "#c8452a";
    private static final String MID = "#5a5650";
    private static final String FAINT = "#9a958f";
    private static final String LINE = "#e8e4de";
    private static final String CARD_BG = "#ffffff";
    private static final String DANGER = "#791f1f";

    private static final String FONT_SERIF = "'Playfair Display', Georgia, 'Times New Roman', serif";
    private static final String FONT_SANS = "'DM Sans', Arial, Helvetica, sans-serif";

    private final GlobalServices globalServices;

    public OrderTemplate(GlobalServices globalServices) {
        this.globalServices = globalServices;
    }

    // ── Utilities ─────────────────────────────────────────────────────────────

    /**
     * HTML-escape user-supplied text (names, notes, descriptions) so that
     * characters like <, >, & and quotes can't break the markup — some clients
     * (Outlook especially) are very unforgiving of malformed HTML, and this
     * also prevents HTML injection via order notes.
     */
    private String esc(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    public String getStatusColor(OrderStatus status) {
        switch (status) {
            case PENDENTE:
                return "#bc6c25"; // warn
            case PROCESSANDO:
                return "#0c447c"; // info
            case ENVIADO:
                return ACCENT;    // accent
            case RECEBIDO:
            case CONCLUÍDO:
                return "#2d6a4f"; // success
            case CANCELADO:
                return DANGER;    // danger
            default:
                return MID;
        }
    }

    public String formattedCountry(String country) {
        if (country == null || country.isEmpty()) return "";
        return Character.toUpperCase(country.charAt(0)) + country.substring(1).toLowerCase();
    }

    // ── Document shell ────────────────────────────────────────────────────────
    public String getHtmlDocumentStructure(StringBuilder html) {
        html.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        // xmlns + mso namespaces keep Outlook (Word rendering engine) well behaved
        html.append("<html lang='pt' xmlns='http://www.w3.org/1999/xhtml' xmlns:v='urn:schemas-microsoft-com:vml' xmlns:o='urn:schemas-microsoft-com:office:office'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'/>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'/>");
        html.append("<meta http-equiv='X-UA-Compatible' content='IE=edge'/>");
        html.append("<meta name='color-scheme' content='light'/>");
        html.append("<meta name='supported-color-schemes' content='light'/>");
        html.append("<title>Confirma\u00e7\u00e3o da encomenda</title>");
        // Outlook desktop: force table widths to behave
        html.append("<!--[if mso]><style>table{border-collapse:collapse;}td{mso-line-height-rule:exactly;}</style><![endif]-->");
        html.append("<style type='text/css'>");
        // Wrap the webfont @import in @media screen — Outlook desktop has a known bug
        // where a bare @import makes it fall back to Times New Roman for EVERYTHING.
        // Gmail strips @import too, so Georgia/Arial fallbacks (already inline on every
        // element) are what most users will actually see. That's expected and fine.
        html.append("@media screen{@import url('https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;700&family=Playfair+Display:ital,wght@0,700;0,900;1,700&display=swap');}");
        html.append("body{margin:0;padding:0;background:").append(CREAM).append(";-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;}");
        html.append("table{border-collapse:collapse;mso-table-lspace:0pt;mso-table-rspace:0pt;}");
        html.append("img{border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;}");
        html.append("a{color:").append(ACCENT).append(";text-decoration:none;}");
        html.append("@media only screen and (max-width:620px){.container{width:100% !important;}}");
        html.append("</style>");
        html.append("</head>");
        // bgcolor attribute duplicated alongside the style — some clients strip style backgrounds
        html.append("<body style=\"margin:0;padding:0;background:").append(CREAM).append(";\" bgcolor='").append(CREAM).append("'>");
        html.append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0' border='0' bgcolor='").append(CREAM)
                .append("' style=\"background:").append(CREAM).append(";\">");
        html.append("<tr><td align='center' style='padding:28px 12px;'>");
        html.append("<table role='presentation' class='container' align='center' width='600' cellpadding='0' cellspacing='0' border='0' bgcolor='").append(CARD_BG).append("' ");
        html.append("style=\"width:600px;max-width:600px;background:").append(CARD_BG).append(";border-top:4px solid ").append(ACCENT).append(";\">");
        return html.toString();
    }

    // ── Header ────────────────────────────────────────────────────────────────
    public String getHtmlOrderHeaderSection(OrderModel order, StringBuilder html) {
        String statusColor = getStatusColor(OrderStatus.valueOf(order.getStatus()));

        html.append("<tr><td bgcolor='").append(INK).append("' style=\"background:").append(INK).append(";padding:32px 28px;\">");

        // Wordmark
        html.append("<div style=\"font-family:").append(FONT_SERIF)
                .append(";font-size:24px;font-weight:900;letter-spacing:-0.5px;color:").append(CREAM).append(";\">")
                .append("Vulc<span style=\"color:").append(ACCENT).append(";\">\u00e3</span>o</div>");

        // Title
        html.append("<div style=\"font-family:").append(FONT_SERIF)
                .append(";font-size:26px;font-weight:700;color:").append(CREAM).append(";margin-top:18px;\">")
                .append("Detalhes da encomenda</div>");

        // Order id eyebrow
        html.append("<div style=\"font-family:").append(FONT_SANS)
                .append(";font-size:12px;letter-spacing:0.12em;text-transform:uppercase;color:").append(FAINT).append(";margin-top:6px;\">")
                .append("Encomenda #").append(esc(String.valueOf(order.getTrackId()))).append("</div>");

        // Status pill + date (single block; every tag properly closed)
        html.append("<div style='margin-top:18px;'>");
        html.append("<span style=\"display:inline-block;font-family:").append(FONT_SANS)
                .append(";font-size:11px;font-weight:700;letter-spacing:0.08em;text-transform:uppercase;color:#ffffff;background-color:")
                .append(statusColor).append(";padding:5px 12px;border-radius:20px;\">")
                .append(esc(order.getStatus()))
                .append("</span>");
        html.append("<span style=\"font-family:").append(FONT_SANS)
                .append(";font-size:12px;color:").append(FAINT).append(";margin-left:12px;\">")
                .append(globalServices.convertDateTime(order.getUpdatedAt()))
                .append("</span>");
        html.append("</div>");

        html.append("</td></tr>");
        return html.toString();
    }

    private void appendSectionTitle(StringBuilder html, String title) {
        html.append("<div style=\"font-family:").append(FONT_SANS)
                .append(";font-size:12px;font-weight:700;letter-spacing:0.1em;text-transform:uppercase;color:")
                .append(ACCENT).append(";padding-bottom:8px;border-bottom:2px solid ").append(ACCENT)
                .append(";margin-bottom:14px;\">").append(title).append("</div>");
    }

    private void appendKeyValue(StringBuilder html, String label, String value) {
        if (value == null || value.isBlank()) return;
        html.append("<tr>")
                .append("<td style=\"font-family:").append(FONT_SANS).append(";font-size:12px;text-transform:uppercase;letter-spacing:0.05em;color:")
                .append(MID).append(";padding:7px 16px 7px 0;white-space:nowrap;vertical-align:top;\">").append(label).append("</td>")
                .append("<td align='right' style=\"font-family:").append(FONT_SANS).append(";font-size:14px;color:")
                .append(INK).append(";padding:7px 0;\">").append(esc(value)).append("</td>")
                .append("</tr>");
    }

    // ── Note ──────────────────────────────────────────────────────────────────
    public String getHtmlOrderNoteSection(String note, StringBuilder html) {
        if (note != null && !note.isEmpty()) {
            html.append("<tr><td style='padding:24px 28px 8px;'>");
            appendSectionTitle(html, "Instru\u00e7\u00f5es / Notas");
            html.append("<div style=\"font-family:").append(FONT_SANS)
                    .append(";font-size:14px;line-height:1.6;color:").append(INK)
                    .append(";background:").append(CREAM).append(";border-left:3px solid ").append(ACCENT)
                    .append(";padding:12px 16px;\">").append(esc(note)).append("</div>");
            html.append("</td></tr>");
        }
        return html.toString();
    }

    // ── Cancellation notice (NEW) ─────────────────────────────────────────────
    /**
     * Simple banner shown when the client cancels an order.
     */
    public String getHtmlOrderCancelledSection(StringBuilder html) {
        html.append("<tr><td style='padding:24px 28px 8px;'>");
        html.append("<div style=\"font-family:").append(FONT_SANS)
                .append(";font-size:14px;line-height:1.6;color:").append(INK)
                .append(";background:#fbeeee;border-left:3px solid ").append(DANGER)
                .append(";padding:12px 16px;\">")
                .append("<strong style=\"color:").append(DANGER).append(";\">Encomenda cancelada.</strong> ")
                .append("Esta encomenda foi cancelada pelo cliente. Nenhuma a\u00e7\u00e3o adicional \u00e9 necess\u00e1ria.")
                .append("</div>");
        html.append("</td></tr>");
        return html.toString();
    }

    // ── Items + total ─────────────────────────────────────────────────────────
    public String getHtmlOrderSection(List<OrderItem> items, StringBuilder html, Double orderTotalCost) {
        html.append("<tr><td style='padding:24px 28px 8px;'>");
        appendSectionTitle(html, "Produtos/Ofertas");
        html.append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0' border='0'>");

        String currency = "";
        for (OrderItem item : items) {
            currency = item.getOffering().getCurrency(); // remember for the total row
            String price = currency + " " + String.format("%.2f", item.getOffering().getPrice());

            html.append("<tr><td style=\"padding:12px 0;border-bottom:1px solid ").append(LINE).append(";\">");

            // Name + price/unit row
            html.append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0' border='0'><tr>");
            html.append("<td style=\"font-family:").append(FONT_SERIF)
                    .append(";font-size:16px;color:").append(INK).append(";\">").append(esc(item.getOffering().getName())).append("</td>");
            html.append("<td align='right' style=\"font-family:").append(FONT_SANS)
                    .append(";font-size:14px;font-weight:700;color:").append(INK).append(";white-space:nowrap;\">")
                    .append(price)
                    .append(" <span style=\"font-weight:400;color:").append(MID).append(";\">/ ").append(esc(item.getOffering().getUnit())).append("</span></td>");
            html.append("</tr></table>");

            // Quantity + expiration
            html.append("<div style=\"font-family:").append(FONT_SANS).append(";font-size:13px;color:").append(MID).append(";margin-top:4px;\">")
                    .append("Quantidade: ").append(item.getQuantity());
            if (item.getOffering().getProduct().getExpirationDate() != null) {
                html.append(" &nbsp;&middot;&nbsp; Validade: ").append(item.getOffering().getProduct().getExpirationDate());
            }
            html.append("</div>");

            // Description
            if (item.getOffering().getDescription() != null && !item.getOffering().getDescription().isBlank()) {
                html.append("<div style=\"font-family:").append(FONT_SANS).append(";font-size:13px;color:").append(MID).append(";margin-top:2px;\">")
                        .append(esc(item.getOffering().getDescription())).append("</div>");
            }

            html.append("</td></tr>");
        }

        // Total row — same table, so it lines up with the items and nothing is left unclosed
        html.append("<tr>");
        html.append("<td style=\"padding-top:14px;\">");
        html.append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0' border='0'><tr>");
        html.append("<td style=\"font-family:").append(FONT_SERIF)
                .append(";font-size:16px;font-weight:700;color:").append(INK).append(";\">PRE\u00c7O FINAL</td>");
        html.append("<td align='right' style=\"font-family:").append(FONT_SANS)
                .append(";font-size:16px;font-weight:700;color:").append(INK).append(";white-space:nowrap;\">")
                .append(currency.isEmpty() ? "" : currency + " ")
                .append(String.format("%.2f", orderTotalCost != null ? orderTotalCost : 0.0))
                .append("</td>");
        html.append("</tr></table>");
        html.append("</td></tr>");

        html.append("</table></td></tr>");
        return html.toString();
    }

    // ── Business info ─────────────────────────────────────────────────────────
    public String getHtmlOrderBusinessSection(BusinessModel business, StringBuilder html) {
        html.append("<tr><td style='padding:24px 28px 8px;'>");
        appendSectionTitle(html, "Informa\u00e7\u00e3o do neg\u00f3cio");

        html.append("<div style=\"font-family:").append(FONT_SERIF).append(";font-size:16px;font-weight:700;color:")
                .append(INK).append(";margin-bottom:10px;\">")
                .append(esc(business.getName())).append("</div>");

        html.append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0' border='0'>");

        if (business.getAddress() != null) {
            Enum<?> country = business.getAddress().getCountry();
            appendKeyValue(html, "Pa\u00eds", country != null ? formattedCountry(country.toString().replace("_", " ")) : null);
            appendKeyValue(html, "Estado", business.getAddress().getState());
            appendKeyValue(html, "Ilha", business.getAddress().getIsland());
            appendKeyValue(html, "Cidade", business.getAddress().getCity());
            appendKeyValue(html, "Zona", business.getAddress().getZone());
            appendKeyValue(html, "C\u00f3digo Postal", business.getAddress().getPostalCode());
            appendKeyValue(html, "Rua", business.getAddress().getStreet());
        }

        appendKeyValue(html, "Telefone", business.getPhoneNumber());
        appendKeyValue(html, "Email", business.getEmail());

        html.append("</table></td></tr>");
        return html.toString();
    }

    // ── Customer info ─────────────────────────────────────────────────────────
    public String getHtmlOrderClientSection(UserDTO client, StringBuilder html) {
        html.append("<tr><td style='padding:24px 28px 8px;'>");
        appendSectionTitle(html, "Informa\u00e7\u00e3o do cliente");

        html.append("<div style=\"font-family:").append(FONT_SERIF).append(";font-size:16px;font-weight:700;color:")
                .append(INK).append(";margin-bottom:10px;\">")
                .append(esc(client.getFirstName() + " " + client.getLastName())).append("</div>");

        html.append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0' border='0'>");

        appendKeyValue(html, "Telefone", client.getPhoneNumber());
        appendKeyValue(html, "Email", client.getEmail());

        if (client.getAddress() != null) {
            Enum<?> country = client.getAddress().getCountry();
            appendKeyValue(html, "Pa\u00eds", country != null ? formattedCountry(country.toString().replace("_", " ")) : null);
            appendKeyValue(html, "Estado", client.getAddress().getState());
            appendKeyValue(html, "Ilha", client.getAddress().getIsland());
            appendKeyValue(html, "Cidade", client.getAddress().getCity());
            appendKeyValue(html, "Zona", client.getAddress().getZone());
            appendKeyValue(html, "C\u00f3digo Postal", client.getAddress().getPostalCode());
            appendKeyValue(html, "Rua", client.getAddress().getStreet());
        }

        html.append("</table></td></tr>");
        return html.toString();
    }

    // ── Footer ────────────────────────────────────────────────────────────────
    public String getHtmlOrderFooterSection(StringBuilder html) {
        html.append("<tr><td bgcolor='").append(INK).append("' style=\"background:").append(INK).append(";padding:28px;text-align:center;\">");
        html.append("<div style=\"font-family:").append(FONT_SERIF).append(";font-size:16px;font-weight:700;color:")
                .append(CREAM).append(";\">Vulc<span style=\"color:").append(ACCENT).append(";\">\u00e3</span>o</div>");
        html.append("<div style=\"font-family:").append(FONT_SANS).append(";font-size:12px;color:#7a756f;margin-top:10px;line-height:1.6;\">")
                .append("Obrigado pela sua prefer\u00eancia.<br/>Mensagem autom\u00e1tica &mdash; por favor n\u00e3o responda a este email.")
                .append("</div>");
        html.append("</td></tr>");

        html.append("</table>");              // .container
        html.append("</td></tr></table>");    // outer wrapper
        html.append("</body></html>");
        return html.toString();
    }

    // ── Builders ──────────────────────────────────────────────────────────────
    public String buildHtmlOrderEmailForClient(OrderModel order, BusinessModel business) {
        StringBuilder html = new StringBuilder();
        getHtmlDocumentStructure(html);
        getHtmlOrderHeaderSection(order, html);
        getHtmlOrderSection(order.getItems(), html, order.getTotalCost());
        getHtmlOrderNoteSection(order.getNote(), html);
        getHtmlOrderBusinessSection(business, html);
        getHtmlOrderFooterSection(html);
        return html.toString();
    }

    public String buildHtmlOrderEmailForBusiness(OrderModel order, UserDTO client) {
        StringBuilder html = new StringBuilder();
        getHtmlDocumentStructure(html);
        getHtmlOrderHeaderSection(order, html);
        getHtmlOrderSection(order.getItems(), html, order.getTotalCost());
        getHtmlOrderNoteSection(order.getNote(), html);
        getHtmlOrderClientSection(client, html);
        getHtmlOrderFooterSection(html);
        return html.toString();
    }

    /**
     * NEW — sent to the business when the client cancels an order.
     * Header already renders the CANCELADO pill; the banner makes it unmissable.
     */
    public String buildHtmlOrderCancelledEmailForBusiness(OrderModel order, UserDTO client) {
        StringBuilder html = new StringBuilder();
        getHtmlDocumentStructure(html);
        getHtmlOrderHeaderSection(order, html);
        getHtmlOrderCancelledSection(html);
        getHtmlOrderSection(order.getItems(), html, order.getTotalCost());
        getHtmlOrderClientSection(client, html);
        getHtmlOrderFooterSection(html);
        return html.toString();
    }
}