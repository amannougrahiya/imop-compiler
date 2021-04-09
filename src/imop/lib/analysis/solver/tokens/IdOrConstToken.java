/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.lib.analysis.solver.tokens;

import imop.ast.node.external.*;
import imop.lib.builder.Builder;
import imop.parser.FrontEnd;

public class IdOrConstToken implements Tokenizable {
	private final Node cfgLeaf;
	private final NodeToken anIdentifier;
	private final Constant aConstant;

	public IdOrConstToken(NodeToken anIdentifier, Node cfgLeaf) {
		this.anIdentifier = anIdentifier;
		this.aConstant = null;
		this.cfgLeaf = cfgLeaf;
	}

	public IdOrConstToken(Constant aConstant) {
		this.aConstant = aConstant;
		this.anIdentifier = null;
		this.cfgLeaf = null;
	}

	/**
	 * Obtain a new {@link IdOrConstToken} object corresponding to a new
	 * identifier.
	 * 
	 * @return
	 *         a new {@link IdOrConstToken} object corresponding to a new
	 *         identifier.
	 */
	public static IdOrConstToken getNewIdToken() {
		NodeToken newId = new NodeToken(Builder.getNewTempName());
		return new IdOrConstToken(newId, null);
	}

	/**
	 * Obtain a new {@link IdOrConstToken} object corresponding to a new
	 * identifier named {@code newIdStr}.
	 * 
	 * @param newIdStr
	 *                 name of the identifier for which a new object has to be
	 *                 created.
	 * @return
	 *         a new {@link IdOrConstToken} object corresponding to a new
	 *         identifier.
	 */
	public static IdOrConstToken getNewIdToken(String newIdStr) {
		NodeToken newId = new NodeToken(newIdStr);
		return new IdOrConstToken(newId, null);
	}

	/**
	 * Obtain a new {@link IdOrConstToken} object corresponding to the given
	 * constant string.
	 * 
	 * @return
	 *         a new {@link IdOrConstToken} object corresponding to the given
	 *         constant string.
	 */
	public static IdOrConstToken getNewConstantToken(String constantString) {
		Constant constant = FrontEnd.parseCrude(constantString, Constant.class);
		return new IdOrConstToken(constant);
	}

	public boolean isAnIdentifier() {
		return anIdentifier != null;
	}

	public boolean isAConstant() {
		return aConstant != null;
	}

	public NodeToken getIdentifier() {
		return anIdentifier;
	}

	public Constant getConstant() {
		return aConstant;
	}

	@Override
	public String toString() {
		if (this.isAnIdentifier()) {
			return this.anIdentifier.getTokenImage();
		} else {
			return this.aConstant.toString();
		}
	}

	public Node getCfgLeaf() {
		return cfgLeaf;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof IdOrConstToken)) {
			return false;
		}
		IdOrConstToken that = (IdOrConstToken) obj;
		if (this.isAConstant()) {
			if (that.isAnIdentifier()) {
				return false;
			} else {
				if (that.aConstant.toString().equals(this.aConstant.toString())) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			if (that.isAConstant()) {
				return false;
			} else {
				if (this.cfgLeaf != that.cfgLeaf) {
					return false;
				}
				if (that.anIdentifier.toString().equals(this.anIdentifier.toString())) {
					return true;
				} else {
					return false;
				}
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (this.isAConstant()) {
			result = prime * result + ((this.aConstant.toString() == null) ? 0 : this.aConstant.toString().hashCode());
			return result;
			// return Arrays.hashCode(new Object[]{this.aConstant.toString()});
		} else {
			result = prime * result
					+ ((this.anIdentifier.toString() == null) ? 0 : this.anIdentifier.toString().hashCode());
			return result;
			// return Arrays.hashCode(new Object[]{this.anIdentifier.toString()});
		}
	}
}
