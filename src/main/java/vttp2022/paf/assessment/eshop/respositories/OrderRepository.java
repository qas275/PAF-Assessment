package vttp2022.paf.assessment.eshop.respositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;

import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

import java.util.Date;

@Repository
public class OrderRepository {
	// TODO: Task 3

	@Autowired
	JdbcTemplate jdbcTemplate;


	@Transactional(rollbackFor = OrderException.class)
	public void saveOrder(Order order) throws OrderException{
		if (jdbcTemplate.update(SQL_INSERT_ORDER, order.getOrderId(), order.getName(), order.getAddress(), order.getEmail(), order.getOrderDate())<1){
			throw new OrderException("unable to add into orders");
		};
		for(LineItem li :order.getLineItems()){
			if (jdbcTemplate.update(SQL_INSERT_LINE_ITEM, li.getItem(), li.getQuantity(), order.getOrderId())<1){
				throw new OrderException("unable to add into line items");
			};

		}
	}

	public void insertStatus(OrderStatus status){
		String deliveryId = null;
		try{
			deliveryId = status.getDeliveryId();
		}catch(Exception e){
			
		}
		jdbcTemplate.update(SQL_INSERT_STATUS, status.getOrderId(), deliveryId, status.getStatus(), new Date());
	}

	public Integer countDispatched(String name){
		SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_COUNT_DISPATCHED, name);
		rs.next();
		return rs.getInt("dispatched");
	}

	public Integer countPending(String name){
		SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_COUNT_PENDING, name);
		rs.next();
		return rs.getInt("pending");
	}

}
