# Q-Learning Implementation

This is a Java application that implements the Q-Learning algorithm for sequential decision making and Multi-Agent Systems. It consists of two main implementations: the Sequential Q-Learning and the Multi-Agent Q-Learning.

## Sequential Q-Learning

The Sequential Q-Learning implementation is located in the `seq` package. It provides a framework for training an agent to make optimal decisions in a sequential environment. The implementation includes the following classes:

### QLearning Class

The `QLearning` class represents the core Q-Learning algorithm for sequential decision making. It contains the following methods:

- `resetState()`: Resets the current state of the agent to the initial state.
- `chooseAction(epsilon)`: Selects the next action for the agent based on the epsilon-greedy policy, balancing exploration and exploitation.
- `executeAction(action)`: Executes the chosen action and updates the agent's state accordingly.
- `finished()`: Checks if the agent has reached the terminal state.
- `showResult()`: Displays the learned Q-Table and the path taken by the agent.
- `runQLearning(numIterations)`: Runs the Q-Learning algorithm for a specified number of iterations and updates the Q-Table.
- `getQTable()`: Returns the final Q-Table learned by the agent.

## Multi-Agent Q-Learning

The Multi-Agent Q-Learning implementation is located in the `sma` package. It extends the sequential Q-Learning approach to handle multiple agents in a cooperative or competitive environment. The implementation includes the following classes:

### MasterAgent Class

The `MasterAgent` class acts as the main controller for the Q-Learning agents in a Multi-Agent System. It performs the following tasks:

- Creates multiple instances of the `QLearningAgent` class.
- Waits for all agents to finish learning and prints the best Q-Table.
- Receives Q-Tables from the agents and selects the best one.
- Registers the `MasterAgent` as a Q-Learning service provider.

### QLearningAgent Class

The `QLearningAgent` class represents an individual Q-Learning agent in a Multi-Agent System. It contains similar methods to the `QLearning` class in the Sequential Q-Learning implementation.

### MultiAgentContainer Class

The `MultiAgentContainer` class is responsible for setting up the Multi-Agent Container and launching the agents.

## How to Run the App

To run the application, follow these steps:

1. Ensure you have Java Development Kit (JDK) installed on your machine.
2. Compile the source code using the following command: `javac -cp jade.jar seq/QLearning.java sma/MasterAgent.java sma/QLearningAgent.java sma/MultiAgentContainer.java`.
3. Start the Multi-Agent Container using the following command: `java -cp jade.jar jade.Boot -gui -agents MasterAgent:sma.MasterAgent`.
4. The Q-Learning agents will start learning and send their Q-Tables to the MasterAgent.
5. Once all agents have finished learning, the MasterAgent will select the best Q-Table and display it.

## Dependencies

This application requires the Jade library (jade.jar) to run the Multi-Agent Container.
