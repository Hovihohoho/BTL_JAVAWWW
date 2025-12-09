package iuh.btl_n7_iuh.services;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import iuh.btl_n7_iuh.entities.OrderStatus;
import iuh.btl_n7_iuh.repositories.OrderStatusRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatusService {
    @Autowired
    private final OrderStatusRepository orderStatusRepository;
    public List<OrderStatus> findAll() {
        return orderStatusRepository.findAll();
    }

}
