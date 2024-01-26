package shop.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import shop.InventoryApplication;
import shop.domain.InventoryUpdated;

@Entity
@Table(name = "Inventory_table")
@Data
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    private Long stockRemain;

    @PostPersist
    public void onPostPersist() {
        InventoryUpdated inventoryUpdated = new InventoryUpdated(this);
        inventoryUpdated.publishAfterCommit();
    }

    public static InventoryRepository repository() {
        InventoryRepository inventoryRepository = InventoryApplication.applicationContext.getBean(
            InventoryRepository.class
        );
        return inventoryRepository;
    }

    public static void orderPlaced(OrderPlaced orderPlaced) {
        if (
            orderPlaced == null ||
            orderPlaced.getProductId() == null ||
            orderPlaced.getQty() == null
        ) {
            throw new IllegalArgumentException(
                "OrderPlaced event was null or contained null fields."
            );
        }
        Long orderedProduct = orderPlaced.getProductId();
        Long orderedQty = orderPlaced.getQty();

        repository()
            .findById(orderedProduct)
            .ifPresent(inventory -> {
                inventory.setProductId(orderedProduct);
                inventory.setStockRemain(
                    inventory.getStockRemain() - orderedQty
                );
                repository().save(inventory);

                InventoryUpdated inventoryUpdated = new InventoryUpdated(
                    inventory
                );
                inventoryUpdated.publishAfterCommit();
            });
    }
}
