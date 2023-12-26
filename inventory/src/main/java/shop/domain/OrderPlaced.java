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

    public void setProductId(String productId) {
        this.productId = Long.parseLong(productId);
    }

    public void setQty(String qty) {
        this.qty = Long.parseLong(qty);
    }
}
