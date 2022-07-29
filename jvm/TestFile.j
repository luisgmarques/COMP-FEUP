.class public TestFile
.super java/lang/Object

.field public field I

.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 2
	.limit locals 1
	iconst_0
	aload_0
	swap
	putfield TestFile/field I
	aload_0
	getfield TestFile/field I
	iconst_1
	iadd
	aload_0
	swap
	putfield TestFile/field I
	aload_0
	getfield TestFile/field I
	invokestatic io/println(I)V
	return
.end method

