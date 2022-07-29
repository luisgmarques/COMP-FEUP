.class public Fac
.super java/lang/Object


.method public blah()I
	.limit stack 1
	.limit locals 1
	iconst_1
	ireturn
.end method

.method public foo()Z
	.limit stack 1
	.limit locals 1
	iconst_1
	ireturn
.end method

.method public ComputeFac(I)I
	.limit stack 17
	.limit locals 8
	new Blah
	dup
	invokespecial Blah/<init>()V
	astore_1
	aload_1
	invokevirtual Blah/poop()I
	istore 4
L3:
L6:
	aload_0
	invokevirtual Fac/foo()Z
	iconst_1
	ixor
	iconst_1
	if_icmpeq L4
	aload_0
	invokevirtual Fac/foo()Z
	iconst_1
	ixor
	goto L5
L4:
	aload_0
	invokevirtual Fac/foo()Z
	iconst_1
	ixor
	invokestatic Blah/bar()Z
	iconst_1
	ixor
	iand
L5:
	iconst_1
	ixor
	iconst_1
	if_icmpeq L1
L9:
	aload_0
	invokevirtual Fac/foo()Z
	iconst_1
	ixor
	iconst_1
	if_icmpeq L7
	aload_0
	invokevirtual Fac/foo()Z
	iconst_1
	ixor
	goto L8
L7:
	aload_0
	invokevirtual Fac/foo()Z
	iconst_1
	ixor
	invokestatic Blah/bar()Z
	iconst_1
	ixor
	iand
L8:
	iconst_1
	ixor
	goto L2
L1:
L12:
	aload_0
	invokevirtual Fac/foo()Z
	iconst_1
	ixor
	iconst_1
	if_icmpeq L10
	aload_0
	invokevirtual Fac/foo()Z
	iconst_1
	ixor
	goto L11
L10:
	aload_0
	invokevirtual Fac/foo()Z
	iconst_1
	ixor
	invokestatic Blah/bar()Z
	iconst_1
	ixor
	iand
L11:
	iconst_1
	ixor
	iload 4
	aload_0
	invokevirtual Fac/blah()I
	iconst_5
	iadd
	if_icmpge L13
	iconst_1
	goto L14
L13:
	iconst_0
L14:
	iand
L2:
	istore_1
	iload 4
	iconst_2
	bipush 23
	imul
	bipush 65
	bipush 87
	iconst_5
	isub
	idiv
	iadd
	iadd
	aload_0
	invokevirtual Fac/blah()I
	isub
	iconst_5
	iadd
	istore_3
	aload_1
	invokevirtual Blah/party()[I
	istore_1
	iconst_3
	newarray int
	astore_2
	aload_2
	iload 4
	iload_1
	iastore
	aload_2
	iload_3
	aload_2
	iload 4
	iaload
	aload_2
	arraylength
	iadd
	iastore
	aload_2
	iload_1
	aload_2
	iload 4
	iaload
	aload_2
	iload_3
	iaload
	iadd
	iastore
	aload_2
	iload 4
	iaload
	aload_2
	iload_3
	iaload
	iadd
	aload_2
	iload_1
	iaload
	iadd
	istore 4
	iload 4
	ireturn
.end method

