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
        repository()
            .findById(Long.parseLong(orderPlaced.getProductId()))
            .ifPresent(inventory -> {
                inventory.setStockRemain(
                    Long.toString(
                        Long.parseLong(inventory.getStockRemain()) -
                        Long.parseLong(orderPlaced.getQty())
                    )
                );
                repository().save(inventory);

                InventoryUpdated inventoryUpdated = new InventoryUpdated(
                    inventory
                );

                inventoryUpdated.setStockRemain(
                    inventory.getStockRemain().toString()
                );

                inventoryUpdated.publishAfterCommit();
            });
    }
}
