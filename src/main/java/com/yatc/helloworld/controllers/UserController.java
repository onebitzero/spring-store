package com.yatc.helloworld.controllers;

import com.yatc.helloworld.dtos.ChangePasswordRequest;
import com.yatc.helloworld.dtos.CreateUserRequest;
import com.yatc.helloworld.dtos.UpdateUserRequest;
import com.yatc.helloworld.dtos.UserDto;
import com.yatc.helloworld.entities.Role;
import com.yatc.helloworld.entities.User;
import com.yatc.helloworld.mappers.UserMapper;
import com.yatc.helloworld.repositories.UserRepository;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
	private final UserMapper userMapper;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@PostMapping
	public ResponseEntity<?> createUser(
			@Valid @RequestBody CreateUserRequest request,
			UriComponentsBuilder uriComponentsBuilder) {
		if (userRepository.existsByEmail(request.getEmail())) {
			return ResponseEntity.badRequest().body(
					Map.of("message", "The email you have provided is already associated with an account."));
		}
		User user = userMapper.toEntity(request);

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.USER);

		userRepository.save(user);

		UserDto userDto = userMapper.toDto(user);

		URI uri = uriComponentsBuilder
				.path("/users/{id}")
				.buildAndExpand(userDto.getId())
				.toUri();

		return ResponseEntity.created(uri).body(userDto);
	}

	@GetMapping
	public List<UserDto> getAllUsers(
			@RequestParam(defaultValue = "", name = "sortBy", required = false) String sortBy) {
		if (!Set.of("email", "name").contains(sortBy)) {
			sortBy = "name";
		}

		return userRepository
				.findAll(Sort.by(sortBy))
				.stream()
				.map(userMapper::toDto)
				.toList();
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUser(@PathVariable long id) {
		User user = userRepository.findById(id).orElse(null);

		if (user == null) {
			// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return ResponseEntity.notFound().build();
		}

		// return new ResponseEntity<>(user, HttpStatus.OK);
		return ResponseEntity.ok(userMapper.toDto(user));
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserDto> updateUser(
			@PathVariable Long id,
			@RequestBody UpdateUserRequest request) {
		User user = userRepository.findById(id).orElse(null);

		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		userMapper.update(request, user);

		userRepository.save(user);

		return ResponseEntity.ok(userMapper.toDto(user));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		User user = userRepository.findById(id).orElse(null);

		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		userRepository.delete(user);

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/change-password")
	public ResponseEntity<Void> changePassword(
			@PathVariable Long id,
			@RequestBody ChangePasswordRequest request) {
		User user = userRepository.findById(id).orElse(null);

		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		if (!user.getPassword().equals(request.getOldPassword())) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		user.setPassword(request.getNewPassword());

		userRepository.save(user);

		return ResponseEntity.noContent().build();
	}
}
