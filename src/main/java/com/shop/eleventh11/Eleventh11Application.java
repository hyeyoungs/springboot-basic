package com.shop.eleventh11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/members")
class MemberController {
	private final List<Member> members = new ArrayList<>();

	public MemberController() {
		members.addAll(List.of(
				new Member("hyeyoungs", "khy1234"),
				new Member("juhui", "kjh1234"),
				new Member("yunjeong", "lee1234"),
				new Member("starbucks", "star1234")
							  ));
	}

	@GetMapping
	Iterable<Member> getMembers() {
		return members;
	}

	@GetMapping("{id}")
	Optional<Member> getMemberById(@PathVariable String id) {
		for (Member m : members) {
			if (m.getId().equals(id)) {
				return Optional.of(m);
			}
		}

		return Optional.empty();
	}

	@PostMapping
	Member postMember(@RequestBody Member member) {
		members.add(member);
		return member;
	}

	@PutMapping("/{id}")
	ResponseEntity<Member> putMember(@PathVariable String id,
								 @RequestBody Member member) {
		int memberIndex = -1;

		for (Member m : members) {
			if (m.getId().equals(id)) {
				memberIndex = members.indexOf(m);
				members.set(memberIndex, member);
			}
		}

		//HTTP status codes are required for PUT method responses
		return (memberIndex == -1) ?
			   new ResponseEntity<>(member, HttpStatus.NO_CONTENT) :
			   new ResponseEntity<>(member, HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	void deleteMember(@PathVariable String id) {
		members.removeIf(m -> m.getId().equals(id));
	}
}

@RestController
@RequestMapping("/items")
class ItemController {
	private final List<Item> items = new ArrayList<>();

	public ItemController() {
		items.addAll(List.of(
				new Item("Macbook 14"),
				new Item("Apple watch 4"),
				new Item("iphone SE"),
				new Item("ipad Air 3")
							));
	}

	@GetMapping
	Iterable<Item> getItems() {
		return items;
	}

	@GetMapping("{id}")
	Optional<Item> getItemById(@PathVariable String id) {
		for (Item c : items) {
			if (c.getId().equals(id)) {
				return Optional.of(c);
			}
		}

		return Optional.empty();
	}

	@PostMapping
	Item postItem(@RequestBody Item item) {
		items.add(item);
		return item;
	}

	@PutMapping("/{id}")
	ResponseEntity<Item> putItem(@PathVariable String id,
								 @RequestBody Item item) {
		int itemIndex = -1;

		for (Item c : items) {
			if (c.getId().equals(id)) {
				itemIndex = items.indexOf(c);
				items.set(itemIndex, item);
			}
		}

		//HTTP status codes are required for PUT method responses
		return (itemIndex == -1) ?
			   new ResponseEntity<>(postItem(item), HttpStatus.CREATED) :
			   new ResponseEntity<>(item, HttpStatus.OK);
	}


	@DeleteMapping("{id}")
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

class Member {
	private static int endUserId = 1;
	private final String id;
	private final String userId;
	private String password;

	public Member(String userId, String password) {
		this.id = String.valueOf(endUserId++);
		this.userId = userId;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
