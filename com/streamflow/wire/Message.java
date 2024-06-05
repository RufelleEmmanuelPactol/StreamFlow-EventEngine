package com.streamflow.wire;

import com.google.gson.Gson;

import java.util.HashMap;

public  class Message {
    private final HashMap<String, Object> data; // contains the data of the message
    private final HashMap<String, String> message; // contains the internal metadata and the data of the message
    private static final Gson gson = new Gson();


    public Message(MessageType messageType) {
        this.data = new HashMap<>();
        this.message = new HashMap<>();
        data.put("protoc-type", "StreamFlow-wire-1.0");
        data.put("msg-type", messageType.toString());
        data.put("message", message);
    }

    @SuppressWarnings("unchecked")
    protected Message(byte[] bytes) {
        String decodedString = new String(bytes);
        HashMap<String, Object> data = gson.fromJson(decodedString, HashMap.class);
        this.data = data;
        this.message = (HashMap<String, String>) data.get("message");
    }

    /**
     * Adds context to the message. The context refers to the message itself.
     * @param contextName The name of the context
     * @param context The context to be added
     */
    public void addContext(String contextName, String context) {
        message.put(contextName, context);
    }


    /**
     * Encodes the message into a JSON string.
     * @return the JSON string representation of the message
     */
    protected String encode () {
        return gson.toJson(data);
    }


    public String toString () {
        return data.toString();
    }





}
