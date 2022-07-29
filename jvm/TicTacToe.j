.class public TicTacToe
.super java/lang/Object

.field public a I

.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 1
	.limit locals 2
	iconst_1
	aload_0
	swap
	putfield TicTacToe/a I
	return
.end method

.method public ret()I
	.limit stack 1
	.limit locals 1
	iconst_1
	ireturn
.end method

.method public test()Z
	.limit stack 2
	.limit locals 5
	iconst_1
	newarray int
	astore_2
	aload_2
	aload_0
	invokevirtual TicTacToe/ret()I
	iaload
	istore_1
	iconst_1
	istore_1
	iload_1
	ireturn
.end method

