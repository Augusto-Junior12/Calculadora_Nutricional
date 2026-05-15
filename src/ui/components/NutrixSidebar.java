package ui.components;

import ui.theme.NutrixIcons;
import ui.theme.NutrixUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * NutrixSidebar v5 — Ultra premium.
 * Estilo: Logo topo, nav items com ícone+texto, avatar rodapé.
 */
public class NutrixSidebar extends JPanel {

    public interface NavListener { void onNavigate(int index); }

    private int selected = 0;
    private final List<NavItem> items = new ArrayList<>();
    private NavListener listener;

    private static final String[] LABELS = {
        "Dashboard", "Pacientes", "Antropometria", "Metas",
        "Prescrição", "Hidratação", "Clínico UTI", "Ingestão",
        "Qualidade", "Fórmulas"
    };

    private static final NutrixIcons.Icon[] ICONS = {
        NutrixIcons.Icon.DASHBOARD, NutrixIcons.Icon.PATIENT,
        NutrixIcons.Icon.SCALE, NutrixIcons.Icon.TARGET,
        NutrixIcons.Icon.PILL, NutrixIcons.Icon.WATER,
        NutrixIcons.Icon.MONITOR, NutrixIcons.Icon.FOOD,
        NutrixIcons.Icon.CHART, NutrixIcons.Icon.BOX
    };

    public NutrixSidebar(NavListener listener) {
        this.listener = listener;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(260, 0));
        setBackground(NutrixUI.SIDEBAR_BG);

        // ── Logo Block ──
        JPanel logoBlock = new JPanel(new BorderLayout());
        logoBlock.setOpaque(false);
        logoBlock.setBorder(new EmptyBorder(32, 24, 28, 24));

        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        logoRow.setOpaque(false);

        // Logo Badge (N)
        JPanel logoBadge = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); NutrixUI.aa(g2);
                GradientPaint gp = new GradientPaint(0, 0, NutrixUI.ACCENT, getWidth(), getHeight(), new Color(139, 92, 246));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, 40, 40, 12, 12);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("N", (40 - fm.stringWidth("N"))/2, (40 + fm.getAscent() - fm.getDescent())/2);
                g2.dispose();
            }
        };
        logoBadge.setPreferredSize(new Dimension(40, 40));
        logoBadge.setOpaque(false);

        JPanel textGroup = new JPanel(new GridLayout(2, 1));
        textGroup.setOpaque(false);
        JLabel brand = new JLabel("Nutrix OS");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 16));
        brand.setForeground(Color.WHITE);
        JLabel version = new JLabel("v2.0 Clinical");
        version.setFont(NutrixUI.SMALL);
        version.setForeground(new Color(148, 163, 184));
        textGroup.add(brand);
        textGroup.add(version);

        logoRow.add(logoBadge);
        logoRow.add(textGroup);
        logoBlock.add(logoRow, BorderLayout.CENTER);
        add(logoBlock, BorderLayout.NORTH);

        // ── Nav Items ──
        JPanel navContainer = new JPanel();
        navContainer.setLayout(new BoxLayout(navContainer, BoxLayout.Y_AXIS));
        navContainer.setOpaque(false);
        navContainer.setBorder(new EmptyBorder(0, 12, 0, 12));

        // Separator label
        JLabel nav_lbl = new JLabel("MÓDULOS");
        nav_lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        nav_lbl.setForeground(new Color(71, 85, 105));
        nav_lbl.setBorder(new EmptyBorder(0, 12, 10, 0));
        navContainer.add(nav_lbl);

        for (int i = 0; i < LABELS.length; i++) {
            NavItem item = new NavItem(i, ICONS[i], LABELS[i]);
            items.add(item);
            navContainer.add(item);
            navContainer.add(Box.createVerticalStrut(4));
        }

        JScrollPane scroll = new JScrollPane(navContainer);
        scroll.setBorder(null); scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        // ── Footer (Avatar + Logout) ──
        JPanel footer = new JPanel(new BorderLayout(12, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(16, 16, 24, 16));

        // Divider
        JSeparator sep = new JSeparator();
        sep.setBackground(new Color(255,255,255,20));
        sep.setForeground(new Color(255,255,255,20));

        JPanel avatarCircle = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); NutrixUI.aa(g2);
                g2.setColor(new Color(99,102,241));
                g2.fillOval(0, 0, 38, 38);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("AJ", (38 - fm.stringWidth("AJ"))/2, (38 + fm.getAscent() - fm.getDescent())/2);
                g2.dispose();
            }
        };
        avatarCircle.setPreferredSize(new Dimension(38, 38));
        avatarCircle.setOpaque(false);

        JPanel userInfo = new JPanel(new GridLayout(2, 1));
        userInfo.setOpaque(false);
        JLabel uname = new JLabel("Augusto Junior");
        uname.setFont(NutrixUI.BODY_BOLD);
        uname.setForeground(Color.WHITE);
        JLabel urole = new JLabel("Nutricionista");
        urole.setFont(NutrixUI.SMALL);
        urole.setForeground(new Color(148, 163, 184));
        userInfo.add(uname);
        userInfo.add(urole);

        footer.add(avatarCircle, BorderLayout.WEST);
        footer.add(userInfo, BorderLayout.CENTER);

        JPanel footerWrap = new JPanel(new BorderLayout());
        footerWrap.setOpaque(false);
        footerWrap.add(sep, BorderLayout.NORTH);
        footerWrap.add(footer, BorderLayout.CENTER);
        add(footerWrap, BorderLayout.SOUTH);

        updateSelection();
    }

    private void setSelected(int index) {
        selected = index;
        updateSelection();
        if (listener != null) listener.onNavigate(index);
    }

    private void updateSelection() {
        for (NavItem item : items) item.refresh();
    }

    // ── Inner NavItem ──
    private class NavItem extends JPanel {
        private final int index;
        private final NutrixIcons.Icon icon;
        private final JLabel label;
        private boolean hovered = false;

        NavItem(int index, NutrixIcons.Icon icon, String text) {
            this.index = index; this.icon = icon;
            setLayout(new BorderLayout(14, 0));
            setOpaque(false);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            setPreferredSize(new Dimension(0, 46));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(0, 14, 0, 14));

            label = new JLabel(text);
            label.setFont(NutrixUI.NAV_LABEL);
            add(label, BorderLayout.CENTER);

            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) { setSelected(index); }
                public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
            });
        }

        public void refresh() {
            boolean sel = (index == selected);
            label.setForeground(sel ? Color.WHITE : new Color(148, 163, 184));
            label.setFont(sel ? new Font("Segoe UI", Font.BOLD, 13) : NutrixUI.NAV_LABEL);
            repaint();
        }

        @Override protected void paintComponent(Graphics g) {
            boolean sel = (index == selected);
            Graphics2D g2 = (Graphics2D) g.create(); NutrixUI.aa(g2);

            if (sel) {
                g2.setColor(new Color(124, 58, 237, 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                // accent left bar
                g2.setColor(NutrixUI.ACCENT);
                g2.fillRoundRect(0, 8, 4, getHeight()-16, 2, 2);
            } else if (hovered) {
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }

            // Draw icon
            int is = 18, iy = (getHeight()-is)/2, ix = 14;
            NutrixIcons.draw(g2, icon, ix, iy, is, sel ? NutrixUI.ACCENT : new Color(100, 116, 139));

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
