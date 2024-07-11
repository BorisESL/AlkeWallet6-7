package cl.alke.AlkeWallet6.controllers;

import cl.alke.AlkeWallet6.models.Usuario;
import cl.alke.AlkeWallet6.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String login(@RequestParam String email, @RequestParam String password, HttpServletRequest request, Model model) {
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
    public String menuprincipal(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            model.addAttribute("nombre", usuario.getNombre());
            model.addAttribute("balance", usuario.getBalance());
            return "menuprincipal";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/depositarfondos")
    public String depositarfondos(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            model.addAttribute("nombre", usuario.getNombre());
            return "depositarfondos";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/retirarfondos")
    public String retirarfondos(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            model.addAttribute("nombre", usuario.getNombre());
            return "retirarfondos";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/ultimosmov")
    public String ultimosmov(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            model.addAttribute("nombre", usuario.getNombre());
            return "ultimosmov";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }
}
