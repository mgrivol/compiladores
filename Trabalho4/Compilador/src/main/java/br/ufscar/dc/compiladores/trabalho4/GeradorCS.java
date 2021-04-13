package br.ufscar.dc.compiladores.trabalho4;

import br.ufscar.dc.compiladores.trabalho4.SemanticoUtils.Inimigo;
import br.ufscar.dc.compiladores.trabalho4.SemanticoUtils.SemanticoUtils;
import br.ufscar.dc.compiladores.trabalho4.SemanticoUtils.TabelaDeSimbolos;

public class GeradorCS extends TGENBaseVisitor<Void> {

    StringBuilder saida;
    TabelaDeSimbolos ts;

    public GeradorCS(TabelaDeSimbolos ts) {
        saida = new StringBuilder();
        this.ts = ts;
    }

    @Override
    public Void visitPrograma(TGENParser.ProgramaContext ctx) {
        // criando o código básico do arquivo
        saida.append(
                "using UnityEngine;\n\n"
                + "public class GameScenarioCompiler {\n\n"
                + "    EnemyFactory factory;\n"
                + "    EnemyWaveCompiler[] waves;\n"
                + "    public State Begin() => new State(this);\n\n"
                + "    public struct State {\n\n"
                + "        GameScenarioCompiler scenario;\n"
                + "        int index;\n"
                + "        EnemyWaveCompiler.State wave;\n\n"
                + "        public State(GameScenarioCompiler scenario) {\n"
                + "            this.scenario = scenario;\n"
                + "            index = 0;\n"
                + "            Debug.Assert(scenario.waves.Length > 0, \"Empty scenario!\");\n"
                + "            wave = scenario.waves[0].Begin();\n"
                + "        }\n\n"
                + "        public bool Progress() {\n"
                + "            float deltaTime = wave.Progress(Time.deltaTime);\n"
                + "            while (deltaTime >= 0f) {\n"
                + "                if (++index >= scenario.waves.Length) {\n"
                + "                    return false;\n"
                + "                }\n"
                + "                wave = scenario.waves[index].Begin();\n"
                + "                deltaTime = wave.Progress(deltaTime);\n"
                + "            }\n"
                + "            return true;\n"
                + "        }\n"
                + "    }\n"
        );
        // criando o código dos inimigos
        visitInimigos(ctx.inimigos());
        // criando o código das ondas
        visitOndas(ctx.ondas());
        saida.append("}\n\n");
        return null;
    }

    @Override
    public Void visitInimigos(TGENParser.InimigosContext ctx) {
        // delay poderá ou não ser usado
        saida.append("   EnemyConfig Delay = new EnemyConfig(0f, 0f, 0, EnemyType.Delay);\n");
        for (var i : ctx.inimigo()) {
            Inimigo inimigo = SemanticoUtils.verificaInimigo(i);
            saida.append(
                    "   EnemyConfig " + inimigo.getNome() + " = new EnemyConfig("
                    + inimigo.getVida() + "f, "
                    + inimigo.getVelocidade() + "f, "
                    + inimigo.getForca() + ", "
                    + inimigo.getModelo()
                    + ");\n"
            );
        }
        saida.append("\n");
        return null;
    }

    @Override
    public Void visitOndas(TGENParser.OndasContext ctx) {
        saida.append("    public GameScenarioCompiler(EnemyFactory factory) {\n"
                + "        this.factory = factory;\n"
                + "        waves = new EnemyWaveCompiler[" + ctx.onda().size() + "];\n"
        );
        
        // define cada onda
        int indexOnda = 0;
        for (var onda : ctx.onda()) {
            // define a quantidade de comandos em cada onda
            saida.append("        waves[" + indexOnda + "] = new EnemyWaveCompiler(" 
                    + "factory, " + onda.comando().size() + ");\n"
            );
            // define o comando de cada onda
            int indexCmd = 0;
            for (var cmd : onda.comando()) {
                saida.append("        waves[" + indexOnda + "].setSpawnSequence(" + indexCmd + ", ");
                visitComando(cmd);
                saida.append(");\n");
                indexCmd++;
            }
            indexOnda++;
        }
        saida.append("  }\n");
        return null;
    }
    
    @Override
    public Void visitComando(TGENParser.ComandoContext ctx) {
        if (ctx.cmdSpawn() != null) {
            // comando para nascer inimigos
            visitCmdSpawn(ctx.cmdSpawn());
            return null;
        }
        // comando aguarde()
        saida.append(ctx.cmdAguarde().FLOAT().getText() + "f, 1, factory, Delay");
        return null;
    }
    
    @Override
    public Void visitCmdSpawn(TGENParser.CmdSpawnContext ctx) {
        // visita o comando spawn para nascer inimigos
        if (ctx.semDelay() != null) {
            // nascer sem delay
            saida.append("1f, " 
                    + ctx.semDelay().INT().getText()
                    + ", factory" 
                    + ", " + ctx.semDelay().IDENT().getText()
            );
            return null;
        }
        // nascer com delay
        saida.append(ctx.comDelay().FLOAT().getText()
                + "f, " + ctx.comDelay().INT().getText()
                + ", factory"
                + ", " + ctx.comDelay().IDENT().getText()
        );
        return null;
    }
}
