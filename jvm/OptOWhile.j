.class public OptOWhile
.super java/lang/Object


.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 5
	.limit locals 3
	iconst_1
	istore_2
	iconst_2
	istore_1
L1:
	iload_1
	iconst_2
	imul
	istore_1
	iload_1
	invokestatic io/println(I)V
	iload_1
	bipush 10
	if_icmplt L1
L2:
	iconst_0
	iconst_1
	if_icmplt L2_end
	iinc 2 1
	goto L2
L2_end:
	return
.end method

