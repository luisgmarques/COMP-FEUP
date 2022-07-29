.class public Break
.super java/lang/Object


.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 8
	.limit locals 2
	iconst_0
	istore_1
L1:
	iconst_0
	invokestatic io/println(I)V
L2:
	iload_1
	bipush 9
	if_icmpge L2_end
	iload_1
	iconst_1
	iadd
	istore_1
L5:
	iload_1
	iconst_5
	if_icmpge L6
	iconst_1
	goto L7
L6:
	iconst_0
L7:
	iconst_1
	ixor
	iconst_1
	if_icmpeq L3
	iload_1
	iconst_5
	if_icmpge L8
	iconst_1
	goto L9
L8:
	iconst_0
L9:
	iconst_1
	ixor
	goto L4
L3:
	iload_1
	iconst_5
	if_icmpge L10
	iconst_1
	goto L11
L10:
	iconst_0
L11:
	iconst_1
	ixor
	iconst_5
	iload_1
	if_icmpge L12
	iconst_1
	goto L13
L12:
	iconst_0
L13:
	iconst_1
	ixor
	iand
L4:
	iconst_1
	if_icmplt L14
	goto L2
L14:
	iload_1
	invokestatic io/println(I)V
	goto L2
L2_end:
	goto L1_end
	goto L1
L1_end:
	return
.end method

