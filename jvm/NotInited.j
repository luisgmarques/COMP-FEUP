.class public NotInited
.super java/lang/Object


.method public add(II)I
	.limit stack 1
	.limit locals 4
	iload_1
	istore_3
	iload_3
	ireturn
.end method

.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 3
	.limit locals 2
	aload_1
	iconst_1
	iconst_2
	invokevirtual NotInited/add(II)I
	pop
	return
.end method

