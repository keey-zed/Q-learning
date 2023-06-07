package sma;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MasterAgent extends Agent {
    private static final int NUM_AGENTS = 5;
    private List<double[][]> qTableList = new ArrayList<>();

    @Override
    protected void setup() {
        // Create QLearningAgent instances
        for (int i = 0; i < NUM_AGENTS; i++) {
            Object[] args = new Object[]{i};
            String agentName = "QLearningAgent" + i;
            addBehaviour(new OneShotBehaviour() {
                @Override
                public void action() {
                    try {
                        getContainerController().createNewAgent(agentName, QLearningAgent.class.getName(), args).start();
                    } catch (ControllerException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            // Wait for all agents to finish
            doWait(NUM_AGENTS * 100);
            // Print best Q-Table
            printBestQTable();
        }
        // Receive Q-tables from QLearningAgent instances
        addBehaviour(new CyclicBehaviour() {
            private int receivedCount = 0;

            @Override
            public void action() {
                ACLMessage received = receive();
                if (received != null) {
                    String[] contentParts = received.getContent().split(";");
                    int agentIndex = Integer.parseInt(contentParts[0]);
                    double[][] qTable = deserializeQTable(contentParts[1]);
                    qTableList.add(qTable);
                    receivedCount++;
                    // Check if all Q-tables have been received
                    if (receivedCount == NUM_AGENTS) {
                        printBestQTable();
                        doDelete();
                    }
                } else {
                    block();
                }
            }
        });
        // Register the MasterAgent as a QLearning service provider
        registerService();
    }

    private void registerService() {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("QLearning");
        serviceDescription.setName("QLearningAgent");
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, dfAgentDescription);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
    }

    private double[][] deserializeQTable(String qTableString) {
        String[] rows = qTableString.substring(1, qTableString.length() - 1).split("\\],");
        double[][] qTable = new double[rows.length][4];
        for (int i = 0; i < rows.length; i++) {
            String[] elements = rows[i].split(", ");
            for (int j = 0; j < elements.length; j++) {
                qTable[i][j] = Double.parseDouble(elements[j]);
            }
        }
        return qTable;
    }

    private void printBestQTable() {
        double[][] bestQTable = qTableList.stream()
                .max(Comparator.comparingDouble(this::calculateQTableScore))
                .orElseThrow(IllegalStateException::new);
        System.out.println("************ Best Q-Table ************");
        for (int i = 0; i < bestQTable.length; i++) {
            System.out.print("State " + i + ": [ ");
            for (int j = 0; j < bestQTable[i].length; j++) {
                System.out.printf("%.2f, ", bestQTable[i][j]);
            }
            System.out.println("]");
        }
    }

    private double calculateQTableScore(double[][] qTable) {
        double totalScore = 0;
        for (double[] row : qTable) {
            for (double value : row) {
                totalScore += value;
            }
        }
        return totalScore;
    }
}