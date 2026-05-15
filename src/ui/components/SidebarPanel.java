package ui.components;

import ui.theme.NutrixIcons;
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

        NutrixIcons.IconType[] iconTypes = {
            NutrixIcons.IconType.DASHBOARD, NutrixIcons.IconType.PATIENT, 
            NutrixIcons.IconType.SCALE, NutrixIcons.IconType.TARGET, 
            NutrixIcons.IconType.PILL, NutrixIcons.IconType.WATER, 
            NutrixIcons.IconType.MONITOR, NutrixIcons.IconType.FOOD, 
            NutrixIcons.IconType.CHART, NutrixIcons.IconType.SEARCH, 
            NutrixIcons.IconType.BOX
        };

        for (int i = 0; i < menuLabels.length; i++) {
            JPanel item = createMenuItem(iconTypes[i % iconTypes.length], menuLabels[i], i);
            menuItems.add(item);
            menuContainer.add(item);
            menuContainer.add(Box.createVerticalStrut(8));
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
        
        JLabel userLabel = new JLabel("Nutricionista Logado");
        userLabel.setFont(NutrixTheme.FONT_SMALL);
        userLabel.setForeground(NutrixTheme.TEXT_MUTED);
        
        JLabel logout = new JLabel("Encerrar Sessão");
        logout.setFont(NutrixTheme.FONT_BODY_BOLD);
        logout.setForeground(Color.WHITE);
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        footer.add(userLabel, BorderLayout.NORTH);
        footer.add(logout, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        updateSelection(0);
    }

    private JPanel createMenuItem(NutrixIcons.IconType type, String label, int index) {
        JPanel item = new JPanel(new BorderLayout(15, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                NutrixTheme.aplicarAntiAliasing(g2);
                if (index == selectedIndex) {
                    g2.setColor(new Color(56, 189, 248, 30));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    
                    g2.setColor(NutrixTheme.ACCENT);
                    g2.fillRoundRect(0, 10, 4, getHeight() - 20, 2, 2);
                }
                
                // Draw Icon
                int iconSize = 20;
                int x = 15;
                int y = (getHeight() - iconSize) / 2;
                g2.translate(x, y);
                NutrixIcons.drawIcon(g2, type, iconSize, index == selectedIndex ? NutrixTheme.ACCENT : NutrixTheme.TEXT_MUTED);
                g2.translate(-x, -y);
                
                g2.dispose();
                super.paintChildren(g);
            }
        };
        item.setOpaque(false);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        item.setBorder(new EmptyBorder(0, 45, 0, 15)); // Offset text for icon
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel textLabel = new JLabel(label);
        textLabel.setFont(NutrixTheme.FONT_BODY);
        textLabel.setForeground(NutrixTheme.TEXT_ON_DARK);
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
                    item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    item.repaint();
                }
            }
        });

        return item;
    }

    private void updateSelection(int index) {
        for (int i = 0; i < menuItems.size(); i++) {
            JPanel item = menuItems.get(i);
            boolean selected = (i == index);
            JLabel textLabel = (JLabel) item.getComponent(0);
            textLabel.setForeground(selected ? Color.WHITE : NutrixTheme.TEXT_MUTED);
            textLabel.setFont(selected ? NutrixTheme.FONT_BODY_BOLD : NutrixTheme.FONT_BODY);
            item.repaint();
        }
    }
}
