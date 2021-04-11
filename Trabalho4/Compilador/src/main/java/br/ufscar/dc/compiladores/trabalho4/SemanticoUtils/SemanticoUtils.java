package br.ufscar.dc.compiladores.trabalho4.SemanticoUtils;

import br.ufscar.dc.compiladores.trabalho4.TGENParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SemanticoUtils {

    public static List<String> erros = new ArrayList<>();
    
    public static void adicionaErro(String msg) {
        erros.add(msg);
    }
    
    public static Inimigo verificaInimigo(TGENParser.InimigoContext ctx) {
        // verifica se um inimigo é válido
        String nomeInimigo = ctx.IDENT().getText();
        Inimigo ret = new Inimigo(nomeInimigo);
        for (var parametro : ctx.parametroInimigo()) {
            // verifica cada parâmetro do inimigo
            if (parametro.parInimigoForca() != null) {
                // parâmetro força
                ret.setForca(verificaParInimigoForca(parametro.parInimigoForca()));
            } else if (parametro.parInimigoVelocidade() != null) {
                // parâmetro velocidade
                ret.setVelocidade(verificaParInimigoVelocidade(parametro.parInimigoVelocidade()));
            } else if (parametro.parInimigoVida() != null) {
                // parâmetro vida
                ret.setVida(verificaParInimigoVida(parametro.parInimigoVida()));
            } else if (parametro.parInimigoModelo() != null) {;
                // parâmetro modelo
                ret.setModelo(verificaParInimigoModelo(parametro.parInimigoModelo()));
            }
        }
        return ret;
    }
    
    public static int verificaParInimigoForca(TGENParser.ParInimigoForcaContext ctx) {
        // verifica se a força declarada é válida
        int forca = Integer.parseInt(ctx.INT().getText());
        if (forca == 0) {
            adicionaErro(String.format(
                    "Erro linha %d: forca deve ser maior que 0", ctx.start.getLine()
            ));
            return -1;
        }
        else if (forca > 100) {
            adicionaErro(String.format(
                    "Erro linha %d: forca deve ser menor ou igual a 100", ctx.start.getLine()
            ));
            return -1;
        }
        return forca;
    }
    
    public static float verificaParInimigoVelocidade(TGENParser.ParInimigoVelocidadeContext ctx) {
        // verifica limites inferiores e superiores
        float velocidade = Float.parseFloat(ctx.FLOAT().getText());
        if (velocidade < 0.1f) {
            adicionaErro(String.format(
                    "Erro linha %d: velocidade deve ser maior ou igual a 0.1", ctx.start.getLine()
            ));
            return -1f;
        }
        else if (velocidade > 4f) {
            adicionaErro(String.format(
                    "Erro linha %d: velocidade deve ser menor ou igual a 4", ctx.start.getLine()
            ));
            return -1f;
        }
        return velocidade;
    }
    
    public static float verificaParInimigoVida(TGENParser.ParInimigoVidaContext ctx) {
        // verifica limites inferiores e superiores
        int vida = Integer.parseInt(ctx.INT().getText());
        if (vida == 0) {
            adicionaErro(String.format(
                    "Erro linha %d: vida deve ser maior que 0", ctx.start.getLine()
            ));
            return -1f;
        }
        else if (vida > 1000) {
            adicionaErro(String.format(
                    "Erro linha %d: vida deve ser menor que 1000", ctx.start.getLine()
            ));
            return -1f;
        }
        return vida;
    }
    
    public static String verificaParInimigoModelo(TGENParser.ParInimigoModeloContext ctx) {
        // verifica se o modelo declarado é válido
        String cadeia = ctx.CADEIA().getText().substring(1, ctx.CADEIA().getText().length() - 1);
        switch (cadeia) {
            case "PEQUENO":
                return cadeia;
            case "MEDIO":
                return cadeia;
            case "GRANDE":
                return cadeia;
            default:
                adicionaErro(String.format(
                        "Erro linha %d: modelo %s nao e valido", 
                        ctx.start.getLine(), cadeia
                ));
                break;
        }
        return null;
    }
}