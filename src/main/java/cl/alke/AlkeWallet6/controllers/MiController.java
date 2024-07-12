package cl.alke.AlkeWallet6.controllers;

import cl.alke.AlkeWallet6.models.Movimiento;
import cl.alke.AlkeWallet6.models.Usuario;
import cl.alke.AlkeWallet6.repositories.MovimientoRepository;
import cl.alke.AlkeWallet6.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class MiController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

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
    public String menuPrincipal(HttpServletRequest request, Model model) {
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
    public String depositarFondos() {
        return "depositarfondos";
    }

    @PostMapping("/depositarfondos")
    public String depositarFondos(HttpServletRequest request, @RequestParam double monto, Model model) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            // Actualizar el balance del usuario
            usuario.setBalance((int) (usuario.getBalance() + monto));
            usuarioRepository.save(usuario);

            // Actualizar el balance en la sesión
            session.setAttribute("usuario", usuario);
            model.addAttribute("nombre", usuario.getNombre());
            model.addAttribute("balance", usuario.getBalance());

            return "redirect:/menuprincipal";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/retirarfondos")
    public String retirarFondos() {
        return "retirarfondos";
    }

    @PostMapping("/retirarfondos")
    public String retirarFondos(HttpServletRequest request, @RequestParam double monto, Model model) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            if (usuario.getBalance() >= monto) {
                // Actualizar el balance del usuario
                usuario.setBalance((int) (usuario.getBalance() - monto));
                usuarioRepository.save(usuario);

                // Actualizar el balance en la sesión
                session.setAttribute("usuario", usuario);
                model.addAttribute("nombre", usuario.getNombre());
                model.addAttribute("balance", usuario.getBalance());

                return "redirect:/menuprincipal";
            } else {
                // Manejar el caso cuando no hay suficiente balance
                model.addAttribute("error", "No tienes suficiente balance para retirar.");
                return "retirarfondos";
            }
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/realizarTransferencia")
    public String realizarTransferencia() {
        return "transferencia";
    }

    @PostMapping("/realizarTransferencia")
    public String realizarTransferencia(HttpServletRequest request, @RequestParam double monto, @RequestParam String contacto, Model model) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            Optional<Usuario> destinatarioOpt = usuarioRepository.findByEmail(contacto);

            if (destinatarioOpt.isPresent()) {
                Usuario destinatario = destinatarioOpt.get();

                if (usuario.getBalance() >= monto) {
                    // Actualizar el balance del usuario
                    usuario.setBalance((int) (usuario.getBalance() - monto));
                    usuarioRepository.save(usuario);

                    // Actualizar el balance del destinatario
                    destinatario.setBalance((int) (destinatario.getBalance() + monto));
                    usuarioRepository.save(destinatario);

                    // Actualizar la sesión del usuario
                    session.setAttribute("usuario", usuario);
                    model.addAttribute("nombre", usuario.getNombre());
                    model.addAttribute("balance", usuario.getBalance());

                    return "redirect:/menuprincipal";
                } else {
                    // Manejar el caso cuando no hay suficiente balance
                    model.addAttribute("error", "No tienes suficiente balance para realizar la transferencia.");
                    return "transferencia";
                }
            } else {
                // Manejar el caso cuando el destinatario no se encuentra
                model.addAttribute("error", "El contacto no se encuentra.");
                return "transferencia";
            }
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/ultimosmov")
    public String ultimosMovimientos(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            List<Movimiento> movimientos = movimientoRepository.findByUsuario(usuario);
            model.addAttribute("movimientos", movimientos);
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
