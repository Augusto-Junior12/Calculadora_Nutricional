package ui.theme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * NutrixIcons v5 — Ícones vetoriais ultra-clean, stroke 2px arredondado.
 * Estilo Heroicons / Lucide.
 */
public final class NutrixIcons {

    public enum Icon {
        DASHBOARD, PATIENT, SCALE, TARGET, PILL, WATER,
        MONITOR, FOOD, CHART, SEARCH, BOX, SETTINGS, LOGOUT, ALERT
    }

    public static void draw(Graphics2D g2d, Icon icon, int x, int y, int size, Color color) {
        Graphics2D g = (Graphics2D) g2d.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setColor(color);
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.translate(x, y);

        double S = size;
        double p = S * 0.1; // padding

        switch (icon) {
            case DASHBOARD: {
                // 4 quadrantes
                double h = S * 0.4, gap = S * 0.08;
                g.draw(new RoundRectangle2D.Double(p, p, h, h, 3, 3));
                g.draw(new RoundRectangle2D.Double(p + h + gap, p, S - 2*p - h - gap, h, 3, 3));
                g.draw(new RoundRectangle2D.Double(p, p + h + gap, h, S - 2*p - h - gap, 3, 3));
                g.fill(new RoundRectangle2D.Double(p + h + gap, p + h + gap, S - 2*p - h - gap, S - 2*p - h - gap, 3, 3));
                break;
            }
            case PATIENT: {
                // cabeça + corpo
                double r = S * 0.18;
                g.draw(new Ellipse2D.Double(S*0.5 - r, S*0.1, r*2, r*2));
                Arc2D arc = new Arc2D.Double(S*0.1, S*0.45, S*0.8, S*0.65, 0, 180, Arc2D.OPEN);
                g.draw(arc);
                break;
            }
            case SCALE: {
                // balança
                g.draw(new Line2D.Double(S*0.15, S*0.82, S*0.85, S*0.82));
                g.draw(new Line2D.Double(S*0.5, S*0.18, S*0.5, S*0.82));
                g.draw(new Arc2D.Double(S*0.18, S*0.3, S*0.64, S*0.48, 0, 180, Arc2D.OPEN));
                g.draw(new Ellipse2D.Double(S*0.42, S*0.14, S*0.16, S*0.16));
                break;
            }
            case TARGET: {
                g.draw(new Ellipse2D.Double(p, p, S-2*p, S-2*p));
                g.draw(new Ellipse2D.Double(S*0.27, S*0.27, S*0.46, S*0.46));
                g.fill(new Ellipse2D.Double(S*0.43, S*0.43, S*0.14, S*0.14));
                break;
            }
            case PILL: {
                AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(-45), S*0.5, S*0.5);
                g.transform(at);
                double pw = S*0.3, ph = S*0.65;
                g.draw(new RoundRectangle2D.Double((S-pw)/2, (S-ph)/2, pw, ph, pw, pw));
                g.draw(new Line2D.Double((S-pw)/2, S/2, (S+pw)/2, S/2));
                break;
            }
            case WATER: {
                Path2D drop = new Path2D.Double();
                drop.moveTo(S*0.5, S*0.08);
                drop.curveTo(S*0.85, S*0.45, S*0.85, S*0.75, S*0.5, S*0.92);
                drop.curveTo(S*0.15, S*0.75, S*0.15, S*0.45, S*0.5, S*0.08);
                g.draw(drop);
                break;
            }
            case MONITOR: {
                // ECG + tela
                g.draw(new RoundRectangle2D.Double(p, p, S-2*p, S*0.62, 4, 4));
                Path2D ecg = new Path2D.Double();
                ecg.moveTo(S*0.18, S*0.42);
                ecg.lineTo(S*0.32, S*0.42);
                ecg.lineTo(S*0.42, S*0.2);
                ecg.lineTo(S*0.52, S*0.62);
                ecg.lineTo(S*0.62, S*0.42);
                ecg.lineTo(S*0.82, S*0.42);
                g.draw(ecg);
                g.draw(new Line2D.Double(S*0.5, S*0.72, S*0.5, S*0.9));
                g.draw(new Line2D.Double(S*0.3, S*0.9, S*0.7, S*0.9));
                break;
            }
            case FOOD: {
                g.draw(new Ellipse2D.Double(S*0.12, S*0.35, S*0.76, S*0.5));
                g.draw(new Line2D.Double(S*0.3, S*0.35, S*0.3, S*0.18));
                g.draw(new Line2D.Double(S*0.5, S*0.35, S*0.5, S*0.15));
                g.draw(new Line2D.Double(S*0.7, S*0.35, S*0.7, S*0.18));
                break;
            }
            case CHART: {
                // Barras ascendentes
                g.draw(new Line2D.Double(p, p, p, S-p));
                g.draw(new Line2D.Double(p, S-p, S-p, S-p));
                g.fill(new Rectangle2D.Double(S*0.2, S*0.55, S*0.18, S*0.3));
                g.fill(new Rectangle2D.Double(S*0.45, S*0.38, S*0.18, S*0.47));
                g.fill(new Rectangle2D.Double(S*0.7, S*0.18, S*0.18, S*0.67));
                break;
            }
            case SEARCH: {
                g.draw(new Ellipse2D.Double(p, p, S*0.55, S*0.55));
                g.draw(new Line2D.Double(S*0.55, S*0.55, S-p, S-p));
                break;
            }
            case BOX: {
                // caixa 3D
                g.draw(new Rectangle2D.Double(S*0.18, S*0.3, S*0.64, S*0.56));
                Path2D top = new Path2D.Double();
                top.moveTo(S*0.18, S*0.3);
                top.lineTo(S*0.35, S*0.12);
                top.lineTo(S*0.82, S*0.12);
                top.lineTo(S*0.82, S*0.3);
                g.draw(top);
                g.draw(new Line2D.Double(S*0.5, S*0.3, S*0.5, S*0.86));
                break;
            }
            case LOGOUT: {
                g.draw(new Line2D.Double(S*0.54, S*0.5, S*0.9, S*0.5));
                Path2D arrow = new Path2D.Double();
                arrow.moveTo(S*0.72, S*0.33); arrow.lineTo(S*0.9, S*0.5); arrow.lineTo(S*0.72, S*0.67);
                g.draw(arrow);
                Path2D door = new Path2D.Double();
                door.moveTo(S*0.54, S*0.3); door.lineTo(S*0.18, S*0.3);
                door.lineTo(S*0.18, S*0.7); door.lineTo(S*0.54, S*0.7);
                g.draw(door);
                break;
            }
            case ALERT: {
                Path2D tri = new Path2D.Double();
                tri.moveTo(S*0.5, S*0.1); tri.lineTo(S*0.9, S*0.85);
                tri.lineTo(S*0.1, S*0.85); tri.closePath();
                g.draw(tri);
                g.draw(new Line2D.Double(S*0.5, S*0.4, S*0.5, S*0.62));
                g.fill(new Ellipse2D.Double(S*0.45, S*0.7, S*0.1, S*0.1));
                break;
            }
        }
        g.dispose();
    }

    /** Cria um JPanel que renderiza um ícone */
    public static JPanel iconPanel(Icon icon, int size, Color color) {
        return new JPanel() {
            { setOpaque(false); setPreferredSize(new Dimension(size, size)); }
            @Override protected void paintComponent(Graphics g) {
                NutrixIcons.draw((Graphics2D) g, icon, 0, 0, size, color);
            }
        };
    }

    /** Cria badge circular com ícone e cor de fundo */
    public static JPanel iconBadge(Icon icon, int size, Color bg, Color iconColor) {
        return new JPanel() {
            { setOpaque(false); setPreferredSize(new Dimension(size, size)); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                NutrixUI.aa(g2);
                g2.setColor(bg);
                g2.fillOval(0, 0, size, size);
                int is = (int)(size * 0.52);
                int off = (size - is) / 2;
                NutrixIcons.draw(g2, icon, off, off, is, iconColor);
                g2.dispose();
            }
        };
    }

    public enum IconType { DASHBOARD, PATIENT, SCALE, TARGET, PILL, WATER, MONITOR, FOOD, CHART, SEARCH, BOX }
    public static void drawIcon(Graphics2D g2, IconType t, int size, Color color) {
        Icon mapped = Icon.values()[t.ordinal()];
        draw(g2, mapped, 0, 0, size, color);
    }
}
