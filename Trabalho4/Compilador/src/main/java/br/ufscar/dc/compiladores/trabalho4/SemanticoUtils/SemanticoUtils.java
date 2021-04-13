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
                    "Linha %d: forca deve ser maior que 0", ctx.start.getLine()
            ));
            return -1;
        }
        else if (forca > 100) {
            adicionaErro(String.format(
                    "Linha %d: forca deve ser menor ou igual a 100", ctx.start.getLine()
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
                    "Linha %d: velocidade deve ser maior ou igual a 0.1", ctx.start.getLine()
            ));
            return -1f;
        }
        else if (velocidade > 4f) {
            adicionaErro(String.format(
                    "Linha %d: velocidade deve ser menor ou igual a 4.0", ctx.start.getLine()
            ));
            return -1f;
        }
        return velocidade;
    }
    
    public static float verificaParInimigoVida(TGENParser.ParInimigoVidaContext ctx) {
        // verifica limites inferiores e superiores
        float vida = Float.parseFloat(ctx.FLOAT().getText());
        if (vida == 0f) {
            adicionaErro(String.format(
                    "Linha %d: vida deve ser maior ou igual a 10.0", ctx.start.getLine()
            ));
            return -1f;
        }
        else if (vida > 1000f) {
            adicionaErro(String.format(
                    "Linha %d: vida deve ser menor ou igual a 1000.0", ctx.start.getLine()
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
                        "Linha %d: modelo %s nao e valido", 
                        ctx.start.getLine(), cadeia
                ));
                break;
        }
        return null;
    }
    
    public static void verificaOnda(TGENParser.OndaContext ctx, TabelaDeSimbolos ts) {
        // verifica os comandos da onda
        for (var cmd : ctx.comando()) {
            // verifica cada comando
            if (cmd.cmdSpawn() != null) {
                // comando spawn
                verificaCmdSpawn(cmd.cmdSpawn(), ts);
            }
            else {
                // comando aguarde
                verificaCmdAguarde(cmd.cmdAguarde());
            }
        }
    }
    
    public static void verificaCmdSpawn(TGENParser.CmdSpawnContext ctx, TabelaDeSimbolos ts) {
        // verifica o comando spawn
        if (ctx.semDelay() != null) {
            // comando spawn sem delay
            String nomeInimigo = ctx.semDelay().IDENT().getText();
            int quantidade = Integer.parseInt(ctx.semDelay().INT().getText());
            verificaInimigoQuantidade(nomeInimigo, quantidade, ctx.start.getLine(), ts);
            return;
        }
        // comando spawn com delay
        String nomeInimigo = ctx.comDelay().IDENT().getText();
        int quantidade = Integer.parseInt(ctx.comDelay().INT().getText());
        float delay = Float.parseFloat(ctx.comDelay().FLOAT().getText());
        verificaInimigoQuantidade(nomeInimigo, quantidade, ctx.start.getLine(), ts);
        verificaDelay(delay, ctx.start.getLine());
    }
    
    public static void verificaCmdAguarde(TGENParser.CmdAguardeContext ctx) {
        // verifica o comando aguarde
        float delay = Float.parseFloat(ctx.FLOAT().getText());
        verificaDelay(delay, ctx.start.getLine());
    }
    
    public static void verificaInimigoQuantidade(String nomeInimigo, int quantidade, int line, TabelaDeSimbolos ts) {
        // verifica se inimigo existe e quantidade é válida
        if (!ts.existe(nomeInimigo)) {
            // inimigo nao declarado
            adicionaErro(String.format(
                    "Linha %d: inimigo %s nao declarado",
                    line, nomeInimigo
            ));
        }
        if (quantidade <= 0) {
            // quantidade invalida
            adicionaErro(String.format(
                    "Linha %d: quantidade de inimigos deve ser maior que 0",
                    line
            ));
        }
    }
    
    public static void verificaDelay(float delay, int line) {
        if (delay <= 0f) {
            adicionaErro(String.format(
                    "Linha %d: tempo deve ser maior que 0.0",
                    line
            ));
        }
    }
}