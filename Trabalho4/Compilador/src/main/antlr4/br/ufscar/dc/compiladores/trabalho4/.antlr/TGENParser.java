// Generated from c:\Users\marco\Desktop\compilers\compiladores\Trabalho4\Compilador\src\main\antlr4\br\u005Cufscar\dc\compiladores\trabalho4\TGEN.g4 by ANTLR 4.8
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TGENParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, INT=16, FLOAT=17, 
		IDENT=18, WS=19, COMENTARIO=20, ERRO_DESCONHECIDO=21;
	public static final int
		RULE_programa = 0, RULE_inimigos = 1, RULE_inimigo = 2, RULE_parametroInimigo = 3, 
		RULE_parInimigoForca = 4, RULE_parInimigoVelocidade = 5, RULE_parInimigoVida = 6, 
		RULE_ondas = 7, RULE_onda = 8, RULE_comando = 9, RULE_cmdSpawn = 10, RULE_semDelay = 11, 
		RULE_comDelay = 12, RULE_cmdAguarde = 13;
	private static String[] makeRuleNames() {
		return new String[] {
			"programa", "inimigos", "inimigo", "parametroInimigo", "parInimigoForca", 
			"parInimigoVelocidade", "parInimigoVida", "ondas", "onda", "comando", 
			"cmdSpawn", "semDelay", "comDelay", "cmdAguarde"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'start'", "'end'", "'inimigos'", "'{'", "'}'", "';'", "'forca'", 
			"'='", "'velocidade'", "'vida'", "'onda'", "'('", "')'", "','", "'aguarde'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, "INT", "FLOAT", "IDENT", "WS", "COMENTARIO", 
			"ERRO_DESCONHECIDO"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "TGEN.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TGENParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgramaContext extends ParserRuleContext {
		public InimigosContext inimigos() {
			return getRuleContext(InimigosContext.class,0);
		}
		public OndasContext ondas() {
			return getRuleContext(OndasContext.class,0);
		}
		public ProgramaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_programa; }
	}

	public final ProgramaContext programa() throws RecognitionException {
		ProgramaContext _localctx = new ProgramaContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_programa);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(28);
			inimigos();
			setState(29);
			match(T__0);
			setState(30);
			ondas();
			setState(31);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InimigosContext extends ParserRuleContext {
		public List<InimigoContext> inimigo() {
			return getRuleContexts(InimigoContext.class);
		}
		public InimigoContext inimigo(int i) {
			return getRuleContext(InimigoContext.class,i);
		}
		public InimigosContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inimigos; }
	}

	public final InimigosContext inimigos() throws RecognitionException {
		InimigosContext _localctx = new InimigosContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_inimigos);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(33);
			match(T__2);
			setState(34);
			match(T__3);
			setState(36); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(35);
				inimigo();
				}
				}
				setState(38); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==IDENT );
			setState(40);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InimigoContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(TGENParser.IDENT, 0); }
		public List<ParametroInimigoContext> parametroInimigo() {
			return getRuleContexts(ParametroInimigoContext.class);
		}
		public ParametroInimigoContext parametroInimigo(int i) {
			return getRuleContext(ParametroInimigoContext.class,i);
		}
		public InimigoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inimigo; }
	}

	public final InimigoContext inimigo() throws RecognitionException {
		InimigoContext _localctx = new InimigoContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_inimigo);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			match(IDENT);
			setState(43);
			match(T__3);
			setState(49);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__8) | (1L << T__9))) != 0)) {
				{
				{
				setState(44);
				parametroInimigo();
				setState(45);
				match(T__5);
				}
				}
				setState(51);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(52);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParametroInimigoContext extends ParserRuleContext {
		public ParInimigoForcaContext parInimigoForca() {
			return getRuleContext(ParInimigoForcaContext.class,0);
		}
		public ParInimigoVelocidadeContext parInimigoVelocidade() {
			return getRuleContext(ParInimigoVelocidadeContext.class,0);
		}
		public ParInimigoVidaContext parInimigoVida() {
			return getRuleContext(ParInimigoVidaContext.class,0);
		}
		public ParametroInimigoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parametroInimigo; }
	}

	public final ParametroInimigoContext parametroInimigo() throws RecognitionException {
		ParametroInimigoContext _localctx = new ParametroInimigoContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_parametroInimigo);
		try {
			setState(57);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__6:
				enterOuterAlt(_localctx, 1);
				{
				setState(54);
				parInimigoForca();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 2);
				{
				setState(55);
				parInimigoVelocidade();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 3);
				{
				setState(56);
				parInimigoVida();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParInimigoForcaContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(TGENParser.INT, 0); }
		public ParInimigoForcaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parInimigoForca; }
	}

	public final ParInimigoForcaContext parInimigoForca() throws RecognitionException {
		ParInimigoForcaContext _localctx = new ParInimigoForcaContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_parInimigoForca);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			match(T__6);
			setState(60);
			match(T__7);
			setState(61);
			match(INT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParInimigoVelocidadeContext extends ParserRuleContext {
		public TerminalNode FLOAT() { return getToken(TGENParser.FLOAT, 0); }
		public ParInimigoVelocidadeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parInimigoVelocidade; }
	}

	public final ParInimigoVelocidadeContext parInimigoVelocidade() throws RecognitionException {
		ParInimigoVelocidadeContext _localctx = new ParInimigoVelocidadeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_parInimigoVelocidade);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
			match(T__8);
			setState(64);
			match(T__7);
			setState(65);
			match(FLOAT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParInimigoVidaContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(TGENParser.INT, 0); }
		public ParInimigoVidaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parInimigoVida; }
	}

	public final ParInimigoVidaContext parInimigoVida() throws RecognitionException {
		ParInimigoVidaContext _localctx = new ParInimigoVidaContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_parInimigoVida);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
			match(T__9);
			setState(68);
			match(T__7);
			setState(69);
			match(INT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OndasContext extends ParserRuleContext {
		public List<OndaContext> onda() {
			return getRuleContexts(OndaContext.class);
		}
		public OndaContext onda(int i) {
			return getRuleContext(OndaContext.class,i);
		}
		public OndasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ondas; }
	}

	public final OndasContext ondas() throws RecognitionException {
		OndasContext _localctx = new OndasContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_ondas);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(71);
				onda();
				}
				}
				setState(74); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__10 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OndaContext extends ParserRuleContext {
		public List<ComandoContext> comando() {
			return getRuleContexts(ComandoContext.class);
		}
		public ComandoContext comando(int i) {
			return getRuleContext(ComandoContext.class,i);
		}
		public OndaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_onda; }
	}

	public final OndaContext onda() throws RecognitionException {
		OndaContext _localctx = new OndaContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_onda);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			match(T__10);
			setState(77);
			match(T__3);
			setState(81); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(78);
				comando();
				setState(79);
				match(T__5);
				}
				}
				setState(83); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__14 || _la==IDENT );
			setState(85);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComandoContext extends ParserRuleContext {
		public CmdSpawnContext cmdSpawn() {
			return getRuleContext(CmdSpawnContext.class,0);
		}
		public CmdAguardeContext cmdAguarde() {
			return getRuleContext(CmdAguardeContext.class,0);
		}
		public ComandoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comando; }
	}

	public final ComandoContext comando() throws RecognitionException {
		ComandoContext _localctx = new ComandoContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_comando);
		try {
			setState(89);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENT:
				enterOuterAlt(_localctx, 1);
				{
				setState(87);
				cmdSpawn();
				}
				break;
			case T__14:
				enterOuterAlt(_localctx, 2);
				{
				setState(88);
				cmdAguarde();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CmdSpawnContext extends ParserRuleContext {
		public SemDelayContext semDelay() {
			return getRuleContext(SemDelayContext.class,0);
		}
		public ComDelayContext comDelay() {
			return getRuleContext(ComDelayContext.class,0);
		}
		public CmdSpawnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdSpawn; }
	}

	public final CmdSpawnContext cmdSpawn() throws RecognitionException {
		CmdSpawnContext _localctx = new CmdSpawnContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_cmdSpawn);
		try {
			setState(93);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(91);
				semDelay();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(92);
				comDelay();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SemDelayContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(TGENParser.IDENT, 0); }
		public TerminalNode INT() { return getToken(TGENParser.INT, 0); }
		public SemDelayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_semDelay; }
	}

	public final SemDelayContext semDelay() throws RecognitionException {
		SemDelayContext _localctx = new SemDelayContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_semDelay);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			match(IDENT);
			setState(96);
			match(T__11);
			setState(97);
			match(INT);
			setState(98);
			match(T__12);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComDelayContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(TGENParser.IDENT, 0); }
		public TerminalNode INT() { return getToken(TGENParser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(TGENParser.FLOAT, 0); }
		public ComDelayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comDelay; }
	}

	public final ComDelayContext comDelay() throws RecognitionException {
		ComDelayContext _localctx = new ComDelayContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_comDelay);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			match(IDENT);
			setState(101);
			match(T__11);
			setState(102);
			match(INT);
			setState(103);
			match(T__13);
			setState(104);
			match(FLOAT);
			setState(105);
			match(T__12);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CmdAguardeContext extends ParserRuleContext {
		public TerminalNode FLOAT() { return getToken(TGENParser.FLOAT, 0); }
		public CmdAguardeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdAguarde; }
	}

	public final CmdAguardeContext cmdAguarde() throws RecognitionException {
		CmdAguardeContext _localctx = new CmdAguardeContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_cmdAguarde);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			match(T__14);
			setState(108);
			match(T__11);
			setState(109);
			match(FLOAT);
			setState(110);
			match(T__12);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\27s\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\6\3"+
		"\'\n\3\r\3\16\3(\3\3\3\3\3\4\3\4\3\4\3\4\3\4\7\4\62\n\4\f\4\16\4\65\13"+
		"\4\3\4\3\4\3\5\3\5\3\5\5\5<\n\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3"+
		"\b\3\b\3\b\3\t\6\tK\n\t\r\t\16\tL\3\n\3\n\3\n\3\n\3\n\6\nT\n\n\r\n\16"+
		"\nU\3\n\3\n\3\13\3\13\5\13\\\n\13\3\f\3\f\5\f`\n\f\3\r\3\r\3\r\3\r\3\r"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\2\2"+
		"\20\2\4\6\b\n\f\16\20\22\24\26\30\32\34\2\2\2l\2\36\3\2\2\2\4#\3\2\2\2"+
		"\6,\3\2\2\2\b;\3\2\2\2\n=\3\2\2\2\fA\3\2\2\2\16E\3\2\2\2\20J\3\2\2\2\22"+
		"N\3\2\2\2\24[\3\2\2\2\26_\3\2\2\2\30a\3\2\2\2\32f\3\2\2\2\34m\3\2\2\2"+
		"\36\37\5\4\3\2\37 \7\3\2\2 !\5\20\t\2!\"\7\4\2\2\"\3\3\2\2\2#$\7\5\2\2"+
		"$&\7\6\2\2%\'\5\6\4\2&%\3\2\2\2\'(\3\2\2\2(&\3\2\2\2()\3\2\2\2)*\3\2\2"+
		"\2*+\7\7\2\2+\5\3\2\2\2,-\7\24\2\2-\63\7\6\2\2./\5\b\5\2/\60\7\b\2\2\60"+
		"\62\3\2\2\2\61.\3\2\2\2\62\65\3\2\2\2\63\61\3\2\2\2\63\64\3\2\2\2\64\66"+
		"\3\2\2\2\65\63\3\2\2\2\66\67\7\7\2\2\67\7\3\2\2\28<\5\n\6\29<\5\f\7\2"+
		":<\5\16\b\2;8\3\2\2\2;9\3\2\2\2;:\3\2\2\2<\t\3\2\2\2=>\7\t\2\2>?\7\n\2"+
		"\2?@\7\22\2\2@\13\3\2\2\2AB\7\13\2\2BC\7\n\2\2CD\7\23\2\2D\r\3\2\2\2E"+
		"F\7\f\2\2FG\7\n\2\2GH\7\22\2\2H\17\3\2\2\2IK\5\22\n\2JI\3\2\2\2KL\3\2"+
		"\2\2LJ\3\2\2\2LM\3\2\2\2M\21\3\2\2\2NO\7\r\2\2OS\7\6\2\2PQ\5\24\13\2Q"+
		"R\7\b\2\2RT\3\2\2\2SP\3\2\2\2TU\3\2\2\2US\3\2\2\2UV\3\2\2\2VW\3\2\2\2"+
		"WX\7\7\2\2X\23\3\2\2\2Y\\\5\26\f\2Z\\\5\34\17\2[Y\3\2\2\2[Z\3\2\2\2\\"+
		"\25\3\2\2\2]`\5\30\r\2^`\5\32\16\2_]\3\2\2\2_^\3\2\2\2`\27\3\2\2\2ab\7"+
		"\24\2\2bc\7\16\2\2cd\7\22\2\2de\7\17\2\2e\31\3\2\2\2fg\7\24\2\2gh\7\16"+
		"\2\2hi\7\22\2\2ij\7\20\2\2jk\7\23\2\2kl\7\17\2\2l\33\3\2\2\2mn\7\21\2"+
		"\2no\7\16\2\2op\7\23\2\2pq\7\17\2\2q\35\3\2\2\2\t(\63;LU[_";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}