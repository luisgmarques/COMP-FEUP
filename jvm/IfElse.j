.class public IfElse
.super java/lang/Object


.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 16
	.limit locals 3
	new IfElse
	dup
	invokespecial IfElse/<init>()V
	astore_1
	aload_1
	invokevirtual IfElse/ret_array()[I
	astore_2
	aload_2
	iconst_1
	iaload
	invokestatic io/println(I)V
	bipush 8
	iconst_3
	if_icmpge L1
	iconst_0
	invokestatic io/println(I)V
	goto L
L1:
	bipush 8
	iconst_5
	if_icmpge L2
	iconst_1
	invokestatic io/println(I)V
	goto L
L2:
L:
	bipush 9
	bipush 7
	if_icmpge L3
	iconst_2
	invokestatic io/println(I)V
	goto L
L3:
L:
	iconst_3
	invokestatic io/println(I)V
L:
	bipush 7
	bipush 8
	if_icmpge L4
	iconst_4
	invokestatic io/println(I)V
L4:
	iconst_5
	invokestatic io/println(I)V
	bipush 9
	bipush 8
	if_icmpge L5
	bipush 6
	invokestatic io/println(I)V
L5:
	bipush 7
	invokestatic io/println(I)V
	bipush 9
	bipush 8
	if_icmpge L6
	bipush 8
	invokestatic io/println(I)V
	goto L
L6:
	bipush 9
	invokestatic io/println(I)V
L:
	bipush 7
	bipush 8
	if_icmpge L7
	bipush 8
	invokestatic io/println(I)V
	goto L
L7:
	bipush 9
	invokestatic io/println(I)V
L:
L:
	goto L
L:
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
	sipush 500
	iastore
	aload_1
	iconst_1
	sipush 1000
	iastore
	aload_1
	areturn
.end method

