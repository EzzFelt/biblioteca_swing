/**
 * Classe Livro - representa um livro no sistema
 */
public class Livro {
    private String id;             
    private String titulo;          
    private String autor;          
    private String ano;            
    private boolean disponivel;     
    private String emprestadoPara;  
    
    /**
     * Construtor completo
     */
    public Livro(String id, String titulo, String autor, String ano, boolean disponivel, String emprestadoPara) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.disponivel = disponivel;
        this.emprestadoPara = emprestadoPara;
    }
    
    /**
     * Construtor para novo livro (sempre disponível)
     */
    public Livro(String id, String titulo, String autor, String ano) {
        this(id, titulo, autor, ano, true, null);
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public String getAutor() {
        return autor;
    }
    
    public String getAno() {
        return ano;
    }
    
    public boolean isDisponivel() {
        return disponivel;
    }
    
    public String getEmprestadoPara() {
        return emprestadoPara;
    }
    
    // Setters
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public void setAutor(String autor) {
        this.autor = autor;
    }
    
    public void setAno(String ano) {
        this.ano = ano;
    }
    
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
    
    public void setEmprestadoPara(String emprestadoPara) {
        this.emprestadoPara = emprestadoPara;
    }
    
    /**
     * Empresta o livro para um usuário
     */
    public void emprestar(String username) {
        this.disponivel = false;
        this.emprestadoPara = username;
    }
    
    /**
     * Devolve o livro
     */
    public void devolver() {
        this.disponivel = true;
        this.emprestadoPara = null;
    }
    
    /**
     * Retorna o status do livro
     */
    public String getStatus() {
        if (disponivel) {
            return "Disponível";
        } else {
            return "Emprestado para: " + emprestadoPara;
        }
    }
    
    /**
     * Converte para string para salvar em arquivo
     * Formato: id|titulo|autor|ano|disponivel|emprestadoPara
     */
    public String paraArquivo() {
        String emprestado = (emprestadoPara == null) ? "" : emprestadoPara;
        return id + "|" + titulo + "|" + autor + "|" + ano + "|" + disponivel + "|" + emprestado;
    }
    
    /**
     * Retorna representação em string para exibição
     */
    @Override
    public String toString() {
        return titulo + " - " + autor + " (" + ano + ") - " + getStatus();
    }
    
    /**
     * Retorna descrição completa do livro
     */
    public String getDescricaoCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append("\n");
        sb.append("Título: ").append(titulo).append("\n");
        sb.append("Autor: ").append(autor).append("\n");
        sb.append("Ano: ").append(ano).append("\n");
        sb.append("Status: ").append(getStatus()).append("\n");
        return sb.toString();
    }
}