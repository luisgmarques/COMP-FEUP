.class public VarOptimization
.super java/lang/Object


.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 3
	.limit locals 3
	iconst_1
	istore_2
	bipush 20
	bipush 54
	iadd
	istore_1
L1:
	iload_2
	iconst_2
	imul
	istore_2
	iload_2
	invokestatic io/println(I)V
	iload_2
	iload_1
	if_icmplt L1
	return
.end method

