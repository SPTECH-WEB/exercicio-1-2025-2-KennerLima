package school.sptech.prova_ac1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

  boolean existsByCpfOrEmail(String cpf, String email);

  List<Usuario> findAllByDataNascimentoGreaterThan(LocalDate nascimento);

  boolean existsByCpfAndIdNot(String cpf, Integer id);

  boolean existsByEmailAndIdNot(String email, Integer id);
}

