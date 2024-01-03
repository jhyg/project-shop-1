package shop.domain;

import java.util.*;
import lombok.*;
import shop.domain.*;
import shop.infra.AbstractEvent;

@Data
@ToString
public class OrderPlaced extends AbstractEvent {

    private Long productId;
    private Long qty;

    public void placeOrder() {
        Inventory.orderPlaced(this);
    }
}
