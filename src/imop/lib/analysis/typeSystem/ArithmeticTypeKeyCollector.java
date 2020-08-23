/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.typeSystem;

import imop.ast.node.external.*;
import imop.ast.node.internal.*;
import imop.baseVisitor.DepthFirstProcess;

import java.util.ArrayList;
import java.util.List;

/**
 * Collects the multiset of all the type-specifying keywords
 * present in the visited portion of a DeclarationSpecifier.
 * 
 * @author aman
 *
 */
public class ArithmeticTypeKeyCollector extends DepthFirstProcess {

	/**
	 * Refers to the immediately enclosing CompoundStatement,
	 * FunctionDefinition, TranslationUnit,
	 * StructType, or UnionType, in which the user-defined structs/unions/enums
	 * and typedefs
	 * should be visible, which might be needed to obtain the type information.
	 */
	private Scopeable definingScope;

	public List<ArithmeticTypeKey> keywords = new ArrayList<>();

	public ArithmeticTypeKeyCollector(Scopeable definingScope) {
		this.definingScope = definingScope;
	}

	/**
	 * f0 ::= ( <VOID> | <CHAR> | <SHORT> | <INT> | <LONG> | <FLOAT> | <DOUBLE>
	 * |
	 * <SIGNED> | <UNSIGNED> | StructOrUnionSpecifier() | EnumSpecifier() |
	 * TypedefName() )
	 */
	@Override
	public void visit(TypeSpecifier n) {
		Node choice = n.getF0().getChoice();
		List<String> keywordList = new ArrayList<>();
		if (!(choice instanceof NodeToken)) {
			if (choice instanceof TypedefName) {
				Typedef tDef = Typedef.getTypedefEntry((TypedefName) choice, definingScope);
				if (tDef != null) {
					if (tDef.getType() instanceof _BoolType) {
						keywordList.add("_Bool");
					} else if (tDef.getType() != null) {
						Type typedefType = tDef.getType();
						for (String keyword : typedefType.toString().split("")) {
							keywordList.add(keyword);
						}
					}
				}
			}
		} else {
			keywordList.add(((NodeToken) n.getF0().getChoice()).getTokenImage());
		}
		for (String keyword : keywordList) {
			switch (keyword) {
			case "void":
				keywords.add(ArithmeticTypeKey.VOID);
				break;
			case "char":
				keywords.add(ArithmeticTypeKey.CHAR);
				break;
			case "short":
				keywords.add(ArithmeticTypeKey.SHORT);
				break;
			case "int":
				keywords.add(ArithmeticTypeKey.INT);
				break;
			case "long":
				keywords.add(ArithmeticTypeKey.LONG);
				break;
			case "float":
				keywords.add(ArithmeticTypeKey.FLOAT);
				break;
			case "double":
				keywords.add(ArithmeticTypeKey.DOUBLE);
				break;
			case "signed":
				keywords.add(ArithmeticTypeKey.SIGNED);
				break;
			case "unsigned":
				keywords.add(ArithmeticTypeKey.UNSIGNED);
				break;
			case "_Bool":
				keywords.add(ArithmeticTypeKey._BOOL);
				break;
			case "_Complex":
				keywords.add(ArithmeticTypeKey._COMPLEX);
				break;
			default:
				// TODO: Check how GCC defines and uses these other keywords.
			}
		}

	}

	/**
	 * f0 ::= StructOrUnion()
	 * f1 ::= ( <IDENTIFIER> )?
	 * f2 ::= "{"
	 * f3 ::= StructDeclarationList()
	 * f4 ::= "}"
	 */
	@Override
	public void visit(StructOrUnionSpecifierWithList n) {
		initProcess(n);
		n.getF0().accept(this);
		n.getF1().accept(this);
		endProcess(n);
	}

}
