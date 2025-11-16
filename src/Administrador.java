/**
 * Classe Administrador - herda de Usuario
 */
public class Administrador extends Usuario {
    
    /**
     * Construtor do Administrador
     * Chama o construtor da classe pai (Usuario) usando super()
     */
    public Administrador(String nome, String username, String senha) {
        super(nome, username, senha); // Chama o construtor da classe pai
        this.tipoUsuario = "Administrador"; // Define o tipo específico
    }
    
    @Override
    public String[] getPermissoes() {
        // Array de Strings (uso da classe String)
        return new String[] {
            "Cadastrar usuários",
            "Remover usuários",
            "Editar usuários",
            "Visualizar todos os usuários",
            "Gerenciar sistema",
            "Cadastrar livros",
            "Remover livros",
            "Gerar relatórios"
        };
    }
    
    @Override
    public String getDescricao() {
        return "Administrador do sistema com permissões totais";
    }
    
    public void gerarRelatorio() {
        System.out.println("Gerando relatório completo do sistema...");
    }
}