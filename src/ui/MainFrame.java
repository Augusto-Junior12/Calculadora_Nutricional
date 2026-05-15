package ui;

import ui.components.SidebarPanel;
import ui.panels.*;
import ui.theme.HospitalTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Frame principal do sistema Facilita Nutri UTI.
 * Usa CardLayout com SidebarPanel para navegação entre módulos.
 */
public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    private static final String[] MENU_LABELS = {
        "Dashboard",
        "Cadastro Paciente",
        "Antropometria",
        "Necessidades Nutri.",
        "Prescrição Dieta",
        "Hidratação",
        "Cálculos Clínicos",
        "Controle Ingestão",
        "Prescrito × Infundido",
        "Consulta Fórmulas",
        "TNE Sistema Aberto"
    };

    private static final String[] PANEL_KEYS = {
        "dashboard", "cadastro", "antropometria", "necessidades",
        "prescricao", "hidratacao", "calculos", "ingestao",
        "infundido", "formulas", "tneaberta"
    };

    public MainFrame() {
        super("Facilita Nutri UTI v2.0 — Sistema de Terapia Nutricional");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1320, 850);
        setMinimumSize(new Dimension(1024, 700));
        setLocationRelativeTo(null);

        // Icon da janela (emoji de hospital como texto)
        setLayout(new BorderLayout());

        // Content area com CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(HospitalTheme.BACKGROUND);

        // Registrar painéis
        contentPanel.add(new DashboardPanel(), PANEL_KEYS[0]);
        contentPanel.add(new PacienteCadastroPanel(), PANEL_KEYS[1]);
        contentPanel.add(new AntropometriaPanel(), PANEL_KEYS[2]);
        contentPanel.add(new NecessidadesPanel(), PANEL_KEYS[3]);
        contentPanel.add(new PrescricaoPanel(), PANEL_KEYS[4]);
        contentPanel.add(new HidratacaoPanel(), PANEL_KEYS[5]);
        contentPanel.add(new CalculosClinicosPanel(), PANEL_KEYS[6]);
        contentPanel.add(new IngestaoOralPanel(), PANEL_KEYS[7]);
        contentPanel.add(new PrescritoInfundidoPanel(), PANEL_KEYS[8]);
        contentPanel.add(new ConsultaFormulasPanel(), PANEL_KEYS[9]);
        contentPanel.add(new TNEAbertaPanel(), PANEL_KEYS[10]);

        // Sidebar
        SidebarPanel sidebar = new SidebarPanel(MENU_LABELS, (index, name) -> {
            if (index < PANEL_KEYS.length) {
                cardLayout.show(contentPanel, PANEL_KEYS[index]);
            }
        });

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Mostrar Dashboard
        cardLayout.show(contentPanel, PANEL_KEYS[0]);
    }

    /**
     * Ponto de entrada principal da aplicação.
     */
    public static void main(String[] args) {
        HospitalTheme.configurarLookAndFeel();

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
