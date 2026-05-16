package ui;

import ui.components.NutrixSidebar;
import ui.panels.*;
import ui.theme.NutrixUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * MainFrame v5.1 — Nutrix Hospital OS.
 * Atualizado: sidebar com logout funcional, mapeamento de índice original.
 */
public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentArea;
    private final JLabel titleBar;
    private final NutrixSidebar sidebar;

    // Índice original corresponde ao índice em ENTRIES do NutrixSidebar
    private static final String[] PANEL_KEYS = {
        "dashboard", "cadastro", "antropometria", "necessidades",
        "prescricao", "hidratacao", "calculos", "ingestao",
        "infundido", "formulas", "evolucao", "relatorios", "config"
    };

    private static final String[] PANEL_TITLES = {
        "Dashboard", "Admissão de Pacientes", "Avaliação Antropométrica", "Metas Nutricionais",
        "Prescrição Enteral", "Hidratação", "Monitoramento UTI", "Ingestão Oral",
        "Controle de Qualidade P×I", "Catálogo de Fórmulas", "Evolução Clínica", "Relatórios Gerenciais", "Configurações do Sistema"
    };

    public MainFrame() {
        super("Nutrix Hospital OS");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1320, 840);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(960, 620));

        // ── Pre-init ──
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setOpaque(false);

        titleBar = new JLabel("Dashboard");
        titleBar.setFont(NutrixUI.DISPLAY);
        titleBar.setForeground(NutrixUI.TEXT_PRIMARY);

        // ── Root ──
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(NutrixUI.SIDEBAR_BG);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(root);

        // ── Sidebar ──
        sidebar = new NutrixSidebar(
            this::navigate,
            () -> {
                // Logout
                dispose();
                new LoginFrame().setVisible(true);
            }
        );
        root.add(sidebar, BorderLayout.WEST);
        sidebar.installHoverTrigger(root);

        // ── Right canvas ──
        JPanel rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.setOpaque(false);
        rightWrapper.setBorder(new EmptyBorder(16, 0, 16, 16));

        JPanel canvas = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); NutrixUI.aa(g2);
                g2.setColor(NutrixUI.BG_PAGE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
            }
        };
        canvas.setOpaque(false);

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(24, 36, 16, 36));
        topBar.add(titleBar, BorderLayout.WEST);

        // Status pill
        JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        statusRow.setOpaque(false);
        
        auth.AuthUser user = auth.UserSession.get().getUser();
        String roleLabel = user != null ? user.getRole().getLabel() : "—";
        JLabel roleBadge = NutrixUI.badge(roleLabel, NutrixUI.ACCENT_LIGHT, NutrixUI.ACCENT);
        JLabel statusLabel = NutrixUI.badge("● Online", NutrixUI.SUCCESS_LIGHT, NutrixUI.SUCCESS);
        statusRow.add(roleBadge);
        statusRow.add(statusLabel);
        topBar.add(statusRow, BorderLayout.EAST);

        canvas.add(topBar, BorderLayout.NORTH);

        // Scroll + content
        JScrollPane scroll = new JScrollPane(contentArea);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(new EmptyBorder(0, 36, 24, 36));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        canvas.add(scroll, BorderLayout.CENTER);

        rightWrapper.add(canvas, BorderLayout.CENTER);
        root.add(rightWrapper, BorderLayout.CENTER);

        // ── Register Panels ──
        contentArea.add(new DashboardPanel(this::navigate), PANEL_KEYS[0]);
        contentArea.add(new PacienteCadastroPanel(),   PANEL_KEYS[1]);
        contentArea.add(new AntropometriaPanel(),      PANEL_KEYS[2]);
        contentArea.add(new NecessidadesPanel(),       PANEL_KEYS[3]);
        contentArea.add(new PrescricaoPanel(),         PANEL_KEYS[4]);
        contentArea.add(new HidratacaoPanel(),         PANEL_KEYS[5]);
        contentArea.add(new CalculosClinicosPanel(),   PANEL_KEYS[6]);
        contentArea.add(new IngestaoOralPanel(),       PANEL_KEYS[7]);
        contentArea.add(new PrescritoInfundidoPanel(), PANEL_KEYS[8]);
        contentArea.add(new ConsultaFormulasPanel(),   PANEL_KEYS[9]);
        contentArea.add(new JPanel(),                  PANEL_KEYS[10]); // Evolução
        contentArea.add(new JPanel(),                  PANEL_KEYS[11]); // Relatórios
        contentArea.add(new ConfiguracoesPanel(),      PANEL_KEYS[12]);
    }

    public void navigate(int originalIndex) {
        if (originalIndex < PANEL_KEYS.length) {
            cardLayout.show(contentArea, PANEL_KEYS[originalIndex]);
            titleBar.setText(PANEL_TITLES[originalIndex]);
            sidebar.setSelectedItem(originalIndex);
        }
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
