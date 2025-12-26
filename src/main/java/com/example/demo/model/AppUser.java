// Change this line in AppUser.java:
@Column(nullable = false)
private String role;  // String instead of Role enum

// Update constructor:
public AppUser(String username, String email, String password, String role) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.role = role;  // String
}

// Update AuthController register method:
AppUser user = new AppUser(request.getUsername(), request.getEmail(), hashedPassword, request.getRole());
