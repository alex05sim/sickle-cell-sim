package sicklecellsimulation;

import javax.swing.*;
import java.awt.*;


public class SimulationFrame extends JFrame {
    private final SimulationPanel simulationPanel;
    private final JTextField populationSizeField, sickleCellStartField, reproductionRateField, deathRateField, mutationRateField;
    private final JButton startButton, pauseButton, resetButton, showGraphButton, treeButton;
    private final JSlider speedSlider;
    private final JCheckBox growthToggle;
    private final JLabel populationLabel, healthyLabel, carrierLabel, sickleLabel, generationLabel;
    public GraphWindow graphWindow; //
    public final JCheckBox healthcareToggle;
    private final JCheckBox malariaToggle;


    public SimulationFrame() {
        setTitle("Simulation of Human Sickle Cell Variation Across Generations");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());






        // Simulation Panel
        simulationPanel = new SimulationPanel();
        simulationPanel.setFrameReference(this);


        // Control Panel (Right Side)
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setPreferredSize(new Dimension(250, 700));
        //UI

        String[] regions = { "West Africa", "United States", "Europe" };
        JComboBox<String> regionSelector = new JComboBox<>(regions);
        controlPanel.add(new JLabel("Region:"));
        controlPanel.add(regionSelector);


        controlPanel.add(new JLabel("Population Size:"));
        populationSizeField = new JTextField("100", 5);
        controlPanel.add(populationSizeField);

        controlPanel.add(new JLabel("\"Sickle Cell % (Initial Human Population):"));
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


        healthcareToggle = new JCheckBox("Healthcare Available");
        healthcareToggle.setSelected(true); // default to ON
        controlPanel.add(healthcareToggle);

        malariaToggle = new JCheckBox("Malaria Region");
        malariaToggle.setSelected(true);
        controlPanel.add(malariaToggle);


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
        treeButton = new JButton("Show Family Tree");
        controlPanel.add(treeButton);

        int buttonWidth = 200; // or 250 to match sidebar
        Dimension buttonSize = new Dimension(buttonWidth, 35);

        startButton.setMaximumSize(buttonSize);
        pauseButton.setMaximumSize(buttonSize);
        resetButton.setMaximumSize(buttonSize);
        showGraphButton.setMaximumSize(buttonSize);
        treeButton.setMaximumSize(buttonSize);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        growthToggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        showGraphButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        treeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        healthcareToggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        malariaToggle.setAlignmentX(Component.CENTER_ALIGNMENT);




        controlPanel.add(Box.createVerticalStrut(10));  // optional spacing
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setBorder(BorderFactory.createTitledBorder("Genotype Legend"));
        legendPanel.setBackground(Color.WHITE);
        legendPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // anchor to left

        legendPanel.add(createLegendItem("Does not carry sickle cell (AA)", Color.BLUE));
        legendPanel.add(createLegendItem("Carrier for sickle cell (AS)", new Color(128, 0, 128)));
        legendPanel.add(createLegendItem("Has sickle cell anemia (SS)", Color.RED));

        // Wrap the legend
        JPanel legendWrapper = new JPanel(new BorderLayout());
        legendWrapper.setMaximumSize(new Dimension(250, Integer.MAX_VALUE));
        legendWrapper.setPreferredSize(new Dimension(250, 160));  // optional boost
        legendWrapper.setBackground(Color.WHITE);
        legendWrapper.add(legendPanel, BorderLayout.CENTER);

        controlPanel.add(Box.createVerticalGlue()); // Push legend down
        controlPanel.add(legendWrapper);


        // Stats Panel (top)
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


        // Speed Slider (Bottom)
        JPanel speedPanel = new JPanel();
        speedPanel.add(new JLabel("Speed:"));

        speedSlider = new JSlider(1, 1000, 100);
        speedSlider.addChangeListener(e -> simulationPanel.setSpeed(510 - speedSlider.getValue()));

        speedPanel.add(speedSlider);

        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel simulationTab = new JPanel(new BorderLayout());
        JScrollPane scrollPanel = new JScrollPane(simulationPanel);
        simulationTab.add(scrollPanel, BorderLayout.CENTER);
        simulationTab.add(controlPanel, BorderLayout.EAST);
        simulationTab.add(statsPanel, BorderLayout.NORTH);

        simulationTab.add(speedPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Simulation", simulationTab);

        JTextArea learnText = new JTextArea();
        learnText.setEditable(false);
        learnText.setLineWrap(true);
        learnText.setWrapStyleWord(true);
        learnText.setText(
                "🔬 Human Sickle Cell Simulation\n\n" +
                        "This simulation models the inheritance of sickle cell alleles (AA, AS, SS) in human populations.\n" +
                        "It shows how traits vary across generations based on real-world factors like:\n" +
                        "- Healthcare availability\n" +
                        "- Malaria pressure (regional selection)\n" +
                        "- Mutation\n\n" +
                        "📚 Sources:\n" +
                        "- CDC: https://www.cdc.gov/ncbddd/sicklecell/data.html\n" +
                        "- WHO: https://www.who.int/news-room/fact-sheets/detail/sickle-cell-disease\n" +
                        "- Tishkoff et al., 2001: Global Patterns of Variation in Sickle Cell Trait\n"
        );
        JScrollPane learnScroll = new JScrollPane(learnText);
        tabbedPane.addTab("Learn", learnScroll);
        add(tabbedPane, BorderLayout.CENTER);





        setVisible(true);

        // Button Actions

        regionSelector.addActionListener(e -> {
            String selectedRegion = (String) regionSelector.getSelectedItem();

            switch (selectedRegion) {
                case "West Africa":
                    sickleCellStartField.setText("10");
                    mutationRateField.setText("0.1");
                    healthcareToggle.setSelected(false);
                    malariaToggle.setSelected(true);
                    break;
                case "United States":
                    sickleCellStartField.setText("1");
                    mutationRateField.setText("0.05");
                    healthcareToggle.setSelected(true);
                    malariaToggle.setSelected(false);
                    break;
                case "Europe":
                    sickleCellStartField.setText("0");
                    mutationRateField.setText("0.01");
                    healthcareToggle.setSelected(true);
                    malariaToggle.setSelected(false);
                    break;
            }
        });

        startButton.addActionListener(e -> {

            String selectedRegion = (String) regionSelector.getSelectedItem();

            switch (selectedRegion) {
                case "West Africa":
                    sickleCellStartField.setText("10");
                    mutationRateField.setText("0.1");
                    healthcareToggle.setSelected(false);
                    malariaToggle.setSelected(true);
                    break;
                case "United States":
                    sickleCellStartField.setText("1");
                    mutationRateField.setText("0.05");
                    healthcareToggle.setSelected(true);
                    malariaToggle.setSelected(false);
                    break;
                case "Europe":
                    sickleCellStartField.setText("0");
                    mutationRateField.setText("0.01");
                    healthcareToggle.setSelected(true);
                    malariaToggle.setSelected(false);
                    break;
            }


            int populationSize = Integer.parseInt(populationSizeField.getText());
            int sickleStartPercent = Integer.parseInt(sickleCellStartField.getText());
            double reproductionRate = Double.parseDouble(reproductionRateField.getText()) / 100;
            double deathRate = Double.parseDouble(deathRateField.getText()) / 100;
            double mutationRate = Double.parseDouble(mutationRateField.getText()) / 100;
            boolean allowGrowth = growthToggle.isSelected();
            boolean healthcare = healthcareToggle.isSelected();
            boolean malaria = malariaToggle.isSelected();
            simulationPanel.startSimulation(populationSize, sickleStartPercent, allowGrowth, reproductionRate, deathRate, mutationRate, healthcare, malaria);
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
        treeButton.addActionListener(e -> {
            Individual root = simulationPanel.getRootAncestor();
            if (root == null) return;

            FamilyTreePanel panel = new FamilyTreePanel(root);
            JScrollPane scrollPane = new JScrollPane(panel);

            JFrame treeWindow = new JFrame("Family Tree Viewer");
            treeWindow.setSize(1000, 800);
            treeWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            treeWindow.add(scrollPane);
            treeWindow.setVisible(true);
        });

    }
    private JPanel createLegendItem(String description, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        item.setBackground(Color.WHITE);
        item.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel colorSwatch = new JLabel();
        colorSwatch.setPreferredSize(new Dimension(24, 24));
        colorSwatch.setOpaque(true);
        colorSwatch.setBackground(color);
        colorSwatch.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel text = new JLabel("<html><body style='width:180px'>" + description + "</body></html>");
        text.setFont(new Font("SansSerif", Font.PLAIN, 14));

        item.add(colorSwatch);
        item.add(text);
        return item;
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
