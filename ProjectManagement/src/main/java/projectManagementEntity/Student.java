public class Student {
    private String id;
    private String name;
    private String lastname;

    public Student(String id, String name, String lastname) {
        this.id = id;
        this.name = name;

    }

    // Getters and Setters
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
