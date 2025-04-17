package sicklecellsimulation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FamilyTreePanel extends JPanel {
    private final Individual root;
    private double scale = 1.0;

    private int minX = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxY = Integer.MIN_VALUE;
    private static final int VERTICAL_SPACING = 80;






    public FamilyTreePanel(Individual root) {
        this.root = root;
        updatePreferredSize();

        addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                double zoomFactor = e.getPreciseWheelRotation() < 0 ? 1.1 : 0.9;
                scale *= zoomFactor;
                scale = Math.max(0.1, Math.min(scale, 5.0)); // Clamp zoom
                revalidate();
                repaint();
            }
        });
    }
    public void setScale(double scale) {
        this.scale = scale;
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (root == null) return;

        // Reset bounds
        minX = Integer.MAX_VALUE;
        maxX = Integer.MIN_VALUE;
        minY = Integer.MAX_VALUE;
        maxY = Integer.MIN_VALUE;

        int centerX = getWidth() / 2;
        int centerY = 50;

        drawIndividual(g, root, centerX, centerY);

        setPreferredSize(new Dimension((maxX - minX) + 400, (maxY - minY) + 200));
        revalidate();

    }





    private void updatePreferredSize() {
        int depth = getDepth(root);
        int width = getMaxChildrenAtAnyLevel(root) * 100;

        int scaledWidth = (int) (width * scale);
        int scaledHeight = (int) (depth * 120 * scale);

        setPreferredSize(new Dimension(scaledWidth, scaledHeight));
        revalidate();
        repaint();
    }
    private int getMaxChildrenAtAnyLevel(Individual root) {
        if (root == null) return 0;
        int[] levels = new int[getDepth(root)];
        countAtEachLevel(root, 0, levels);
        int max = 0;
        for (int count : levels) {
            max = Math.max(max, count);
        }
        return max;
    }

    private void countAtEachLevel(Individual node, int level, int[] levels) {
        if (node == null) return;
        levels[level]++;
        for (Individual child : node.getChildren()) {
            countAtEachLevel(child, level + 1, levels);
        }
    }

    private int getDepth(Individual ind) {
        if (ind == null || ind.getChildren().isEmpty()) return 1;

        int max = 0;
        for (Individual child : ind.getChildren()) {
            max = Math.max(max, getDepth(child));
        }
        return max + 1;
    }

    private Color getColor(String genotype) {
        return switch (genotype) {
            case "AA" -> Color.BLUE;
            case "AS" -> new Color(128, 0, 128);
            case "SS" -> Color.RED;
            default -> Color.GRAY;
        };
    }
    private void drawIndividual(Graphics g, Individual ind, int x, int y) {
        minX = Math.min(minX, x);
        maxX = Math.max(maxX, x);
        minY = Math.min(minY, y);
        maxY = Math.max(maxY, y);

        if (ind == null) return;

        // Draw current node
        drawNode(g, ind, x, y);

        // Base case: no children
        if (ind.getChildren().isEmpty()) return;

        // Layout children proportionally based on subtree size
        int totalWidth = 0;
        Map<Individual, Integer> subtreeWidths = new HashMap<>();
        for (Individual child : ind.getChildren()) {
            int width = child.getSubtreeSize();
            subtreeWidths.put(child, width);
            totalWidth += width;
        }

        int childX = x - totalWidth * 20 / 2; // center the group of children

        for (Individual child : ind.getChildren()) {
            int width = subtreeWidths.get(child);
            int centerX = childX + (width * 25 / 2);

            // draw edge
            g.drawLine(x, y, centerX, y + VERTICAL_SPACING);

            // recurse
            drawIndividual(g, child, centerX, y + VERTICAL_SPACING);

            childX += width * 20;
        }
    }

    private void drawNode(Graphics g, Individual ind, int x, int y) {
        String genotype = ind.getGenotype();

        // Choose color based on genotype
        switch (genotype) {
            case "AA":
                g.setColor(Color.BLUE);
                break;
            case "AS":
                g.setColor(new Color(128, 0, 128)); // purple
                break;
            case "SS":
                g.setColor(Color.RED);
                break;
            default:
                g.setColor(Color.GRAY);
                break;
        }

        // Draw the circle
        g.fillOval(x - 10, y - 10, 20, 20);

        // Draw label (genotype)
        g.setColor(Color.BLACK);
        g.drawString(genotype, x - 12, y - 15);
    }




}
