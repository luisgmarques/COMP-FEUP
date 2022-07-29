.class public TestArea
.super java/lang/Object


.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 4
	.limit locals 4
	iconst_0
	bipush 10
	isub
	istore_2
	new TestArea
	dup
	invokespecial TestArea/<init>()V
	astore_1
	aload_1
	bipush 30
	iload_2
	invokevirtual TestArea/add(II)I
	istore_1
	iload_1
	invokestatic io/println(I)V
	return
.end method

.method public v(II)Z
	.limit stack 1
	.limit locals 3
	iconst_1
	ireturn
.end method

.method public ret(I)I
	.limit stack 1
	.limit locals 2
	iconst_5
	ireturn
.end method

.method public add(II)I
	.limit stack 4
	.limit locals 5
	new TestArea
	dup
	invokespecial TestArea/<init>()V
	astore_3
	iconst_1
	istore_1
	aload_3
	iconst_1
	if_icmplt L1
	iload_2
	istore_1
	iload_1
	istore_2
L1:
	iload_1
	aload_0
	iconst_4
	invokevirtual TestArea/ret(I)I
	iadd
	ireturn
.end method

