package shop.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import shop.domain.*;
import shop.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class InventoryUpdated extends AbstractEvent {

    private Long productId;
    private Long stockRemain;

    public InventoryUpdated(Inventory aggregate) {
        super(aggregate);
    }

    public InventoryUpdated() {
        super();
    }
}
//>>> DDD / Domain Event
