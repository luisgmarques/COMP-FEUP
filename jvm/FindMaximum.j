.class public FindMaximum
.super java/lang/Object

.field public test_arr [I

.method public find_maximum([I)I
	.limit stack 7
	.limit locals 5
	iconst_1
	istore 4
	aload_1
	iconst_0
	iaload
	istore_3
L1:
	iload 4
	aload_1
	arraylength
	if_icmpge L1_end
	aload_1
	iload 4
	iaload
	istore_2
	iload_3
	iload_2
	if_icmpge L2
	iload_2
	istore_3
	goto L3
L2:
L3:
	iinc 4 1
	goto L1
L1_end:
	iload_3
	ireturn
.end method

.method public build_test_arr()I
	.limit stack 4
	.limit locals 1
	iconst_5
	newarray int
	aload_0
	swap
	putfield FindMaximum/test_arr [I
	aload_0
	getfield FindMaximum/test_arr [I
	iconst_0
	bipush 14
	iastore
	aload_0
	getfield FindMaximum/test_arr [I
	iconst_1
	bipush 28
	iastore
	aload_0
	getfield FindMaximum/test_arr [I
	iconst_2
	iconst_0
	iastore
	aload_0
	getfield FindMaximum/test_arr [I
	iconst_3
	iconst_0
	iconst_5
	isub
	iastore
	aload_0
	getfield FindMaximum/test_arr [I
	iconst_4
	bipush 12
	iastore
	iconst_0
	ireturn
.end method

.method public get_array()[I
	.limit stack 1
	.limit locals 1
	aload_0
	getfield FindMaximum/test_arr [I
	areturn
.end method

.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 4
	.limit locals 2
	new FindMaximum
	dup
	invokespecial FindMaximum/<init>()V
	astore_1
	aload_1
	invokevirtual FindMaximum/build_test_arr()I
	pop
	aload_1
	aload_1
	invokevirtual FindMaximum/get_array()[I
	invokevirtual FindMaximum/find_maximum([I)I
	invokestatic ioPlus/printResult(I)V
	return
.end method

