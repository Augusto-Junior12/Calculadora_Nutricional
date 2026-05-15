package ui;

import ui.components.SidebarPanel;
import ui.panels.*;
import ui.theme.NutrixTheme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Nutrix Hospital OS — Frame Principal.
 */
public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentWrapper;
    private JLabel activePanelLabel;

    private static final String[] MENU_LABELS = {
        "Dashboard", "Admissão", "Antropometria", "Metas Nutri.", 
        "Prescrição", "Hidratação", "Monitor UTI", "Ingestão Oral", 
        "Qualidade (P×I)", "Fórmulas", "Sistema Aberto"
    };

    private static final String[] PANEL_KEYS = {
        "dashboard", "cadastro", "antropometria", "necessidades",
        "prescricao", "hidratacao", "calculos", "ingestao",
        "infundido", "formulas", "tneaberta"
    };

    public MainFrame() {
        super("Nutrix Hospital OS v2.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1350, 880);
        setLocationRelativeTo(null);

        // Main Container
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(NutrixTheme.BG_MAIN);

        // --- Right Side Wrapper ---
        JPanel rightSide = new JPanel(new BorderLayout());
        rightSide.setOpaque(false);

        // Global Header (Top Bar)
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 65));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, NutrixTheme.BORDER));
        header.setBorder(new CompoundBorder(header.getBorder(), new EmptyBorder(0, 45, 0, 45)));

        activePanelLabel = new JLabel("DASHBOARD");
        activePanelLabel.setFont(NutrixTheme.FONT_H3);
        activePanelLabel.setForeground(NutrixTheme.TEXT_H1);
        header.add(activePanelLabel, BorderLayout.WEST);

        // Patient Status Placeholder
        JLabel statusInfo = new JLabel("Sessão: Dr. Nutricionista | UTI Central");
        statusInfo.setFont(NutrixTheme.FONT_SMALL);
        statusInfo.setForeground(NutrixTheme.TEXT_MUTED);
        header.add(statusInfo, BorderLayout.EAST);

        // Content Area
        cardLayout = new CardLayout();
        contentWrapper = new JPanel(cardLayout);
        contentWrapper.setOpaque(false);

        // Adicionar painéis
        contentWrapper.add(new DashboardPanel(), PANEL_KEYS[0]);
        contentWrapper.add(new PacienteCadastroPanel(), PANEL_KEYS[1]);
        contentWrapper.add(new AntropometriaPanel(), PANEL_KEYS[2]);
        contentWrapper.add(new NecessidadesPanel(), PANEL_KEYS[3]);
        contentWrapper.add(new PrescricaoPanel(), PANEL_KEYS[4]);
        contentWrapper.add(new HidratacaoPanel(), PANEL_KEYS[5]);
        contentWrapper.add(new CalculosClinicosPanel(), PANEL_KEYS[6]);
        contentWrapper.add(new IngestaoOralPanel(), PANEL_KEYS[7]);
        contentWrapper.add(new PrescritoInfundidoPanel(), PANEL_KEYS[8]);
        contentWrapper.add(new ConsultaFormulasPanel(), PANEL_KEYS[9]);
        contentWrapper.add(new TNEAbertaPanel(), PANEL_KEYS[10]);

        // --- Sidebar ---
        SidebarPanel sidebar = new SidebarPanel(MENU_LABELS, (index, name) -> {
            cardLayout.show(contentWrapper, PANEL_KEYS[index]);
            activePanelLabel.setText(name.toUpperCase());
        });

        rightSide.add(header, BorderLayout.NORTH);
        rightSide.add(contentWrapper, BorderLayout.CENTER);

        mainContainer.add(sidebar, BorderLayout.WEST);
        mainContainer.add(rightSide, BorderLayout.CENTER);

        add(mainContainer);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
