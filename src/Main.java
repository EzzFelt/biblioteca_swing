import javax.swing.UIManager;

public class Main {
    
    /**
     * Método main - ponto de entrada do programa
     * @param args Argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        // Define o Look and Feel do sistema operacional
        // Isso faz a interface parecer mais nativa
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Se falhar, usa o look and feel padrão do Java
            System.err.println("Erro ao definir Look and Feel: " + e.getMessage());
        }
        
        // Exibe mensagem de boas-vindas no console
        System.out.println("=".repeat(50));
        System.out.println("Sistema de Gerenciamento de Biblioteca");
        System.out.println("Versão 1.0");
        System.out.println("=".repeat(50));
        System.out.println();

        GerenciadorUsuarios gerenciador = new GerenciadorUsuarios();
        GerenciadorLivros gerenciadorLivros = new GerenciadorLivros();
        
        System.out.println("Gerenciador de usuários inicializado.");
        System.out.println("Usuários cadastrados: " + gerenciador.getUsuarios().size());
        System.out.println();
        
        // Exibe informações sobre o usuário admin padrão
        System.out.println("INFORMAÇÃO IMPORTANTE:");
        System.out.println("Um usuário administrador padrão foi criado:");
        System.out.println("  Username: admin");
        System.out.println("  Senha: admin");
        System.out.println();
        System.out.println("Use estas credenciais para o primeiro acesso.");
        System.out.println("Você pode criar outros usuários através da tela de cadastro.");
        System.out.println("=".repeat(50));
        System.out.println();
        
        // SwingUtilities.invokeLater garante que a interface gráfica
        // seja criada na EDT (Event Dispatch Thread)
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Cria e exibe a tela de login
                TelaLogin telaLogin = new TelaLogin(gerenciador, gerenciadorLivros);     
                telaLogin.setVisible(true);
                
                System.out.println("Tela de login aberta.");
                System.out.println("Aguardando autenticação do usuário...");
            }
        });
    }
}