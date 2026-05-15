package ui.components;

import auth.UserSession;
import ui.theme.NutrixIcons;
import ui.theme.NutrixUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * NutrixSidebar v5.1 — Corrigido sobreposição ícone/texto.
 * Adicionado: suporte a roles, perfil, logout funcional.
 */
public class NutrixSidebar extends JPanel {

    public interface NavListener { void onNavigate(int index); }
    public interface LogoutListener { void onLogout(); }

    private int selected = 0;
    private final List<NavItem> navItems = new ArrayList<>();
    private NavListener navListener;
    private LogoutListener logoutListener;

    // Todos os módulos com visibilidade por role
    public record NavEntry(String label, NutrixIcons.Icon icon, boolean adminOnly) {}

    private static final NavEntry[] ENTRIES = {
        new NavEntry("Dashboard",       NutrixIcons.Icon.DASHBOARD, false),
        new NavEntry("Pacientes",       NutrixIcons.Icon.PATIENT,   false),
        new NavEntry("Antropometria",   NutrixIcons.Icon.SCALE,     false),
        new NavEntry("Metas",           NutrixIcons.Icon.TARGET,    false),
        new NavEntry("Prescrição",      NutrixIcons.Icon.PILL,      false),
        new NavEntry("Hidratação",      NutrixIcons.Icon.WATER,     false),
        new NavEntry("Clínico UTI",     NutrixIcons.Icon.MONITOR,   false),
        new NavEntry("Ingestão Oral",   NutrixIcons.Icon.FOOD,      false),
        new NavEntry("Qualidade P×I",   NutrixIcons.Icon.CHART,     false),
        new NavEntry("Fórmulas",        NutrixIcons.Icon.BOX,       false),
        // Admin only
        new NavEntry("Configurações",   NutrixIcons.Icon.SETTINGS,  true),
    };

    public NutrixSidebar(NavListener navListener, LogoutListener logoutListener) {
        this.navListener = navListener;
        this.logoutListener = logoutListener;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(260, 0));
        setBackground(NutrixUI.SIDEBAR_BG);

        add(buildLogo(), BorderLayout.NORTH);
        add(buildNav(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    // ──────────── Logo ────────────
    private JPanel buildLogo() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(28, 16, 24, 16));

        JPanel badge = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); NutrixUI.aa(g2);
                GradientPaint gp = new GradientPaint(0, 0, NutrixUI.ACCENT, 40, 40, new Color(139, 92, 246));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, 40, 40, 12, 12);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("N", (40 - fm.stringWidth("N"))/2, (40 + fm.getAscent() - fm.getDescent())/2);
                g2.dispose();
            }
        };
        badge.setOpaque(false);
        badge.setPreferredSize(new Dimension(40, 40));

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 0));
        text.setOpaque(false);
        JLabel name = new JLabel("Nutrix OS");
        name.setFont(new Font("Segoe UI", Font.BOLD, 15));
        name.setForeground(Color.WHITE);
        JLabel ver = new JLabel("v2.0 Clinical");
        ver.setFont(NutrixUI.SMALL);
        ver.setForeground(new Color(100, 116, 139));
        text.add(name); text.add(ver);

        p.add(badge); p.add(text);
        return p;
    }

    // ──────────── Navigation ────────────
    private JScrollPane buildNav() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(0, 12, 0, 12));

        // Section label
        JLabel sectionLbl = new JLabel("MÓDULOS");
        sectionLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        sectionLbl.setForeground(new Color(71, 85, 105));
        sectionLbl.setBorder(new EmptyBorder(0, 12, 10, 0));
        sectionLbl.setAlignmentX(0f);
        container.add(sectionLbl);

        boolean isAdmin = UserSession.get().isAdmin();
        int visibleIndex = 0;

        for (int i = 0; i < ENTRIES.length; i++) {
            NavEntry entry = ENTRIES[i];
            if (entry.adminOnly() && !isAdmin) continue;

            // Separador antes de seção Admin
            if (entry.adminOnly()) {
                container.add(Box.createVerticalStrut(6));
                JSeparator sep = new JSeparator();
                sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                sep.setForeground(new Color(255, 255, 255, 20));
                container.add(sep);
                container.add(Box.createVerticalStrut(6));
                JLabel adminLbl = new JLabel("ADMINISTRAÇÃO");
                adminLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
                adminLbl.setForeground(new Color(71, 85, 105));
                adminLbl.setBorder(new EmptyBorder(0, 12, 10, 0));
                adminLbl.setAlignmentX(0f);
                container.add(adminLbl);
            }

            NavItem item = new NavItem(visibleIndex, i, entry.icon(), entry.label());
            navItems.add(item);
            container.add(item);
            container.add(Box.createVerticalStrut(4));
            visibleIndex++;
        }

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        return scroll;
    }

    // ──────────── Footer (Perfil + Logout) ────────────
    private JPanel buildFooter() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 20));
        wrapper.add(sep, BorderLayout.NORTH);

        JPanel footer = new JPanel(new BorderLayout(12, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(14, 16, 22, 16));
        footer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Avatar
        auth.AuthUser user = UserSession.get().getUser();
        String initials = user != null ? user.getInitials() : "?";
        String name = user != null ? user.getDisplayName() : "Usuário";
        String role = user != null ? user.getRole().getLabel() : "";

        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); NutrixUI.aa(g2);
                g2.setColor(NutrixUI.ACCENT);
                g2.fillOval(0, 0, 38, 38);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(initials, (38 - fm.stringWidth(initials))/2,
                    (38 + fm.getAscent() - fm.getDescent())/2);
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(38, 38));

        JPanel info = new JPanel(new GridLayout(2, 1, 0, 1));
        info.setOpaque(false);
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(NutrixUI.BODY_BOLD);
        nameLabel.setForeground(Color.WHITE);
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(NutrixUI.SMALL);
        roleLabel.setForeground(new Color(100, 116, 139));
        info.add(nameLabel); info.add(roleLabel);

        // Logout icon
        JPanel logoutBtn = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); NutrixUI.aa(g2);
                NutrixIcons.draw(g2, NutrixIcons.Icon.LOGOUT, 0, 0, 20, new Color(100, 116, 139));
                g2.dispose();
            }
        };
        logoutBtn.setOpaque(false);
        logoutBtn.setPreferredSize(new Dimension(20, 20));
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    NutrixSidebar.this, "Deseja sair do sistema?",
                    "Encerrar Sessão", JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    UserSession.get().logout();
                    if (logoutListener != null) logoutListener.onLogout();
                }
            }
        });

        footer.add(avatar, BorderLayout.WEST);
        footer.add(info, BorderLayout.CENTER);
        footer.add(logoutBtn, BorderLayout.EAST);
        wrapper.add(footer, BorderLayout.CENTER);
        return wrapper;
    }

    private void updateSelection() {
        for (NavItem item : navItems) item.refresh();
    }

    // ──────────── NavItem (FIX: ícone separado do texto) ────────────
    private class NavItem extends JPanel {
        private final int visibleIndex;
        private final NutrixIcons.Icon icon;
        private final JLabel label;
        private boolean hovered = false;

        NavItem(int visibleIndex, int originalIndex, NutrixIcons.Icon icon, String text) {
            this.visibleIndex = visibleIndex;
            this.icon = icon;

            // ── KEY FIX: FlowLayout com gap fixo, ícone como component, não paint ──
            setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
            setOpaque(false);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            setPreferredSize(new Dimension(236, 46));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(0, 8, 0, 8));

            // Ícone como JPanel com tamanho fixo
            JPanel iconComp = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create(); NutrixUI.aa(g2);
                    Color c = (visibleIndex == selected) ? NutrixUI.ACCENT : new Color(100, 116, 139);
                    NutrixIcons.draw(g2, icon, 0, 3, 18, c);
                    g2.dispose();
                }
                @Override public Dimension getPreferredSize() { return new Dimension(20, 18); }
            };
            iconComp.setOpaque(false);

            // Label de texto
            label = new JLabel(text);
            label.setFont(NutrixUI.NAV_LABEL);
            label.setForeground(new Color(148, 163, 184));

            add(iconComp);
            add(label);

            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    selected = visibleIndex;
                    updateSelection();
                    if (navListener != null) navListener.onNavigate(originalIndex);
                }
                public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
            });
        }

        public void refresh() {
            boolean sel = (visibleIndex == selected);
            label.setForeground(sel ? Color.WHITE : new Color(148, 163, 184));
            label.setFont(sel ? new Font("Segoe UI", Font.BOLD, 13) : NutrixUI.NAV_LABEL);
            repaint();
        }

        @Override protected void paintComponent(Graphics g) {
            boolean sel = (visibleIndex == selected);
            Graphics2D g2 = (Graphics2D) g.create(); NutrixUI.aa(g2);

            if (sel) {
                g2.setColor(new Color(124, 58, 237, 45));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                // Accent left bar
                g2.setColor(NutrixUI.ACCENT);
                g2.fillRoundRect(0, 9, 4, getHeight() - 18, 3, 3);
            } else if (hovered) {
                g2.setColor(new Color(255, 255, 255, 12));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
