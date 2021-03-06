package limpo.orderservice.order.repository;

import limpo.orderservice.order.dto.Order;
import limpo.orderservice.order.dto.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.orderNumber = :orderNumber")
    Optional<Order> findByOrderNumber(String orderNumber);

    @Query("SELECT o FROM Order o WHERE o.status <> 3 AND (o.orderNumber LIKE %:searchInput% OR o.client.firstName LIKE %:searchInput% OR o.client.lastName LIKE %:searchInput% OR o.createdAt LIKE %:searchInput% OR o.client.corporateName LIKE %:searchInput% OR o.client.bulstat LIKE %:searchInput% OR o.client.email LIKE %:searchInput% OR o.client.phone LIKE %:searchInput%)")
    Page<Order> findBySearchInput(String searchInput,Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.status = :status AND (o.orderNumber LIKE %:searchInput% OR o.client.firstName LIKE %:searchInput% OR o.client.lastName LIKE %:searchInput% OR o.createdAt LIKE %:searchInput% OR o.client.corporateName LIKE %:searchInput% OR o.client.bulstat LIKE %:searchInput% OR o.client.email LIKE %:searchInput% OR o.client.phone LIKE %:searchInput%)")
    Page<Order> findBySearchInputAndStatusFilter(String searchInput, Status status,Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM orders o WHERE o.status != 3 LIMIT 5 OFFSET :startIndex ;")
    Collection<Order> findAll(int startIndex);

    @Query(nativeQuery = true, value = "SELECT * FROM orders o WHERE o.status = :status LIMIT 5 OFFSET :startIndex ;")
    Collection<Order> findAllOrdersByStatus(int status, int startIndex);

    @Query("SELECT count(o) FROM Order o WHERE o.status <> 3")
    int findAllOrdersCount();

    @Query("SELECT count(o) FROM Order o WHERE o.status = :status")
    int findAllOrdersCountByStatus(Status status);
}
