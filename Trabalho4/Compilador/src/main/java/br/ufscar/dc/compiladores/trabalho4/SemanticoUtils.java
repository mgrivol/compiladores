package br.ufscar.dc.compiladores.trabalho4;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SemanticoUtils {

    public static List<String> erros = new ArrayList<>();
    
    public static void adicionaErro(String msg) {
        erros.add(msg);
    }
    
    public static boolean verificaInimigo(TGENParser.InimigoContext ctx, Set<String> ts) {
        // verifica se um inimigo é válido
        String nomeInimigo = ctx.IDENT().getText();
        if (ts.contains(nomeInimigo)) {
            // inimigo já existe
            adicionaErro(String.format("Erro linha %d: inimigo ja declarado", ctx.start.getLine()));
            return false;
        }
        for (var parametro : ctx.parametroInimigo()) {
            // verifica cada parâmetro do inimigo
            verificaParametroInimigo(parametro);
        }
        return true;
    }
    
    public static void verificaParametroInimigo(TGENParser.ParametroInimigoContext ctx) {
        // verifica o parâmetro do inimigo
        
    }
}