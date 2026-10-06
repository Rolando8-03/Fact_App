package ni.edu.uam.fact_app.model;

public class Usuario {
    private String username;
    private String password;
    private Empleado empleado;

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public Empleado getEmpleado() {
        return this.empleado;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setEmpleado(final Empleado empleado) {
        this.empleado = empleado;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof Usuario)) return false;
        final Usuario other = (Usuario) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$username = this.getUsername();
        final java.lang.Object other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) return false;
        final java.lang.Object this$password = this.getPassword();
        final java.lang.Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        final java.lang.Object this$empleado = this.getEmpleado();
        final java.lang.Object other$empleado = other.getEmpleado();
        if (this$empleado == null ? other$empleado != null : !this$empleado.equals(other$empleado)) return false;
        return true;
    }

    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof Usuario;
    }

    @java.lang.Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $username = this.getUsername();
        result = result * PRIME + ($username == null ? 43 : $username.hashCode());
        final java.lang.Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final java.lang.Object $empleado = this.getEmpleado();
        result = result * PRIME + ($empleado == null ? 43 : $empleado.hashCode());
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Usuario(username=" + this.getUsername() + ", password=" + this.getPassword() + ", empleado=" + this.getEmpleado() + ")";
    }

    public Usuario() {
    }

    public Usuario(final String username, final String password, final Empleado empleado) {
        this.username = username;
        this.password = password;
        this.empleado = empleado;
    }
}
