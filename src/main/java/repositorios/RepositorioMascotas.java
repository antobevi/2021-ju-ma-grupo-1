package repositorios;

import mascotas.EstadoMascota;
import mascotas.Mascota;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import usuarios.Duenio;

import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositorioMascotas implements WithGlobalEntityManager {

    private static final RepositorioMascotas instance= new RepositorioMascotas();

    private RepositorioMascotas() {
    }

    public static RepositorioMascotas getInstance(){
        return instance;
    }

    public void agregarMascota(Mascota unaMascota){
        entityManager().persist(unaMascota);
    }

    public List<Mascota> getMascotas(){
        return entityManager().createQuery("from Mascota").getResultList();
    }

    public Mascota getMascotaById(Long id){
        return entityManager().find(Mascota.class, id);
    }

    public List<Mascota> getMascotasPerdidas(){
//        return this.mascotasRegistradas.stream().filter(mascota -> mascota.estaPerdida()).collect(Collectors.toList());
        return entityManager().createQuery("from Mascota where estado = :estado")
            .setParameter("estado", EstadoMascota.PERDIDA)
            .getResultList();
    }

    public void actualizarMascota(Mascota mascota){
        entityManager().merge(mascota);
    }

    public Optional<Mascota> buscarSiEstaLaMascota(Mascota unaMascota){
        return this.getMascotasPerdidas().stream().filter(otraMascota -> otraMascota == unaMascota).findFirst();
    }

    public List<Mascota> getMascotasPorDuenio(Duenio duenio){
        return entityManager().createQuery("from Mascota where duenio=:duenio")
            .setParameter("duenio", duenio)
            .getResultList();
    }

    public boolean existeLaMascotaConID(Long id) {
        return this.getMascotaById(id) != null;
    }
}
