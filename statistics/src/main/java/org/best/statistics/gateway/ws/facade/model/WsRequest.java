package org.best.statistics.gateway.ws.facade.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class WsRequest implements Serializable {
    private static final long serialVersionUID = -8524233440575864058L;

    private String messageType;
}
