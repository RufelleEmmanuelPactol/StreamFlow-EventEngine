package com.streamflow.wire;

public enum MessageType {
    EVENT_FIRE, AUTH, SUBSCRIBE, UNSUBSCRIBE, HEARTBEAT;

    public String toString() {
        return switch (this) {
            case EVENT_FIRE -> "EVENT_FIRE";
            case AUTH -> "AUTH";
            case SUBSCRIBE -> "SUBSCRIBE";
            case UNSUBSCRIBE -> "UNSUBSCRIBE";
            case HEARTBEAT -> "HEARTBEAT";
        };
    }

}
