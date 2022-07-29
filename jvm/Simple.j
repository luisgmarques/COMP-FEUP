.class public Simple
.super java/lang/Object


.method public add(II)I
	.limit stack 2
	.limit locals 4
	iload_1
	aload_0
	invokevirtual Simple/constInstr()I
	iadd
	istore_1
	iload_1
	ireturn
.end method

.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 4
	.limit locals 4
	bipush 20
	istore_2
	new Simple
	dup
	invokespecial Simple/<init>()V
	astore_1
	aload_1
	iload_2
	bipush 10
	invokevirtual Simple/add(II)I
	istore_1
	iload_1
	invokestatic io/println(I)V
	return
.end method

.method public constInstr()I
	.limit stack 1
	.limit locals 2
	iconst_0
	istore_1
	iconst_4
	istore_1
	bipush 8
	istore_1
	bipush 14
	istore_1
	sipush 250
	istore_1
	sipush 400
	istore_1
	sipush 1000
	istore_1
	ldc 100474650
	istore_1
	bipush 10
	istore_1
	iload_1
	ireturn
.end method

