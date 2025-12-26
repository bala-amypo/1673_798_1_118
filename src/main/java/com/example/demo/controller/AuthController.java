@PostMapping("/register")
public AppUser register(@RequestBody RegisterRequest request) {
    if (userRepository.existsByUsername(request.getUsername())) {
        throw new RuntimeException("Username already taken");
    }
    if (userRepository.existsByEmail(request.getEmail())) {
        throw new RuntimeException("Email already taken");
    }

    String hashedPassword = passwordEncoder.encode(request.getPassword());
    AppUser user = new AppUser(request.getUsername(), request.getEmail(), hashedPassword, request.getRole());  // String role
    return userRepository.save(user);
}
