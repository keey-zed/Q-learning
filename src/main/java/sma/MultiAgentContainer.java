package sma;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

public class MultiAgentContainer {
    public static void main(String[] args) throws ControllerException {
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        AgentContainer agentContainer = runtime.createAgentContainer(profile);
        try {
            // Create and start the MasterAgent
            AgentController masterAgentController = agentContainer.createNewAgent("MasterAgent", MasterAgent.class.getName(), null);
            masterAgentController.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Start the agent container
        agentContainer.start();
    }
}