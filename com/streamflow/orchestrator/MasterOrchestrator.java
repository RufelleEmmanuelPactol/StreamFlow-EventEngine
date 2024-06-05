package com.streamflow.orchestrator;

import com.streamflow.errors.LogExceptEngine;
import com.streamflow.errors.SIGINTHandler;
import com.streamflow.state.PersistentState;
import com.streamflow.state.GlobalMetaVariables;
import com.streamflow.state.GlobalStateVariables;

public class MasterOrchestrator {



    public static void main(String[] args) {

        initializerBranding();

        // setup error handler
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptEngine());
        LogExceptEngine.DEBUG = true;



        GlobalMetaVariables.initializeMetaVariables();

        // let's start the Relational Persistence Unit
        PersistentState persistence = new PersistentState();
        GlobalStateVariables.RELATIONAL_PERSISTENCE = persistence;
        persistence.initializeSystem();


        // start the server
        ServerEngineUnit engine = new ServerEngineUnit();
        GlobalStateVariables.MAIN_SERVER = engine;


        engine.startup();


        SIGINTHandler handler = new SIGINTHandler();
        handler.handleSignal("INT");
    }


    public static void initializerBranding() {
        LogExceptEngine.log(MasterOrchestrator.class, ">> StreamFlow Events Engine << ", LogExceptEngine.Level.INFO);
        LogExceptEngine.log(MasterOrchestrator.class, ">> Version 0.0.1 <<", LogExceptEngine.Level.INFO);
        LogExceptEngine.log(MasterOrchestrator.class, "Written by Rufelle Emmanuel Pactol, 2024", LogExceptEngine.Level.INFO);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            LogExceptEngine.log(MasterOrchestrator.class, "Error initializing branding. Please check if the thread is interrupted.", LogExceptEngine.Level.ERROR);
        }
    }










}
