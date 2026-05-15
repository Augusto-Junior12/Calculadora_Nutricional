package ui.components;

import ui.theme.NutrixTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Sidebar Nutrix Hospital OS.
 * Estilo moderno com indicador vertical e ícones.
 */
public class SidebarPanel extends JPanel {

    private final List<JPanel> menuItems = new ArrayList<>();
    private int selectedIndex = 0;
    private NavigationListener listener;

    public interface NavigationListener {
        void onNavigate(int index, String panelName);
    }

    public SidebarPanel(String[] menuLabels, NavigationListener listener) {
        this.listener = listener;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(260, 0));
        setBackground(NutrixTheme.BG_SIDEBAR);

        // --- Logo Section ---
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);
        logoPanel.setBorder(new EmptyBorder(35, 25, 30, 25));

        JLabel logo = new JLabel("NUTRIX");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setForeground(Color.WHITE);
        
        JLabel subLogo = new JLabel("HOSPITAL OS");
        subLogo.setFont(new Font("Segoe UI", Font.BOLD, 10));
        subLogo.setForeground(NutrixTheme.ACCENT);
        subLogo.setBorder(new EmptyBorder(-5, 2, 0, 0));

        JPanel textLogo = new JPanel(new GridLayout(2, 1, 0, -5));
        textLogo.setOpaque(false);
        textLogo.add(logo);
        textLogo.add(subLogo);
        
        logoPanel.add(textLogo, BorderLayout.CENTER);
        add(logoPanel, BorderLayout.NORTH);

        // --- Menu Section ---
        JPanel menuContainer = new JPanel();
        menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));
        menuContainer.setOpaque(false);
        menuContainer.setBorder(new EmptyBorder(0, 15, 0, 15));

        String[] icons = {"▤", "👤", "📏", "🎯", "💊", "💧", "⚙", "🍽", "⚖", "🔍", "📦"};

        for (int i = 0; i < menuLabels.length; i++) {
            JPanel item = createMenuItem(i < icons.length ? icons[i] : "•", menuLabels[i], i);
            menuItems.add(item);
            menuContainer.add(item);
            menuContainer.add(Box.createVerticalStrut(6));
        }

        JScrollPane scroll = new JScrollPane(menuContainer);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        // --- Footer ---
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(20, 25, 25, 25));
        
        JLabel userLabel = new JLabel("Nutricionista Ativo");
        userLabel.setFont(NutrixTheme.FONT_SMALL);
        userLabel.setForeground(NutrixTheme.TEXT_MUTED);
        
        JLabel logout = new JLabel("Sair do Sistema");
        logout.setFont(NutrixTheme.FONT_BODY_BOLD);
        logout.setForeground(Color.WHITE);
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        footer.add(userLabel, BorderLayout.NORTH);
        footer.add(logout, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        updateSelection(0);
    }

    private JPanel createMenuItem(String icon, String label, int index) {
        JPanel item = new JPanel(new BorderLayout(15, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                if (index == selectedIndex) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    NutrixTheme.aplicarAntiAliasing(g2);
                    g2.setColor(new Color(255, 255, 255, 20));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    
                    // Indicador vertical
                    g2.setColor(NutrixTheme.ACCENT);
                    g2.fillRoundRect(0, 8, 4, getHeight() - 16, 2, 2);
                    g2.dispose();
                }
                super.paintChildren(g);
            }
        };
        item.setOpaque(false);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        item.setBorder(new EmptyBorder(0, 20, 0, 15));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        iconLabel.setForeground(NutrixTheme.TEXT_MUTED);

        JLabel textLabel = new JLabel(label);
        textLabel.setFont(NutrixTheme.FONT_BODY);
        textLabel.setForeground(NutrixTheme.TEXT_ON_DARK);

        item.add(iconLabel, BorderLayout.WEST);
        item.add(textLabel, BorderLayout.CENTER);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedIndex = index;
                updateSelection(index);
                if (listener != null) listener.onNavigate(index, label);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (index != selectedIndex) {
                    item.setBackground(new Color(255, 255, 255, 10));
                    item.setOpaque(true);
                    item.repaint();
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                item.setOpaque(false);
                item.repaint();
            }
        });

        return item;
    }

    private void updateSelection(int index) {
        for (int i = 0; i < menuItems.size(); i++) {
            JPanel item = menuItems.get(i);
            boolean selected = (i == index);
            JLabel iconLabel = (JLabel) item.getComponent(0);
            JLabel textLabel = (JLabel) item.getComponent(1);
            
            iconLabel.setForeground(selected ? NutrixTheme.ACCENT : NutrixTheme.TEXT_MUTED);
            textLabel.setForeground(selected ? Color.WHITE : NutrixTheme.TEXT_ON_DARK);
            textLabel.setFont(selected ? NutrixTheme.FONT_BODY_BOLD : NutrixTheme.FONT_BODY);
            item.repaint();
        }
    }
}
