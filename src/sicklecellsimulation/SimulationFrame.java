package sicklecellsimulation;

import javax.swing.*;
import java.awt.*;


public class SimulationFrame extends JFrame {
    private final SimulationPanel simulationPanel;
    private final JTextField populationSizeField, sickleCellStartField, reproductionRateField, deathRateField, mutationRateField;
    private final JButton startButton, pauseButton, resetButton, showGraphButton;
    private final JSlider speedSlider;
    private final JCheckBox growthToggle;
    private final JLabel populationLabel, healthyLabel, carrierLabel, sickleLabel, generationLabel;
    public GraphWindow graphWindow; // ✅ Separate graph window

    public SimulationFrame() {
        setTitle("Sickle Cell Spread Simulation");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ✅ Simulation Panel (Main Display)
        simulationPanel = new SimulationPanel();
        simulationPanel.setFrameReference(this);
        add(simulationPanel, BorderLayout.CENTER);

        // ✅ Control Panel (Right Side)
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(12, 1, 5, 5));
        controlPanel.setPreferredSize(new Dimension(250, 700));

        controlPanel.add(new JLabel("Population Size:"));
        populationSizeField = new JTextField("100", 5);
        controlPanel.add(populationSizeField);

        controlPanel.add(new JLabel("Sickle Cell %:"));
        sickleCellStartField = new JTextField("30", 3);
        controlPanel.add(sickleCellStartField);

        controlPanel.add(new JLabel("Reproduction Rate (%):"));
        reproductionRateField = new JTextField("2", 3);
        controlPanel.add(reproductionRateField);

        controlPanel.add(new JLabel("Death Rate (%):"));
        deathRateField = new JTextField("1", 3);
        controlPanel.add(deathRateField);

        controlPanel.add(new JLabel("Mutation Rate (%):"));
        mutationRateField = new JTextField("0.5", 3);
        controlPanel.add(mutationRateField);

        growthToggle = new JCheckBox("Enable Population Growth");
        controlPanel.add(growthToggle);

        startButton = new JButton("Start");
        controlPanel.add(startButton);

        pauseButton = new JButton("Pause");
        controlPanel.add(pauseButton);

        resetButton = new JButton("Reset");
        controlPanel.add(resetButton);

        showGraphButton = new JButton("Show Graph");
        controlPanel.add(showGraphButton);

        add(controlPanel, BorderLayout.EAST);

        // ✅ Stats Panel (Top)
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(1, 5));

        populationLabel = new JLabel("Total Population: 0");
        healthyLabel = new JLabel("Healthy: 0");
        carrierLabel = new JLabel("Carriers: 0");
        sickleLabel = new JLabel("Affected (Sickle Cell): 0");
        generationLabel = new JLabel("Generations: 0");

        statsPanel.add(populationLabel);
        statsPanel.add(healthyLabel);
        statsPanel.add(carrierLabel);
        statsPanel.add(sickleLabel);
        statsPanel.add(generationLabel);
        add(statsPanel, BorderLayout.NORTH);

        // Speed Slider (Bottom)
        JPanel speedPanel = new JPanel();
        speedPanel.add(new JLabel("Speed:"));

        speedSlider = new JSlider(10, 500, 100);
        speedSlider.addChangeListener(e -> simulationPanel.setSpeed(510 - speedSlider.getValue()));

        speedPanel.add(speedSlider);
        add(speedPanel, BorderLayout.SOUTH);

        setVisible(true);

        // Button Actions
        startButton.addActionListener(e -> {
            int populationSize = Integer.parseInt(populationSizeField.getText());
            int sickleStartPercent = Integer.parseInt(sickleCellStartField.getText());
            double reproductionRate = Double.parseDouble(reproductionRateField.getText()) / 100;
            double deathRate = Double.parseDouble(deathRateField.getText()) / 100;
            double mutationRate = Double.parseDouble(mutationRateField.getText()) / 100;
            boolean allowGrowth = growthToggle.isSelected();
            simulationPanel.startSimulation(populationSize, sickleStartPercent, allowGrowth, reproductionRate, deathRate, mutationRate);
        });

        pauseButton.addActionListener(e -> {
            simulationPanel.togglePause();
            pauseButton.setText(simulationPanel.isPaused() ? "Resume" : "Pause");
        });

        resetButton.addActionListener(e -> simulationPanel.resetSimulation());

        showGraphButton.addActionListener(e -> {
            if (graphWindow == null) {
                graphWindow = new GraphWindow();
            } else {
                graphWindow.setVisible(true);
            }
        });
    }

    public void updateStats(int generation, int healthy, int carriers, int sickle) {
        populationLabel.setText("Total Population: " + (healthy + carriers + sickle));
        healthyLabel.setText("Healthy: " + healthy);
        carrierLabel.setText("Carriers: " + carriers);
        sickleLabel.setText("Affected (Sickle Cell): " + sickle);
        generationLabel.setText("Generations: " + generation);

        if (graphWindow != null) {
            graphWindow.updateGraph(generation, healthy, carriers, sickle);
        }
    }
}
