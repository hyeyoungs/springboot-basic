package com.shop.eleventh11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface ItemRepository extends CrudRepository<Item, String> {
}

@SpringBootApplication
public class Eleventh11Application {

	public static void main(String[] args) {
		SpringApplication.run(Eleventh11Application.class, args);
	}
}

@Component
class DataLoader {
	private final ItemRepository itemRepository;

	public DataLoader(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@PostConstruct
	private void loadData() {
		itemRepository.saveAll(List.of(
				new Item("Macbook 14"),
				new Item("Apple watch 4"),
				new Item("iphone SE"),
				new Item("ipad Air 3")
									  ));
	}
}

@RestController
@RequestMapping("/items")
class RestApiDemoController {
	private final ItemRepository itemRepository;

	public RestApiDemoController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@GetMapping
	Iterable<Item> getItems() {
		return itemRepository.findAll();
	}

	@GetMapping("/{id}")
	Optional<Item> getItemById(@PathVariable String id) {
		return itemRepository.findById(id);
	}

	@PostMapping
	Item postItem(@RequestBody Item item) {
		return itemRepository.save(item);
	}

	@PutMapping("/{id}")
	ResponseEntity<Item> putItem(@PathVariable String id,
								 @RequestBody Item item) {

		return (itemRepository.existsById(id))
			   ? new ResponseEntity<>(itemRepository.save(item), HttpStatus.OK)
			   : new ResponseEntity<>(itemRepository.save(item), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	void deleteItem(@PathVariable String id) {
		itemRepository.deleteById(id);
	}
}

@Entity
class Item {
	@Id
	private String id;
	private String name;

	public Item() {
	}

	public Item(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Item(String name) {
		this(UUID.randomUUID().toString(), name);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
