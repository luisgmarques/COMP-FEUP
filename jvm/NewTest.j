.class public NewTest
.super java/lang/Object


.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 28
	.limit locals 7
	new NewTest
	dup
	invokespecial NewTest/<init>()V
	astore_1
	iconst_1
	newarray int
	astore 5
	iconst_2
	iconst_3
	iadd
	iconst_5
	iadd
	istore 6
	bipush 8
	iconst_3
	if_icmpge L1
	iconst_0
	invokestatic io/println(I)V
	goto L2
L1:
	bipush 8
	iconst_5
	if_icmpge L3
	iconst_1
	invokestatic io/println(I)V
	goto L2
L3:
L2:
	bipush 9
	bipush 7
	if_icmpge L4
	iconst_2
	invokestatic io/println(I)V
	goto L2
L4:
L2:
	iconst_3
	invokestatic io/println(I)V
L2:
	bipush 7
	bipush 8
	if_icmpge L5
	iconst_4
	invokestatic io/println(I)V
L5:
	iconst_5
	invokestatic io/println(I)V
	bipush 9
	bipush 8
	if_icmpge L7
	bipush 6
	invokestatic io/println(I)V
L7:
	bipush 7
	invokestatic io/println(I)V
	bipush 9
	bipush 8
	if_icmpge L9
	bipush 8
	invokestatic io/println(I)V
	goto L10
L9:
	bipush 9
	invokestatic io/println(I)V
L10:
	bipush 7
	bipush 8
	if_icmpge L11
	bipush 8
	invokestatic io/println(I)V
	goto L12
L11:
	bipush 9
	invokestatic io/println(I)V
L12:
	return
.end method

.method public ret_array()[I
	.limit stack 3
	.limit locals 2
	iconst_2
	newarray int
	astore_1
	aload_1
	iconst_0
	iconst_1
	iastore
	aload_1
	iconst_1
	iconst_2
	iastore
	aload_1
	areturn
.end method

