package vttp2022.paf.assessment.eshop.controllers;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp2022.paf.assessment.eshop.models.Customer;
import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.respositories.CustomerRepository;
import vttp2022.paf.assessment.eshop.respositories.OrderRepository;
import vttp2022.paf.assessment.eshop.services.WarehouseService;

@RestController
public class OrderController {

	@Autowired
	CustomerRepository custRepo;

	@Autowired
	OrderRepository orderRepo;

	@Autowired
	WarehouseService warehouseService;

	//TODO: Task 3
	@PostMapping(path = "/api/order")
	public ResponseEntity<String> findCustomer(@RequestBody String body){
		JsonReader reader = Json.createReader(new StringReader(body));
        JsonObject data = reader.readObject();
		String name = data.getString("name");
		JsonArray lineitems = data.getJsonArray("lineItems");
		Optional<Customer> customerOp = custRepo.findCustomerByName(name);
		if(customerOp.isEmpty()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body("{'error': Customer " + "name"+ " not found}");
		}
		Customer customer = customerOp.get();
		Order order = new Order();
		List<LineItem> items = new LinkedList<>();
		for(JsonValue val: lineitems){
			JsonObject obj = val.asJsonObject();
			LineItem li = LineItem.createLineItems(obj);
			items.add(li);
		}
		order.setName(customer.getName());
		order.setAddress(customer.getAddress());
		order.setEmail(customer.getEmail());
		order.setLineItems(items);
		try {
			orderRepo.saveOrder(order);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body("{'error': " + e.getMessage());
		}
		OrderStatus status = warehouseService.dispatch(order);
		orderRepo.insertStatus(status);			
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("orderId", status.getOrderId());
		try {
			job.add("deliveryId", status.getDeliveryId());
		} catch (Exception e) {
		}
		job.add("status", status.getStatus());
		JsonObject jo = job.build();
		return ResponseEntity.status(HttpStatus.OK).body(jo.toString());
	}

	@GetMapping(path = "/api/order/{name}/status")
	public ResponseEntity<String> getAllOrder(@PathVariable String name){
		Integer dispatched = orderRepo.countDispatched(name);
		Integer pending = orderRepo.countPending(name);
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("name", name);
		job.add("dispatched", dispatched);
		job.add("pending", pending);
		JsonObject jo = job.build();
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(jo.toString());
	}

}
