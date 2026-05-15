package repository;

import model.Paciente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repositório de pacientes em memória.
 * HashMap indexado por código de identificação — O(1) para busca.
 */
public class PacienteRepository {

    private static PacienteRepository instancia;
    private final Map<String, Paciente> pacientes;

    private PacienteRepository() {
        pacientes = new HashMap<>();
    }

    public static PacienteRepository getInstance() {
        if (instancia == null) {
            instancia = new PacienteRepository();
        }
        return instancia;
    }

    public void adicionar(Paciente paciente) {
        if (paciente.getCodigo() == null || paciente.getCodigo().isEmpty()) {
            throw new IllegalArgumentException("Código do paciente é obrigatório");
        }
        pacientes.put(paciente.getCodigo(), paciente);
    }

    public Paciente buscarPorCodigo(String codigo) {
        return pacientes.get(codigo);
    }

    public List<Paciente> listarAtivos() {
        List<Paciente> ativos = new ArrayList<>();
        for (Paciente p : pacientes.values()) {
            if (p.isAtivo()) ativos.add(p);
        }
        return ativos;
    }

    public List<Paciente> listarTodos() {
        return new ArrayList<>(pacientes.values());
    }

    public int getTotalAtivos() {
        int count = 0;
        for (Paciente p : pacientes.values()) {
            if (p.isAtivo()) count++;
        }
        return count;
    }

    public int getTotal() { return pacientes.size(); }

    public boolean existe(String codigo) {
        return pacientes.containsKey(codigo);
    }

    public void remover(String codigo) {
        Paciente p = pacientes.get(codigo);
        if (p != null) p.inativar();
    }
}
