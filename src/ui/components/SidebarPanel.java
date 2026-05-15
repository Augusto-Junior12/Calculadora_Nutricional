package ui.components;

import ui.theme.HospitalTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Painel de navegação lateral (sidebar).
 * Menu com itens clicáveis que alternam os painéis na área principal.
 */
public class SidebarPanel extends JPanel {

    private final List<JPanel> menuItems = new ArrayList<>();
    private int selectedIndex = 0;

    public interface NavigationListener {
        void onNavigate(int index, String panelName);
    }

    private NavigationListener listener;

    public SidebarPanel(String[] menuLabels, NavigationListener listener) {
        this.listener = listener;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(HospitalTheme.SIDEBAR_WIDTH, 0));
        setBackground(HospitalTheme.SIDEBAR_BG);

        // Header do sidebar
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HospitalTheme.PRIMARY_DARK);
        header.setBorder(new EmptyBorder(20, 16, 20, 16));

        JLabel logo = new JLabel("🏥 Facilita Nutri");
        logo.setFont(HospitalTheme.FONT_SIDEBAR_TITLE);
        logo.setForeground(Color.WHITE);
        header.add(logo, BorderLayout.CENTER);

        JLabel versao = new JLabel("UTI v2.0");
        versao.setFont(HospitalTheme.FONT_SMALL);
        versao.setForeground(HospitalTheme.SIDEBAR_TEXT);
        header.add(versao, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);

        // Menu items
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(HospitalTheme.SIDEBAR_BG);
        menuPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        String[] icons = {"📊", "👤", "📐", "🎯", "💊", "💧", "🔬", "📋", "🍽", "📈", "📑", "🧪", "📚"};

        for (int i = 0; i < menuLabels.length; i++) {
            JPanel item = criarMenuItem(i < icons.length ? icons[i] : "•", menuLabels[i], i);
            menuItems.add(item);
            menuPanel.add(item);
        }

        JScrollPane scroll = new JScrollPane(menuPanel);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(HospitalTheme.SIDEBAR_BG);
        add(scroll, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(HospitalTheme.SIDEBAR_BG);
        footer.setBorder(new EmptyBorder(10, 16, 15, 16));
        JLabel credits = new JLabel("Augusto Jr — IFS");
        credits.setFont(HospitalTheme.FONT_SMALL);
        credits.setForeground(HospitalTheme.TEXT_SECONDARY);
        footer.add(credits, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        // Selecionar primeiro item
        atualizarSelecao(0);
    }

    private JPanel criarMenuItem(String icon, String label, int index) {
        JPanel item = new JPanel(new BorderLayout());
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        item.setBackground(HospitalTheme.SIDEBAR_BG);
        item.setBorder(new EmptyBorder(8, 16, 8, 16));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lbl = new JLabel(icon + "  " + label);
        lbl.setFont(HospitalTheme.FONT_SIDEBAR);
        lbl.setForeground(HospitalTheme.SIDEBAR_TEXT);
        item.add(lbl, BorderLayout.CENTER);

        item.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedIndex = index;
                atualizarSelecao(index);
                if (listener != null) listener.onNavigate(index, label);
            }
            public void mouseEntered(MouseEvent e) {
                if (index != selectedIndex) item.setBackground(HospitalTheme.SIDEBAR_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                if (index != selectedIndex) item.setBackground(HospitalTheme.SIDEBAR_BG);
            }
        });

        return item;
    }

    private void atualizarSelecao(int index) {
        for (int i = 0; i < menuItems.size(); i++) {
            JPanel item = menuItems.get(i);
            boolean selected = (i == index);
            item.setBackground(selected ? HospitalTheme.SIDEBAR_ACTIVE : HospitalTheme.SIDEBAR_BG);
            Component lbl = item.getComponent(0);
            if (lbl instanceof JLabel) {
                ((JLabel) lbl).setForeground(selected ? Color.WHITE : HospitalTheme.SIDEBAR_TEXT);
                ((JLabel) lbl).setFont(selected ? HospitalTheme.FONT_BODY_BOLD : HospitalTheme.FONT_SIDEBAR);
            }
        }
    }
}
