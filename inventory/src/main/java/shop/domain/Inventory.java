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

    public void setStockRemain(String stockRemain) {
        this.stockRemain = Long.parseLong(stockRemain);
    }

    public static InventoryRepository repository() {
        InventoryRepository inventoryRepository = InventoryApplication.applicationContext.getBean(
            InventoryRepository.class
        );
        return inventoryRepository;
    }

    public static void orderPlaced(OrderPlaced orderPlaced) {
        Inventory inventory = repository()
            .findById(orderPlaced.getProductId())
            .orElse(null);
        if (inventory != null) {
            Long newStockRemain =
                inventory.getStockRemain() - orderPlaced.getQty();
            inventory.setStockRemain(newStockRemain.toString());
            repository().save(inventory);
            InventoryUpdated inventoryUpdated = new InventoryUpdated(inventory);
            inventoryUpdated.publishAfterCommit();
        }
    }
}
