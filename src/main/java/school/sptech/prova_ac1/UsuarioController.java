package school.sptech.prova_ac1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodos() {
        return usuarioRepository.findAll()
          .isEmpty()
          ? ResponseEntity.noContent().build()
          : ResponseEntity.ok(usuarioRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        if (usuarioRepository.existsByCpfOrEmail(usuario.getCpf(), usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        URI location = ServletUriComponentsBuilder
          .fromCurrentRequest()
          .path("/{id}")
          .buildAndExpand(usuarioSalvo.getId())
          .toUri();
        return ResponseEntity.created(location).body(usuarioSalvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
          .map(ResponseEntity::ok)
          .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/filtro-data")
    public ResponseEntity<List<Usuario>> buscarPorDataNascimento(
      @RequestParam LocalDate nascimento) {
        List<Usuario> usuarios = usuarioRepository.findAllByDataNascimentoGreaterThan(nascimento);
        return usuarios.isEmpty()
          ? ResponseEntity.noContent().build()
          : ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable Integer id,
            @RequestBody Usuario usuario
    ) {

        usuario.setId(id);

        if (usuarioRepository.existsByCpfAndIdNot(usuario.getCpf(), usuario.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(usuario);
        }

        if (usuarioRepository.existsByEmailAndIdNot(usuario.getEmail(), usuario.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(usuario);
        }

        usuarioRepository.save(usuario);
        return ResponseEntity.ok().body(usuario);
    }
}
