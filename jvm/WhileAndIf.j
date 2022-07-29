.class public WhileAndIF
.super java/lang/Object


.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 6
	.limit locals 5
	bipush 20
	istore 4
	bipush 10
	istore_3
	bipush 10
	newarray int
	astore_1
	iload 4
	iload_3
	if_icmpge L1
	iload 4
	iconst_1
	isub
	istore_2
	goto L2
L1:
	iload_3
	iconst_1
	isub
	istore_2
L2:
L3:
	aload_1
	iload_2
	iload 4
	iload_3
	isub
	iastore
	iinc 2 -1
	iinc 4 -1
	iinc 3 -1
	iconst_0
	iconst_1
	isub
	iload_2
	if_icmplt L3
	iconst_0
	istore_2
L4:
	aload_1
	iload_2
	iaload
	invokestatic io/println(I)V
	iinc 2 1
	iload_2
	aload_1
	arraylength
	if_icmplt L4
	return
.end method

