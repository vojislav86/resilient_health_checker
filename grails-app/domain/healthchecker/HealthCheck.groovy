package healthchecker

class HealthCheck {

    static constraints = {
        status nullable: false
        message nullable: false
    }

    Integer status
    String message
}
