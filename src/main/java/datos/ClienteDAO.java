
package datos;

import java.util.List;
import domain.Cliente;

public interface ClienteDAO {
    public int insert(Cliente cli);
    public int update(Cliente cli);
    public int delete(Cliente cli);
    public Cliente encontrar(Cliente cli);
    public List<Cliente> listar();
}
