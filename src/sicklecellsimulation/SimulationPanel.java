package sicklecellsimulation;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

class SimulationPanel extends JPanel {
    private int populationSize;
    private Individual[] individuals;
    private final Random random = new Random();
    private Timer timer;
    private int sickleCellCount, carrierCount, healthyCount, generationCount;
    private SimulationFrame frame;
    private boolean paused = false;
    private boolean allowGrowth;
    private double reproductionRate, deathRate, mutationRate;

    public SimulationPanel() {
        setBackground(Color.WHITE);
        timer = new Timer(100, e -> {
            if (!paused) {
                updateSimulation();
                repaint();
            }
        });
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

    public void startSimulation(int populationSize, int sickleStartPercent, boolean allowGrowth, double reproductionRate, double deathRate, double mutationRate) {
        this.populationSize = populationSize;
        this.allowGrowth = allowGrowth;
        this.reproductionRate = reproductionRate;
        this.deathRate = deathRate;
        this.mutationRate = mutationRate;
        individuals = new Individual[populationSize];
        sickleCellCount = 0;
        carrierCount = 0;
        healthyCount = 0;
        generationCount = 0;

        for (int i = 0; i < populationSize; i++) {
            boolean hasSickleCell = random.nextInt(100) < sickleStartPercent;
            individuals[i] = new Individual(random.nextInt(getWidth()), random.nextInt(getHeight()), hasSickleCell);
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
        }

        // Mutation Logic with Animation
        for (Individual individual : individuals) {
            if (Math.random() < mutationRate) {
                if (individual.isHealthy()) {
                    individual.becomeCarrier();
                    carrierCount++;
                    healthyCount--;
                    individual.animateMutation(); // 🔥 Mutation effect
                } else if (individual.isCarrier()) {
                    individual.becomeSickleCell();
                    carrierCount--;
                    sickleCellCount++;
                    individual.animateMutation(); // 🔥 Mutation effect
                }
            }
        }

        // Reproduction with Animation
        if (allowGrowth && Math.random() < reproductionRate) {
            Individual newInd = addNewIndividual();
            if (newInd != null) newInd.animateGrowth(); // 🔥 Reproduction effect
        }

        // Death with Animation
        if (Math.random() < deathRate && populationSize > 10) {
            int indexToRemove = random.nextInt(populationSize);
            individuals[indexToRemove].animateDeath(); // 🔥 Death effect
            removeRandomIndividual(indexToRemove);
        }

        repaint();
        if (frame != null) {
            frame.updateStats(generationCount, healthyCount, carrierCount, sickleCellCount);
        }
    }




    private Individual addNewIndividual() {


        // Select two random parents
        Individual parent1 = individuals[random.nextInt(populationSize)];
        Individual parent2 = individuals[random.nextInt(populationSize)];

        // Create offspring near parents
        int birthX = (int) ((parent1.getX() + parent2.getX()) / 2 + random.nextInt(20) - 10);
        int birthY = (int) ((parent1.getY() + parent2.getY()) / 2 + random.nextInt(20) - 10);

        Individual newInd = new Individual(birthX, birthY, parent1, parent2);

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
        timer.stop();  //
        startSimulation(populationSize, 30, allowGrowth, reproductionRate, deathRate, mutationRate);
        paused = false;  //
    }

    public void togglePause() {
        paused = !paused;
    }

    public boolean isPaused() {
        return paused;
    }
    public void setSpeed(int speed) {
        timer.setDelay(speed);
    }
    public int getPopulationSize() {
        return populationSize;
    }

    public int getHealthyCount() {
        return healthyCount;
    }

    public int getCarrierCount() {
        return carrierCount;
    }

    public int getSickleCellCount() {
        return sickleCellCount;
    }

    public int getGenerationCount() {
        return generationCount;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (individuals != null) {
            for (Individual individual : individuals) {
                individual.draw(g);
            }
        }
    }
}
