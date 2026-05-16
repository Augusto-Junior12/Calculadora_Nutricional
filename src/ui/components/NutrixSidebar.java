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
 * NutrixSidebar v6 — Auto-reveal ao passar o mouse na borda esquerda.
 * Ícones e labels harmonizados com alinhamento perfeito.
 */
public class NutrixSidebar extends JPanel {

    public interface NavListener   { void onNavigate(int index); }
    public interface LogoutListener { void onLogout(); }

    public record NavEntry(String label, NutrixIcons.Icon icon, boolean adminOnly) {}

    private static final NavEntry[] ENTRIES = {
        new NavEntry("Dashboard",        NutrixIcons.Icon.DASHBOARD, false),
        new NavEntry("Pacientes",        NutrixIcons.Icon.PATIENT,   false),
        new NavEntry("Antropometria",    NutrixIcons.Icon.SCALE,     false),
        new NavEntry("Metas Nutricionais", NutrixIcons.Icon.TARGET,  false),
        new NavEntry("Prescrição",       NutrixIcons.Icon.PILL,      false),
        new NavEntry("Hidratação",       NutrixIcons.Icon.WATER,     false),
        new NavEntry("Clínico UTI",      NutrixIcons.Icon.MONITOR,   false),
        new NavEntry("Ingestão Oral",    NutrixIcons.Icon.FOOD,      false),
        new NavEntry("Qualidade P×I",    NutrixIcons.Icon.CHART,     false),
        new NavEntry("Fórmulas",         NutrixIcons.Icon.BOX,       false),
        new NavEntry("Evolução Clínica", NutrixIcons.Icon.DASHBOARD, false),
        new NavEntry("Relatórios",       NutrixIcons.Icon.CHART,     false),
        new NavEntry("Configurações",    NutrixIcons.Icon.SETTINGS,  true),
    };

    // ── Tamanhos ──
    private static final int W_COLLAPSED = 64;   // apenas ícones
    private static final int W_EXPANDED  = 250;  // ícones + texto
    private static final int ANIM_MS     = 200;  // duração da animação (ms)
    private static final int TRIGGER_PX  = 8;    // zona de ativação (px da borda)

    private int currentWidth = W_COLLAPSED;
    private boolean expanded = false;
    private Timer animTimer;

    private int selected = 0;
    private final List<NavItem> navItems = new ArrayList<>();
    private final NavListener navListener;
    private final LogoutListener logoutListener;

    private boolean triggerInstalled = false;

    public NutrixSidebar(NavListener navListener, LogoutListener logoutListener) {
        this.navListener   = navListener;
        this.logoutListener = logoutListener;

        setLayout(new BorderLayout());
        setBackground(NutrixUI.SIDEBAR_BG);
        setPreferredSize(new Dimension(W_COLLAPSED, 0));
        setBorder(null);

        add(buildLogo(),   BorderLayout.NORTH);
        add(buildNav(),    BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    /**
     * Instala o detector de mouse no painel pai para abrir a sidebar
     * quando o cursor se aproxima da borda esquerda.
     */
    public void installHoverTrigger(JPanel parent) {
        if (triggerInstalled) return;
        triggerInstalled = true;

        // AWTEventListener garante detecção mesmo quando o mouse está sobre componentes filhos
        java.awt.Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (!(event instanceof MouseEvent)) return;
            MouseEvent me = (MouseEvent) event;
            if (me.getID() != MouseEvent.MOUSE_MOVED && me.getID() != MouseEvent.MOUSE_DRAGGED) return;

            // Converter coordenada global para coordenada do frame pai
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame == null) return;

            Point screenPt = me.getLocationOnScreen();
            Point framePt  = frame.getLocationOnScreen();
            int relX = screenPt.x - framePt.x;

            boolean nearEdge = relX <= TRIGGER_PX;
            boolean farRight  = relX > W_EXPANDED + 60;

            if (nearEdge && !expanded) {
                SwingUtilities.invokeLater(this::expand);
            } else if (farRight && expanded) {
                SwingUtilities.invokeLater(this::collapse);
            }
        }, java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK);

        // Garante fechamento ao sair pelo lado direito do próprio sidebar
        addMouseListener(new MouseAdapter() {
            @Override public void mouseExited(MouseEvent e) {
                Point p = e.getPoint();
                if (p.x >= getWidth() - 2) {
                    collapse();
                }
            }
        });
    }

    private void expand() {
        if (animTimer != null) animTimer.stop();
        expanded = true;
        animTimer = new Timer(12, null);
        animTimer.addActionListener(e -> {
            currentWidth = Math.min(currentWidth + 18, W_EXPANDED);
            updateSidebarWidth();
            if (currentWidth >= W_EXPANDED) animTimer.stop();
        });
        animTimer.start();
    }

    private void collapse() {
        if (animTimer != null) animTimer.stop();
        expanded = false;
        animTimer = new Timer(12, null);
        animTimer.addActionListener(e -> {
            currentWidth = Math.max(currentWidth - 18, W_COLLAPSED);
            updateSidebarWidth();
            if (currentWidth <= W_COLLAPSED) animTimer.stop();
        });
        animTimer.start();
    }

    private void updateSidebarWidth() {
        setPreferredSize(new Dimension(currentWidth, 0));
        boolean showText = currentWidth > W_COLLAPSED + 40;
        for (NavItem item : navItems) item.setTextVisible(showText);
        revalidate();
        repaint();
    }

    /** Permite selecionar um item programaticamente */
    public void setSelectedItem(int originalIndex) {
        for (NavItem item : navItems) {
            if (item.originalIndex == originalIndex) {
                selected = item.visibleIndex;
                updateSelection();
                return;
            }
        }
    }

    // ──────────── Logo ────────────
    private JPanel buildLogo() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(24, 12, 20, 12));

        // Badge "N"
        JPanel badge = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); NutrixUI.aa(g2);
                GradientPaint gp = new GradientPaint(0, 0, NutrixUI.ACCENT, 40, 40, new Color(139, 92, 246));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, 40, 40, 12, 12);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("N", (40 - fm.stringWidth("N")) / 2,
                    (40 + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        badge.setOpaque(false);
        badge.setPreferredSize(new Dimension(40, 40));

        p.add(badge);
        return p;
    }

    // ──────────── Nav ────────────
    private JScrollPane buildNav() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(4, 8, 4, 8));

        boolean isAdmin = UserSession.get().isAdmin();
        int visibleIdx = 0;

        for (int i = 0; i < ENTRIES.length; i++) {
            NavEntry entry = ENTRIES[i];
            if (entry.adminOnly() && !isAdmin) continue;

            // Divisor admin
            if (entry.adminOnly()) {
                container.add(Box.createVerticalStrut(8));
                JSeparator sep = new JSeparator();
                sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                sep.setForeground(new Color(255, 255, 255, 18));
                container.add(sep);
                container.add(Box.createVerticalStrut(8));
            }

            NavItem item = new NavItem(visibleIdx, i, entry.icon(), entry.label());
            navItems.add(item);
            container.add(item);
            container.add(Box.createVerticalStrut(2));
            visibleIdx++;
        }

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        return scroll;
    }

    // ──────────── Footer ────────────
    private JPanel buildFooter() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 18));
        wrapper.add(sep, BorderLayout.NORTH);

        JPanel footer = new JPanel(new BorderLayout(10, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(12, 12, 20, 12));
        footer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        auth.AuthUser user = UserSession.get().getUser();
        String initials = user != null ? user.getInitials() : "?";
        String userName  = user != null ? user.getDisplayName() : "Usuário";
        String role      = user != null ? user.getRole().getLabel() : "";

        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); NutrixUI.aa(g2);
                g2.setColor(NutrixUI.ACCENT);
                g2.fillOval(0, 0, 38, 38);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(initials, (38 - fm.stringWidth(initials)) / 2,
                    (38 + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(38, 38));

        JPanel info = new JPanel(new GridLayout(2, 1, 0, 1));
        info.setOpaque(false);
        JLabel nameLabel = new JLabel(userName);
        nameLabel.setFont(NutrixUI.BODY_BOLD);
        nameLabel.setForeground(Color.WHITE);
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(NutrixUI.SMALL);
        roleLabel.setForeground(new Color(100, 116, 139));
        info.add(nameLabel); info.add(roleLabel);

        // Botão logout (ícone)
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
        footer.add(info,   BorderLayout.CENTER);
        footer.add(logoutBtn, BorderLayout.EAST);

        footer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (navListener != null) navListener.onNavigate(12);
            }
        });

        wrapper.add(footer, BorderLayout.CENTER);
        return wrapper;
    }

    private void updateSelection() {
        for (NavItem item : navItems) item.refresh();
    }

    // ──────────── NavItem ────────────
    private class NavItem extends JPanel {
        private final int visibleIndex;
        final int originalIndex;
        private final NutrixIcons.Icon icon;
        private final JLabel label;
        private boolean hovered = false;

        NavItem(int visibleIndex, int originalIndex, NutrixIcons.Icon icon, String text) {
            this.visibleIndex  = visibleIndex;
            this.originalIndex = originalIndex;
            this.icon          = icon;

            setLayout(new BorderLayout());
            setOpaque(false);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            setPreferredSize(new Dimension(W_COLLAPSED, 44));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(0, 4, 0, 4));

            // Ícone centralizado à esquerda
            JPanel iconWrap = new JPanel(new GridBagLayout());
            iconWrap.setOpaque(false);
            iconWrap.setPreferredSize(new Dimension(40, 44));

            JPanel iconComp = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create(); NutrixUI.aa(g2);
                    Color c = (visibleIndex == selected) ? NutrixUI.ACCENT : new Color(120, 130, 150);
                    NutrixIcons.draw(g2, icon, 0, 0, 20, c);
                    g2.dispose();
                }
                @Override public Dimension getPreferredSize() { return new Dimension(20, 20); }
            };
            iconComp.setOpaque(false);
            iconWrap.add(iconComp, new GridBagConstraints());

            // Label de texto (oculto quando colapsado)
            label = new JLabel(text);
            label.setFont(NutrixUI.NAV_LABEL);
            label.setForeground(new Color(148, 163, 184));
            label.setBorder(new EmptyBorder(0, 6, 0, 8));
            label.setVisible(false); // começa oculto

            add(iconWrap, BorderLayout.WEST);
            add(label,    BorderLayout.CENTER);

            MouseAdapter ma = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    selected = visibleIndex;
                    updateSelection();
                    if (navListener != null) navListener.onNavigate(originalIndex);
                }
                public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            };
            addMouseListener(ma);
            iconComp.addMouseListener(ma);
            label.addMouseListener(ma);
        }

        void setTextVisible(boolean visible) {
            label.setVisible(visible);
        }

        void refresh() {
            boolean sel = (visibleIndex == selected);
            label.setForeground(sel ? Color.WHITE : new Color(148, 163, 184));
            label.setFont(sel ? new Font("Segoe UI", Font.BOLD, 13) : NutrixUI.NAV_LABEL);
            repaint();
        }

        @Override protected void paintComponent(Graphics g) {
            boolean sel = (visibleIndex == selected);
            Graphics2D g2 = (Graphics2D) g.create(); NutrixUI.aa(g2);

            if (sel) {
                g2.setColor(new Color(124, 58, 237, 50));
                g2.fillRoundRect(0, 2, getWidth(), getHeight() - 4, 10, 10);
                // Barra indicadora à esquerda
                g2.setColor(NutrixUI.ACCENT);
                g2.fillRoundRect(2, 10, 3, getHeight() - 20, 3, 3);
            } else if (hovered) {
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillRoundRect(0, 2, getWidth(), getHeight() - 4, 10, 10);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
