package limpo.orderservice.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String name;

    //    Единична цена
    private double price;

    //    Брой пъти извършване на услуга
    private int quantity;

    //    For example - "ЧИСТЕНЕ НА ВХОД"
    private String type;

    //    Подробно обяснение на услугата
    private String description;


}
