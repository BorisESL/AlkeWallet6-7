package cl.alke.AlkeWallet6.controllers;

import cl.alke.AlkeWallet6.models.Usuario;
import cl.alke.AlkeWallet6.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.Optional;

@Controller
public class MiController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpServletRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent() && usuarioOpt.get().getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuarioOpt.get());
            return "redirect:/menuprincipal";
        } else {
            return "redirect:/?error=true";
        }
    }

    @GetMapping("/menuprincipal")
    public String menuprincipal() {
        return "menuprincipal";
    }

    @GetMapping("/depositarfondos")
    public String depositarfondos() {
        return "depositarfondos";
    }

    @GetMapping("/retirarfondos")
    public String retirarfondos() {
        return "retirarfondos";
    }

    @GetMapping("/ultimosmov")
    public String ultimosmov() {
        return "ultimosmov";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }
}
