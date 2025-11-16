/**
 * Classe Bibliotecario - herda de Usuario
 */
public class Bibliotecario extends Usuario {
    
    // Atributo específico do Bibliotecário
    private String setor;
    
    /**
     * Construtor do Bibliotecario
     */
    public Bibliotecario(String nome, String username, String senha) {
        super(nome, username, senha);
        this.tipoUsuario = "Bibliotecario";
        this.setor = "Geral"; // Setor padrão
    }
    
    /**
     * Implementação do método abstrato getPermissoes()
     * Bibliotecário tem permissões intermediárias
     */
    @Override
    public String[] getPermissoes() {
        return new String[] {
            "Cadastrar livros",
            "Editar livros",
            "Remover livros",
            "Registrar empréstimos",
            "Registrar devoluções",
            "Consultar leitores",
            "Gerar relatórios de empréstimos"
        };
    }

    @Override
    public String getDescricao() {
        return "Bibliotecário responsável pelo gerenciamento de livros e empréstimos";
    }
    
    /**
     * Métodos Bibliotecário
     */
    public void registrarEmprestimo(String livro, String leitor) {
        System.out.println("Empréstimo registrado: " + livro + " para " + leitor);
    }
    
    public void registrarDevolucao(String livro) {
        System.out.println("Devolução registrada: " + livro);
    }
    
    // Getter e Setter para o setor
    public String getSetor() {
        return setor;
    }
    
    public void setSetor(String setor) {
        this.setor = setor;
    }
}