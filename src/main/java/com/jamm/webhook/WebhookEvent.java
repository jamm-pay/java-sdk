package com.jamm.webhook;

import com.api.v1.EventType;

/**
 * A parsed webhook: the envelope's {@code event_type} together with the deserialized
 * {@code content}. Use {@link #getEventType()} to distinguish events that share a content
 * type — e.g. {@code EVENT_TYPE_CHARGE_SUCCESS} vs {@code EVENT_TYPE_CHARGE_FAIL}, or charge
 * events vs refund events (both of which deserialize to a {@code ChargeMessage}).
 */
public final class WebhookEvent {

    private final EventType eventType;
    private final Object content;

    WebhookEvent(EventType eventType, Object content) {
        this.eventType = eventType;
        this.content = content;
    }

    /**
     * Returns the webhook's event type (e.g. {@code EVENT_TYPE_CHARGE_SUCCESS}).
     *
     * @return the event type
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Returns the parsed content: a {@code ChargeMessage}, {@code ContractMessage}, or
     * {@code UserAccountMessage} depending on the event type.
     *
     * @return the parsed content object
     */
    public Object getContent() {
        return content;
    }
}
