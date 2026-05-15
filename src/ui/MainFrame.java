package ui;

import ui.panels.*;
import ui.theme.NutrixIcons;
import ui.theme.NutrixTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Nutrix Hospital OS — Minimal Hub Edition.
 * Navegação direta por "Apps" e Top Bar simplificada.
 */
public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentWrapper;
    private final JPanel navBar;

    private static final String[] APPS = {
        "DASHBOARD", "PACIENTES", "ANTROPOMETRIA", "METAS", 
        "PRESCRIÇÃO", "HIDRATAÇÃO", "CLÍNICO", "INGESTÃO", "QUALIDADE", "FÓRMULAS"
    };

    private static final String[] KEYS = {
        "dashboard", "cadastro", "antropometria", "necessidades",
        "prescricao", "hidratacao", "calculos", "ingestao", "infundido", "formulas"
    };

    public MainFrame() {
        super("Nutrix");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        // --- Layout Principal: Top Nav + Content ---
        setLayout(new BorderLayout());

        // Top Navigation Bar (Modular & Direct)
        navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        navBar.setBackground(Color.WHITE);
        navBar.setPreferredSize(new Dimension(0, 70));
        navBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, NutrixTheme.BORDER));
        
        // Brand Logo
        JLabel brand = new JLabel("NUTRIX");
        brand.setFont(new Font("Inter", Font.BOLD, 20));
        brand.setForeground(NutrixTheme.TEXT_PRIMARY);
        brand.setBorder(new EmptyBorder(0, 30, 0, 40));
        navBar.add(brand);

        // Navigation Items
        for (int i = 0; i < APPS.length; i++) {
            navBar.add(createNavLink(APPS[i], i));
        }

        add(navBar, BorderLayout.NORTH);

        // Content Area
        cardLayout = new CardLayout();
        contentWrapper = new JPanel(cardLayout);
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(40, 60, 40, 60));

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

        add(contentWrapper, BorderLayout.CENTER);
        
        showApp(0);
    }

    private JLabel createNavLink(String text, int index) {
        JLabel link = new JLabel(text);
        link.setFont(new Font("Inter", Font.BOLD, 12));
        link.setForeground(NutrixTheme.TEXT_SECONDARY);
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        link.setBorder(new EmptyBorder(25, 0, 25, 0));

        link.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showApp(index);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                link.setForeground(NutrixTheme.ACCENT);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!link.getForeground().equals(NutrixTheme.ACCENT)) {
                    link.setForeground(NutrixTheme.TEXT_SECONDARY);
                }
            }
        });
        return link;
    }

    private void showApp(int index) {
        cardLayout.show(contentWrapper, KEYS[index]);
        for (Component c : navBar.getComponents()) {
            if (c instanceof JLabel && !((JLabel)c).getText().equals("NUTRIX")) {
                ((JLabel)c).setForeground(NutrixTheme.TEXT_SECONDARY);
            }
        }
        ((JLabel)navBar.getComponent(index + 1)).setForeground(NutrixTheme.ACCENT);
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");
        
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
