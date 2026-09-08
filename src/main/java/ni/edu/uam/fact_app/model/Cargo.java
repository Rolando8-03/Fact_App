package ni.edu.uam.fact_app.model;

import lombok.*;

import java.time.LocalDate;

@Data @NoArgsConstructor
@AllArgsConstructor

public class Cargo {
    private Integer id;
    private String nombre;
    private String apellidos;
    private Cargo cargo;
    private LocalDate fechaContratacion;
    private boolean activo;

}
