package vttp2022.paf.assessment.eshop.services;

import java.io.StringReader;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;

@Service
public class WarehouseService {

	public static final String urlChara = "http://paf.chuklee.com/dispatch/";

	// You cannot change the method's signature
	// You may add one or more checked exceptions
	public OrderStatus dispatch(Order order) {

		// TODO: Task 4
		String orderUri = urlChara + order.getOrderId();
		System.out.println(orderUri);
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("orderId", order.getOrderId());
		job.add("name", order.getName());
		job.add("address", order.getAddress());
		job.add("email", order.getEmail());
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for(LineItem li: order.getLineItems()){
			JsonObjectBuilder liJob = Json.createObjectBuilder();
			liJob.add("item", li.getItem());
			liJob.add("quantity", li.getQuantity());
			jab.add(liJob.build());
		}
		job.add("lineItems", jab.build());
		job.add("createdBy", "Lim Wei Shun");
		JsonObject jo = job.build();
		System.out.println(jo.toString());
		RequestEntity<String> req = RequestEntity.post(orderUri)
			.contentType(MediaType.APPLICATION_JSON)
			.body(jo.toString());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> resp = null;
		OrderStatus status = new OrderStatus();
		try {
			resp = restTemplate.exchange(req, String.class);
			String payload = resp.getBody();
			System.out.println("\n\n\n PAYLOAD \n" +payload + "\n\n\n");
			JsonReader reader = Json.createReader(new StringReader(payload));
			JsonObject result = reader.readObject();
			status.setOrderId(result.getString("orderId"));
			status.setDeliveryId(result.getString("deliveryId"));
			status.setStatus("dispatched");

			return status;
		} catch (Exception e) {
			status.setOrderId(order.getOrderId());
			status.setStatus("pending");
			return status;
		}
	}
}
