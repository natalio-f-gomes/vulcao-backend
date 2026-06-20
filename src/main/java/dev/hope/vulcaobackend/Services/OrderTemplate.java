package dev.hope.vulcaobackend.Services;

import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
import dev.hope.vulcaobackend.Models.Offerings.WearableModel;
import dev.hope.vulcaobackend.Models.Properties.OrderStatus;
import dev.hope.vulcaobackend.Models.Shopping.OrderModel;
import dev.hope.vulcaobackend.Repositories.CompanyRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class OrderTemplate {

    // ── Brand palette (mirrors the site stylesheet) ───────────────────────────
    private static final String INK     = "#0f0e0e";
    private static final String CREAM   = "#f7f4ef";
    private static final String ACCENT  = "#c8452a";
    private static final String MID     = "#5a5650";
    private static final String FAINT   = "#9a958f";
    private static final String LINE    = "#e8e4de";
    private static final String CARD_BG = "#ffffff";

    private static final String FONT_SERIF = "'Playfair Display', Georgia, 'Times New Roman', serif";
    private static final String FONT_SANS  = "'DM Sans', Arial, Helvetica, sans-serif";
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public OrderTemplate(UserRepository userRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public String getHtmlDocumentStructure(StringBuilder html) {
        html.append("<!DOCTYPE html>");
        html.append("<html lang='pt'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<meta http-equiv='X-UA-Compatible' content='IE=edge'>");
        html.append("<title>Confirmação da encomenda</title>");
        html.append("<style>");
        html.append("@import url('https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;700&family=Playfair+Display:ital,wght@0,700;0,900;1,700&display=swap');");
        html.append("body{margin:0;padding:0;background:").append(CREAM).append(";}");
        html.append("table{border-collapse:collapse;}");
        html.append("a{color:").append(ACCENT).append(";text-decoration:none;}");
        html.append("@media only screen and (max-width:620px){.container{width:100% !important;}}");
        html.append("</style>");
        html.append("</head>");
        html.append("<body style=\"margin:0;padding:0;background:").append(CREAM).append(";\">");
        html.append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0' style=\"background:").append(CREAM).append(";\">");
        html.append("<tr><td align='center' style='padding:28px 12px;'>");
        html.append("<table role='presentation' class='container' align='center' width='600' cellpadding='0' cellspacing='0' ");
        html.append("style=\"width:100%;max-width:600px;background:").append(CARD_BG).append(";border-top:4px solid ").append(ACCENT).append(";\">");
        return html.toString();
    }


    // ── Header
    public String getHtmlOrderHeaderSection(OrderModel order, StringBuilder html) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDate = order.getCreatedAt().format(formatter);
        String statusColor = getStatusColor(order.getStatus());

        html.append("<tr><td style=\"background:").append(INK).append(";padding:32px 28px;\">");

        // Wordmark
        html.append("<div style=\"font-family:").append(FONT_SERIF)
                .append(";font-size:24px;font-weight:900;letter-spacing:-0.5px;color:").append(CREAM).append(";\">")
                .append("Vulc<span style=\"color:").append(ACCENT).append(";\">ã</span>o</div>");

        // Title
        html.append("<div style=\"font-family:").append(FONT_SERIF)
                .append(";font-size:26px;font-weight:700;color:").append(CREAM).append(";margin-top:18px;\">")
                .append("Confirmação da encomenda</div>");

        // Order id eyebrow
        html.append("<div style=\"font-family:").append(FONT_SANS)
                .append(";font-size:12px;letter-spacing:0.12em;text-transform:uppercase;color:").append(FAINT).append(";margin-top:6px;\">")
                .append("Encomenda #").append(esc(String.valueOf(order.getTrackId()))).append("</div>");

        // Status pill + date
        html.append("<div style='margin-top:18px;'>");
        html.append("<span style=\"display:inline-block;font-family:").append(FONT_SANS)
                .append(";font-size:11px;font-weight:700;letter-spacing:0.08em;text-transform:uppercase;color:#ffffff;background:")
                .append(statusColor).append(";padding:5px 12px;border-radius:20px;\">")
                .append(getStatusInPortuguese(order.getStatus())).append("</span>");
        html.append("<span style=\"font-family:").append(FONT_SANS)
                .append(";font-size:12px;color:").append(FAINT).append(";margin-left:12px;\">")
                .append(formattedDate).append("</span>");
        html.append("</div>");

        html.append("</td></tr>");
        return html.toString();
    }


    // ── Products ───────────────────────────────────────────────────────────────
    public String getHtmlProductOrderSection(List<ProductOrder> products, StringBuilder html) {
        html.append("<tr><td style='padding:24px 28px 8px;'>");
        appendSectionTitle(html, "Produtos");
        html.append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0'>");

        for (ProductOrder po : products) {
            ProductModel p = po.product();
            String price = esc(p.getCurrency().toString()) + " " + String.format("%.2f", p.getPricePerUnit());

            html.append("<tr><td style=\"padding:12px 0;border-bottom:1px solid ").append(LINE).append(";\">");

            // Name + price/unit row
            html.append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0'><tr>");
            html.append("<td style=\"font-family:").append(FONT_SERIF)
                    .append(";font-size:16px;color:").append(INK).append(";\">").append(esc(p.getName())).append("</td>");
            html.append("<td align='right' style=\"font-family:").append(FONT_SANS)
                    .append(";font-size:14px;font-weight:700;color:").append(INK).append(";white-space:nowrap;\">")
                    .append(price)
                    .append(" <span style=\"font-weight:400;color:").append(MID).append(";\">/ ").append(esc(p.getUnitType().toString())).append("</span></td>");
            html.append("</tr></table>");

            // Quantity + expiration
            html.append("<div style=\"font-family:").append(FONT_SANS).append(";font-size:13px;color:").append(MID).append(";margin-top:4px;\">")
                    .append("Quantidade: ").append(po.quantity());
            if (p.getExpirationDate() != null) {
                html.append(" &nbsp;&middot;&nbsp; Validade: ").append(esc(String.valueOf(p.getExpirationDate())));
            }
            html.append("</div>");

            // Description
            if (p.getDescription() != null && !p.getDescription().isBlank()) {
                html.append("<div style=\"font-family:").append(FONT_SANS).append(";font-size:13px;color:").append(MID).append(";margin-top:2px;\">")
                        .append(esc(p.getDescription())).append("</div>");
            }

            html.append("</td></tr>");
        }

        html.append("</table></td></tr>");
        return html.toString();
    }


    // ── Wearables ───────────────────────────────────────────────────────────────
    public String getHtmlWearableOrderSection(List<WearableOrder> wearables, StringBuilder html) {
        html.append("<tr><td style='padding:24px 28px 8px;'>");
        appendSectionTitle(html, "Vestíveis");
        html.append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0'>");

        for (WearableOrder wo : wearables) {
            WearableModel w = wo.item();
            String price = String.format("%.2f", w.getPrice());

            html.append("<tr><td style=\"padding:12px 0;border-bottom:1px solid ").append(LINE).append(";\">");

            html.append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0'><tr>");
            html.append("<td style=\"font-family:").append(FONT_SERIF)
                    .append(";font-size:16px;color:").append(INK).append(";\">").append(esc(w.getName())).append("</td>");
            html.append("<td align='right' style=\"font-family:").append(FONT_SANS)
                    .append(";font-size:14px;font-weight:700;color:").append(INK).append(";white-space:nowrap;\">$")
                    .append(price).append("</td>");
            html.append("</tr></table>");

            html.append("<div style=\"font-family:").append(FONT_SANS).append(";font-size:13px;color:").append(MID).append(";margin-top:4px;\">")
                    .append("Quantidade: ").append(wo.quantity()).append("</div>");

            if (w.getDescription() != null && !w.getDescription().isBlank()) {
                html.append("<div style=\"font-family:").append(FONT_SANS).append(";font-size:13px;color:").append(MID).append(";margin-top:2px;\">")
                        .append(esc(w.getDescription())).append("</div>");
            }

            html.append("</td></tr>");
        }

        html.append("</table></td></tr>");
        return html.toString();
    }


    // ── Note / instructions ─────────────────────────────────────────────────────
    public String getHtmlOrderNoteSection(String note, StringBuilder html) {
        if (note != null && !note.isEmpty()) {
            html.append("<tr><td style='padding:24px 28px 8px;'>");
            appendSectionTitle(html, "Instruções / Notas");
            html.append("<div style=\"font-family:").append(FONT_SANS)
                    .append(";font-size:14px;line-height:1.6;color:").append(INK)
                    .append(";background:").append(CREAM).append(";border-left:3px solid ").append(ACCENT)
                    .append(";padding:12px 16px;\">").append(esc(note)).append("</div>");
            html.append("</td></tr>");
        }
        return html.toString();
    }


    // ── Customer info ────────────────────────────────────────────────────────────
    public String getHtmlOrderUserSection(UserModel user, StringBuilder html) {
        html.append("<tr><td style='padding:24px 28px 8px;'>");
        appendSectionTitle(html, "Informação do cliente");

        html.append("<div style=\"font-family:").append(FONT_SERIF).append(";font-size:16px;font-weight:700;color:")
                .append(INK).append(";margin-bottom:10px;\">")
                .append(esc(user.getFirstName() + " " + user.getLastName())).append("</div>");

        html.append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0'>");

        if (user.getAddress() != null) {
            Enum<?> island = user.getAddress().getIsland();
            appendKeyValue(html, "Ilha", island != null ? formattedIsland(island.toString()) : null);
            // NOTE: user Address didn't expose getCity() in the original code, so it's omitted here.
            // If it exists, add:  appendKeyValue(html, "Cidade", user.getAddress().getCity());
            appendKeyValue(html, "Zona", user.getAddress().getZone());
            appendKeyValue(html, "Código Postal", user.getAddress().getPostalCode());
            appendKeyValue(html, "Rua", user.getAddress().getStreet());
        }

        appendKeyValue(html, "Telefone", user.getPhoneNumber());
        appendKeyValue(html, "Email", user.getEmail());

        html.append("</table></td></tr>");
        return html.toString();
    }


    // ── Company info ─────────────────────────────────────────────────────────────
    public String getHtmlOrderCompanySection(CompanyModel company, StringBuilder html) {
        html.append("<tr><td style='padding:24px 28px 8px;'>");
        appendSectionTitle(html, "Informação da empresa");

        html.append("<div style=\"font-family:").append(FONT_SERIF).append(";font-size:16px;font-weight:700;color:")
                .append(INK).append(";margin-bottom:10px;\">")
                .append(esc(company.getName())).append("</div>");

        html.append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0'>");

        if (company.getAddress() != null) {
            Enum<?> island = company.getAddress().getIsland();
            appendKeyValue(html, "Ilha", island != null ? formattedIsland(island.toString()) : null);
            appendKeyValue(html, "Cidade", company.getAddress().getCity());
            appendKeyValue(html, "Zona", company.getAddress().getZone());
            appendKeyValue(html, "Código Postal", company.getAddress().getPostalCode());
            appendKeyValue(html, "Rua", company.getAddress().getStreet());
        }

        appendKeyValue(html, "Telefone", company.getPhoneNumber());
        appendKeyValue(html, "Email", company.getEmail());

        html.append("</table></td></tr>");
        return html.toString();   // footer is appended once by getHtmlOrderFooterSection()
    }


    // ── Footer (closes the document) ─────────────────────────────────────────────
    public String getHtmlOrderFooterSection(StringBuilder html) {
        html.append("<tr><td style=\"background:").append(INK).append(";padding:28px;text-align:center;\">");
        html.append("<div style=\"font-family:").append(FONT_SERIF).append(";font-size:16px;font-weight:700;color:")
                .append(CREAM).append(";\">Vulc<span style=\"color:").append(ACCENT).append(";\">ã</span>o</div>");
        html.append("<div style=\"font-family:").append(FONT_SANS).append(";font-size:12px;color:#7a756f;margin-top:10px;line-height:1.6;\">")
                .append("Obrigado pela sua preferência.<br>Mensagem automática &mdash; por favor não responda a este email.")
                .append("</div>");
        html.append("</td></tr>");

        html.append("</table>");              // .container
        html.append("</td></tr></table>");    // outer wrapper
        html.append("</body></html>");
        return html.toString();
    }


    // ── Builders (unchanged structure; footer now appended once) ─────────────────
    public String buildHtmlOrderEmailForCompany(OrderModel order, List<ProductOrder> products, List<WearableOrder> wearables) {
        StringBuilder html = new StringBuilder();
        getHtmlDocumentStructure(html);
        getHtmlOrderHeaderSection(order, html);

        if (!products.isEmpty())  getHtmlProductOrderSection(products, html);
        if (!wearables.isEmpty()) getHtmlWearableOrderSection(wearables, html);

        String note = order.getNote();
        if (note != null && !note.isEmpty()) getHtmlOrderNoteSection(note, html);

        UserModel user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new RuntimeException("This user does not exist"));
        getHtmlOrderUserSection(user, html);

        getHtmlOrderFooterSection(html);
        return html.toString();
    }

    public String buildHtmlOrderEmailForUser(OrderModel order, List<ProductOrder> products, List<WearableOrder> wearables) {
        StringBuilder html = new StringBuilder();
        getHtmlDocumentStructure(html);
        getHtmlOrderHeaderSection(order, html);
        System.out.println("products=" + products.size() + " wearables=" + wearables.size());

        if (!products.isEmpty())  getHtmlProductOrderSection(products, html);
        if (!wearables.isEmpty()) getHtmlWearableOrderSection(wearables, html);

        String note = order.getNote();
        if (note != null && !note.isEmpty()) getHtmlOrderNoteSection(note, html);

        CompanyModel company = companyRepository.findById(order.getCompanyId())
                .orElseThrow(() -> new RuntimeException("There is no company with this id: " + order.getCompanyId()));
        getHtmlOrderCompanySection(company, html);

        getHtmlOrderFooterSection(html);
        return html.toString();
    }


    // ── Helpers ──────────────────────────────────────────────────────────────────
    private void appendSectionTitle(StringBuilder html, String title) {
        html.append("<div style=\"font-family:").append(FONT_SANS)
                .append(";font-size:12px;font-weight:700;letter-spacing:0.1em;text-transform:uppercase;color:")
                .append(ACCENT).append(";padding-bottom:8px;border-bottom:2px solid ").append(ACCENT)
                .append(";margin-bottom:14px;\">").append(esc(title)).append("</div>");
    }

    private void appendKeyValue(StringBuilder html, String label, String value) {
        if (value == null || value.isBlank()) return;
        html.append("<tr>")
                .append("<td style=\"font-family:").append(FONT_SANS).append(";font-size:12px;text-transform:uppercase;letter-spacing:0.05em;color:")
                .append(MID).append(";padding:7px 16px 7px 0;white-space:nowrap;vertical-align:top;\">").append(esc(label)).append("</td>")
                .append("<td align='right' style=\"font-family:").append(FONT_SANS).append(";font-size:14px;color:")
                .append(INK).append(";padding:7px 0;\">").append(esc(value)).append("</td>")
                .append("</tr>");
    }

    /** Minimal HTML escaping for user-supplied values (names, notes, addresses). */
    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    public String formattedIsland(String island) {
        if (island == null || island.isEmpty()) return "";
        return Character.toUpperCase(island.charAt(0)) + island.substring(1).toLowerCase();
    }

    public String getStatusInPortuguese(OrderStatus status) {
        switch (status) {
            case PENDING:    return "Pendente";
            case PROCESSING: return "Em processamento";
            case SENT:       return "Enviado";
            case RECEIVED:   return "Recebido";
            case COMPLETED:  return "Concluído";
            case CANCELLED:  return "Cancelado";
            default:         return "Enviado";
        }
    }

    /** Badge colour per status (matches the dashboard status palette). */
    public String getStatusColor(OrderStatus status) {
        switch (status) {
            case PENDING:    return "#bc6c25"; // warn
            case PROCESSING: return "#0c447c"; // info
            case SENT:       return ACCENT;    // accent
            case RECEIVED:   return "#2d6a4f"; // success
            case COMPLETED:  return "#2d6a4f"; // success
            case CANCELLED:  return "#791f1f"; // danger
            default:         return MID;
        }
    }

    public static record ProductOrder(ProductModel product, Integer quantity) { }
    public static record WearableOrder(WearableModel item, Integer quantity) { }
}
