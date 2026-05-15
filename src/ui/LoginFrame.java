package ui;

import auth.AuthUser;
import auth.UserSession;
import firebase.AuthService;
import firebase.FirebaseConfig;
import ui.theme.NutrixUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * LoginFrame — Tela de Login Nutrix Hospital OS.
 *
 * Design: dois painéis.
 *   LEFT  — Branding / Identidade Visual
 *   RIGHT — Formulário de Login
 *
 * Suporta demo mode quando Firebase não está configurado.
 */
public class LoginFrame extends JFrame {

    private final AuthService authService = new AuthService();
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private JButton loginButton;

    public LoginFrame() {
        super("Nutrix Hospital OS — Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 580);
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(true);

        // Rounded window shape
        setShape(new RoundRectangle2D.Double(0, 0, 900, 580, 28, 28));

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setBackground(Color.WHITE);
        setContentPane(root);

        root.add(buildBrandPanel());
        root.add(buildFormPanel());

        // Window drag (undecorated)
        installDrag(root);
    }

    // ──────────────── LEFT: Brand ────────────────
    private JPanel buildBrandPanel() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); NutrixUI.aa(g2);
                GradientPaint gp = new GradientPaint(0, 0, new Color(109, 40, 217),
                    getWidth(), getHeight(), new Color(15, 23, 42));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(60, 50, 60, 50));

        // Logo badge
        JPanel badge = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); NutrixUI.aa(g2);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(0, 0, 60, 60, 18, 18);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
                g2.drawString("N", 18, 43);
                g2.dispose();
            }
        };
        badge.setOpaque(false);
        badge.setPreferredSize(new Dimension(60, 60));
        badge.setMaximumSize(new Dimension(60, 60));
        badge.setAlignmentX(0f);
        p.add(badge);
        p.add(Box.createVerticalStrut(40));

        addBrandText(p, "Nutrix", 30, Font.BOLD, Color.WHITE);
        addBrandText(p, "Hospital OS", 16, Font.PLAIN, new Color(196, 181, 253));
        p.add(Box.createVerticalStrut(30));
        addBrandText(p, "Sistema de Gestão de", 14, Font.PLAIN, new Color(196, 181, 253));
        addBrandText(p, "Terapia Nutricional Enteral", 14, Font.PLAIN, new Color(196, 181, 253));
        p.add(Box.createVerticalStrut(50));

        // Feature list
        String[] features = {"• Avaliação Antropométrica", "• Prescrição Enteral Avançada",
            "• Monitoramento de UTI", "• Catálogo com 51 Fórmulas"};
        for (String f : features) {
            JLabel l = new JLabel(f);
            l.setFont(NutrixUI.BODY);
            l.setForeground(new Color(221, 214, 254));
            l.setAlignmentX(0f);
            p.add(l);
            p.add(Box.createVerticalStrut(8));
        }

        p.add(Box.createVerticalGlue());
        addBrandText(p, "v2.0 — Nutrix Clinical Suite", 11, Font.PLAIN, new Color(139, 92, 246));

        return p;
    }

    private void addBrandText(JPanel p, String text, int size, int style, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", style, size));
        l.setForeground(color);
        l.setAlignmentX(0f);
        p.add(l);
    }

    // ──────────────── RIGHT: Form ────────────────
    private JPanel buildFormPanel() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(50, 50, 40, 50));

        // Close button
        JLabel close = new JLabel("✕");
        close.setFont(NutrixUI.H2);
        close.setForeground(NutrixUI.TEXT_MUTED);
        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.setAlignmentX(1f);
        close.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { System.exit(0); }
        });
        p.add(close);
        p.add(Box.createVerticalGlue());

        addFormText(p, "Bem-vindo de volta", 26, Font.BOLD, NutrixUI.TEXT_PRIMARY);
        addFormText(p, "Faça login para continuar", 14, Font.PLAIN, NutrixUI.TEXT_MUTED);
        p.add(Box.createVerticalStrut(36));

        // Email
        addFieldLabel(p, "E-MAIL INSTITUCIONAL");
        emailField = NutrixUI.input("nutricionista@hospital.com");
        emailField.setAlignmentX(0f);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        p.add(emailField);
        p.add(Box.createVerticalStrut(18));

        // Password
        addFieldLabel(p, "SENHA");
        passwordField = new JPasswordField();
        passwordField.setFont(NutrixUI.BODY);
        passwordField.setOpaque(false);
        passwordField.setBorder(new EmptyBorder(12, 16, 12, 16));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin();
            }
        });
        p.add(passwordField);
        p.add(Box.createVerticalStrut(6));

        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(NutrixUI.SMALL);
        errorLabel.setForeground(NutrixUI.DANGER);
        errorLabel.setAlignmentX(0f);
        p.add(errorLabel);
        p.add(Box.createVerticalStrut(18));

        // Login button
        loginButton = NutrixUI.btnPrimary("ENTRAR NO SISTEMA");
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        loginButton.setAlignmentX(0f);
        loginButton.addActionListener(e -> doLogin());
        p.add(loginButton);
        p.add(Box.createVerticalStrut(14));

        // Demo mode notice
        if (!FirebaseConfig.isConfigured()) {
            JLabel demo = new JLabel("⚡ Modo Demonstração ativo — credenciais não verificadas");
            demo.setFont(NutrixUI.SMALL);
            demo.setForeground(NutrixUI.WARNING);
            demo.setAlignmentX(0f);
            p.add(demo);
        }

        p.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("© 2025 Nutrix Hospital OS. Uso clínico autorizado.");
        footer.setFont(NutrixUI.SMALL);
        footer.setForeground(NutrixUI.TEXT_MUTED);
        footer.setAlignmentX(0.5f);
        p.add(footer);

        return p;
    }

    private void addFormText(JPanel p, String text, int size, int style, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", style, size));
        l.setForeground(color);
        l.setAlignmentX(0f);
        p.add(l);
        p.add(Box.createVerticalStrut(6));
    }

    private void addFieldLabel(JPanel p, String text) {
        JLabel l = new JLabel(text);
        l.setFont(NutrixUI.LABEL);
        l.setForeground(NutrixUI.TEXT_SECONDARY);
        l.setAlignmentX(0f);
        l.setBorder(new EmptyBorder(0, 2, 6, 0));
        p.add(l);
    }

    // ──────────────── Login Logic ────────────────
    private void doLogin() {
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (email.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Preencha e-mail e senha.");
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Autenticando...");
        errorLabel.setText(" ");

        // Run in background thread to not block EDT
        new Thread(() -> {
            AuthUser user;

            if (FirebaseConfig.isConfigured()) {
                // Firebase real
                AuthService.AuthResult result = authService.signIn(email, pass);
                if (!result.isSuccess()) {
                    SwingUtilities.invokeLater(() -> {
                        errorLabel.setText(result.errorMessage());
                        loginButton.setEnabled(true);
                        loginButton.setText("ENTRAR NO SISTEMA");
                    });
                    return;
                }
                user = result.user();
            } else {
                // Demo mode — aceita qualquer credencial
                auth.UserRole role = email.contains("admin") ? auth.UserRole.ADMIN : auth.UserRole.MEDICO;
                String name = email.split("@")[0];
                user = new AuthUser("demo-uid", email, name, role, "demo-token");
            }

            final AuthUser finalUser = user;
            SwingUtilities.invokeLater(() -> {
                UserSession.get().login(finalUser);
                dispose();
                new MainFrame().setVisible(true);
            });
        }).start();
    }

    // ──────────────── Window Drag ────────────────
    private void installDrag(Component comp) {
        final Point[] dragStart = {null};
        comp.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { dragStart[0] = e.getPoint(); }
        });
        comp.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (dragStart[0] == null) return;
                Point loc = getLocation();
                setLocation(loc.x + e.getX() - dragStart[0].x, loc.y + e.getY() - dragStart[0].y);
            }
        });
    }
}
