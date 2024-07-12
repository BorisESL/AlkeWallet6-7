package cl.alke.AlkeWallet6.repositories;

import cl.alke.AlkeWallet6.models.Movimiento;
import cl.alke.AlkeWallet6.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByUsuario(Usuario usuario);
}

