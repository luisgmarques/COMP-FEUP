.class public LessThan
.super java/lang/Object

.field public global I

.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 2
	.limit locals 2
	aload_0
	getfield LessThan/global I
	iconst_5
	iadd
	istore_1
	iload_1
	invokestatic io/println(I)V
	return
.end method

