import java.io.*;
import java.util.ArrayList;

/**
 * Classe GerenciadorUsuarios
 */
public class GerenciadorUsuarios {
    private ArrayList<Usuario> usuarios;
    private static final String ARQUIVO_USUARIOS = "usuarios.txt";
    
    /**
     * Construtor - inicializa a lista e carrega usuários do arquivo
     */
    public GerenciadorUsuarios() {
        usuarios = new ArrayList<>();
        carregarUsuarios();
        
        // Se não houver usuários, cria um admin padrão
        if (usuarios.isEmpty()) {
            cadastrarUsuario(new Administrador("Admin", "admin", "admin"));
            salvarUsuarios();
        }
    }
    
    /**
     * Cadastra um novo usuário no sistema
     * @param usuario Objeto Usuario a ser cadastrado
     * @return true se cadastrado com sucesso, false se username já existe
     */
    public boolean cadastrarUsuario(Usuario usuario) {
        // Verifica se o username já existe (uso de métodos da classe String)
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(usuario.getUsername())) {
                return false; // Username já existe
            }
        }
        
        // Adiciona o usuário à lista
        usuarios.add(usuario);
        // Salva no arquivo
        salvarUsuarios();
        return true;
    }
    
    public Usuario fazerLogin(String username, String senha) {
        // Percorre a lista de usuários buscando correspondência
        for (Usuario u : usuarios) {
            // Uso de métodos da classe String: equals()
            if (u.getUsername().equals(username) && u.getSenha().equals(senha)) {
                return u; // Login bem-sucedido - retorna o usuário
            }
        }
        return null; // Login falhou
    }
    
    /**
     * Salva todos os usuários em um arquivo de texto
     * Cada linha do arquivo representa um usuário
     */
    private void salvarUsuarios() {
        try {
            // FileWriter - classe para escrever em arquivos
            FileWriter fw = new FileWriter(ARQUIVO_USUARIOS);
            BufferedWriter bw = new BufferedWriter(fw);
            
            // Percorre todos os usuários e salva no arquivo
            for (Usuario u : usuarios) {
                // Usa o método paraArquivo() de cada usuário
                bw.write(u.paraArquivo());
                bw.newLine(); // Pula para próxima linha
            }
            
            bw.close(); // Fecha o arquivo
            fw.close();
            
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }
    
    /**
     * Carrega os usuários do arquivo de texto
     * Demonstra leitura de arquivo e uso de métodos da classe String
     */
    private void carregarUsuarios() {
        File arquivo = new File(ARQUIVO_USUARIOS);
        
        // Verifica se o arquivo existe
        if (!arquivo.exists()) {
            return; // Arquivo não existe, nada a carregar
        }
        
        try {
            FileReader fr = new FileReader(arquivo);
            BufferedReader br = new BufferedReader(fr);
            
            String linha;
            // Lê linha por linha do arquivo
            while ((linha = br.readLine()) != null) {
                // Split - método da classe String para dividir a string
                String[] dados = linha.split("\\|");
                
                // dados[0] = tipo, dados[1] = username, dados[2] = senha, dados[3] = nome
                String tipo = dados[0];
                String username = dados[1];
                String senha = dados[2];
                String nome = dados[3];
                
                // Cria o objeto apropriado baseado no tipo 
                Usuario usuario = null;
                
                if (tipo.equals("Administrador")) {
                    usuario = new Administrador(nome, username, senha);
                } else if (tipo.equals("Bibliotecario")) {
                    usuario = new Bibliotecario(nome, username, senha);
                } else if (tipo.equals("Leitor")) {
                    usuario = new Leitor(nome, username, senha);
                }
                
                if (usuario != null) {
                    usuarios.add(usuario);
                }
            }
            
            br.close();
            fr.close();
            
        } catch (IOException e) {
            System.err.println("Erro ao carregar usuários: " + e.getMessage());
        }
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }
    
    public boolean removerUsuario(String username) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getUsername().equals(username)) {
                usuarios.remove(i);
                salvarUsuarios();
                return true;
            }
        }
        return false;
    }
}   

