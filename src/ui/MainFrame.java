package ui;

import ui.components.NutrixSidebar;
import ui.panels.*;
import ui.theme.NutrixUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * MainFrame v5 — Nutrix Hospital OS.
 * Layout: Sidebar escura | Content canvas arredondado.
 */
public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentArea;
    private final JLabel titleBar;

    private static final String[] PANEL_KEYS = {
        "dashboard", "cadastro", "antropometria", "necessidades",
        "prescricao", "hidratacao", "calculos", "ingestao",
        "infundido", "formulas"
    };

    private static final String[] PANEL_TITLES = {
        "Dashboard", "Admissão de Pacientes", "Avaliação Antropométrica", "Metas Nutricionais",
        "Prescrição Enteral", "Hidratação", "Monitoramento UTI", "Ingestão Oral",
        "Controle de Qualidade", "Catálogo de Fórmulas"
    };

    public MainFrame() {
        super("Nutrix Hospital OS");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1300, 820);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        // Root panel com fundo escuro (cobre a janela inteira)
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(NutrixUI.SIDEBAR_BG);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(root);

        // ── Pre-init required fields ──
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setOpaque(false);

        titleBar = new JLabel("Dashboard");
        titleBar.setFont(NutrixUI.DISPLAY);
        titleBar.setForeground(NutrixUI.TEXT_PRIMARY);

        // ── Sidebar ──
        NutrixSidebar sidebar = new NutrixSidebar(index -> {
            cardLayout.show(contentArea, PANEL_KEYS[index]);
            titleBar.setText(PANEL_TITLES[index]);
        });
        root.add(sidebar, BorderLayout.WEST);

        // ── Right Side (Content "canvas" arredondado) ──
        JPanel rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.setOpaque(false);
        rightWrapper.setBorder(new EmptyBorder(18, 0, 18, 18));

        // Canvas com cantos arredondos e fundo BG_PAGE
        JPanel canvas = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); NutrixUI.aa(g2);
                g2.setColor(NutrixUI.BG_PAGE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
            }
        };
        canvas.setOpaque(false);
        canvas.setLayout(new BorderLayout());

        // Top bar dentro do canvas
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(28, 36, 16, 36));

        topBar.add(titleBar, BorderLayout.WEST);

        // Status pill no canto superior direito
        JPanel statusPill = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        statusPill.setOpaque(false);
        JLabel statusLabel = NutrixUI.badge("● Sistema Ativo", NutrixUI.SUCCESS_LIGHT, NutrixUI.SUCCESS);
        statusPill.add(statusLabel);
        topBar.add(statusPill, BorderLayout.EAST);

        canvas.add(topBar, BorderLayout.NORTH);

        JScrollPane scrollArea = new JScrollPane(contentArea);
        scrollArea.setOpaque(false);
        scrollArea.getViewport().setOpaque(false);
        scrollArea.setBorder(new EmptyBorder(0, 36, 28, 36));
        scrollArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        canvas.add(scrollArea, BorderLayout.CENTER);
        rightWrapper.add(canvas, BorderLayout.CENTER);
        root.add(rightWrapper, BorderLayout.CENTER);

        // ── Register Panels ──
        contentArea.add(new DashboardPanel(), PANEL_KEYS[0]);
        contentArea.add(new PacienteCadastroPanel(), PANEL_KEYS[1]);
        contentArea.add(new AntropometriaPanel(), PANEL_KEYS[2]);
        contentArea.add(new NecessidadesPanel(), PANEL_KEYS[3]);
        contentArea.add(new PrescricaoPanel(), PANEL_KEYS[4]);
        contentArea.add(new HidratacaoPanel(), PANEL_KEYS[5]);
        contentArea.add(new CalculosClinicosPanel(), PANEL_KEYS[6]);
        contentArea.add(new IngestaoOralPanel(), PANEL_KEYS[7]);
        contentArea.add(new PrescritoInfundidoPanel(), PANEL_KEYS[8]);
        contentArea.add(new ConsultaFormulasPanel(), PANEL_KEYS[9]);
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
