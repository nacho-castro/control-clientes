package web;

import datos.ClienteDaoJDBC;
import domain.Cliente;
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ServletControlador")
public class ServletControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "editar":
                    this.editarCliente(req, resp);
                    break;
                case "eliminar":
                    this.eliminarCliente(req,resp);
                    break;
                default:
                    this.accionDefault(req, resp);
            }
        } else {
            this.accionDefault(req, resp);
        }
    }

    private void accionDefault(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Cliente> clientes = new ClienteDaoJDBC().listar();

        //Scope de Session para que la informacion continue disponible
        HttpSession sesion = req.getSession();

        sesion.setAttribute("clientes", clientes);
        sesion.setAttribute("saldoTotal", this.calcularSaldoTotal(clientes));
        sesion.setAttribute("totalClientes", clientes.size());
        resp.sendRedirect("clientes.jsp");
    }

    private double calcularSaldoTotal(List<Cliente> clientes) {
        double saldoTotal = 0.00;
        for (Cliente cli : clientes) {
            saldoTotal += cli.getSaldo();
        }
        return saldoTotal;
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "insertar":
                    this.insertarCliente(req, resp);
                    break;
                case "modificar":
                    this.modificarCliente(req, resp);
                    break;
                default:
                    this.accionDefault(req, resp);
            }
        } else {
            this.accionDefault(req, resp);
        }
    }

    private void insertarCliente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Recuperamos valores del form de agregarCliente.jsp
        String nombre = req.getParameter("nombre");
        String apellido = req.getParameter("apellido");
        String email = req.getParameter("email");
        String telefono = req.getParameter("telefono");
        double saldo = Double.parseDouble(req.getParameter("saldo"));

        Cliente cliente = new Cliente(nombre, apellido, email, telefono, saldo);
        
        //Insertar el objeto en la BD
        int registrosModificados = new ClienteDaoJDBC().insert(cliente);
        System.out.println(registrosModificados);

        this.accionDefault(req, resp);
    }

    private void editarCliente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idCliente = Integer.parseInt(req.getParameter("idCliente"));
        
        Cliente cliente = new ClienteDaoJDBC().encontrar(new Cliente(idCliente));
        req.setAttribute("cliente", cliente);
        
        String jspEditar = "/WEB-INF/paginas/cliente/editarCliente.jsp";
        req.getRequestDispatcher(jspEditar).forward(req, resp);
    }
    
    public void modificarCliente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //Recuperamos valores del form de editarCliente.jsp
        int idCliente = Integer.parseInt(req.getParameter("idCliente"));
        String nombre = req.getParameter("nombre");
        String apellido = req.getParameter("apellido");
        String email = req.getParameter("email");
        String telefono = req.getParameter("telefono");
        double saldo = Double.parseDouble(req.getParameter("saldo"));

        Cliente cliente = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);
        
        //Modificar el objeto en la BD
        int registrosModificados = new ClienteDaoJDBC().update(cliente);
        System.out.println(registrosModificados);
        
        this.accionDefault(req, resp);
    }
    
    public void eliminarCliente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //Recuperamos valores 
        int idCliente = Integer.parseInt(req.getParameter("idCliente"));
        
        //Eliminar el objeto en la BD
        int registrosModificados = new ClienteDaoJDBC().delete(new Cliente(idCliente));
        System.out.println(registrosModificados);
        
        this.accionDefault(req, resp);
    }
}
