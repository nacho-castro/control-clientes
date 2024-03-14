package datos;

import static datos.Conexion.*;
import java.sql.*;
import java.util.*;
import domain.Cliente;

//Data Access Object asociada a la Entidad
public class ClienteDaoJDBC implements ClienteDAO {

    private static final String SQL_SELECT = "SELECT * FROM cliente";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM cliente WHERE id_cliente = ?";
    private static final String SQL_DELETE = "DELETE FROM cliente WHERE id_cliente=?";
    private static final String SQL_INSERT = "INSERT INTO cliente(nombre,apellido,email,telefono,saldo) VALUES (?,?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE cliente SET nombre=?, apellido=?, email=?, telefono=?, saldo=? WHERE id_cliente=?";

    @Override
    public int insert(Cliente cli) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, cli.getNombre());
            stmt.setString(2, cli.getApellido());
            stmt.setString(3, cli.getEmail());
            stmt.setString(4, cli.getTelefono());
            stmt.setDouble(5, cli.getSaldo());
            rows = stmt.executeUpdate(); //modifica estado de la BD
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            close(stmt);
            close(conn);
        }
        return rows;
    }

    @Override
    public int update(Cliente cli) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, cli.getNombre());
            stmt.setString(2, cli.getApellido());
            stmt.setString(3, cli.getEmail());
            stmt.setString(4, cli.getTelefono());
            stmt.setDouble(5, cli.getSaldo());
            stmt.setInt(6, cli.getIdCliente());
            rows = stmt.executeUpdate(); //modifica estado de la BD
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            close(stmt);
            close(conn);
        }
        return rows;
    }

    @Override
    public int delete(Cliente cli) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, cli.getIdCliente());
            rows = stmt.executeUpdate(); //modifica estado de la BD
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            close(stmt);
            close(conn);
        }
        return rows;
    }

    @Override
    public Cliente encontrar(Cliente cli) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, cli.getIdCliente());
            rs = stmt.executeQuery();

            rs.absolute(1);//Nos posicionamos en el primer registro

            String nombre = rs.getString("nombre");
            String apellido = rs.getString("apellido");
            String email = rs.getString("email");
            String telefono = rs.getString("telefono");
            double saldo = rs.getDouble("saldo");

            cli.setNombre(nombre);
            cli.setApellido(apellido);
            cli.setEmail(email);
            cli.setTelefono(telefono);
            cli.setSaldo(saldo);

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
        return cli;
    }

    @Override
    public List<Cliente> listar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Cliente cli = null;
        List<Cliente> lista = new ArrayList<>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int idCliente = rs.getInt("id_cliente");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String email = rs.getString("email");
                String telefono = rs.getString("telefono");
                double saldo = rs.getDouble("saldo");

                cli = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);
                lista.add(cli);
                
                System.out.println("Número de clientes recuperados: " + lista.size());
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
        return lista;
    }

}
