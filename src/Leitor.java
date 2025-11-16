/**
 * Classe Leitor - herda de Usuario
 */
public class Leitor extends Usuario {
    
    // Atributos específicos do Leitor
    private int livrosEmprestados;
    private static final int LIMITE_EMPRESTIMOS = 3; // Constante
    
    /**
     * Construtor do Leitor
     */
    public Leitor(String nome, String username, String senha) {
        super(nome, username, senha);
        this.tipoUsuario = "Leitor";
        this.livrosEmprestados = 0;
    }
    
    /**
     * Implementação do método abstrato getPermissoes()
     * Leitor tem permissões limitadas
     */
    @Override
    public String[] getPermissoes() {
        return new String[] {
            "Consultar livros disponíveis",
            "Reservar livros",
            "Visualizar meus empréstimos",
            "Renovar empréstimos",
            "Alterar minha senha"
        };
    }
    
    /**
     * Implementação do método abstrato getDescricao()
     */
    @Override
    public String getDescricao() {
        return "Leitor da biblioteca com " + livrosEmprestados + " livro(s) emprestado(s)";
    }
    
    /**
     * Métodos específicos do Leitor
     */
    public boolean podeEmprestar() {
        // Verifica se o leitor pode pegar mais livros emprestados
        return livrosEmprestados < LIMITE_EMPRESTIMOS;
    }
    
    public void emprestarLivro() {
        if (podeEmprestar()) {
            livrosEmprestados++;
            System.out.println("Livro emprestado com sucesso!");
        } else {
            System.out.println("Limite de empréstimos atingido!");
        }
    }
    
    public void devolverLivro() {
        if (livrosEmprestados > 0) {
            livrosEmprestados--;
            System.out.println("Livro devolvido com sucesso!");
        }
    }
    
    // Getters
    public int getLivrosEmprestados() {
        return livrosEmprestados;
    }
    
    public int getLimiteEmprestimos() {
        return LIMITE_EMPRESTIMOS;
    }
}