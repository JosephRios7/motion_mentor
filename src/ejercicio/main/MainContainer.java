package ejercicio.main;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Runtime;
import jade.wrapper.StaleProxyException;
import ejercicio.agentes.FeedbackAgent;
import ejercicio.agentes.VisionAgent;

public class MainContainer {

    public static void main(String[] args) {
        try {
            // Inicializar el entorno JADE
            Runtime rt = Runtime.instance();
            Profile p = new ProfileImpl();
            ContainerController container = rt.createMainContainer(p);

            // Iniciar el Agente de Visión
            AgentController agenteVision = container.createNewAgent("AgenteVision", VisionAgent.class.getName(), null);
            agenteVision.start();

            // Iniciar el Agente de Retroalimentación
            AgentController agenteRetroalimentacion = container.createNewAgent("AgenteRetroalimentacion", FeedbackAgent.class.getName(), null);
            agenteRetroalimentacion.start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
