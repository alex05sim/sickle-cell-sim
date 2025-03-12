package sicklecellsimulation;

import java.awt.*;
import java.util.Random;

class Individual {
    private double x, y;
    private double velocityX, velocityY;
    private Color color;
    private final Random random = new Random();

    // Constructor for initial population
    public Individual(int x, int y, boolean hasSickleCell) {
        this.x = x;
        this.y = y;
        this.velocityX = random.nextInt(3) - 1;
        this.velocityY = random.nextInt(3) - 1;

        if (hasSickleCell) {
            this.color = Color.RED;
        } else {
            this.color = Math.random() < 0.5 ? Color.BLUE : new Color(128, 0, 128);
        }
    }

    // ✅ New Constructor for Genetic Inheritance
    public Individual(int x, int y, Individual parent1, Individual parent2) {
        this.x = x;
        this.y = y;
        this.velocityX = random.nextInt(3) - 1;
        this.velocityY = random.nextInt(3) - 1;

        boolean parent1Sickle = parent1.isSickleCell();
        boolean parent2Sickle = parent2.isSickleCell();
        boolean parent1Carrier = parent1.isCarrier();
        boolean parent2Carrier = parent2.isCarrier();

        if (parent1Sickle && parent2Sickle) {
            this.color = Color.RED; // Both parents have sickle cell = child must have it
        } else if ((parent1Sickle && parent2Carrier) || (parent2Sickle && parent1Carrier)) {
            this.color = Color.RED; // One sickle parent + one carrier = sickle cell
        } else if (parent1Carrier && parent2Carrier) {
            this.color = Math.random() < 0.25 ? Color.RED : new Color(128, 0, 128); // 25% chance sickle, 50% carrier
        } else if (parent1Sickle || parent2Sickle || parent1Carrier || parent2Carrier) {
            this.color = new Color(128, 0, 128); // If any parent is sickle or carrier, 50% chance of carrier
        } else {
            this.color = Color.BLUE; // Both parents healthy = healthy child
        }
    }

    public void moveSmoothly(int maxWidth, int maxHeight, Individual[] others) {
        x += velocityX;
        y += velocityY;

        // Wall collision
        if (x < 0 || x > maxWidth - 10) velocityX *= -1;
        if (y < 0 || y > maxHeight - 10) velocityY *= -1;

        // Collision with other individuals
        for (Individual other : others) {
            if (other == this) continue; // Skip self-check

            double dx = x - other.getX();
            double dy = y - other.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < 12) { // If overlapping
                if (distance == 0) distance = 0.1; // Prevent division by zero

                // Normalize collision axis
                double nx = dx / distance;
                double ny = dy / distance;

                // Relative velocity
                double dvx = velocityX - other.velocityX;
                double dvy = velocityY - other.velocityY;

                // Velocity along normal (dot product)
                double velocityAlongNormal = dvx * nx + dvy * ny;

                // Only resolve if moving towards each other
                if (velocityAlongNormal > 0) continue;

                // Elastic collision response
                double restitution = 0.9; // Bounciness factor
                double impulse = -(1 + restitution) * velocityAlongNormal;
                impulse /= 2; // Distribute equally between both individuals

                velocityX += impulse * nx;
                velocityY += impulse * ny;
                other.velocityX -= impulse * nx;
                other.velocityY -= impulse * ny;

                // Small push apart to prevent sticking
                double pushFactor = 0.5;
                x += nx * pushFactor;
                y += ny * pushFactor;
                other.x -= nx * pushFactor;
                other.y -= ny * pushFactor;
            }
        }

        // Apply movement
        x = Math.max(0, Math.min(x, maxWidth - 10));
        y = Math.max(0, Math.min(y, maxHeight - 10));
    }




    public boolean isHealthy() { return color.equals(Color.BLUE); }
    public boolean isCarrier() { return color.equals(new Color(128, 0, 128)); }
    public boolean isSickleCell() { return color.equals(Color.RED); }
    public void becomeCarrier() { this.color = new Color(128, 0, 128); }
    public void becomeSickleCell() { this.color = Color.RED; }

    public double getX() { return x; }
    public double getY() { return y; }

    private int animationSize = 10;
    private int fadeAlpha = 255;
    private boolean fadingOut = false;

    public void draw(Graphics g) {
        if (fadingOut) {
            fadeAlpha -= 15;
            if (fadeAlpha <= 0) fadeAlpha = 0;
        }

        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), fadeAlpha));
        g.fillOval((int) x, (int) y, animationSize, animationSize);

        // Reset animation after one frame
        animationSize = 10;
    }

    public void animateGrowth() {
        animationSize = 15; // Briefly grows when reproducing
    }

    public void animateDeath() {
        fadingOut = true; // Slowly fades away
    }

    public void animateMutation() {
        animationSize = 12; // Slightly grows when mutation occurs
    }


}
