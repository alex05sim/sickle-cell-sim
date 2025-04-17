package sicklecellsimulation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

class SimulationPanel extends JPanel {
    private int populationSize;
    private Individual[] individuals;
    private final Random random = new Random();
    private Timer timer;
    private int sickleCellCount, carrierCount, healthyCount, generationCount;
    private SimulationFrame frame;
    private boolean paused = false;
    private boolean allowGrowth;
    private boolean healthcareAvailable = true;
    private boolean malariaRegion;




    private double reproductionRate, deathRate, mutationRate;
    private List<Generation> generations = new ArrayList<>();

    public SimulationPanel() {
        setBackground(Color.WHITE);
        timer = new Timer(100, e -> {
            if (!paused) {
                updateSimulation();
                repaint();
            }
        });
    }
    // Based on carrier rates ~8-10% in U.S. Black population
// and SS ~0.2–1%
    private String getRandomGenotype(int sickleStartPercent) {
        int roll = random.nextInt(1000);
        if (roll < 10) return "SS";         // 1%
        else if (roll < 100) return "AS";   // 9%
        else return "AA";                   // 90%
    }

    private void updateCounts(Individual ind) {
        if (ind.isHealthy()) {
            healthyCount++;
        } else if (ind.isCarrier()) {
            carrierCount++;
        } else {
            sickleCellCount++;
        }
    }
    private void removeCounts(Individual ind) {
        if (ind.isHealthy()) {
            healthyCount--;
        } else if (ind.isCarrier()) {
            carrierCount--;
        } else {
            sickleCellCount--;
        }
    }

    public void setFrameReference(SimulationFrame frame) {
        this.frame = frame;
    }

    public void startSimulation(int populationSize, int sickleStartPercent, boolean allowGrowth,
                                double reproductionRate, double deathRate, double mutationRate, boolean healthcare, boolean malariaRegion) {
        this.populationSize = populationSize;
        this.allowGrowth = allowGrowth;
        this.reproductionRate = reproductionRate;
        this.deathRate = deathRate;
        this.mutationRate = mutationRate;
        this.healthcareAvailable = healthcare;
        this.malariaRegion = malariaRegion;
        individuals = new Individual[populationSize];
        sickleCellCount = 0;
        carrierCount = 0;
        healthyCount = 0;
        generationCount = 0;

        for (int i = 0; i < populationSize; i++) {
            String genotype = getRandomGenotype(sickleStartPercent);
            individuals[i] = new Individual(random.nextInt(getWidth()), random.nextInt(getHeight()), genotype);
            updateCounts(individuals[i]);
        }

        timer.start();
        repaint();
        if (frame != null) {
            frame.updateStats(generationCount, healthyCount, carrierCount, sickleCellCount);
        }
    }

    private void updateSimulation() {
        generationCount++;


        for (Individual individual : individuals) {
            individual.moveSmoothly(getWidth(), getHeight(), individuals);
            individual.incrementAge();
        }

        // Mutation Logic with Animation
        for (Individual individual : individuals) {
            if (Math.random() < mutationRate) {
                if (individual.isHealthy()) {
                    individual.becomeCarrier();
                    carrierCount++;
                    healthyCount--;
                    individual.animateMutation(); // Mutation effect
                } else if (individual.isCarrier()) {
                    individual.becomeSickleCell();
                    carrierCount--;
                    sickleCellCount++;
                    individual.animateMutation(); // Mutation effect
                }
            }
        }

        // Reproduction with Animation
        if (allowGrowth) {
            int maxBirthsPerTick = 5; // adjustable cap for realism
            int births = Math.min((int)(populationSize * reproductionRate), maxBirthsPerTick);

            for (int i = 0; i < births; i++) {
                Individual newInd = addNewIndividual();
                if (newInd != null) newInd.animateGrowth();
            }
        }


        int deathsThisGen = 0;
        int deathLimit = (int)(populationSize * 0.1); // Max 10% per gen
        for (int i = 0; i < individuals.length; i++) {
            if (deathsThisGen >= deathLimit) break;

            Individual ind = individuals[i];
            if (ind == null) continue;

            String g = ind.getGenotype();
            int age = ind.getAge();
            double adjustedDeathRate = deathRate;

            // Age-based mortality model:
            if (g.equals("SS")) {
                if (age < 5) {
                    adjustedDeathRate *= healthcareAvailable ? 0.05 : 0.9; // SS infants
                } else {
                    adjustedDeathRate *= healthcareAvailable ? 0.1 : 0.5;
                }
            } else if (g.equals("AS")) {
                adjustedDeathRate *= 0.6;
            } else if (g.equals("AA")) {
                adjustedDeathRate *= 0.2;
            }
            // Optional: natural death for elderly
            if (age > 30) {
                adjustedDeathRate += 0.05;
            }
            if (Math.random() < adjustedDeathRate) {
                ind.animateDeath();
                removeRandomIndividual(i);
                deathsThisGen++;
                break;
            }
        }

        repaint();
        if (frame != null) {
            frame.updateStats(generationCount, healthyCount, carrierCount, sickleCellCount);
        }
    }
    public Individual getRandomIndividual() {
        if (individuals == null || individuals.length == 0) return null;
        return individuals[random.nextInt(individuals.length)];
    }

    private Individual addNewIndividual() {

        // Select two random parents
        Individual parent1 = individuals[random.nextInt(populationSize)];
        Individual parent2 = individuals[random.nextInt(populationSize)];

        // Create offspring near parents
        int birthX = (int) ((parent1.getX() + parent2.getX()) / 2 + random.nextInt(20) - 10);
        int birthY = (int) ((parent1.getY() + parent2.getY()) / 2 + random.nextInt(20) - 10);

        Individual newInd = new Individual(birthX, birthY, parent1, parent2);
        parent1.addChild(newInd);
        parent2.addChild(newInd);

        // Resize array to accommodate new individual
        Individual[] newPop = new Individual[populationSize + 1];
        System.arraycopy(individuals, 0, newPop, 0, populationSize);
        newPop[populationSize] = newInd;
        individuals = newPop;

        populationSize++;
        updateCounts(newInd);

        return newInd;
    }

    private void removeRandomIndividual(int indexToRemove) {
        if (populationSize <= 10) return; // Prevent total wipeout
        removeCounts(individuals[indexToRemove]);

        // Move last individual to the removed slot to avoid shifting the entire array
        individuals[indexToRemove] = individuals[populationSize - 1];
        Individual[] newPop = new Individual[populationSize - 1];
        System.arraycopy(individuals, 0, newPop, 0, populationSize - 1);
        individuals = newPop;
        populationSize--;
    }
    public void resetSimulation() {
        timer.stop();
        if (frame != null) {
            startSimulation(
                    populationSize,
                    30,
                    allowGrowth,
                    reproductionRate,
                    deathRate,
                    mutationRate,
                    frame.healthcareToggle.isSelected(),
                    malariaRegion
            );
        } else {
            startSimulation(populationSize, 30, allowGrowth, reproductionRate, deathRate, mutationRate, healthcareAvailable, malariaRegion);
        }
        paused = false;
    }


    public void togglePause() {
        paused = !paused;
    }

    public Individual getRootAncestor() {
        if (individuals == null || individuals.length == 0) return null;

        Individual best = individuals[0];
        while (best.getParent1() != null) {
            best = best.getParent1(); // Go up as far as possible
        }
        System.out.println("Root genotype: " + best.getGenotype());
        System.out.println("Root children: " + best.getChildren().size());
        return best;
    }

    public void setSpeed(int speed) {
        timer.setDelay(Math.max(speed, 1)); //
    }
    public boolean isPaused() {
        return paused;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.drawString("Genotype Distribution:", 10, 100);
        g.setColor(Color.BLUE);
        g.drawString("AA: " + healthyCount, 10, 120);
        g.setColor(new Color(128, 0, 128));
        g.drawString("AS: " + carrierCount, 10, 140);
        g.setColor(Color.RED);
        g.drawString("SS: " + sickleCellCount, 10, 160);


        if (individuals != null) {
            for (Individual individual : individuals) {
                individual.draw(g);
            }
        }
        if (malariaRegion) {
            g.setColor(new Color(0, 100, 0, 180)); // green banner
            g.fillRect(10, 10, 220, 30);
            g.setColor(Color.WHITE);
            g.drawString("MALARIA REGION ACTIVE", 20, 30);
        }

        if (!healthcareAvailable) {
            g.setColor(new Color(150, 0, 0, 180)); // red banner
            g.fillRect(10, 50, 220, 30);
            g.setColor(Color.WHITE);
            g.drawString("NO HEALTHCARE ACCESS", 20, 70);
        }

        // Bar graph legend
        g.setColor(Color.BLACK);
        g.drawString("Genotype Frequency (This Gen)", 10, getHeight() - 70);

        int barWidth = 40;
        int baseY = getHeight() - 50;

        int total = healthyCount + carrierCount + sickleCellCount;
        if (total == 0) total = 1;

        int aaHeight = (int)((healthyCount * 100.0 / total) * 2);
        int asHeight = (int)((carrierCount * 100.0 / total) * 2);
        int ssHeight = (int)((sickleCellCount * 100.0 / total) * 2);

        g.setColor(Color.BLUE);
        g.fillRect(10, baseY - aaHeight, barWidth, aaHeight);
        g.drawString("AA", 15, baseY + 15);

        g.setColor(new Color(128, 0, 128));
        g.fillRect(60, baseY - asHeight, barWidth, asHeight);
        g.drawString("AS", 65, baseY + 15);

        g.setColor(Color.RED);
        g.fillRect(110, baseY - ssHeight, barWidth, ssHeight);
        g.drawString("SS", 115, baseY + 15);

    }
}
