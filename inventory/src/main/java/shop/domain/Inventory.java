package shop.domain;

import java.time.LocalDate;
import java.util.Date;
import javax.persistence.*;
import lombok.*;

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
        repository()
            .findById(orderPlaced.getProductId())
            .ifPresent(inventory -> {
                inventory.setStockRemain(
                    inventory.getStockRemain() - orderPlaced.getQty()
                );
                repository().save(inventory);
                InventoryUpdated inventoryUpdated = new InventoryUpdated(
                    inventory
                );
                inventoryUpdated.publishAfterCommit();
            });
    }
}
