package sicklecellsimulation;

import java.awt.*;
import java.util.List;

import java.util.ArrayList;
import java.util.Random;

class Individual {
    private double x, y;
    private double velocityX, velocityY;
    private Color color;
    private final Random random = new Random();
    private final Individual parent1;
    private final Individual parent2;
    private final String genotype;
    private final List<Individual> children = new ArrayList<>();
    private int age = 0;




    public Individual(int x, int y, Individual parent1, Individual parent2) {
        this.x = x;
        this.y = y;
        this.velocityX = random.nextInt(3) - 1;
        this.velocityY = random.nextInt(3) - 1;
        this.parent1 = parent1;
        this.parent2 = parent2;

        // Inherit genotype
        String allele1 = parent1.getRandomAllele();
        String allele2 = parent2.getRandomAllele();
        String rawGenotype = allele1 + allele2;
        if (rawGenotype.equals("SA")) rawGenotype = "AS";
        this.genotype = rawGenotype;


        // Set color based on genotype
        switch (genotype) {
            case "AA" -> this.color = Color.BLUE;
            case "AS" -> this.color = new Color(128, 0, 128); // Purple
            case "SS" -> this.color = Color.RED;
        }
    }
    public String getGenotype() {
        return genotype;
    }

    public Individual getParent1() {
        return parent1;
    }

    public Individual getParent2() {
        return parent2;
    }

    // Randomly returns either "A" or "S" based on parent's genotype
    public String getRandomAllele() {
        if (genotype.equals("AA")) return "A";
        if (genotype.equals("SS")) return "S";
        return Math.random() < 0.5 ? "A" : "S";
    }
    public Individual(int x, int y, String genotype) {
        this.x = x;
        this.y = y;
        this.velocityX = random.nextInt(3) - 1;
        this.velocityY = random.nextInt(3) - 1;
        this.genotype = genotype;
        this.parent1 = null;
        this.parent2 = null;

        // Set color
        switch (genotype) {
            case "AA" -> this.color = Color.BLUE;
            case "AS" -> this.color = new Color(128, 0, 128);
            case "SS" -> this.color = Color.RED;
        }
    }
    public void addChild(Individual child) {
        children.add(child);
    }

    public List<Individual> getChildren() {
        return children;
    }

    //basic physics engine for visual
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

    public int getAge() {
        return age;
    }

    public void incrementAge() {
        age++;
    }

    public int getSubtreeSize() {
        int size = 1;
        for (Individual child : children) {
            size += child.getSubtreeSize();
        }
        return size;
    }




}
