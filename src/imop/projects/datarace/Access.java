/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.projects.datarace;

public class Access {
	public String location;
	public int mode; // 0: read; 1 : write

	public Access(String location, int mode) {
		this.mode = mode;
		this.location = location;
	}
}
