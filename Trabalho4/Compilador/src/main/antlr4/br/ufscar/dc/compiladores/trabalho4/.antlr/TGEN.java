// Generated from c:\Users\marco\Desktop\compilers\compiladores\Trabalho4\Compilador\src\main\antlr4\br\u005Cufscar\dc\compiladores\trabalho4\TGEN.g4 by ANTLR 4.8
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TGEN extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		RESERVED=1, INT=2, FLOAT=3, IDENT=4, WS=5, COMENTARIO=6, DELIM=7, ERRO_DESCONHECIDO=8;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"RESERVED", "INT", "FLOAT", "IDENT", "WS", "COMENTARIO", "DELIM", "ERRO_DESCONHECIDO"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "RESERVED", "INT", "FLOAT", "IDENT", "WS", "COMENTARIO", "DELIM", 
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


	public TGEN(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "TGEN.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\nx\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2H\n\2\3\3\6\3"+
		"K\n\3\r\3\16\3L\3\4\6\4P\n\4\r\4\16\4Q\3\4\3\4\6\4V\n\4\r\4\16\4W\3\5"+
		"\3\5\7\5\\\n\5\f\5\16\5_\13\5\3\6\3\6\3\6\5\6d\n\6\3\6\3\6\3\7\3\7\3\7"+
		"\3\7\7\7l\n\7\f\7\16\7o\13\7\3\7\3\7\3\7\3\7\3\b\3\b\3\t\3\t\2\2\n\3\3"+
		"\5\4\7\5\t\6\13\7\r\b\17\t\21\n\3\2\7\4\2C\\c|\6\2\62;C\\aac|\5\2\13\f"+
		"\17\17\"\"\3\2\f\f\b\2*+..<=??}}\177\177\2\u0085\2\3\3\2\2\2\2\5\3\2\2"+
		"\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\3G\3\2\2\2\5J\3\2\2\2\7O\3\2\2\2\tY\3\2\2\2\13c\3\2\2\2\rg\3"+
		"\2\2\2\17t\3\2\2\2\21v\3\2\2\2\23\24\7k\2\2\24\25\7p\2\2\25\26\7k\2\2"+
		"\26\27\7o\2\2\27\30\7k\2\2\30\31\7i\2\2\31\32\7q\2\2\32H\7u\2\2\33\34"+
		"\7h\2\2\34\35\7q\2\2\35\36\7t\2\2\36\37\7e\2\2\37H\7c\2\2 !\7x\2\2!\""+
		"\7k\2\2\"#\7f\2\2#H\7c\2\2$%\7x\2\2%&\7g\2\2&\'\7n\2\2\'(\7q\2\2()\7e"+
		"\2\2)*\7k\2\2*+\7f\2\2+,\7c\2\2,-\7f\2\2-H\7g\2\2./\7o\2\2/\60\7q\2\2"+
		"\60\61\7f\2\2\61\62\7g\2\2\62\63\7n\2\2\63H\7q\2\2\64\65\7u\2\2\65\66"+
		"\7v\2\2\66\67\7c\2\2\678\7t\2\28H\7v\2\29:\7q\2\2:;\7p\2\2;<\7f\2\2<H"+
		"\7c\2\2=>\7g\2\2>?\7p\2\2?H\7f\2\2@A\7c\2\2AB\7i\2\2BC\7w\2\2CD\7c\2\2"+
		"DE\7t\2\2EF\7f\2\2FH\7g\2\2G\23\3\2\2\2G\33\3\2\2\2G \3\2\2\2G$\3\2\2"+
		"\2G.\3\2\2\2G\64\3\2\2\2G9\3\2\2\2G=\3\2\2\2G@\3\2\2\2H\4\3\2\2\2IK\4"+
		"\62;\2JI\3\2\2\2KL\3\2\2\2LJ\3\2\2\2LM\3\2\2\2M\6\3\2\2\2NP\4\62;\2ON"+
		"\3\2\2\2PQ\3\2\2\2QO\3\2\2\2QR\3\2\2\2RS\3\2\2\2SU\7\60\2\2TV\4\62;\2"+
		"UT\3\2\2\2VW\3\2\2\2WU\3\2\2\2WX\3\2\2\2X\b\3\2\2\2Y]\t\2\2\2Z\\\t\3\2"+
		"\2[Z\3\2\2\2\\_\3\2\2\2][\3\2\2\2]^\3\2\2\2^\n\3\2\2\2_]\3\2\2\2`d\t\4"+
		"\2\2ab\7\17\2\2bd\7\f\2\2c`\3\2\2\2ca\3\2\2\2de\3\2\2\2ef\b\6\2\2f\f\3"+
		"\2\2\2gh\7\61\2\2hi\7\61\2\2im\3\2\2\2jl\n\5\2\2kj\3\2\2\2lo\3\2\2\2m"+
		"k\3\2\2\2mn\3\2\2\2np\3\2\2\2om\3\2\2\2pq\7\f\2\2qr\3\2\2\2rs\b\7\2\2"+
		"s\16\3\2\2\2tu\t\6\2\2u\20\3\2\2\2vw\13\2\2\2w\22\3\2\2\2\n\2GLQW]cm\3"+
		"\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}