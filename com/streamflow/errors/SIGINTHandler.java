package com.streamflow.errors;

import com.streamflow.orchestrator.MasterOrchestrator;
import com.streamflow.state.GlobalStateVariables;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class SIGINTHandler implements SignalHandler {


    public void handleSignal(String signalName) {
        Signal signal = new Signal(signalName);
        Signal.handle(signal, this);
    }

    @Override
    public void handle(Signal sig) {
        LogExceptEngine.log(SIGINTHandler.class, "Received signal: " + sig.getName() + ". The engine will now terminate.", LogExceptEngine.Level.INFO);
        LogExceptEngine.log(MasterOrchestrator.class, "StreamFlow Orchestrator is initializing closing sequence.", LogExceptEngine.Level.INFO);
        // CLEANUP here
        GlobalStateVariables.MAIN_SERVER.terminate();

        LogExceptEngine.log(MasterOrchestrator.class, "Goodbye :)", LogExceptEngine.Level.INFO);
        System.exit(0);
    }
}