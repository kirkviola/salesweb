package com.acme.sales.orderline;

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

import com.acme.sales.order.OrderRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/orderlines")
public class OrderlinesController {
	
	@Autowired
	private OrderlineRepository olineRepos;
	@Autowired
	private OrderRepository orderRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<Orderline>> GetAll(){
		var olines = olineRepos.findAll();	
		return new ResponseEntity<Iterable<Orderline>>(olines, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Orderline> GetById(@PathVariable int id){
		var oline = olineRepos.findById(id);
		if(oline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Orderline>(oline.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Orderline> Insert(@RequestBody Orderline orderline) throws Exception {
		if(orderline == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var newOline = olineRepos.save(orderline);
		RecalculateOrder(orderline.getOrder().getId());
		return new ResponseEntity<Orderline>(newOline, HttpStatus.CREATED);
	}
	// add recalc order
	@PutMapping("{id}")
	public ResponseEntity Update(@PathVariable int id, @RequestBody Orderline orderline) {
		if (id != orderline.getId())
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		var oldOline = olineRepos.findById(id);
		if (oldOline == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		olineRepos.save(orderline);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity Delete(@PathVariable int id) {
		var oline = olineRepos.findById(id);
		if (oline.isEmpty())
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		olineRepos.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
	}
	
	private void RecalculateOrder(int orderId) throws Exception {
		var optOrder = orderRepo.findById(orderId);
		if(optOrder.isEmpty()) {
			throw new Exception("order id is invalid");			
		}
		var order = optOrder.get();
		var orderLines = olineRepos.findOrderlineByOrderId(orderId);
		var total = 0;
		for(var orderLine : orderLines) {
			total += orderLine.getQuantity() * orderLine.getPrice();
		}
		order.setTotal(total);
		orderRepo.save(order);
		
	}
}
