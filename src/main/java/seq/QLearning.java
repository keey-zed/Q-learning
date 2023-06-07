package seq;

import java.util.Random;

public class QLearning {
    private final double ALPHA = 0.1;
    private final double GAMMA = 0.9;
    private final double EPS = 0.4;
    private final int MAX_EPOCH = 20000;
    private final int GRID_SIZE = 6;
    private final int ACTION_SIZE = 4;
    private int[][] grid = new int[GRID_SIZE][GRID_SIZE];
    private double[][] qTable = new double[GRID_SIZE * GRID_SIZE][ACTION_SIZE];
    private int[][] actions;
    private int stateI;
    private int stateJ;

    public QLearning() {
        actions = new int[][] {
                {0, -1},  // Left
                {0, 1},   // Right
                {1, 0},   // Down
                {-1, 0}   // Up
        };
        grid = new int[][] {
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, -1, 0},
                {0, 0, 0, 0, 0, 0},
                {-1, -1, -1, -1, -1, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1}
        };
    }

    private void resetState() {
        stateI = 2;
        stateJ = 0;
    }

    private int chooseAction(double eps) {
        Random random = new Random();
        double bestQ = 0;
        int action = 0;
        if (random.nextDouble() < eps) {
            // Exploration
            action = random.nextInt(ACTION_SIZE);
        } else {
            // Exploitation
            int state = stateI * GRID_SIZE + stateJ;
            for (int i = 0; i < ACTION_SIZE; i++) {
                if (qTable[state][i] > bestQ) {
                    bestQ = qTable[state][i];
                    action = i;
                }
            }
        }
        return action;
    }

    private int executeAction(int action) {
        stateI = Math.max(0, Math.min(actions[action][0] + stateI, GRID_SIZE - 1));
        stateJ = Math.max(0, Math.min(actions[action][1] + stateJ, GRID_SIZE - 1));
        return stateI * GRID_SIZE + stateJ;
    }

    private boolean finished() {
        return grid[stateI][stateJ] == 1;
    }

    private void showResult() {
        System.out.println("************ Q-Table ************");
        for (int i = 0; i < qTable.length; i++) {
            System.out.print("State " + i + ": [ ");
            for (int j = 0; j < qTable[i].length; j++) {
                System.out.printf("%.2f, ", qTable[i][j]);
            }
            System.out.println("]");
        }
        resetState();
        System.out.println("************ Path ************");
        while (!finished()) {
            int action = chooseAction(0);
            System.out.println("Current State: (" + stateI + ", " + stateJ + ")");
            System.out.println("Action: " + action);
            executeAction(action);
        }
        System.out.println("Final State: (" + stateI + ", " + stateJ + ")");
    }

    public void runQlearning() {
        int iteration = 0;
        int currentState;
        int nextState;
        int action, action_;
        while (iteration < MAX_EPOCH) {
            resetState();
            while (!finished()) {
                currentState = stateI * GRID_SIZE + stateJ;
                action = chooseAction(0.4);
                nextState = executeAction(action);
                action_ = chooseAction(0);
                qTable[currentState][action] = qTable[currentState][action] + ALPHA * (grid[stateI][stateJ] + GAMMA * qTable[nextState][action_] - qTable[currentState][action]);
            }
            iteration++;
        }
        showResult();
    }

    public double[][] getQTable() {
        return qTable;
    }
}