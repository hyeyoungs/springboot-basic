package com.shop.eleventh11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class Eleventh11Application {

	public static void main(String[] args) {
		SpringApplication.run(Eleventh11Application.class, args);
	}
}

@RestController
//@RequestMapping("/items")
class RestApiDemoController {
	private final List<Item> items = new ArrayList<>();

	public RestApiDemoController() {
		items.addAll(List.of(
				new Item("Macbook 14"),
				new Item("Apple watch 4"),
				new Item("iphone SE"),
				new Item("ipad Air 3")
							));
	}

	@GetMapping("/items")
	Iterable<Item> getItems() {
		return items;
	}

	@GetMapping("/items/{id}")
	Optional<Item> getItemById(@PathVariable String id) {
		for (Item c : items) {
			if (c.getId().equals(id)) {
				return Optional.of(c);
			}
		}

		return Optional.empty();
	}

	@PostMapping("/items")
	Item postItem(@RequestBody Item item) {
		items.add(item);
		return item;
	}

	@PutMapping("/items/{id}")
	Item putItem(@PathVariable String id,
				 @RequestBody Item item) {
		int itemIndex = -1;

		for (Item c : items) {
			if (c.getId().equals(id)) {
				itemIndex = items.indexOf(c);
				items.set(itemIndex, item);
			}
		}

		return (itemIndex == -1) ?
			   postItem(item) : item;
	}


	@DeleteMapping("/items/{id}")
	void deleteItem(@PathVariable String id) {
		items.removeIf(c -> c.getId().equals(id));
	}
}

class Item {
	private final String id;
	private String name;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
