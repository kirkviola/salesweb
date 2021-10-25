package com.acme.sales.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

	@Autowired
	private OrderRepository orderRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<Order>> GetAll(){
		var orders = orderRepo.findAll();
		return new ResponseEntity<Iterable<Order>>(orders, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Order> GetById(@PathVariable int id) {
		var order = orderRepo.findById(id);
		if (order.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Order>(order.get(), HttpStatus.OK);
	}
	
	@GetMapping("customer/{customerId}")
	public ResponseEntity<Iterable<Order>> GetOrderNotCustomer(@PathVariable int customerId){
		var orders = orderRepo.findByCustomerIdNot(customerId);
		return new ResponseEntity<Iterable<Order>>(orders, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Order> Insert(@RequestBody Order order){
		if (order == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var newOrder = orderRepo.save(order);
		return new ResponseEntity<Order>(newOrder, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity Update(@PathVariable int id, @RequestBody Order order) {
		if (id != order.getId())
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		var oldOrder = orderRepo.findById(order.getId());
		if(oldOrder == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		orderRepo.save(order);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("review/{id}")
	public ResponseEntity SetOrderToZero(@PathVariable int id, @RequestBody Order order) {
		var newTotal = order.getTotal() <= 100
				? 0 : 1000;		
		order.setTotal(newTotal);
		return Update(order.getId(), order);		
	}
	
	@PutMapping("approved/{id}")
	public ResponseEntity SetOrderTo5000 (@PathVariable int id, @RequestBody Order order) {
		order.setTotal(5000);
		return Update(id, order);
	}
	
	@PutMapping("reject/{id}")
	public ResponseEntity SetOrderToNegative5000 (@PathVariable int id, @RequestBody Order order) {
		order.setTotal(-5000);
		return Update(id, order);
	}
	
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping
	public ResponseEntity Delete(@PathVariable int id) {
		var order = orderRepo.findById(id);
		if (order.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		orderRepo.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
	
}
