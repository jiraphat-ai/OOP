public class User {
    private String username;
    private String password;
    private String role;
    // Getters and Setters
}
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        // Save the user to the database
    }

    public Optional<User> findByUsername(String username) {
        // Find the user by their username
    }

    public boolean checkUsernameExists(String username) {
        // Check if the username already exists
    }
}