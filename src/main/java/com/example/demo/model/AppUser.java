public String getRole() {
    return role;
}

// Handle BOTH String and Role automatically
public void setRole(Object roleObj) {
    if (roleObj instanceof Role) {
        this.role = ((Role) roleObj).name();
    } else {
        this.role = roleObj != null ? roleObj.toString() : null;
    }
}
