/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.solver.tokens;

/**
 * This enumerator is used to represent almost any C operator (which we
 * mostly
 * will eventually map to some Z3 operator).
 * 
 * @author Aman Nougrahiya
 *
 */
public enum OperatorToken implements Tokenizable {
	ASSIGN {
		@Override
		public String toString() {
			return " = ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.RIGHT_TO_LEFT;
		}

		@Override
		public int precedence() {
			return 1;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	ITE {
		@Override
		public String toString() {
			return " ITE ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.RIGHT_TO_LEFT;
		}

		@Override
		public int precedence() {
			return 2;
		}

		@Override
		public int arity() {
			return 3;
		}
	},
	LOGIC_OR {
		@Override
		public String toString() {
			return " || ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 3;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	BIT_OR {
		@Override
		public String toString() {
			return " | ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 5;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	BIT_XOR {
		@Override
		public String toString() {
			return " ^ ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 6;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	BIT_AND {
		@Override
		public String toString() {
			return " & ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 7;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	ISEQ {
		@Override
		public String toString() {
			return " == ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 8;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	ISNEQ {
		@Override
		public String toString() {
			return " != ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 8;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	LT {
		@Override
		public String toString() {
			return " < ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 9;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	GT {
		@Override
		public String toString() {
			return " > ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 9;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	LE {
		@Override
		public String toString() {
			return " <= ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 9;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	GE {
		@Override
		public String toString() {
			return " >= ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 9;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	SR {
		@Override
		public String toString() {
			return " >> ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 10;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	SL {
		@Override
		public String toString() {
			return " << ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 10;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	PLUS {
		@Override
		public String toString() {
			return " + ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 11;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	MINUS {
		@Override
		public String toString() {
			return " - ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 11;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	MUL {
		@Override
		public String toString() {
			return " * ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 12;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	DIV {
		@Override
		public String toString() {
			return " / ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 12;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	MOD {
		@Override
		public String toString() {
			return " % ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 12;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	AMP {
		@Override
		public String toString() {
			return " &";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.RIGHT_TO_LEFT;
		}

		@Override
		public int precedence() {
			return 13;
		}

		@Override
		public int arity() {
			return 1;
		}
	},
	PTR {
		@Override
		public String toString() {
			return " *";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.RIGHT_TO_LEFT;
		}

		@Override
		public int precedence() {
			return 13;
		}

		@Override
		public int arity() {
			return 1;
		}
	},
	NOT {
		@Override
		public String toString() {
			return " !";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.RIGHT_TO_LEFT;
		}

		@Override
		public int precedence() {
			return 13;
		}

		@Override
		public int arity() {
			return 1;
		}
	},
	BIT_NEGATION {
		@Override
		public String toString() {
			return " ~";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.RIGHT_TO_LEFT;
		}

		@Override
		public int precedence() {
			return 13;
		}

		@Override
		public int arity() {
			return 1;
		}
	},
	UNARY_PLUS {
		@Override
		public String toString() {
			return " +";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.RIGHT_TO_LEFT;
		}

		@Override
		public int precedence() {
			return 13;
		}

		@Override
		public int arity() {
			return 1;
		}
	},
	UNARY_MINUS {
		@Override
		public String toString() {
			return " -";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.RIGHT_TO_LEFT;
		}

		@Override
		public int precedence() {
			return 13;
		}

		@Override
		public int arity() {
			return 1;
		}
	},
	PRE_INC {
		@Override
		public String toString() {
			return " ++";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.RIGHT_TO_LEFT;
		}

		@Override
		public int precedence() {
			return 13;
		}

		@Override
		public int arity() {
			return 1;
		}
	},
	PRE_DEC {
		@Override
		public String toString() {
			return "--";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.RIGHT_TO_LEFT;
		}

		@Override
		public int precedence() {
			return 13;
		}

		@Override
		public int arity() {
			return 1;
		}
	},
	POST_INC {
		@Override
		public String toString() {
			return "++ ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 14;
		}

		@Override
		public int arity() {
			return 1;
		}
	},
	POST_DEC {
		@Override
		public String toString() {
			return "-- ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 14;
		}

		@Override
		public int arity() {
			return 1;
		}
	},
	POW {
		@Override
		public String toString() {
			return " POW ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.LEFT_TO_RIGHT;
		}

		@Override
		public int precedence() {
			return 15;
		}

		@Override
		public int arity() {
			return 2;
		}
	},
	OP {
		@Override
		public String toString() {
			return " (";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.NOT_APPLICABLE;
		}

		@Override
		public int precedence() {
			return -1;
		}

		@Override
		public int arity() {
			return 0;
		}
	},
	CP {
		@Override
		public String toString() {
			return ") ";
		}

		@Override
		public OperatorAssociativity associativity() {
			return OperatorAssociativity.NOT_APPLICABLE;
		}

		@Override
		public int precedence() {
			return -1;
		}

		@Override
		public int arity() {
			return -1;
		}
	};

	public abstract int arity();

	public abstract int precedence();

	public abstract OperatorAssociativity associativity();
}
