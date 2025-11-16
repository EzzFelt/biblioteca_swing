import java.io.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * GerenciadorLivros - Gerencia livros e empréstimos
 * Salva dados em arquivos
 */
public class GerenciadorLivros {
    private ArrayList<Livro> livros;
    private static final String ARQUIVO_LIVROS = "livros.txt";
    private static final String ARQUIVO_EMPRESTIMOS = "historico_emprestimos.txt";
    private int proximoId = 1;
    
    /**
     * Construtor - carrega livros do arquivo
     */
    public GerenciadorLivros() {
        livros = new ArrayList<>();
        carregarLivros();
        atualizarProximoId();
        
        // Se não houver livros, cria alguns de exemplo
        if (livros.isEmpty()) {
            criarLivrosExemplo();
        }
    }
    
    /**
     * Cria livros de exemplo
     */
    private void criarLivrosExemplo() {
        cadastrarLivro(new Livro("1", "Dom Casmurro", "Machado de Assis", "1899"));
        cadastrarLivro(new Livro("2", "O Cortiço", "Aluísio Azevedo", "1890"));
        cadastrarLivro(new Livro("3", "Grande Sertão: Veredas", "Guimarães Rosa", "1956"));
        cadastrarLivro(new Livro("4", "Capitães da Areia", "Jorge Amado", "1937"));
        cadastrarLivro(new Livro("5", "Memórias Póstumas de Brás Cubas", "Machado de Assis", "1881"));
        proximoId = 6;
    }
    
    /**
     * Atualiza o próximo ID baseado nos livros existentes
     */
    private void atualizarProximoId() {
        int maxId = 0;
        for (Livro livro : livros) {
            try {
                int id = Integer.parseInt(livro.getId());
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
                // Ignora IDs não numéricos
            }
        }
        proximoId = maxId + 1;
    }
    
    /**
     * Cadastra um novo livro
     */
    public boolean cadastrarLivro(Livro livro) {
        livros.add(livro);
        salvarLivros();
        return true;
    }
    
    /**
     * Cadastra livro gerando ID automaticamente
     */
    public boolean cadastrarLivro(String titulo, String autor, String ano) {
        String id = String.valueOf(proximoId++);
        Livro livro = new Livro(id, titulo, autor, ano);
        return cadastrarLivro(livro);
    }
    
    /**
     * Remove um livro pelo ID
     */
    public boolean removerLivro(String id) {
        for (int i = 0; i < livros.size(); i++) {
            if (livros.get(i).getId().equals(id)) {
                livros.remove(i);
                salvarLivros();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Busca livro por ID
     */
    public Livro buscarLivroPorId(String id) {
        for (Livro livro : livros) {
            if (livro.getId().equals(id)) {
                return livro;
            }
        }
        return null;
    }
    
    /**
     * Busca livros por título (busca parcial)
     */
    public ArrayList<Livro> buscarLivrosPorTitulo(String titulo) {
        ArrayList<Livro> resultado = new ArrayList<>();
        String buscaLower = titulo.toLowerCase();
        
        for (Livro livro : livros) {
            if (livro.getTitulo().toLowerCase().contains(buscaLower)) {
                resultado.add(livro);
            }
        }
        return resultado;
    }
    
    /**
     * Retorna todos os livros
     */
    public ArrayList<Livro> getLivros() {
        return livros;
    }
    
    /**
     * Retorna apenas livros disponíveis
     */
    public ArrayList<Livro> getLivrosDisponiveis() {
        ArrayList<Livro> disponiveis = new ArrayList<>();
        for (Livro livro : livros) {
            if (livro.isDisponivel()) {
                disponiveis.add(livro);
            }
        }
        return disponiveis;
    }
    
    /**
     * Retorna livros emprestados para um usuário específico
     */
    public ArrayList<Livro> getLivrosEmprestadosPara(String username) {
        ArrayList<Livro> emprestados = new ArrayList<>();
        for (Livro livro : livros) {
            if (!livro.isDisponivel() && username.equals(livro.getEmprestadoPara())) {
                emprestados.add(livro);
            }
        }
        return emprestados;
    }
    
    /**
     * Realiza empréstimo de um livro
     */
    public boolean emprestarLivro(String idLivro, String username) {
        Livro livro = buscarLivroPorId(idLivro);
        
        if (livro == null) {
            return false; // Livro não encontrado
        }
        
        if (!livro.isDisponivel()) {
            return false; // Livro já emprestado
        }
        
        // Realiza o empréstimo
        livro.emprestar(username);
        salvarLivros();
        
        // Registra no histórico
        registrarHistorico("EMPRÉSTIMO", livro, username);
        
        return true;
    }
    
    /**
     * Realiza devolução de um livro
     */
    public boolean devolverLivro(String idLivro) {
        Livro livro = buscarLivroPorId(idLivro);
        
        if (livro == null) {
            return false; // Livro não encontrado
        }
        
        if (livro.isDisponivel()) {
            return false; // Livro já está disponível
        }
        
        String username = livro.getEmprestadoPara();
        
        // Realiza a devolução
        livro.devolver();
        salvarLivros();
        
        // Registra no histórico
        registrarHistorico("DEVOLUÇÃO", livro, username);
        
        return true;
    }
    
    /**
     * Conta quantos livros um usuário tem emprestado
     */
    public int contarEmprestimosPorUsuario(String username) {
        int count = 0;
        for (Livro livro : livros) {
            if (!livro.isDisponivel() && username.equals(livro.getEmprestadoPara())) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Registra operação no histórico
     */
    private void registrarHistorico(String tipo, Livro livro, String username) {
        try {
            FileWriter fw = new FileWriter(ARQUIVO_EMPRESTIMOS, true); // append
            BufferedWriter bw = new BufferedWriter(fw);
            
            String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            
            String registro = timestamp + " | " + tipo + " | " + 
                            "Livro: " + livro.getTitulo() + " | " +
                            "Usuário: " + username;
            
            bw.write(registro);
            bw.newLine();
            
            bw.close();
            fw.close();
            
        } catch (IOException e) {
            System.err.println("Erro ao registrar histórico: " + e.getMessage());
        }
    }
    
    /**
     * Salva todos os livros no arquivo
     */
    private void salvarLivros() {
        try {
            FileWriter fw = new FileWriter(ARQUIVO_LIVROS);
            BufferedWriter bw = new BufferedWriter(fw);
            
            for (Livro livro : livros) {
                bw.write(livro.paraArquivo());
                bw.newLine();
            }
            
            bw.close();
            fw.close();
            
        } catch (IOException e) {
            System.err.println("Erro ao salvar livros: " + e.getMessage());
        }
    }
    
    /**
     * Carrega livros do arquivo
     */
    private void carregarLivros() {
        File arquivo = new File(ARQUIVO_LIVROS);
        
        if (!arquivo.exists()) {
            return;
        }
        
        try {
            FileReader fr = new FileReader(arquivo);
            BufferedReader br = new BufferedReader(fr);
            
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split("\\|");
                
                // dados[0]=id, dados[1]=titulo, dados[2]=autor, dados[3]=ano, 
                // dados[4]=disponivel, dados[5]=emprestadoPara
                if (dados.length >= 4) {
                    String id = dados[0];
                    String titulo = dados[1];
                    String autor = dados[2];
                    String ano = dados[3];
                    boolean disponivel = dados.length >= 5 ? Boolean.parseBoolean(dados[4]) : true;
                    String emprestadoPara = (dados.length >= 6 && !dados[5].isEmpty()) ? dados[5] : null;
                    
                    Livro livro = new Livro(id, titulo, autor, ano, disponivel, emprestadoPara);
                    livros.add(livro);
                }
            }
            
            br.close();
            fr.close();
            
        } catch (IOException e) {
            System.err.println("Erro ao carregar livros: " + e.getMessage());
        }
    }
    
    /**
     * Retorna estatísticas dos livros
     */
    public String getEstatisticas() {
        int total = livros.size();
        int disponiveis = getLivrosDisponiveis().size();
        int emprestados = total - disponiveis;
        
        return "Total de livros: " + total + "\n" +
               "Disponíveis: " + disponiveis + "\n" +
               "Emprestados: " + emprestados;
    }
}