package limpo.orderservice.order.service;

import limpo.orderservice.order.dto.OrdersPage;
import limpo.orderservice.order.repository.OrderRepository;
import limpo.orderservice.order.dto.Order;
import limpo.orderservice.order.dto.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * This method returns the enum status value from a string
     *
     * @param statusFilter Status name
     * @return Status enum value
     */
    private Status getStatus(String statusFilter) {
        Status status;
        switch (statusFilter) {
            case "NEW":
                status = Status.NEW;
                break;
            case "PENDING":
                status = Status.PENDING;
                break;
            case "APPROVED":
                status = Status.APPROVED;
                break;
            case "COMPLETED":
                status = Status.COMPLETED;
                break;
            default:
                status = Status.ALL;
        }
        return status;
    }

    /**
     * Get all orders, based on a status filter
     * example (statusFilter = "PENDING")
     *
     * @param statusFilter status criteria
     */
    public ArrayList<Order> getAllOrders(String statusFilter, int startIndex) {

        Status status = getStatus(statusFilter);

        if (status == Status.ALL) {
            return (ArrayList<Order>) orderRepository.findAll(startIndex);
        }

        return (ArrayList<Order>) orderRepository.findAllOrdersByStatus(status.ordinal(), startIndex);
    }

    /**
     * Get all orders count, based on a status filter
     * example (statusFilter = "PENDING")
     *
     * @param statusFilter status criteria
     */
    public int getOrdersCount(String statusFilter) {

        Status status = getStatus(statusFilter);

        if (status == Status.ALL) {
            return orderRepository.findAllOrdersCount();
        }

        return orderRepository.findAllOrdersCountByStatus(status);
    }

    /**
     * Get an order by its order number
     *
     * @param orderNumber Order number string
     * @return Order
     */
    public Order getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber).orElse(null);
    }

    /**
     * Get orders by order number,client's name or other order details
     *
     * @param searchInput Input string
     * @param status Order status filter
     * @return Page<Order>
     */
    public Page<Order> getOrdersBySearchInput(String searchInput, String status, int pageNumber, int pageSize) {
        Status statusFilter = getStatus(status);

        Pageable pageable  = PageRequest.of(pageNumber,pageSize, Sort.by("scheduledAt").ascending());

        if(statusFilter== Status.ALL){

            return orderRepository.findBySearchInput(searchInput,pageable);
        }
        return orderRepository.findBySearchInputAndStatusFilter(searchInput,statusFilter,pageable);

    }

    /**
     * Create an order
     *
     * @param order Order object
     * @return Created order
     */
    public Order createOrder(Order order) {
        String createdAt = new SimpleDateFormat("dd.MM.yyyy/HH:mm").format(new Date());

        order.setStatus(Status.NEW);
        order.setCreatedAt(createdAt);

        Order createdOrder = orderRepository.save(order);
        createdOrder.setOrderNumber(new SimpleDateFormat("ddMMyyyy").format(new Date()) + "L" + createdOrder.getId());
        return orderRepository.save(createdOrder);
    }

    /**
     * Update order status
     *
     * @param orderNumber Order number string
     * @param status      Status to be promoted
     * @return Updated order
     */
    public Order updateOrder(String orderNumber, String status) {
        Order updatedOrder = orderRepository.findByOrderNumber(orderNumber).orElse(null);

        if (updatedOrder != null) {
            Status updatedStatus = getStatus(status);
            updatedOrder.setStatus(updatedStatus);
            orderRepository.save(updatedOrder);
            return updatedOrder;
        }

        return null;
    }

    /**
     * Delete an order by order number
     *
     * @param orderNumber Order number string
     * @return Deleted order
     */
    public Order deleteOrder(String orderNumber) {
        Order orderToDelete = orderRepository.findByOrderNumber(orderNumber).orElse(null);

        if (orderToDelete != null) {
            orderRepository.deleteById(orderToDelete.getId());
        }
        return orderToDelete;
    }
}
