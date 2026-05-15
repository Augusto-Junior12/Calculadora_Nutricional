package ui;

import ui.panels.*;
import ui.theme.NutrixIcons;
import ui.theme.NutrixTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Nutrix Hospital OS — v4 Premium Edition.
 * Design robusto, arredondado e estético.
 */
public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentWrapper;
    private final JPanel sidebar;
    private int selectedIndex = 0;

    private static final String[] APPS = {
        "DASHBOARD", "PACIENTES", "ANTROPOMETRIA", "METAS", 
        "PRESCRIÇÃO", "HIDRATAÇÃO", "CLÍNICO", "INGESTÃO", "QUALIDADE", "FÓRMULAS"
    };

    private static final String[] KEYS = {
        "dashboard", "cadastro", "antropometria", "necessidades",
        "prescricao", "hidratacao", "calculos", "ingestao", "infundido", "formulas"
    };

    private static final NutrixIcons.IconType[] ICONS = {
        NutrixIcons.IconType.DASHBOARD, NutrixIcons.IconType.PATIENT, 
        NutrixIcons.IconType.SCALE, NutrixIcons.IconType.TARGET, 
        NutrixIcons.IconType.PILL, NutrixIcons.IconType.WATER, 
        NutrixIcons.IconType.MONITOR, NutrixIcons.IconType.FOOD, 
        NutrixIcons.IconType.CHART, NutrixIcons.IconType.SEARCH
    };

    public MainFrame() {
        super("Nutrix Hospital OS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        getContentPane().setBackground(NutrixTheme.BG_PAGE);

        setLayout(new BorderLayout());

        // --- Elegant Sidebar ---
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(NutrixTheme.BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setBorder(new EmptyBorder(40, 20, 40, 20));

        // Logo
        JLabel logo = new JLabel("NUTRIX");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(40));

        // Nav Items
        for (int i = 0; i < APPS.length; i++) {
            sidebar.add(createSidebarItem(i));
            sidebar.add(Box.createVerticalStrut(10));
        }

        add(sidebar, BorderLayout.WEST);

        // --- Content Area (Rounded Canvas) ---
        JPanel canvas = new JPanel(new BorderLayout());
        canvas.setOpaque(false);
        canvas.setBorder(new EmptyBorder(25, 25, 25, 25));

        cardLayout = new CardLayout();
        contentWrapper = NutrixTheme.createRoundedPanel(30, Color.WHITE);
        contentWrapper.setLayout(cardLayout);
        contentWrapper.setBorder(new EmptyBorder(40, 50, 40, 50));

        // Registrar painéis
        contentWrapper.add(new DashboardPanel(), KEYS[0]);
        contentWrapper.add(new PacienteCadastroPanel(), KEYS[1]);
        contentWrapper.add(new AntropometriaPanel(), KEYS[2]);
        contentWrapper.add(new NecessidadesPanel(), KEYS[3]);
        contentWrapper.add(new PrescricaoPanel(), KEYS[4]);
        contentWrapper.add(new HidratacaoPanel(), KEYS[5]);
        contentWrapper.add(new CalculosClinicosPanel(), KEYS[6]);
        contentWrapper.add(new IngestaoOralPanel(), KEYS[7]);
        contentWrapper.add(new PrescritoInfundidoPanel(), KEYS[8]);
        contentWrapper.add(new ConsultaFormulasPanel(), KEYS[9]);

        canvas.add(contentWrapper, BorderLayout.CENTER);
        add(canvas, BorderLayout.CENTER);
        
        updateSidebarSelection();
    }

    private JPanel createSidebarItem(int index) {
        JPanel item = new JPanel(new BorderLayout(15, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                if (index == selectedIndex) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    NutrixTheme.aplicarAntiAliasing(g2);
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2.dispose();
                }
                super.paintChildren(g);
            }
        };
        item.setOpaque(false);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.setBorder(new EmptyBorder(0, 20, 0, 20));

        // Icon Badge (Vector)
        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                NutrixIcons.drawIcon(g2, ICONS[index], 18, (index == selectedIndex) ? Color.WHITE : NutrixTheme.TEXT_MUTED);
                g2.dispose();
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(20, 20));

        JLabel label = new JLabel(APPS[index]);
        label.setFont(NutrixTheme.FONT_SMALL);
        label.setForeground((index == selectedIndex) ? Color.WHITE : NutrixTheme.TEXT_MUTED);

        item.add(iconBadge, BorderLayout.WEST);
        item.add(label, BorderLayout.CENTER);

        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectedIndex = index;
                cardLayout.show(contentWrapper, KEYS[index]);
                updateSidebarSelection();
            }
        });

        return item;
    }

    private void updateSidebarSelection() {
        for (int i = 0; i < sidebar.getComponentCount(); i++) {
            Component c = sidebar.getComponent(i);
            if (c instanceof JPanel) {
                c.repaint();
                for (Component sub : ((JPanel)c).getComponents()) {
                    if (sub instanceof JLabel) {
                        int index = (i - 2) / 2; // Adjust for logo and struts
                        // Note: Logic for index might be fragile, but works for this structure
                    }
                }
            }
        }
        sidebar.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
